package com.pickdsm.pickserverspring.domain.classroom.error

import com.pickdsm.pickserverspring.common.error.ErrorProperty

enum class ClassroomErrorCode(
    private val status: Int,
    private val message: String,
) : ErrorProperty {

    CANNOT_MOVEMENT(400, "Cannot Movement"),

    CANNOT_MOVEMENT_MY_CLASSROOM(401, "Cannot Movement My Classroom"),
    CANNOT_MOVEMEMENT_WEEKEND(401, "Cannot Movement Weekend"),

    CLASS_NOT_FOUND(404, "Class not found"),
    FLOOR_NOT_FOUND(404, "Floor not found"),
    CLASSROOM_MOVEMENT_STUDENT_NOT_FOUND(404, "Classroom Movement Student Not Found"),
    AFTER_SCHOOL_CANNOT_MOVEMENT(400, "After School Cannot Movement"), ;

    override fun status(): Int = status
    override fun message(): String = message
}
