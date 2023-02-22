package com.pickdsm.pickserverspring.global.security

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {

    override fun addCorsMappings(registry: CorsRegistry) {
        registry
            .addMapping("/**")
            .allowedMethods("GET", "POST", "PATCH", "PUT", "DELETE")
            .allowedOrigins("https://service.xquare.app/", "http://localhost:3000")
    }
}
