package tkuik.alexkarav.tkuikstudent.utils

sealed class UIState {
    data object Loading: UIState()
    data object Pending: UIState()
    data class Error(val message: String): UIState()
    data class Success<T>(val data: T): UIState()
}