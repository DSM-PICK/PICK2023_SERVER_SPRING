package com.pickdsm.pickserverspring.domain.user.spi

import java.util.UUID

interface UserSpi {
    fun getCurrentUserId(): UUID
}
