package com.trivago.utils

import org.valiktor.Constraint
import org.valiktor.ConstraintViolationException
import org.valiktor.Validator
import org.valiktor.i18n.mapToMessage
import java.net.MalformedURLException
import java.net.URISyntaxException
import java.net.URL
import java.util.*


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
object ValidUrl : Constraint
object LengthOfInt : Constraint

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

fun <E> Validator<E>.Property<String?>.isValidUrl() = this.validate(ValidUrl) {
    it == null ||
    try {
        URL(it).toURI()
        true
    } catch (e: MalformedURLException) {
        false
    } catch (e: URISyntaxException) {
        false
    }
}

fun <E> Validator<E>.Property<Int?>.hasLength(size: Int) = this.validate(LengthOfInt) {
    it.toString().length == size
}