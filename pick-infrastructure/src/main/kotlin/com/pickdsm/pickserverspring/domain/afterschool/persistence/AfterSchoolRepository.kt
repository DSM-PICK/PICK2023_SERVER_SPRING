package com.pickdsm.pickserverspring.domain.afterschool.persistence

import com.pickdsm.pickserverspring.domain.afterschool.persistence.entity.AfterSchoolEntity
import com.pickdsm.pickserverspring.domain.application.persistence.entity.ApplicationEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface AfterSchoolRepository : CrudRepository<AfterSchoolEntity, UUID>
