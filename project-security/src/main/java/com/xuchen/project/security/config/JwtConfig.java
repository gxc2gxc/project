package com.xuchen.project.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import java.security.KeyPair;

/**
 * Jwt令牌相关配置
 */
@Configuration
public class JwtConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public KeyPair keyPair(JwtProperties jwtProperties) {
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(
                jwtProperties.getLocation(),
                jwtProperties.getPassword().toCharArray()
        );
        return keyStoreKeyFactory.getKeyPair(
                jwtProperties.getAlias(),
                jwtProperties.getPassword().toCharArray()
        );
    }
}
