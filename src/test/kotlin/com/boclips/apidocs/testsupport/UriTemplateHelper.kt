package com.boclips.apidocs.testsupport

object UriTemplateHelper {
    fun stripOptionalParameters(template: String?) = template!!.substringBefore("{?")
}
