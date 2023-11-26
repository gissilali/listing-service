package com.trivago

import org.valiktor.ConstraintViolationException
import org.valiktor.i18n.mapToMessage
import java.util.*
import kotlin.collections.ArrayList


fun parseValidationError(validationError: ConstraintViolationException): MutableMap<String, ArrayList<String>> {
    val errorsMap = hashMapOf<String, ArrayList<String>>()
    validationError.constraintViolations.mapToMessage(baseName = "messages", locale = Locale.ENGLISH).forEach {
        val errors = errorsMap[it.property]
        if (errors.isNullOrEmpty()) {
            errorsMap[it.property] = arrayListOf(it.message)
        } else {
            errors.add(it.message)
        }
    }
    return errorsMap
}