package com.xuchen.project.security.util;

import cn.hutool.core.convert.NumberWithFormat;
import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTValidator;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.xuchen.project.model.common.enums.ResponseStatus;
import com.xuchen.project.model.common.exception.ClientException;
import com.xuchen.project.model.security.constant.SecurityConstant;
import com.xuchen.project.security.config.JwtProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.util.Date;


@Component
public class JwtTool {

    @Autowired
    private JwtProperties jwtProperties;

    private final JWTSigner jwtSigner;

    public JwtTool(KeyPair keyPair) {
        this.jwtSigner = JWTSignerUtil.createSigner("rs256", keyPair);
    }

    /**
     * 根据用户Id生成Jwt令牌
     *
     * @param userId 用户Id
     * @return Jwt令牌
     */
    public String createToken(Long userId) {
        return JWT.create()
                .setPayload(SecurityConstant.PAYLOAD_NAME, userId)
                .setExpiresAt(new Date(System.currentTimeMillis() + jwtProperties.getTtl().toMillis()))
                .setSigner(jwtSigner)
                .sign();
    }

    /**
     * 解析Jwt令牌
     *
     * @param token Jwt令牌
     * @return 用户Id
     */
    public Long parseToken(String token) {
        // 用户是否登录
        if (token == null) {
            throw new ClientException(ResponseStatus.USER_NOLOGGING_EXCEPTION);
        }

        // 解析Token
        JWT jwt;
        try {
            jwt = JWT.of(token).setSigner(jwtSigner);
        } catch (Exception e) {
            throw new ClientException(ResponseStatus.INVALID_TOKEN_EXCEPTION);
        }

        // 校验Token
        if (!jwt.verify()) {
            throw new ClientException(ResponseStatus.INVALID_TOKEN_EXCEPTION);
        }

        // 检查过期时间
        try {
            JWTValidator.of(jwt).validateDate();
        } catch (ValidateException e) {
            throw new ClientException(ResponseStatus.EXPIRED_TOKEN_EXCEPTION);
        }

        // 检查数据格式
        NumberWithFormat payload = (NumberWithFormat) jwt.getPayload(SecurityConstant.PAYLOAD_NAME);
        if (payload == null) {
            throw new ClientException(ResponseStatus.INVALID_TOKEN_EXCEPTION);
        }

        // 解析Payload
        try {
            return payload.longValue();
        } catch (NumberFormatException e) {
            throw new ClientException(ResponseStatus.INVALID_TOKEN_EXCEPTION);
        }
    }
}