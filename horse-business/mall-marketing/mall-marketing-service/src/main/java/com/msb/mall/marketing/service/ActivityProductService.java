package com.msb.mall.marketing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.framework.common.model.PageDTO;
import com.msb.framework.common.utils.DateUtil;
import com.msb.framework.web.result.BizAssert;
import com.msb.mall.marketing.api.model.ActivityProductDO;
import com.msb.mall.marketing.api.model.ActivityProductSkuDO;
import com.msb.mall.marketing.api.model.ProductSkuDO;
import com.msb.mall.marketing.mapper.ActivityProductMapper;
import com.msb.mall.marketing.model.bo.ActivityProcessTimeBO;
import com.msb.mall.marketing.model.bo.ActivityProductBO;
import com.msb.mall.marketing.model.dto.ActivityProductDTO;
import com.msb.mall.marketing.model.dto.ActivityProductQueryDTO;
import com.msb.mall.marketing.model.entity.Activity;
import com.msb.mall.marketing.model.entity.ActivityProduct;
import com.msb.mall.marketing.model.entity.ActivityProductSku;
import com.msb.mall.marketing.model.entity.ActivityTime;
import com.msb.mall.marketing.model.vo.ActivityProductVO;
import com.msb.mall.marketing.model.vo.app.AppActivityProductVO;
import com.msb.mall.marketing.service.convert.ActivityProductConvert;
import com.msb.mall.marketing.service.convert.ActivityProductSkuConvert;
import com.msb.mall.product.api.dubbo.ProductDubboService;
import com.msb.mall.product.api.model.ProductDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 活动商品表(ActivityProduct)表服务实现类
 *
 * @author makejava
 * @date 2022-04-08 13:38:54
 */
@Slf4j
@Service("activityProductService")
public class ActivityProductService extends ServiceImpl<ActivityProductMapper, ActivityProduct> {


    @DubboReference
    private ProductDubboService productDubboService;

    @Resource
    private ActivityProductConvert activityProductConvert;

    @Resource
    private ActivityProductSkuService activityProductSkuService;

    @Resource
    private ActivityTimeService activityTimeService;

    @Resource
    private ActivityService activityService;

    @Resource
    private ActivityProductSkuConvert activityProductSkuConvert;

    @Resource
    private ActivityCacheService activityCacheService;

    public ActivityProductSkuDO getActivityProductSku(Long activityId, Long activityTimeId, Long productId, Long skuId) {
        ActivityProductDO activityProductDO = this.getActivityProductDO(productId);
        if (activityProductDO.getIsInProgress()) {
            Optional<ProductSkuDO> productSkuOptional = activityProductDO.getActivityProductSkuListDO()
                    .stream().filter(p -> p.getProductSkuId().equals(skuId)).findAny();
            return productSkuOptional.map(productSkuDO -> new ActivityProductSkuDO()
                    .setId(productSkuDO.getId())
                    .setActivityProductId(productSkuDO.getActivityProductId())
                    .setProductId(productId)
                    .setProductSkuId(productSkuDO.getProductSkuId())
                    .setPrice(productSkuDO.getPrice())
                    .setOriginalPrice(productSkuDO.getOriginalPrice())
                    .setNumber(productSkuDO.getNumber())
                    .setStock(productSkuDO.getStock())).orElse(null);
        }
        return null;
    }


    /**
     * 获取活动产品下一次活动时间
     */
    private Optional<ActivityProcessTimeBO> getCurrentOrNextActivityStartTime(ActivityProductBO activityProductBO, LocalDateTime day) {
        Activity activity = activityProductBO.getActivity();
        ActivityTime activityTime = activityProductBO.getActivityTime();

        if (day.isAfter(activity.getActivityEndTime())) {
            //活动已结束
            return Optional.empty();
        }

        if (day.isBefore(activity.getActivityStartTime())) {
            //活动未开始 直接取活动开始那天的时间
            return Optional.of(new ActivityProcessTimeBO()
                    .setActivityProductBO(activityProductBO)
                    .setActivityTimeId(activityTime.getId())
                    .setStartTime(activity.getActivityStartTime().toLocalDate().atTime(activityTime.getStartTime()))
                    .setEndTime(activity.getActivityStartTime().toLocalDate().atTime(activityTime.getEndTime())));
        }

        if (day.toLocalTime().isBefore(activityTime.getEndTime())) {
            return Optional.of(new ActivityProcessTimeBO()
                    .setActivityProductBO(activityProductBO)
                    .setActivityTimeId(activityTime.getId())
                    .setStartTime(day.toLocalDate().atTime(activityTime.getStartTime()))
                    .setEndTime(day.toLocalDate().atTime(activityTime.getEndTime())));
        }
        //今天这个时间段已经过了 要看看明天还有没有,从明天早上开始算起
        LocalDateTime localDateTime = day.plusDays(1).toLocalDate().atTime(LocalTime.MIN);
        return getCurrentOrNextActivityStartTime(activityProductBO, localDateTime);
    }

    /**
     * 根据产品id 获取最近N天 在秒杀或者即将的活动 以及他的sku 列表
     */
    public ActivityProductDO getActivityProductDO(Long productId) {
        //根据productId 获取当前正在进行的活动的商品sku数据
        Optional<ActivityProduct> activityProduct = this.lambdaQuery().eq(ActivityProduct::getProductId, productId).list().stream().findAny();
        return activityProduct
                .map(ActivityProduct::getProductId)
                .map(this::isProductInActivity)
                .orElse(ActivityProductDO.buildNotActivity());
    }

    /**
     * 根据产品id 获取最近N天 即将秒杀的活动 以及他的sku 列表
     */
    private ActivityProductDO isProductInActivity(Long productId) {
        //查询未来N天内，没有结束的活动
        List<Activity> activityListNotOver = activityService.listActivity(5);
        Optional<ActivityProcessTimeBO> productInActivity = isProductInActivity(productId, activityListNotOver);
        return productInActivity.map(this::activityProductToDO).orElse(ActivityProductDO.buildNotActivity());
    }

    /**
     * 根据查到的秒杀活动信息
     */
    private ActivityProductDO activityProductToDO(ActivityProcessTimeBO activityProcessTimeBO) {
        ActivityProductBO activityProductBO = activityProcessTimeBO.getActivityProductBO();
        ActivityTime activityTime = activityProductBO.getActivityTime();
        Activity activity = activityProductBO.getActivity();

        ActivityProductDO activityProductDO = buildActivityProductDO(activityTime.getId(), activityProductBO.getActivityProduct().getProductId());

        Boolean isBetweenTime = DateUtil.isBetweenTime(LocalTime.now(), activityTime.getStartTime(), activityTime.getEndTime());
        Boolean isBetweenDateTime = DateUtil.isBetweenDateTime(LocalDateTime.now(), activity.getActivityStartTime(), activity.getActivityEndTime());

        //活动是否正在进行
        activityProductDO.setActivityId(activity.getId());
        activityProductDO.setIsInProgress(isBetweenTime && isBetweenDateTime);
        activityProductDO.setActivityStartTime(activityProcessTimeBO.getStartTime());
        activityProductDO.setActivityEndTime(activityProcessTimeBO.getEndTime());
        return activityProductDO;
    }

    /**
     * 判定这个product 是否在这个活动列表中存在，并且返回最近要开始的活动
     */
    private Optional<ActivityProcessTimeBO> isProductInActivity(Long productId, List<Activity> activityList) {
        List<Long> activityIdList = activityList.stream().map(Activity::getId).collect(Collectors.toList());

        List<ActivityTime> activityTimeListVO = activityTimeService.listActivityTime(activityIdList);
        if (activityTimeListVO.isEmpty()) {
            return Optional.empty();
        }

        Map<Long, Activity> activityMap = activityList.stream().collect(Collectors.toMap(Activity::getId, Function.identity()));
        Map<Long, ActivityTime> activityTimeMap = activityTimeListVO.stream().collect(Collectors.toMap(ActivityTime::getId, Function.identity()));

        //不同的活动，或者秒杀时间段内的产品
        List<ActivityProduct> list = lambdaQuery()
                .in(ActivityProduct::getActivityTimeId, activityTimeListVO.stream().map(ActivityTime::getId).collect(Collectors.toList()))
                .eq(ActivityProduct::getProductId, productId).list();
        LocalDateTime now = LocalDateTime.now();
        //找出这些个不同的活动和时间段内的活动产品，根据比较 活动日期 + 时间段开始时间 来找最早的一个
        List<ActivityProcessTimeBO> activityProcessTimeListBO = list.stream().map(activityProduct -> {
            ActivityTime activityTime = activityTimeMap.get(activityProduct.getActivityTimeId());
            return getCurrentOrNextActivityStartTime(new ActivityProductBO()
                    .setActivityProduct(activityProduct)
                    .setActivityTime(activityTime)
                    .setActivity(activityMap.get(activityTime.getActivityId())), now).orElse(null);
        }).collect(Collectors.toList());
        log.info("ActivityProcessTimeBO list {}", activityProcessTimeListBO);
        return activityProcessTimeListBO.stream().filter(Objects::nonNull).min(Comparator.comparing(ActivityProcessTimeBO::getStartTime));
    }

    private ActivityProductDO buildActivityProductDO(Long activityTimeId, Long productId) {
        ActivityProduct activityProduct = this.lambdaQuery().eq(ActivityProduct::getProductId, productId).eq(ActivityProduct::getActivityTimeId, activityTimeId).one();
        List<ActivityProductSku> activityProductSkus = activityProductSkuService.listActivityProductSku(activityProduct.getId());
        return new ActivityProductDO()
                .setIsActivityProduct(Boolean.TRUE)
                .setActivityProductSkuListDO(activityProductSkuConvert.toDO(activityProductSkus))
                .setActivityTimeId(activityTimeId);
    }

    /**
     * 根据活动时间段id 查询活动商品列表 ，查询前N条
     */
    public List<ActivityProduct> listActivityProduct(Long activityTimeId, Integer topNumber) {
        Page<ActivityProduct> page = this.lambdaQuery().eq(ActivityProduct::getActivityTimeId, activityTimeId).page(new Page<>(1, topNumber));
        return page.getRecords();
    }

    /**
     * 根据活动时间段id 查询活动商品列表 ，查询前N条
     */
    public List<ActivityProduct> listActivityProduct(Long activityTimeId) {
        return this.lambdaQuery().eq(ActivityProduct::getActivityTimeId, activityTimeId).list();
    }

    /**
     * 根据活动时间段id 查询活动商品列表 ，查询前N条
     */
    public List<Long> listActivityProductId(Long activityTimeId) {
        return this.lambdaQuery()
                .select(ActivityProduct::getProductId)
                .eq(ActivityProduct::getActivityTimeId, activityTimeId)
                .list()
                .stream()
                .map(ActivityProduct::getProductId)
                .collect(Collectors.toList());
    }

    /**
     * app，分页根据活动时间段id 查询活动商品列表
     */
    public IPage<AppActivityProductVO> pageActivityProduct(PageDTO pageDTO, Long activityTimeId) {
        Page<ActivityProduct> page = this.lambdaQuery().eq(ActivityProduct::getActivityTimeId, activityTimeId).page(pageDTO.page());
        Page<AppActivityProductVO> appActivityProductPageVO = new Page<>();
        BeanUtils.copyProperties(page, appActivityProductPageVO);
        return appActivityProductPageVO.setRecords(toAppVo(page.getRecords()));
    }

    /**
     * 后管，分页根据活动时间段id，商品名称，分类id 查询活动商品列表
     */
    public IPage<ActivityProductVO> pageActivityProduct(ActivityProductQueryDTO activityProductDTO) {
        Page<ActivityProduct> page = this.lambdaQuery()
                .eq(ActivityProduct::getActivityTimeId, activityProductDTO.getActivityTimeId())
                .like(StringUtils.isNotBlank(activityProductDTO.getProductName()), ActivityProduct::getProductName, activityProductDTO.getProductName())
                .eq(Objects.nonNull(activityProductDTO.getCategoryId()), ActivityProduct::getCategoryId, activityProductDTO.getCategoryId())
                .page(activityProductDTO.page());
        return activityProductConvert.toVo(page);
    }

    /**
     * 保存商品到某个活动时间段内
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveActivityProduct(ActivityProductDTO activityProductDTO) {
        BizAssert.isTrue(!activityTimeService.checkActivityTimeProceed(activityProductDTO.getActivityTimeId()), "秒杀正在进行中无法修改");

        List<Long> productIds = activityProductDTO.getProductId();
        productIds.forEach(productId -> {
            ProductDO productDO = productDubboService.getProductById(productId);
            Optional<ActivityProduct> activityProduct = this.lambdaQuery().eq(ActivityProduct::getActivityTimeId, activityProductDTO.getActivityTimeId())
                    .eq(ActivityProduct::getProductId, productId).oneOpt();
            if (!activityProduct.isPresent()) {
                this.save(new ActivityProduct()
                        .setActivityTimeId(activityProductDTO.getActivityTimeId())
                        .setProductId(productId)
                        .setProductName(productDO.getName())
                        .setProductMainPicture(productDO.getMainPicture())
                        .setProductStartingPrice(productDO.getStartingPrice())
                        .setCategoryId(productDO.getCategoryId()));
            }
        });
        activityCacheService.updateProductIsActivityByProduct(productIds);
        return Boolean.TRUE;
    }


    public List<AppActivityProductVO> toAppVo(List<ActivityProduct> activityProducts) {
        return activityProducts.stream().map(activityProduct -> {
            List<ActivityProductSku> activityProductSkus = activityProductSkuService.listActivityProductSku(activityProduct.getId());
            AppActivityProductVO appActivityProductVO = new AppActivityProductVO()
                    .setProductId(activityProduct.getProductId()).setActivityProductId(activityProduct.getId())
                    .setProductMainPicture(activityProduct.getProductMainPicture()).setProductName(activityProduct.getProductName())
                    .setStock(activityProductSkus.stream().mapToInt(ActivityProductSku::getStock).sum())
                    .setNumber(activityProductSkus.stream().mapToInt(ActivityProductSku::getNumber).sum());
            activityProductSkus.stream().map(ActivityProductSku::getPrice).min(BigDecimal::compareTo).ifPresent(appActivityProductVO::setActivityPrice);
            activityProductSkus.stream().map(ActivityProductSku::getOriginalPrice).min(BigDecimal::compareTo).ifPresent(appActivityProductVO::setOriginalPrice);
            return appActivityProductVO;
        }).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(List<Long> idList) {
        List<ActivityProduct> activityProducts = idList.stream().map(id -> {
            ActivityProduct activityProduct = this.getById(id);
            Optional.ofNullable(activityProduct).ifPresent(activityProduct1 -> {
                BizAssert.isTrue(!activityTimeService.checkActivityTimeProceed(activityProduct1.getActivityTimeId()), "秒杀正在进行中无法修改");
            });
            return activityProduct;
        }).collect(Collectors.toList());
        this.removeByIds(idList);
        activityProducts.forEach(activityProduct -> activityCacheService.updateProductIsActivityByProduct(activityProduct.getProductId()));
        return true;
    }
}

