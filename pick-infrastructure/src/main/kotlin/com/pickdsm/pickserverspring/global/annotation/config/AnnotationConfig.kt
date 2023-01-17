package com.pickdsm.pickserverspring.global.annotation.config

import com.pickdsm.pickserverspring.common.annotation.ReadOnlyUseCase
import com.pickdsm.pickserverspring.common.annotation.UseCase
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.FilterType

@Configuration
@ComponentScan(
    basePackages = ["com.pickdsm.pickserverspring"],
    includeFilters = [
        ComponentScan.Filter(
            type = FilterType.ANNOTATION,
            value = [UseCase::class]
        ),
        ComponentScan.Filter(
            type = FilterType.ANNOTATION,
            value = [ReadOnlyUseCase::class]
        )
    ]
)
class AnnotationConfig