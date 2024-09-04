package com.eoi.Fruggy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class AppConfig {

    //@Bean de BCryptPasswordEncoder que será utilizado para la codificación de contraseñas.
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
         return new BCryptPasswordEncoder();
     }

}
