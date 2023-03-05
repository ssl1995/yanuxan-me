package com.msb.pay.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.framework.common.exception.BizException;
import com.msb.pay.channel.IPaymentService;
import com.msb.pay.channel.context.ChannelContext;
import com.msb.pay.enums.DisabledEnum;
import com.msb.pay.mapper.MchInfoMapper;
import com.msb.pay.model.dto.MchInfoDTO;
import com.msb.pay.model.dto.MchInfoPageDTO;
import com.msb.pay.model.dto.UpdateMchInfoDTO;
import com.msb.pay.model.entity.MchInfo;
import com.msb.pay.model.vo.MchInfoPageVO;
import com.msb.pay.model.vo.MchInfoVO;
import com.msb.pay.model.vo.MchSelectorVO;
import com.msb.pay.service.convert.MchInfoConvert;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 商户信息表(MchInfo)表服务实现类
 *
 * @author makejava
 * @date 2022-06-06 10:42:43
 */
@Slf4j
@Service("mchInfoService")
public class MchInfoService extends ServiceImpl<MchInfoMapper, MchInfo> {

    @Resource
    private MchInfoConvert mchInfoConvert;
    @Lazy
    @Resource
    private NotifyService notifyService;

    @Override
    public MchInfo getById(Serializable mchPrimaryId) {
        return this.getByPrimaryIdOrThrow((Long) mchPrimaryId);
    }

    /**
     * 获取商户信息
     *
     * @param mchPrimaryId：商户主键ID
     * @return com.msb.pay.model.entity.MchInfo
     * @author peng.xy
     * @date 2022/6/6
     */
    public MchInfo getByPrimaryIdOrThrow(Long mchPrimaryId) {
        return super.lambdaQuery()
                .eq(MchInfo::getId, mchPrimaryId)
                .eq(MchInfo::getIsDisabled, DisabledEnum.ENABLED.getCode())
                .oneOpt().orElseThrow(() -> new BizException("商户信息不存在或已禁用"));
    }

    /**
     * 获取商户信息
     *
     * @param mchPrimaryId：商户主键ID
     * @return com.msb.pay.model.entity.MchInfo
     * @author peng.xy
     * @date 2022/6/6
     */
    public MchInfo getByPrimaryId(Long mchPrimaryId) {
        return super.lambdaQuery()
                .eq(MchInfo::getId, mchPrimaryId)
                .oneOpt().orElseThrow(() -> new BizException("商户信息不存在"));
    }

    /**
     * 获取商户信息
     *
     * @param mchPrimaryId：商户主键ID
     * @return com.msb.pay.model.entity.MchInfo
     * @author peng.xy
     * @date 2022/6/6
     */
    public MchInfo getByPrimaryIdOrNull(Long mchPrimaryId) {
        return super.lambdaQuery()
                .eq(MchInfo::getId, mchPrimaryId)
                .oneOpt().orElse(null);
    }

    /**
     * 商户选择列表
     *
     * @return java.util.List<com.msb.pay.model.vo.MchSelectorVO>
     * @author peng.xy
     * @date 2022/6/28
     */
    public List<MchSelectorVO> mchSelector() {
        List<MchInfo> list = super.lambdaQuery()
                .select(MchInfo::getId, MchInfo::getMchCode, MchInfo::getMchName)
                .eq(MchInfo::getIsDisabled, DisabledEnum.ENABLED.getCode()).list();
        return mchInfoConvert.toMchSelectorVOList(list);
    }

    /**
     * 获取商户信息Map
     *
     * @param mchPrimaryIds：商户ID列表
     * @return java.util.Map<java.lang.Long, com.msb.pay.model.entity.MchInfo>
     * @author peng.xy
     * @date 2022/6/28
     */
    public Map<Long, MchInfo> getMchInfoMap(List<Long> mchPrimaryIds) {
        if (CollectionUtils.isEmpty(mchPrimaryIds)) {
            return Collections.emptyMap();
        }
        List<MchInfo> list = super.lambdaQuery().in(MchInfo::getId, mchPrimaryIds).list();
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }
        return list.stream().collect(Collectors.toMap(MchInfo::getId, Function.identity()));
    }

    /**
     * 查询商户信息分页列表
     *
     * @param mchInfoPageDTO：参数
     * @return Page<com.msb.pay.model.vo.MchInfoPageVO>
     * @author peng.xy
     * @date 2022/6/27
     */
    @Transactional(readOnly = true)
    public Page<MchInfoPageVO> page(MchInfoPageDTO mchInfoPageDTO) {
        Page<MchInfo> entityPage = super.lambdaQuery()
                .eq(StringUtils.isNotBlank(mchInfoPageDTO.getMchCode()), MchInfo::getMchCode, mchInfoPageDTO.getMchCode())
                .like(StringUtils.isNotBlank(mchInfoPageDTO.getMchName()), MchInfo::getMchName, mchInfoPageDTO.getMchName())
                .like(StringUtils.isNotBlank(mchInfoPageDTO.getMchId()), MchInfo::getMchId, mchInfoPageDTO.getMchId())
                .eq(Objects.nonNull(mchInfoPageDTO.getIsDisabled()), MchInfo::getIsDisabled, mchInfoPageDTO.getIsDisabled())
                .orderByDesc(MchInfo::getId)
                .page(mchInfoPageDTO.page());
        return mchInfoConvert.toMchInfoPage(entityPage);
    }

    /**
     * 查询商户详情信息
     *
     * @param mchPrimaryId：商户主键ID
     * @return com.msb.pay.model.vo.MchInfoInfoVO
     * @author peng.xy
     * @date 2022/6/28
     */
    @Transactional(readOnly = true)
    public MchInfoVO mchInfo(Long mchPrimaryId) {
        // 获取商户信息
        MchInfo mchInfo = this.getByPrimaryId(mchPrimaryId);
        MchInfoVO mchInfoVO = mchInfoConvert.toMchInfoInfoVO(mchInfo);
        // 获取支付通道
        IPaymentService paymentService = ChannelContext.getIPaymentService(mchInfo.getMchCode());
        // 获取商户资料，对数据脱敏
        paymentService.parseMchInfoData(mchInfo, mchInfoVO);
        return mchInfoVO;
    }

    /**
     * 新增商户信息
     *
     * @param mchInfoDTO：新增参数
     * @return boolean
     * @author peng.xy
     * @date 2022/6/28
     */
    public boolean saveMchInfo(MchInfoDTO mchInfoDTO) {
        log.info("新增商户信息：{}", mchInfoDTO);
        MchInfo mchInfo = mchInfoConvert.toMchInfo(mchInfoDTO);
        mchInfo.setMchType(1);
        // 获取支付通道
        IPaymentService paymentService = ChannelContext.getIPaymentService(mchInfo.getMchCode());
        boolean flag = paymentService.saveMchInfoData(mchInfo, mchInfoDTO);
        boolean save = super.save(mchInfo);
        // 发送商户证书同步
        if (flag) {
            notifyService.sendMchCertSyncNotify(mchInfo.getId());
        }
        return save;
    }

    /**
     * 修改商户信息
     *
     * @param updateMchInfoDTO：修改参数
     * @return boolean
     * @author peng.xy
     * @date 2022/6/28
     */
    public boolean updateMchInfo(UpdateMchInfoDTO updateMchInfoDTO) {
        log.info("修改商户信息：{}", updateMchInfoDTO);
        // 获取原数据
        MchInfo originalMchInfo = this.getByPrimaryId(updateMchInfoDTO.getMchPrimaryId());
        // 复制商户资料
        MchInfo mchInfo = mchInfoConvert.toMchInfo(updateMchInfoDTO);
        mchInfo.setMchCode(originalMchInfo.getMchCode());
        mchInfo.setMchData(originalMchInfo.getMchData());
        // 获取支付通道
        IPaymentService paymentService = ChannelContext.getIPaymentService(mchInfo.getMchCode());
        boolean flag = paymentService.saveMchInfoData(mchInfo, updateMchInfoDTO);
        boolean update = super.updateById(mchInfo);
        // 发送商户证书同步 = 证书同步，发送消息通知
        if (flag) {
            notifyService.sendMchCertSyncNotify(mchInfo.getId());
        }
        return update;
    }

}

