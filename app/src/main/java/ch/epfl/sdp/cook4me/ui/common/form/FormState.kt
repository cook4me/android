package ch.epfl.sdp.cook4me.ui.common.form

class FormState(private vararg val states: TextFieldState /*Should probably be a more general type*/) {
    private fun formIsValid(): Boolean {
        return states.map { it.isValid }.reduce {x, y -> x && y}
    }

    private fun showErrors() {
        states.forEach { it.enableShowErrors() }
    }

    fun submitForm(storeForm: () -> Unit) {
        showErrors()
        if (formIsValid()) {
            storeForm(something)
        }
    }

}