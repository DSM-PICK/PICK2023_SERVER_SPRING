package com.pickdsm.pickserverspring.domain.selfstudydirector.spi

import com.pickdsm.pickserverspring.domain.selfstudydirector.SelfStudyDirector

interface CommandSelfStudyDirectorSpi {
    fun setRestrictionMovementTrue(selfStudyDirector: SelfStudyDirector)

    fun updateSelfStudyDirector(selfStudyDirector: SelfStudyDirector)

    fun setRestrictionMovementFalse(selfStudyDirector: SelfStudyDirector)

    fun saveSelfStudyDirector(selfStudyDirector: SelfStudyDirector)
}
