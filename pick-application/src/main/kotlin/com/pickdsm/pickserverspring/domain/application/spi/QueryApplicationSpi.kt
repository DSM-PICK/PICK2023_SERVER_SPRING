package com.pickdsm.pickserverspring.domain.application.spi

import com.pickdsm.pickserverspring.domain.application.Application
import java.util.*

interface QueryApplicationSpi {

    fun queryPicnicApplicationList(): List<Application>

    fun queryAllStudentId(): List<UUID>
}
