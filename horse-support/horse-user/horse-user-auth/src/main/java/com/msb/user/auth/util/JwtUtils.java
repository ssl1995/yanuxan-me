package com.msb.user.auth.util;

import com.msb.framework.common.utils.DateUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.Optional;


/**
 * @author liao
 */
@Slf4j
@Component
public final class JwtUtils {

    private JwtUtils() {
    }

    public static String generateToken(Map<String, Object> user, PrivateKey privateKey, Long refreshExpiration) {
        return Jwts.builder()
                .setClaims(user)
                .setExpiration(DateUtil.toDate(LocalDateTime.now().plusMinutes(refreshExpiration)))
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact();
    }

    public static String generateToken(Map<String, Object> user, PrivateKey privateKey) {

        return Jwts.builder()
                .setClaims(user)
                .setExpiration(DateUtil.toDate(LocalDate.of(2099, 1, 1)))
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact();
    }


    /**
     * 使用公钥解析token
     */
    public static Jws<Claims> parserToken(String token, PublicKey publicKey) {
        return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);
    }


    /**
     * 从token中获取过期时间
     */
    private static Date getExpiredDateFromToken(String token, PublicKey publicKey) {
        Claims claims = parserToken(token, publicKey).getBody();
        return Optional.ofNullable(claims).map(Claims::getExpiration).orElse(null);
    }


    /**
     * 判断token是否已经失效
     */
    public static boolean isTokenExpired(String token, PublicKey publicKey) {
        Date expiredDate = getExpiredDateFromToken(token, publicKey);
        return Optional.ofNullable(expiredDate).map(obj -> obj.before(new Date())).orElse(false);
    }

}
