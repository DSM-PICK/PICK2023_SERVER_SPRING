package com.pickdsm.pickserverspring.domain.afterschool.usecase

import com.pickdsm.pickserverspring.common.annotation.UseCase
import com.pickdsm.pickserverspring.domain.afterschool.AfterSchool
import com.pickdsm.pickserverspring.domain.afterschool.api.AfterSchoolApi
import com.pickdsm.pickserverspring.domain.afterschool.api.dto.request.DomainCreateAfterSchoolStudentRequest
import com.pickdsm.pickserverspring.domain.afterschool.api.dto.request.DomainDeleteAfterSchoolStudentRequest
import com.pickdsm.pickserverspring.domain.afterschool.api.dto.response.QueryAfterSchoolStudentList
import com.pickdsm.pickserverspring.domain.afterschool.api.dto.response.QueryAfterSchoolStudentList.QueryAfterSchoolStudentElement
import com.pickdsm.pickserverspring.domain.afterschool.exception.AfterSchoolNotFoundException
import com.pickdsm.pickserverspring.domain.afterschool.exception.AfterSchoolStudentExistsException
import com.pickdsm.pickserverspring.domain.afterschool.spi.CommandAfterSchoolSpi
import com.pickdsm.pickserverspring.domain.afterschool.spi.QueryAfterSchoolSpi
import com.pickdsm.pickserverspring.domain.user.User
import com.pickdsm.pickserverspring.domain.user.dto.request.UserInfoRequest
import com.pickdsm.pickserverspring.domain.user.exception.UserNotFoundException
import com.pickdsm.pickserverspring.domain.user.spi.UserSpi
import java.util.UUID

@UseCase
class AfterSchoolUseCase(
    private val commandAfterSchoolSpi: CommandAfterSchoolSpi,
    private val queryAfterSchoolSpi: QueryAfterSchoolSpi,
    private val userSpi: UserSpi,
) : AfterSchoolApi {

    override fun deleteAfterSchoolStudent(domainDeleteAfterSchoolStudentRequest: DomainDeleteAfterSchoolStudentRequest) {
        val afterSchool = queryAfterSchoolSpi.findByAfterSchoolIdAndStudentId(
            domainDeleteAfterSchoolStudentRequest.afterSchoolId,
            domainDeleteAfterSchoolStudentRequest.studentId,
        ) ?: throw AfterSchoolNotFoundException

        commandAfterSchoolSpi.deleteByAfterSchoolIdAndStudentId(afterSchool.id)
    }

    override fun createAfterSchoolStudent(request: DomainCreateAfterSchoolStudentRequest) {
        val afterSchoolInfo = queryAfterSchoolSpi.queryAfterSchoolInfoByAfterSchoolId(request.afterSchoolId)
            ?: throw AfterSchoolNotFoundException

        val studentIdList = request.studentIds.distinct()

        if (queryAfterSchoolSpi.existsAfterSchoolByStudentIds(studentIdList)) {
            throw AfterSchoolStudentExistsException
        }

        val afterSchools = studentIdList.map {
            AfterSchool(
                afterSchoolInfoId = afterSchoolInfo.id,
                studentId = it
            )
        }
        commandAfterSchoolSpi.saveAll(afterSchools)
    }

    override fun getAfterSchoolStudents(afterSchoolId: UUID): QueryAfterSchoolStudentList {
        val afterSchoolInfo = queryAfterSchoolSpi.queryAfterSchoolInfoByAfterSchoolId(afterSchoolId)
            ?: throw AfterSchoolNotFoundException
        val afterSchoolList = queryAfterSchoolSpi.queryAfterSchoolListByAfterSchoolId(afterSchoolId)
        val afterSchoolStudentIdList = afterSchoolList.map { it.studentId }
        val userIdRequest = UserInfoRequest(afterSchoolStudentIdList)
        val afterSchoolStudentInfos = userSpi.queryUserInfo(userIdRequest)

        val afterSchoolUsers = afterSchoolList.map {
            val user = afterSchoolStudentInfos.find { user -> user.id == it.studentId }
                ?: throw UserNotFoundException

            QueryAfterSchoolStudentElement(
                studentId = user.id,
                studentNumber = "${user.grade}${user.classNum}${user.paddedUserNum()}",
                studentName = user.name,
            )
        }.sortedBy(QueryAfterSchoolStudentElement::studentNumber)

        return QueryAfterSchoolStudentList(
            afterSchoolName = afterSchoolInfo.afterSchoolName,
            afterSchoolUserList = afterSchoolUsers,
        )
    }

    private fun User.paddedUserNum(): String =
        this.num.toString().padStart(2, '0')
}
