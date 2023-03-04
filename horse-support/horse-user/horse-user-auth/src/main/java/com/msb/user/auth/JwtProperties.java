package com.msb.user.auth;

import com.msb.user.auth.util.RsaUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.security.PrivateKey;
import java.security.PublicKey;


@Slf4j
@Data
@ConfigurationProperties(prefix = "jwt")
@Configuration
public class JwtProperties {

    /**
     * 是否开启自解析token，建议在生产环境关闭
     */
    private Boolean selfDecryptToken = false;
    /**
     * 是否为客户端
     */
    private boolean client = true;
    /**
     * 密文
     */
    private String secret;
    /**
     * 公钥串
     */
    private String publicKeyStr;
    /**
     * 私钥串
     */
    private String privateKeyStr;
    /**
     * token过期时间
     */
    private Integer expiration;
    /**
     * refreshToken过期时间
     */
    private Integer refreshExpiration;
    /**
     * 跳过的url
     */
    private String[] skipAuthUrls;
    /**
     * 公钥
     */
    private PublicKey publicKey;
    /**
     * 私钥
     */
    private PrivateKey privateKey;
    /**
     * 请求头的默认前缀
     */
    private String tokenHead = "Bearer ";
    /**
     * jwt token 的认证头部
     */
    private String tokenHeader = "Authorization";
    /**
     * jwt 刷新token 的认证头部
     */
    private String refreshTokenHeader = "R-Authorization";
    /**
     * 服务之间的用户认证头部
     */
    private String userInfoHeader = "UserInfo";


    @PostConstruct
    public void init() {
        try {
            if (client) {
                if (StringUtils.isEmpty(publicKeyStr)) {
                    throw new RuntimeException("JWT客户端的公钥串不能为空");
                }
                // 获取公钥和私钥
                this.publicKey = RsaUtils.getPublicKey(Base64.decodeBase64(publicKeyStr));
            } else {
                if (StringUtils.isEmpty(publicKeyStr) || StringUtils.isEmpty(privateKeyStr)) {
                    throw new RuntimeException("JWT服务端的公钥私钥对不能为空");
                }
                // 获取公钥和私钥
                this.publicKey = RsaUtils.getPublicKey(Base64.decodeBase64(publicKeyStr));
                this.privateKey = RsaUtils.getPrivateKey(Base64.decodeBase64(privateKeyStr));
            }
        } catch (Exception e) {
            log.error("初始化公钥和私钥失败! " + e);
            throw new RuntimeException("初始化公钥和私钥失败");
        }
    }
}
