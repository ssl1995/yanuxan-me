package com.msb.third.service;

import com.msb.framework.common.exception.BizException;
import com.msb.third.enums.ThirdExceptionCodeEnum;
import com.msb.third.model.vo.WxOAuth2UserInfoVO;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.bean.menu.WxMenuButton;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service("thirdWxMpService")
public class ThirdWxMpService {

    @Resource
    private WxMpService wxMpService;

    /**
     * 切换应用
     *
     * @param appId：公众号应用ID
     * @author peng.xy
     * @date 2022/5/12
     */
    private void switchoverAppId(String appId) {
        if (!this.wxMpService.switchover(appId)) {
            throw new BizException(ThirdExceptionCodeEnum.WX_APPID_EXCEPTION);
        }
    }

    /**
     * 微信服务器认证
     *
     * @param appId：app应用ID
     * @param signature：签名
     * @param timestamp：时间戳
     * @param nonce：随机数
     * @param echostr：随机字符串
     * @return java.lang.String
     * @author peng.xy
     * @date 2022/5/12
     */
    public String auth(String appId, String signature, String timestamp, String nonce, String echostr) {
        if (StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
            log.error("error argument");
            return "error argument";
        }
        if (!this.wxMpService.switchover(appId)) {
            log.error("error appId");
            return "error appId";
        }
        if (wxMpService.checkSignature(timestamp, nonce, signature)) {
            log.info("auth success");
            return echostr;
        }
        log.error("auth fail");
        return "error fail";
    }

    /**
     * 获取授权页面地址
     *
     * @param appId：app应用ID
     * @param redirectUrl：重定向回调页面地址
     * @param scope：授权作用域（snsapi_base/snsapi_userinfo）
     * @param state：重定向后state参数
     * @return java.lang.String
     * @author peng.xy
     * @date 2022/5/12
     */
    public String getAuthorizationUrl(String appId, String redirectUrl, String scope, String state) {
        this.switchoverAppId(appId);
        return wxMpService.getOAuth2Service().buildAuthorizationUrl(redirectUrl, scope, state);
    }

    /**
     * 获取用户openId
     *
     * @param appId：app应用ID
     * @param code：授权code
     * @return com.msb.mall.third.model.vo.WxOAuth2UserInfoVO
     * @author peng.xy
     * @date 2022/5/12
     */
    public WxOAuth2UserInfoVO getOpenId(@PathVariable String appId,
                                        @ApiParam(value = "授权code", required = true) @RequestParam(name = "code") String code) {
        this.switchoverAppId(appId);
        try {
            WxOAuth2AccessToken accessToken = wxMpService.getOAuth2Service().getAccessToken(code);
            return new WxOAuth2UserInfoVO(accessToken.getOpenId());
        } catch (WxErrorException e) {
            log.error("获取用户openId异常", e);
            throw new BizException(ThirdExceptionCodeEnum.WX_API_EXCEPTION);
        }
    }

    /**
     * 获取AccessToken
     *
     * @param appId：app应用ID
     * @return java.lang.String
     * @author peng.xy
     * @date 2022/5/12
     */
    public String getAccessToken(@PathVariable String appId) {
        this.switchoverAppId(appId);
        try {
            return wxMpService.getAccessToken();
        } catch (WxErrorException e) {
            log.error("获取AccessToken异常", e);
            throw new BizException(ThirdExceptionCodeEnum.WX_API_EXCEPTION);
        }
    }

    /**
     * 获取JsapiTicket
     *
     * @param appId：app应用ID
     * @return java.lang.String
     * @author peng.xy
     * @date 2022/5/12
     */
    public String getJsapiTicket(@PathVariable String appId) {
        this.switchoverAppId(appId);
        try {
            return wxMpService.getJsapiTicket();
        } catch (WxErrorException e) {
            log.error("获取JsapiTicket异常", e);
            throw new BizException(ThirdExceptionCodeEnum.WX_API_EXCEPTION);
        }
    }

    /**
     * 获取微信公众号用户信息
     *
     * @param appId：app应用ID
     * @param openId：openId
     * @return void
     * @author peng.xy
     * @date 2022/5/12
     */
    public WxMpUser getWxUserInfo(String appId, String openId) {
        this.switchoverAppId(appId);
        try {
            return wxMpService.getUserService().userInfo(openId);
        } catch (WxErrorException e) {
            log.error("获取微信公众号用户信息异常", e);
            return null;
        }
    }

    /**
     * 发送模板消息，参数数组
     *
     * @param appId：app应用ID
     * @param templateId：模板ID
     * @param openId：openId
     * @param url：跳转地址
     * @param first：消息头部标题
     * @param remark：消息备注
     * @param keywords：消息内容数组
     * @param keywordMap：消息内容Map
     * @return boolean
     * @author peng.xy
     * @date 2022/5/13
     */
    public boolean sendTemplateMessage(String appId, String templateId, String openId, String url, String first, String remark, String[] keywords, Map<String, String> keywordMap) {
        this.switchoverAppId(appId);
        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder().templateId(templateId).toUser(openId).url(url).build();
        templateMessage.addData(new WxMpTemplateData("first", first));
        templateMessage.addData(new WxMpTemplateData("remark", remark));
        if (ArrayUtils.isNotEmpty(keywords)) {
            for (int index = 0; index < keywords.length; index++) {
                templateMessage.addData(new WxMpTemplateData("keyword" + (index + 1), keywords[index]));
            }
        }
        if (MapUtils.isNotEmpty(keywordMap)) {
            Set<Map.Entry<String, String>> entrySet = keywordMap.entrySet();
            entrySet.stream().forEach(entry -> {
                templateMessage.addData(new WxMpTemplateData(entry.getKey(), entry.getValue()));
            });
        }
        try {
            wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
            return true;
        } catch (WxErrorException e) {
            log.error("发送微信公众号模板消息异常", e);
            return false;
        }
    }

    /**
     * 生成公众号菜单
     *
     * @param appId：app应用ID
     * @return java.lang.String
     * @author peng.xy
     * @date 2022/5/16
     */
    public Boolean createMenu(String appId) {
        this.switchoverAppId(appId);
        WxMenu wxMenu = new WxMenu();
        // 首页
        WxMenuButton home = new WxMenuButton();
        home.setName("首页");
        home.setUrl("https://you-app.mashibing.com");
        home.setType(WxConsts.MenuButtonType.VIEW);

        WxMenuButton center = new WxMenuButton();
        center.setName("个人中心");
        center.setType(WxConsts.MenuButtonType.VIEW);

        WxMenuButton order = new WxMenuButton();
        order.setName("交易订单");
        order.setType(WxConsts.MenuButtonType.VIEW);
        order.setUrl("https://you-app.mashibing.com/orderList");

        WxMenuButton saleAfter = new WxMenuButton();
        saleAfter.setName("售后订单");
        saleAfter.setType(WxConsts.MenuButtonType.VIEW);
        saleAfter.setUrl("https://you-app.mashibing.com/saleAfterList");

        center.getSubButtons().add(order);
        center.getSubButtons().add(saleAfter);

        wxMenu.setButtons(Arrays.asList(home, center));
        try {
            wxMpService.getMenuService().menuCreate(wxMenu);
            return true;
        } catch (WxErrorException e) {
            log.error("生成微信公众号菜单异常", e);
            return false;
        }
    }

}
