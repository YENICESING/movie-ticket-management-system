package com.rabbiter.cm.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
///////JwtUtil 是一个工具类，主要用于处理 JWT（JSON Web Token）的生成、验证和信息提取操作。
// 它使用 com.auth0.jwt 库来实现 JWT 的创建和解析功能，包含生成带有过期时间的签名令牌、验证令牌的有效性、从令牌中提取用户名等功能
// ，同时使用 slf4j 日志框架记录操作信息和异常信息。
/**
 * 签名与校验token工具类
 */
public class JwtUtil {
    static Logger log = LoggerFactory.getLogger(JwtUtil.class);
    /**
     * JWT验证过期时间 EXPIRE_TIME 分钟 暂定一小时
     */
    private static final long EXPIRE_TIME = 60 * 60 * 1000;

    /**
     * 校验token是否正确
     *
     * @param token  密钥
     * @param secret 用户的密码
     * @return 是否正确
     */
    public static boolean verify(String token, String username, String secret) {
        try {
            //根据密码生成JWT效验器
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("username", username)
                    .build();
            //效验TOKEN
            DecodedJWT jwt = verifier.verify(token);
            log.info("登录验证成功!");
            return true;
        } catch (Exception exception) {
            log.error("JwtUtil登录验证失败!");
            return false;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的用户名
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 生成token签名EXPIRE_TIME 分钟后过期
     *
     * @param username 用户名
     * @param secret   用户的密码
     * @return 加密的token
     */
    public static String sign(String username, String secret) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        // 附带username信息
        return JWT.create()
                .withClaim("username", username)
                .withExpiresAt(date)
                .sign(algorithm);

    }

    public static void main(String[] args) {
        /**
         * 测试生成一个token
         * 结果：eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MDc0ODI5OTYsInVzZXJuYW1lIjoi5ZGo5YWrIn0.UP6kFC0BofuX7FLoPDMWCQno-NhVuYA0NlQG8xgt2Rc
         */
        String sign = sign("周八", "f93643c0eacc54a5ee1783744466ab9e");
        log.warn("测试生成一个token\n" + sign);
    }

}
