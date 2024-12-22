package tkuik.alexkarav.tkuikstudent.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import tkuik.alexkarav.tkuikstudent.domain.repo.TimetableRepositoryImpl
import tkuik.alexkarav.tkuikstudent.utils.UIState
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repo: TimetableRepositoryImpl): ViewModel() {
    private val _authorized = MutableStateFlow<UIState>(UIState.Pending)
    val authorized = _authorized.asStateFlow()

    private val _groupList = MutableStateFlow<UIState>(UIState.Pending)
    val groupList = _groupList.asStateFlow()

    fun loadGroupList() {
        viewModelScope.launch {
            _groupList.value = UIState.Loading
            val groups = repo.getGroupList()
            if(groups.isSuccess) {
                _groupList.value = UIState.Success(groups.getOrDefault(listOf()))
            }
            else {
                _groupList.value = UIState.Error(groups.exceptionOrNull()?.message ?: "Произошла ошибка при загрузке списка групп. Проверьте подключение к интернету, и попробуйте позже")
            }
        }
    }

    fun authorizeUser(username: String, password: String, group: Int) {
        viewModelScope.launch {
            _authorized.value = UIState.Loading
            val result = repo.authorizeUser(username, password, group)
            if(result.isSuccess) {
                _authorized.value = UIState.Success(Unit)
            }
            else {
                _authorized.value = UIState.Error(result.exceptionOrNull()?.message ?: "Произошла ошибка при авторизации. Проверьте подключение к интернету, и попробуйте позже.")
            }
        }
    }
}