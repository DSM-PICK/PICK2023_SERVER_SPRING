package com.pickdsm.pickserverspring.domain.selfstudydirector.persistence

import com.pickdsm.pickserverspring.domain.selfstudydirector.persistence.entity.SelfStudyDirectorEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface SelfStudyDirectorRepository : CrudRepository<SelfStudyDirectorEntity, UUID>
