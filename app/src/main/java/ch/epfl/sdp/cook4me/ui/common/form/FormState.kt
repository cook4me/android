package ch.epfl.sdp.cook4me.ui.common.form

interface FormElementState {
    val isValid: Boolean
    val errorMessage: String
    fun showErrors(): Boolean
    fun enableShowErrors()
}

class FormState(private vararg val formElements: FormElementState) {
    val isValid: Boolean
        get() = formElements.all { it.isValid }

    fun enableShowErrors() {
        formElements.forEach { it.enableShowErrors() }
    }

    fun getFirstError() =
        formElements.first { !it.isValid }.errorMessage
}
