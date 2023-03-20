package com.pickdsm.pickserverspring.domain.classroom.error

import com.pickdsm.pickserverspring.common.error.ErrorProperty

enum class ClassroomErrorCode(
    private val status: Int,
    private val message: String,
) : ErrorProperty {

    CANNOT_MOVEMENT(400, "Cannot Movement"),

    CLASS_NOT_FOUND(404, "Class not found"),
    FLOOR_NOT_FOUND(404, "Floor not found"),
    CLASSROOM_MOVEMENT_STUDENT_NOT_FOUND(404, "Classroom Movement Student Not Found"), ;

    override fun status(): Int = status
    override fun message(): String = message
}
