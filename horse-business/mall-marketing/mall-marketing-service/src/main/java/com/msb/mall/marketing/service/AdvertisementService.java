package com.msb.mall.marketing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.framework.common.utils.DateUtil;
import com.msb.framework.web.result.BizAssert;
import com.msb.mall.marketing.mapper.AdvertisementMapper;
import com.msb.mall.marketing.model.dto.AdvertisementModifyDTO;
import com.msb.mall.marketing.model.dto.AdvertisementPageQueryDTO;
import com.msb.mall.marketing.model.dto.AdvertisementQueryDTO;
import com.msb.mall.marketing.model.dto.AdvertisementSortModifyDTO;
import com.msb.mall.marketing.model.entity.Advertisement;
import com.msb.mall.marketing.model.vo.AdvertisementVO;
import com.msb.mall.marketing.model.vo.app.AdvertisementSimpleVO;
import com.msb.mall.marketing.service.convert.AdvertisementConvert;
import io.seata.common.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 广告(Advertisement)表服务实现类
 *
 * @author shumengjiao
 * @since 2022-05-21 16:36:27
 */
@Service("advertisementService")
public class AdvertisementService extends ServiceImpl<AdvertisementMapper, Advertisement> {

    @Resource
    private AdvertisementConvert advertisementConvert;
    @Resource
    private AdvertisementMapper advertisementMapper;

    /**
     * 分页查询广告信息
     * @param pageQueryDTO 广告分页查询DTO
     * @return 广告VO
     */
    public IPage<AdvertisementVO> page(AdvertisementPageQueryDTO pageQueryDTO) {
        Page<Advertisement> advertisementPage = this.lambdaQuery()
                .like(StringUtils.isNotBlank(pageQueryDTO.getName()), Advertisement::getName, pageQueryDTO.getName())
                .eq(Objects.nonNull(pageQueryDTO.getPlatform()), Advertisement::getPlatform, pageQueryDTO.getPlatform())
                .eq(Objects.nonNull(pageQueryDTO.getLocation()), Advertisement::getLocation, pageQueryDTO.getLocation())
                .eq(Objects.nonNull(pageQueryDTO.getIsEnable()), Advertisement::getIsEnable, pageQueryDTO.getIsEnable())
                .ge(Objects.nonNull(pageQueryDTO.getAdvertisementEndTime()), Advertisement::getAdvertisementEndTime, pageQueryDTO.getAdvertisementEndTime())
                .le(Objects.nonNull(pageQueryDTO.getAdvertisementEndTimeEnd()), Advertisement::getAdvertisementEndTime, pageQueryDTO.getAdvertisementEndTimeEnd())
                .eq(Advertisement::getIsDeleted, Boolean.FALSE)
                .orderByAsc(Advertisement::getSort)
                .page(pageQueryDTO.page());
        return advertisementConvert.toVo(advertisementPage);
    }

    /**
     * 查询广告信息
     * @param advertisementQueryDTO 广告查询DTO
     * @return 广告列表
     */
    public List<AdvertisementSimpleVO> list(AdvertisementQueryDTO advertisementQueryDTO) {
        List<Advertisement> list = this.lambdaQuery()
                .eq(StringUtils.isNotBlank(advertisementQueryDTO.getName()), Advertisement::getName, advertisementQueryDTO.getName())
                .eq(Objects.nonNull(advertisementQueryDTO.getPlatform()), Advertisement::getPlatform, advertisementQueryDTO.getPlatform())
                .eq(Objects.nonNull(advertisementQueryDTO.getLocation()), Advertisement::getLocation, advertisementQueryDTO.getLocation())
                .eq(Advertisement::getIsEnable, Boolean.TRUE)
                .eq(Advertisement::getIsDeleted, Boolean.FALSE)
                .eq(Objects.nonNull(advertisementQueryDTO.getProductCategoryId()), Advertisement::getProductCategoryId, advertisementQueryDTO.getProductCategoryId())
                .ge(Objects.nonNull(advertisementQueryDTO.getAdvertisementStartTime()), Advertisement::getAdvertisementStartTime, advertisementQueryDTO.getAdvertisementStartTime())
                .le(Objects.nonNull(advertisementQueryDTO.getAdvertisementEndTime()), Advertisement::getAdvertisementEndTime, advertisementQueryDTO.getAdvertisementEndTime())
                .orderByAsc(Advertisement::getSort).list();
        return advertisementConvert.toSimpleVo(list);
    }

    /**
     * 根据id查询广告信息
     * @param id 广告id
     * @return 广告VO
     */
    public AdvertisementVO getOne(Serializable id) {
        return advertisementConvert.toVo(this.getById(id));
    }

    /**
     * 新增广告信息 排序按照创建时间倒序 最新增加的获取最小的sort-1
     * @param advertisementModifyDTO 广告保存DTO
     * @return Boolean
     */
    public Boolean save(AdvertisementModifyDTO advertisementModifyDTO) {
        Advertisement advertisement = advertisementConvert.toEntity(advertisementModifyDTO);
        if (advertisement.getIsEnable() == null) {
            advertisement.setIsEnable(Boolean.FALSE);
        }
        if (advertisement.getAdvertisementStartTime() == null) {
            advertisement.setAdvertisementStartTime(LocalDateTime.now());
        }
        if (advertisement.getAdvertisementEndTime() == null) {
            advertisement.setAdvertisementEndTime(getPermanentTime());
        }
        // 查询该平台、位置、分类中最小的sort
        Integer minSort = advertisementMapper.selectMinSort(advertisement);
        minSort = minSort == null ? 0 : minSort-1;
        advertisement.setSort(minSort);
        return this.save(advertisement);
    }

    /**
     * 根据id修改广告信息
     * 如果修改了平台platform或者位置location，需要重新设置排序值
     * @param advertisementModifyDTO 广告DTO
     * @param id 广告id
     * @return Boolean
     */
    public Boolean update(AdvertisementModifyDTO advertisementModifyDTO, Long id) {
        Advertisement advertisement = advertisementConvert.toEntity(advertisementModifyDTO);
        advertisement.setId(id);
        if (advertisement.getAdvertisementStartTime() == null) {
            advertisement.setAdvertisementStartTime(LocalDateTime.now());
        }
        if (advertisement.getAdvertisementEndTime() == null) {
            advertisement.setAdvertisementEndTime(getPermanentTime());
        }
        Advertisement one = this.lambdaQuery().eq(Advertisement::getId, id).one();
        BizAssert.notNull(one, "广告数据异常，找不到广告信息，广告id："+id);
        if (!one.getPlatform().equals(advertisement.getPlatform()) || !one.getLocation().equals(advertisement.getLocation())) {
            // 获取修改之后的平台+位置的最小排序值
            Integer minSort = advertisementMapper.selectMinSort(advertisement);
            advertisement.setSort(minSort-1);
        }
        return this.updateById(advertisement);
    }

    /**
     * 获取永久时间
     * @return LocalDateTime
     */
    public LocalDateTime getPermanentTime() {
        Calendar instance = Calendar.getInstance();
        instance.setTime(DateUtil.toDate(DateUtil.todayEnd()));
        instance.set(Calendar.YEAR, 9999);
        Date time = instance.getTime();
        return DateUtil.toLocalDateTime(time);
    }

    /**
     * 修改广告排序
     * @param advertisementSortModifyDTO 广告排序修改DTO
     * @return Boolean
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateSort(AdvertisementSortModifyDTO advertisementSortModifyDTO) {
        Advertisement advertisement = Advertisement.builder()
                .platform(advertisementSortModifyDTO.getPlatform())
                .location(advertisementSortModifyDTO.getLocation()).build();
        // 修改其他排序
        advertisementMapper.updateSort(advertisement, advertisementSortModifyDTO.getOldSort(), advertisementSortModifyDTO.getNewSort());
        // 修改选择的排序
        lambdaUpdate().set(Advertisement::getSort, advertisementSortModifyDTO.getNewSort()).eq(Advertisement::getId, advertisementSortModifyDTO.getId()).update();
        return true;
    }

    /**
     * 删除广告 将is_deleted置为true
     * @param idList 广告id
     * @return Boolean
     */
    public Boolean delete(List<Long> idList) {
        return this.removeByIds(idList);
    }
}

