package com.pickdsm.pickserverspring.domain.afterschool.usecase

import com.pickdsm.pickserverspring.common.annotation.UseCase
import com.pickdsm.pickserverspring.domain.afterschool.AfterSchool
import com.pickdsm.pickserverspring.domain.afterschool.api.AfterSchoolApi
import com.pickdsm.pickserverspring.domain.afterschool.api.dto.request.DomainCreateAfterSchoolStudentRequest
import com.pickdsm.pickserverspring.domain.afterschool.api.dto.request.DomainDeleteAfterSchoolStudentRequest
import com.pickdsm.pickserverspring.domain.afterschool.api.dto.response.QueryAfterSchoolStudentList
import com.pickdsm.pickserverspring.domain.afterschool.api.dto.response.QueryAfterSchoolStudentList.QueryAfterSchoolStudentElement
import com.pickdsm.pickserverspring.domain.afterschool.exception.AfterSchoolNotFoundException
import com.pickdsm.pickserverspring.domain.afterschool.exception.AfterSchoolStudentNotFoundException
import com.pickdsm.pickserverspring.domain.afterschool.spi.CommandAfterSchoolSpi
import com.pickdsm.pickserverspring.domain.afterschool.spi.QueryAfterSchoolSpi
import com.pickdsm.pickserverspring.domain.user.exception.UserNotFoundException
import com.pickdsm.pickserverspring.domain.user.spi.UserSpi
import java.util.*

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
        )

        require(!afterSchool?.studentId!!.equals(null)) { AfterSchoolStudentNotFoundException }
        require(!afterSchool.id.equals(null)) { AfterSchoolNotFoundException }

        commandAfterSchoolSpi.deleteByAfterSchoolIdAndStudentId(
            afterSchool.id,
            afterSchool.studentId,
        )
    }

    override fun createAfterSchoolStudent(request: DomainCreateAfterSchoolStudentRequest) {
        val afterSchoolInfo = queryAfterSchoolSpi.findByAfterSchoolInfoId(request.afterSchoolId)
            ?: throw AfterSchoolNotFoundException

        val afterSchools = request.studentIds.map {
            AfterSchool(
                afterSchoolInfoId = afterSchoolInfo.id,
                studentId = it
            )
        }
        commandAfterSchoolSpi.saveAll(afterSchools)
    }

    override fun getAfterSchoolStudents(afterSchoolId: UUID): QueryAfterSchoolStudentList {
        val afterSchoolInfo = queryAfterSchoolSpi.findByAfterSchoolInfoId(afterSchoolId)
            ?: throw AfterSchoolNotFoundException
        val afterSchoolList = queryAfterSchoolSpi.queryAfterSchoolListByAfterSchoolId(afterSchoolId)
        val afterSchoolStudentIdList = afterSchoolList.map { it.studentId }
        val afterSchoolStudentInfos = userSpi.queryUserInfo(afterSchoolStudentIdList)

        val afterSchoolUsers = afterSchoolList.map {
            val user = afterSchoolStudentInfos.find { user -> user.id == it.studentId }
                ?: throw UserNotFoundException

            QueryAfterSchoolStudentElement(
                studentId = user.id,
                studentNumber = "${user.grade}${user.classNum}${checkUserNumLessThanTen(user.num)}",
                studentName = user.name,
            )
        }

        return QueryAfterSchoolStudentList(
            afterSchoolName = afterSchoolInfo.afterSchoolName,
            afterSchoolUserList = afterSchoolUsers,
        )
    }

    private fun checkUserNumLessThanTen(userNum: Int) =
        if (userNum < 10) {
            "0$userNum"
        } else {
            userNum.toString()
        }
}
