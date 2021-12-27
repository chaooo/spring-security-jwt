package com.example.jwt.config.security;

/**
 * Description
 *
 * @author : Charles
 * @date : 2021/12/2
 */
public class ConstantKey {
    /**
     * 签名key
     */
    public static final String SIGNING_KEY = "Charles@Jwt!&Secret^#";
    /**
     * Token存入header的键名
     */
    public static final String TOKEN_NAME = "X-Token";
    /**
     * Token过期时间（分钟）
     */
    public static final Integer TOKEN_EXPIRE = 5;
}
