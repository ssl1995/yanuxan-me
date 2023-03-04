package com.msb.third.controller;

import com.msb.framework.common.utils.EqualsUtil;
import com.msb.framework.web.util.ResponseUtil;
import com.msb.third.enums.PlatformEnum;
import com.msb.third.model.dto.WxMpEventDTO;
import com.msb.third.model.vo.WxOAuth2UserInfoVO;
import com.msb.third.service.ThirdInfoService;
import com.msb.third.service.ThirdWxMpService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Api(tags = "微信公众号")
@RestController
@RequestMapping("/wx/mp")
public class WxMpController {

    @Resource
    private ThirdWxMpService thirdWxMpService;
    @Resource
    private ThirdInfoService thirdInfoService;

    @ApiOperation(value = "服务器配置认证", hidden = true)
    @GetMapping("/server/{appId}")
    public void auth(@PathVariable String appId,
                     @RequestParam(name = "signature", required = false) String signature,
                     @RequestParam(name = "timestamp", required = false) String timestamp,
                     @RequestParam(name = "nonce", required = false) String nonce,
                     @RequestParam(name = "echostr", required = false) String echostr,
                     HttpServletResponse response) {
        log.info("接收微信服务器认证消息：[{}, {}, {}, {}]", signature, timestamp, nonce, echostr);
        String auth = thirdWxMpService.auth(appId, signature, timestamp, nonce, echostr);
        ResponseUtil.write(response, auth);
    }

    @ApiOperation(value = "接收事件推送", hidden = true)
    @PostMapping("/server/{appId}")
    public void event(@PathVariable String appId, @RequestBody WxMpEventDTO wxMpEventDTO, HttpServletResponse response) {
        log.info("接收微信服务器事件推送，[{}, {}]", appId, wxMpEventDTO);
        String event = wxMpEventDTO.getEvent();
        if (EqualsUtil.anyEquals(event, "subscribe", "unsubscribe")) {
            thirdInfoService.bindThirdInfo(PlatformEnum.WX_MP, appId, wxMpEventDTO.getFromUserName(), StringUtils.equals(event, "subscribe"));
        }
        ResponseUtil.write(response, StringUtils.EMPTY);
    }

    @ApiOperation(value = "获取授权页面地址")
    @GetMapping("/getAuthorizationUrl/{appId}")
    public String getAuthorizationUrl(@PathVariable String appId,
                                      @ApiParam(value = "重定向回调页面地址", required = true) @RequestParam(name = "redirectUrl") String redirectUrl,
                                      @ApiParam(value = "授权作用域（snsapi_base/snsapi_userinfo）", required = true) @RequestParam(name = "scope") String scope,
                                      @ApiParam(value = "重定向后state参数") @RequestParam(name = "state", required = false) String state) {
        return thirdWxMpService.getAuthorizationUrl(appId, redirectUrl, scope, state);
    }

    @ApiOperation(value = "获取用户openId")
    @GetMapping("/getOpenId/{appId}")
    public WxOAuth2UserInfoVO getOpenId(@PathVariable String appId, @ApiParam(value = "授权code", required = true) @RequestParam(name = "code") String code) {
        return thirdWxMpService.getOpenId(appId, code);
    }

    @ApiOperation(value = "获取AccessToken")
    @GetMapping("/getAccessToken/{appId}")
    public String getAccessToken(@PathVariable String appId) {
        return thirdWxMpService.getAccessToken(appId);
    }

    @ApiOperation(value = "获取JsapiTicket")
    @GetMapping("/getJsapiTicket/{appId}")
    public String getJsapiTicket(@PathVariable String appId) {
        return thirdWxMpService.getJsapiTicket(appId);
    }

    @ApiOperation(value = "生成公众号菜单")
    @PostMapping("/createMenu/{appId}")
    public Boolean createMenu(@PathVariable String appId) {
        return thirdWxMpService.createMenu(appId);
    }

}
