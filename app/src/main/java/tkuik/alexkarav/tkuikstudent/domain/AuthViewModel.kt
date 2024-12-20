package tkuik.alexkarav.tkuikstudent.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import tkuik.alexkarav.tkuikstudent.data.remote.models.GroupRemoteModel
import tkuik.alexkarav.tkuikstudent.domain.repo.TimetableRepositoryImpl
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repo: TimetableRepositoryImpl): ViewModel() {
    private val _authorized = MutableStateFlow(false)
    val authorized = _authorized.asStateFlow()

    private val _groupList = MutableStateFlow<List<GroupRemoteModel>>(listOf())
    val groupList = _groupList.asStateFlow()

    fun loadGroupList() {
        viewModelScope.launch {
            val groups = repo.getGroupList()
            if(groups.isSuccess) {
                _groupList.value = groups.getOrDefault(listOf())
            }
            else {
                _groupList.value = listOf()
            }
        }
    }

    fun authorizeUser(username: String, password: String, group: Int) {
        viewModelScope.launch {
            val result = repo.authorizeUser(username, password, group)
            _authorized.value = result.isSuccess
        }
    }
}