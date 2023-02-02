package com.pickdsm.pickserverspring.domain.teacher.spi

import com.pickdsm.pickserverspring.domain.application.Status

interface StatusCommandTeacherSpi {

    fun saveAllStatus(statusList: List<Status>)
    
    fun saveStatus(status: Status)
}
