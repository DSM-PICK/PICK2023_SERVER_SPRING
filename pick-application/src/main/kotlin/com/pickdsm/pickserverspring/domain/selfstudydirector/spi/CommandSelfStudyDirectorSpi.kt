package com.pickdsm.pickserverspring.domain.selfstudydirector.spi

import com.pickdsm.pickserverspring.domain.selfstudydirector.SelfStudyDirector

interface CommandSelfStudyDirectorSpi {
    fun updateSelfStudyDirector(selfStudyDirector: SelfStudyDirector)
}
