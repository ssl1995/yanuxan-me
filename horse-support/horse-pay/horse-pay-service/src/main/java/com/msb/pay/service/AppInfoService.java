package com.msb.pay.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.framework.common.exception.BizException;
import com.msb.framework.common.utils.ListUtil;
import com.msb.pay.channel.IPaymentService;
import com.msb.pay.channel.context.ChannelContext;
import com.msb.pay.enums.DisabledEnum;
import com.msb.pay.mapper.AppInfoMapper;
import com.msb.pay.model.dto.AppInfoDTO;
import com.msb.pay.model.dto.AppInfoPageDTO;
import com.msb.pay.model.dto.UpdateAppInfoDTO;
import com.msb.pay.model.entity.AppInfo;
import com.msb.pay.model.entity.MchInfo;
import com.msb.pay.model.vo.*;
import com.msb.pay.service.convert.AppInfoConvert;
import com.msb.pay.service.convert.MchInfoConvert;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 应用信息表(AppInfo)表服务实现类
 *
 * @author makejava
 * @date 2022-06-06 10:42:41
 */
@Slf4j
@Service("appInfoService")
public class AppInfoService extends ServiceImpl<AppInfoMapper, AppInfo> {

    @Resource
    private AppInfoConvert appInfoConvert;
    @Resource
    private MchInfoConvert mchInfoConvert;
    @Resource
    private MchInfoService mchInfoService;
    @Lazy
    @Resource
    private NotifyService notifyService;

    /**
     * 根据应用主键ID查询支付应用信息
     *
     * @param appPrimaryId：应用主键ID
     * @return com.msb.pay.model.entity.AppInfo
     * @author peng.xy
     * @date 2022/6/6
     */
    public AppInfo getByPrimaryIdOrThrow(Long appPrimaryId) {
        return super.lambdaQuery().eq(AppInfo::getId, appPrimaryId)
                .eq(AppInfo::getIsDisabled, DisabledEnum.ENABLED.getCode())
                .oneOpt().orElseThrow(() -> new BizException("支付应用不存在或已禁用"));
    }

    /**
     * 根据应用主键ID查询支付应用信息
     *
     * @param appPrimaryId：应用主键ID
     * @return com.msb.pay.model.entity.AppInfo
     * @author peng.xy
     * @date 2022/6/6
     */
    public AppInfo getByPrimaryId(Long appPrimaryId) {
        return super.lambdaQuery().eq(AppInfo::getId, appPrimaryId)
                .oneOpt().orElseThrow(() -> new BizException("应用信息不存在"));
    }

    /**
     * 根据应用主键ID查询支付应用信息
     *
     * @param appPrimaryId：应用主键ID
     * @return com.msb.pay.model.entity.AppInfo
     * @author peng.xy
     * @date 2022/6/6
     */
    public AppInfo getByPrimaryIdOrNull(Long appPrimaryId) {
        return super.lambdaQuery().eq(AppInfo::getId, appPrimaryId)
                .oneOpt().orElse(null);
    }

    /**
     * 根据应用代号查询支付应用信息
     *
     * @param appCode：应用代号
     * @return com.msb.pay.model.entity.AppInfo
     * @author peng.xy
     * @date 2022/6/6
     */
    public AppInfo getByCodeOrThrow(String appCode) {
        return super.lambdaQuery().eq(AppInfo::getAppCode, appCode)
                .eq(AppInfo::getIsDisabled, DisabledEnum.ENABLED.getCode())
                .oneOpt().orElseThrow(() -> new BizException("支付应用不存在或已禁用"));
    }

    /**
     * 根据应用代号查询支付应用信息
     *
     * @param appCode：应用代号
     * @return com.msb.pay.model.entity.AppInfo
     * @author peng.xy
     * @date 2022/6/6
     */
    public AppInfo getByCode(String appCode) {
        return super.lambdaQuery().eq(AppInfo::getAppCode, appCode)
                .eq(AppInfo::getIsDisabled, DisabledEnum.ENABLED.getCode())
                .one();
    }

    /**
     * 应用选择列表
     *
     * @return java.util.List<com.msb.pay.model.vo.AppSelectorVO>
     * @author peng.xy
     * @date 2022/7/5
     */
    public List<AppSelectorVO> appSelector() {
        List<AppInfo> list = super.lambdaQuery()
                .select(AppInfo::getId, AppInfo::getAppCode, AppInfo::getAppName)
                .eq(AppInfo::getIsDisabled, DisabledEnum.ENABLED.getCode()).list();
        return appInfoConvert.toAppSelectorVOList(list);
    }

    /**
     * 获取应用信息Map
     *
     * @param appPrimaryIds：应用ID列表
     * @return java.util.Map<java.lang.Long, com.msb.pay.model.entity.AppInfo>
     * @author peng.xy
     * @date 2022/6/28
     */
    public Map<Long, AppInfo> getAppInfoMap(List<Long> appPrimaryIds) {
        if (CollectionUtils.isEmpty(appPrimaryIds)) {
            return Collections.emptyMap();
        }
        List<AppInfo> list = super.lambdaQuery().in(AppInfo::getId, appPrimaryIds).list();
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }
        return list.stream().collect(Collectors.toMap(AppInfo::getId, Function.identity()));
    }

    /**
     * 查询应用信息分页列表
     *
     * @param appInfoPageDTO：参数
     * @return Page<com.msb.pay.model.vo.AppInfoPageVO>
     * @author peng.xy
     * @date 2022/6/28
     */
    @Transactional(readOnly = true)
    public Page<AppInfoPageVO> page(AppInfoPageDTO appInfoPageDTO) {
        Page<AppInfo> entityPage = super.lambdaQuery()
                .eq(Objects.nonNull(appInfoPageDTO.getMchPrimaryId()), AppInfo::getMchPrimaryId, appInfoPageDTO.getMchPrimaryId())
                .like(StringUtils.isNotBlank(appInfoPageDTO.getAppName()), AppInfo::getAppName, appInfoPageDTO.getAppName())
                .like(StringUtils.isNotBlank(appInfoPageDTO.getAppId()), AppInfo::getAppId, appInfoPageDTO.getAppId())
                .like(StringUtils.isNotBlank(appInfoPageDTO.getPayCode()), AppInfo::getPayCodes, appInfoPageDTO.getPayCode())
                .eq(Objects.nonNull(appInfoPageDTO.getIsDisabled()), AppInfo::getIsDisabled, appInfoPageDTO.getIsDisabled())
                .orderByDesc(AppInfo::getId)
                .page(appInfoPageDTO.page());
        Page<AppInfoPageVO> voPage = appInfoConvert.toAppInfoPage(entityPage);
        List<AppInfoPageVO> voList = voPage.getRecords();
        if (CollectionUtils.isEmpty(voList)) {
            return voPage;
        }
        Map<Long, MchInfo> mchInfoMap = mchInfoService.getMchInfoMap(ListUtil.convertDistinct(voList, AppInfoPageVO::getMchPrimaryId));
        if (MapUtils.isEmpty(mchInfoMap)) {
            return voPage;
        }
        voList.forEach(appInfoPageVO -> {
            MchInfo mchInfo = mchInfoMap.get(appInfoPageVO.getMchPrimaryId());
            appInfoPageVO.setMchInfo(mchInfoConvert.toMchSimpleVO(mchInfo));
        });
        return voPage;
    }

    /**
     * 查询应用详情信息
     *
     * @param appPrimaryId：应用主键ID
     * @return com.msb.pay.model.vo.AppInfoVO
     * @author peng.xy
     * @date 2022/6/28
     */
    @Transactional(readOnly = true)
    public AppInfoVO appInfo(Long appPrimaryId) {
        // 获取应用信息
        AppInfo appInfo = this.getByPrimaryId(appPrimaryId);
        AppInfoVO appInfoVO = appInfoConvert.toAppInfoVO(appInfo);
        // 获取商户信息
        MchInfo mchInfo = mchInfoService.getByPrimaryId(appInfoVO.getMchPrimaryId());
        MchSimpleInfoVO mchSimpleInfoVO = mchInfoConvert.toMchSimpleVO(mchInfo);
        appInfoVO.setMchInfo(mchSimpleInfoVO);
        // 获取支付通道
        IPaymentService paymentService = ChannelContext.getIPaymentService(mchInfo.getMchCode());
        paymentService.parseAppInfoData(appInfo, appInfoVO);
        return appInfoVO;
    }

    /**
     * 新增应用信息
     *
     * @param appInfoDTO：新增参数
     * @return boolean
     * @author peng.xy
     * @date 2022/6/28
     */
    public boolean saveAppInfo(AppInfoDTO appInfoDTO) {
        log.info("新增应用信息：{}", appInfoDTO);
        // 获取应用信息
        AppInfo appInfo = appInfoConvert.toAppInfo(appInfoDTO);
        // 获取商户信息
        MchInfo mchInfo = mchInfoService.getByPrimaryIdOrThrow(appInfoDTO.getMchPrimaryId());
        // 获取支付通道
        IPaymentService paymentService = ChannelContext.getIPaymentService(mchInfo.getMchCode());
        boolean flag = paymentService.saveAppInfoData(mchInfo, appInfo, appInfoDTO);
        boolean save = super.save(appInfo);
        // 发送证书同步消息
        if (flag) {
            notifyService.sendAppCertSyncNotify(appInfo.getId());
        }
        return save;
    }

    /**
     * 修改应用信息
     *
     * @param updateAppInfoDTO：新增参数
     * @return boolean
     * @author peng.xy
     * @date 2022/6/28
     */
    public boolean updateAppInfo(UpdateAppInfoDTO updateAppInfoDTO) {
        log.info("修改应用信息：{}", updateAppInfoDTO);
        // 获取原数据
        AppInfo originalAppInfo = this.getByPrimaryId(updateAppInfoDTO.getAppPrimaryId());
        // 复制应用资料
        AppInfo appInfo = appInfoConvert.toAppInfo(updateAppInfoDTO);
        appInfo.setMchPrimaryId(originalAppInfo.getMchPrimaryId());
        appInfo.setAppData(originalAppInfo.getAppData());
        // 获取商户信息
        MchInfo mchInfo = mchInfoService.getByPrimaryId(originalAppInfo.getMchPrimaryId());
        // 获取支付通道
        IPaymentService paymentService = ChannelContext.getIPaymentService(mchInfo.getMchCode());
        boolean flag = paymentService.saveAppInfoData(mchInfo, appInfo, updateAppInfoDTO);
        boolean update = super.updateById(appInfo);
        // 发送证书同步消息
        if (flag) {
            notifyService.sendAppCertSyncNotify(appInfo.getId());
        }
        return update;
    }

    /**
     * 获取预支付应用
     *
     * @return com.msb.pay.model.vo.PrepayWxAppVO
     * @author peng.xy
     * @date 2022/7/6
     */
    public PrepayAppVO getPrepayApp(String prepayAppCode) {
        AppInfo appInfo = this.getByCode(prepayAppCode);
        if (Objects.isNull(appInfo)) {
            return null;
        }
        return new PrepayAppVO()
                .setPrepayAppId(appInfo.getAppId())
                .setPrepayAppCode(appInfo.getAppCode());
    }

}

