package com.pickdsm.pickserverspring.domain.afterschool.spi

import com.pickdsm.pickserverspring.domain.afterschool.AfterSchool

interface QueryAfterSchoolSpi {

    fun queryAfterSchoolList(): List<AfterSchool>
}
