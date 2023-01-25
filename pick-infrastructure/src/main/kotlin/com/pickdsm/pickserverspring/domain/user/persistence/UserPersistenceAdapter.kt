package com.pickdsm.pickserverspring.domain.user.persistence

import com.pickdsm.pickserverspring.domain.user.spi.UserSpi
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class UserPersistenceAdapter : UserSpi {

    override fun getCurrentUserId(): UUID {
        return UUID.fromString(SecurityContextHolder.getContext().authentication.name)
    }
}