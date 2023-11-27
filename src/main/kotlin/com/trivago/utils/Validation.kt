package com.trivago.utils

import org.valiktor.Constraint
import org.valiktor.ConstraintViolationException
import org.valiktor.Validator
import org.valiktor.i18n.mapToMessage
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.List


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

object ValidUUID : Constraint

object Keyword : Constraint


fun <E> Validator<E>.Property<String?>.doesNotHaveKeyword(keywords: List<String>) =
    this.validate(Keyword) { inputString ->
        !keywords.any { keyword ->
            inputString?.contains(keyword, ignoreCase = true) == true
        }
    }


fun <E> Validator<E>.Property<String?>.isValidUUID() = this.validate(ValidUUID) {
    it == null || try {
        UUID.fromString(it)
        true
    } catch (e: Throwable) {
        false
    }
}