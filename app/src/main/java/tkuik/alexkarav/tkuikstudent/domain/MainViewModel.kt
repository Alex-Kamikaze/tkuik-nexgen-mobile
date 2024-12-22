package tkuik.alexkarav.tkuikstudent.domain

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import tkuik.alexkarav.tkuikstudent.domain.repo.TimetableRepositoryImpl
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repo: TimetableRepositoryImpl): ViewModel() {
    private val _splashReady = MutableStateFlow(false)
    val splashReady = _splashReady.asStateFlow()

    private val _showBottomBar = MutableStateFlow(false)
    val showBottomBar = _showBottomBar.asStateFlow()

    private val _skipAuthorization = MutableStateFlow(true)
    val skipAuthorization = _skipAuthorization.asStateFlow()

    init {
        viewModelScope.launch {
            Log.d("TKUIK_MAIN", "Main view model passed")
            val token = repo.getAuthToken()
            val group = repo.getUserGroup()
            val authRequired = token.isNotBlank() && token.isNotEmpty() && group != 0
            _skipAuthorization.value = authRequired
            _showBottomBar.value = !authRequired
            _splashReady.value = true
        }
    }

    @Deprecated(message = "Используйте setBottomBarVisibility для большей гибкости", replaceWith = ReplaceWith("setBottomBarVisibility", "mainViewModel.setBottomBarVisibility()"))
    fun toggleBottomBarVisibility() {
        _showBottomBar.value = !_showBottomBar.value
    }

    fun setBottomBarVisibility(value: Boolean) { _showBottomBar.value = value }
}