package com.pickdsm.pickserverspring.common.security

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {

    override fun addCorsMappings(registry: CorsRegistry) {
        registry
            .addMapping("/**")
            .allowedMethods("*")
            .allowedHeaders("*")
            .allowedOrigins(
                "https://service.xquare.app/",
                "http://localhost:3000",
                "http://localhost:3001",
                "https://admin.dsm-pick.com",
                "https://teacher.dsm-pick.com",
                "https://keeper.dsm-pick.com",
                "chrome-extension://apjpfknndginnahobenblkdfbibckcom",
                "chrome-extension://llbipbgiobbnjgkomnlpjidhebiokimc"
            )
    }
}
