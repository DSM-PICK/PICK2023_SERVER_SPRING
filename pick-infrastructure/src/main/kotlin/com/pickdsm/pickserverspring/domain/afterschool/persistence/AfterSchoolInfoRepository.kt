package com.pickdsm.pickserverspring.domain.afterschool.persistence

import com.pickdsm.pickserverspring.domain.afterschool.persistence.entity.AfterSchoolInfoEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AfterSchoolInfoRepository : CrudRepository<AfterSchoolInfoEntity, UUID>
