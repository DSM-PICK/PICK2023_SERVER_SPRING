package com.pickdsm.pickserverspring.domain.application.spi

import com.pickdsm.pickserverspring.domain.teacher.spi.StatusCommandTeacherSpi

interface StatusSpi : CommandStatusSpi, QueryStatusSpi, StatusCommandTeacherSpi
