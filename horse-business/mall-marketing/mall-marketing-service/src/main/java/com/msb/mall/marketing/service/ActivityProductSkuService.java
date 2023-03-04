package com.msb.mall.marketing.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.framework.common.exception.BizException;
import com.msb.framework.web.result.BizAssert;
import com.msb.mall.marketing.api.model.ActivityProductSkuDTO;
import com.msb.mall.marketing.api.model.ActivityProductSkuReturnStockDTO;
import com.msb.mall.marketing.mapper.ActivityProductSkuMapper;
import com.msb.mall.marketing.model.dto.ActivityProductSkuSaveDTO;
import com.msb.mall.marketing.model.entity.ActivityProduct;
import com.msb.mall.marketing.model.entity.ActivityProductSku;
import com.msb.mall.marketing.model.vo.ActivityProductSkuVO;
import com.msb.mall.product.api.dubbo.ProductDubboService;
import com.msb.mall.product.api.model.ProductSkuDO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 活动商品表(ActivityProductSku)表服务实现类
 *
 * @author makejava
 * @date 2022-04-08 13:38:55
 */
@Service("activityProductSkuService")
public class ActivityProductSkuService extends ServiceImpl<ActivityProductSkuMapper, ActivityProductSku> {

    @Resource
    private ActivityProductService activityProductService;

    @Resource
    private ActivityTimeService activityTimeService;

    @DubboReference
    private ProductDubboService productDubboService;

    public List<ActivityProductSku> listActivityProductSku(Long activityProductId) {
        return this.lambdaQuery().eq(ActivityProductSku::getActivityProductId, activityProductId).list();
    }

    public List<ActivityProductSkuVO> listActivityProductSkuVO(Long activityProductId) {
        ActivityProduct activityProduct = activityProductService.getById(activityProductId);
        List<ActivityProductSku> list = listActivityProductSku(activityProductId);
        List<ProductSkuDO> productSkuListDO = productDubboService.listProductSku(activityProduct.getProductId());
        Map<Long, ActivityProductSku> activityProductSkuMap = list.stream().collect(Collectors.toMap(ActivityProductSku::getProductSkuId, Function.identity()));

        List<ActivityProductSkuVO> activityProductSkusVO = new ArrayList<>(productSkuListDO.size());
        productSkuListDO.forEach(productSkuDO -> {
            ActivityProductSkuVO activityProductSkuVO = new ActivityProductSkuVO()
                    .setProductSkuId(productSkuDO.getSkuId())
                    .setSkuName(productSkuDO.getSkuName())
                    .setOriginalPrice(productSkuDO.getSellPrice())
                    .setStock(productSkuDO.getStock());
            Optional.ofNullable(activityProductSkuMap.get(activityProductSkuVO.getProductSkuId()))
                    .ifPresent(activityProductSku -> activityProductSkuVO.setPrice(activityProductSku.getPrice())
                            .setNumber(activityProductSku.getNumber())
                            .setActivitySellNumber(activityProductSku.getNumber() - activityProductSku.getStock()));
            activityProductSkusVO.add(activityProductSkuVO);
        });
        return activityProductSkusVO;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean save(ActivityProductSkuSaveDTO activityProductSkuDTO) {

        Long activityProductId = activityProductSkuDTO.getActivityProductId();
        ActivityProduct activityProduct = activityProductService.getById(activityProductId);

        BizAssert.isTrue(!activityTimeService.checkActivityTimeProceed(activityProduct.getActivityTimeId()), "秒杀正在进行中无法修改");

        List<ActivityProductSkuSaveDTO.ActivityProductSkuInfoDTO> activityProductSkuInfoList = activityProductSkuDTO.getActivityProductSkuInfoList();
        activityProductSkuInfoList.forEach(activityProductSkuInfoDTO -> {
            Optional<ActivityProductSku> activityProductSkuOptional = lambdaQuery().eq(ActivityProductSku::getActivityProductId, activityProductSkuDTO.getActivityProductId())
                    .eq(ActivityProductSku::getProductSkuId, activityProductSkuInfoDTO.getProductSkuId()).oneOpt();
            activityProductSkuOptional.ifPresent(activityProductSku ->
                    activityProductSku
                            .setPrice(activityProductSkuInfoDTO.getPrice())
                            .setNumber(activityProductSkuInfoDTO.getNumber())
                            .setStock(activityProductSkuInfoDTO.getNumber()));
            this.saveOrUpdate(activityProductSkuOptional.orElseGet(() -> new ActivityProductSku()
                    .setActivityProductId(activityProductSkuDTO.getActivityProductId())
                    .setProductId(activityProduct.getProductId())
                    .setProductSkuId(activityProductSkuInfoDTO.getProductSkuId())
                    .setPrice(activityProductSkuInfoDTO.getPrice())
                    .setNumber(activityProductSkuInfoDTO.getNumber())
                    .setStock(activityProductSkuInfoDTO.getNumber())
                    .setOriginalPrice(activityProductSkuInfoDTO.getOriginalPrice())));
        });
        List<Long> saveActivityProductSkuId = activityProductSkuInfoList.stream().map(ActivityProductSkuSaveDTO.ActivityProductSkuInfoDTO::getProductSkuId).collect(Collectors.toList());
        this.lambdaUpdate().eq(ActivityProductSku::getActivityProductId, activityProductId).notIn(ActivityProductSku::getProductSkuId, saveActivityProductSkuId).remove();
        return Boolean.TRUE;
    }

    @Transactional(rollbackFor = Exception.class)
    public void checkAndReduceActivityStock(List<ActivityProductSkuDTO> activityProductSkuListDTO) {
        activityProductSkuListDTO.forEach(activityProductSkuDTO -> {
            boolean result = this.baseMapper.checkAndReduceStock(activityProductSkuDTO.getActivityProductSkuId(), activityProductSkuDTO.getQuantity());
            BizAssert.isTrue(result, "秒杀商品" + activityProductSkuDTO.getProductName() + "-[" + activityProductSkuDTO.getSkuName() + "]" + "库存不足");
        });
    }


    /**
     * 批量返还商品库存
     *
     * @param returnStockDTOList：返还库存DTO列表
     * @return void
     * @author peng.xy
     * @date 2022/5/16
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchReturnStock(List<ActivityProductSkuReturnStockDTO> returnStockDTOList) {
        for (ActivityProductSkuReturnStockDTO returnStockDTO : returnStockDTOList) {
            this.returnStock(returnStockDTO);
        }
    }

    /**
     * 返还商品库存
     *
     * @param returnStockDTO：返还库存DTO
     * @return void
     * @author peng.xy
     * @date 2022/5/16
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean returnStock(ActivityProductSkuReturnStockDTO returnStockDTO) {
        Integer quantity = returnStockDTO.getQuantity();
        if (quantity == null || quantity.intValue() <= 0) {
            throw new BizException("数量必须大于0");
        }
        return this.baseMapper.addStock(returnStockDTO.getActivityProductSkuId(), returnStockDTO.getQuantity());
    }

}

