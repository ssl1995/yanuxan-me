package com.msb.third.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.third.enums.PlatformEnum;
import com.msb.third.mapper.ThirdInfoMapper;
import com.msb.third.model.entity.ThirdInfo;
import com.msb.third.service.convert.ThirdInfoConvert;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * 第三方授权表(ThirdInfo)表服务实现类
 *
 * @author makejava
 * @date 2022-05-12 18:09:47
 */
@Service("thirdInfoService")
public class ThirdInfoService extends ServiceImpl<ThirdInfoMapper, ThirdInfo> {

    @Resource
    private ThirdInfoConvert thirdInfoConvert;
    @Resource
    private ThirdWxMpService thirdWxMpService;

    /**
     * 绑定第三方授权信息
     *
     * @param platformEnum：第三方授权平台
     * @param appId：第三方应用ID
     * @param appUserId：第三方用户ID
     * @param userId：系统用户ID
     * @param isSubscribe：是否关注
     * @return boolean
     * @author peng.xy
     * @date 2022/5/13
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean bindThirdInfo(PlatformEnum platformEnum, String appId, String appUserId, Long userId, boolean isSubscribe) {
        // 创建第三方应用授权实体
        ThirdInfo thirdInfo = new ThirdInfo().setUserId(userId).setPlatformType(platformEnum.getCode())
                .setAppId(appId).setAppUserId(appUserId).setIsSubscribe(isSubscribe);
        // 如果为微信公众号，查询用户unionId，以及是否进行订阅
        if (!isSubscribe && Objects.equals(PlatformEnum.WX_MP.getCode(), platformEnum.getCode())) {
            WxMpUser wxUserInfo = thirdWxMpService.getWxUserInfo(appId, appUserId);
            if (Objects.nonNull(wxUserInfo)) {
                thirdInfo.setAppUnionId(wxUserInfo.getUnionId());
                thirdInfo.setIsSubscribe(wxUserInfo.getSubscribe());
            }
        }
        // 根据平台用户ID查询是否已经存在数据
        if (Objects.isNull(this.getByAppUserId(appUserId))) {
            return super.save(thirdInfo);
        }
        // 否则进行更新操作
        else {
            return this.updateByAppUserId(thirdInfo);
        }
    }

    public boolean bindThirdInfo(PlatformEnum platformEnum, String appId, String appUserId, boolean isSubscribe) {
        return this.bindThirdInfo(platformEnum, appId, appUserId, null, isSubscribe);
    }

    public boolean bindThirdInfo(PlatformEnum platformEnum, String appId, String appUserId, Long userId) {
        return this.bindThirdInfo(platformEnum, appId, appUserId, userId, false);
    }

    /**
     * 根据平台用户ID获取
     *
     * @param appUserId：平台用户ID
     * @return com.msb.mall.third.model.entity.ThirdInfo
     * @author peng.xy
     * @date 2022/5/12
     */
    public ThirdInfo getByAppUserId(String appUserId) {
        return super.lambdaQuery().eq(ThirdInfo::getAppUserId, appUserId).one();
    }

    /**
     * 根据平台ID和平台系统ID获取
     *
     * @param appId：平台应用ID
     * @param userId：系统用户ID
     * @return com.msb.mall.third.model.entity.ThirdInfo
     * @author peng.xy
     * @date 2022/5/12
     */
    public ThirdInfo getByAppIdAndUserId(String appId, Long userId) {
        return super.lambdaQuery().eq(ThirdInfo::getAppId, appId).eq(ThirdInfo::getUserId, userId).one();
    }

    /**
     * 根据平台用户ID更新
     *
     * @param thirdInfo：授权信息
     * @return boolean
     * @author peng.xy
     * @date 2022/5/12
     */
    private boolean updateByAppUserId(ThirdInfo thirdInfo) {
        return super.lambdaUpdate().eq(ThirdInfo::getAppUserId, thirdInfo.getAppUserId())
                .set(ThirdInfo::getPlatformType, thirdInfo.getPlatformType())
                .set(ThirdInfo::getAppId, thirdInfo.getAppId())
                .set(ThirdInfo::getIsSubscribe, thirdInfo.getIsSubscribe())
                .set(StringUtils.isNotBlank(thirdInfo.getAppUnionId()), ThirdInfo::getAppUnionId, thirdInfo.getAppUnionId())
                .set(Objects.nonNull(thirdInfo.getUserId()), ThirdInfo::getUserId, thirdInfo.getUserId())
                .set(ThirdInfo::getUpdateUser, thirdInfo.getUpdateUser())
                .set(ThirdInfo::getUpdateTime, thirdInfo.getUpdateTime())
                .update();
    }

}

