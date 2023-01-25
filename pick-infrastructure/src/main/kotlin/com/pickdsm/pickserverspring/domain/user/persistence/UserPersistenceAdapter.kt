package com.pickdsm.pickserverspring.domain.user.persistence

import com.pickdsm.pickserverspring.domain.user.spi.UserSpi
import com.pickdsm.pickserverspring.global.annotation.Adapter
import org.springframework.security.core.context.SecurityContextHolder
import java.util.UUID

@Adapter
class UserPersistenceAdapter : UserSpi {

    override fun getCurrentUserId(): UUID = UUID.fromString(SecurityContextHolder.getContext().authentication.name)
}
