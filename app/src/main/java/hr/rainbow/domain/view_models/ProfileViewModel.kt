package hr.rainbow.domain.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hr.rainbow.domain.DataRepository
import hr.rainbow.domain.model.UserProfile
import hr.rainbow.util.Resource
import hr.rainbow.util.UiEvents
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val dataRepository: DataRepository
) : ViewModel() {

    private val _profileData = MutableStateFlow(ProfileState())
    val profileData = _profileData.asStateFlow()

    private val _loggedIn = MutableStateFlow(false)
    val loggedIn = _loggedIn.asStateFlow()

    private val _successfullyDeleted = MutableStateFlow(false)
    val successfullyDeleted = _successfullyDeleted.asStateFlow()

    private val _uiEvents = MutableSharedFlow<UiEvents>()
    val uiEvents = _uiEvents.asSharedFlow()


    fun login(email: String, password: String) {
        viewModelScope.launch {
            dataRepository.logIn(email, password).collect {
                when (it) {
                    is Resource.Success -> {
                        _loggedIn.value = true
                    }
                    is Resource.Error -> {
                        _loggedIn.value = false
                        _uiEvents.emit(
                            UiEvents.ShowSnackBar(
                                message = it.message ?: "Unexpected error occurred!",
                                action = "Ok"
                            )
                        )
                    }
                    is Resource.Loading -> {
                        _loggedIn.value = false
                    }
                }
            }
        }
    }

    fun register(userProfile: UserProfile, password: String, confirmPassword: String) {
        viewModelScope.launch {
            dataRepository.register(userProfile, password, confirmPassword).collect {
                when (it) {
                    is Resource.Success -> {
                        _loggedIn.value = true
                    }
                    is Resource.Error -> {
                        _loggedIn.value = false
                        _uiEvents.emit(
                            UiEvents.ShowSnackBar(
                                message = it.message ?: "Unexpected error occurred!",
                                action = "Ok"
                            )
                        )
                    }
                    is Resource.Loading -> {
                        _loggedIn.value = false
                    }
                }
            }
        }
    }

    fun fetchProfileData() {
        viewModelScope.launch {
            dataRepository.getProfile().collect {
                when (it) {
                    is Resource.Success -> {
                        it.data?.let { profile ->
                            _profileData.value = _profileData.value.copy(
                                profileData = profile,
                                isLoading = false
                            )
                        } ?: run {
                            _uiEvents.emit(
                                UiEvents.ShowSnackBar(
                                    message = "Unable to fetch data",
                                    action = "Ok"
                                )
                            )
                        }
                    }
                    is Resource.Error -> {
                        _profileData.value = _profileData.value.copy(
                            isLoading = false
                        )
                        _uiEvents.emit(
                            UiEvents.ShowSnackBar(
                                message = it.message ?: "Unexpected error occurred!",
                                action = "Ok"
                            )
                        )
                    }
                    is Resource.Loading -> {
                        _profileData.value = _profileData.value.copy(
                            isLoading = true
                        )
                    }
                }
            }
        }
    }

    fun updateProfile(userProfile: UserProfile) {
        viewModelScope.launch {
            userProfile.id = _profileData.value.profileData.id
            dataRepository.updateProfile(userProfile).collect {
                when (it) {
                    is Resource.Success -> {
                        it.data?.let {
                            _profileData.value = _profileData.value.copy(
                                profileData = userProfile,
                                isLoading = false
                            )
                        } ?: run {
                            _uiEvents.emit(
                                UiEvents.ShowSnackBar(
                                    message = "Unable to update profile",
                                    action = "Ok"
                                )
                            )
                        }
                    }
                    is Resource.Error -> {
                        _profileData.value = _profileData.value.copy(
                            isLoading = false
                        )
                        _uiEvents.emit(
                            UiEvents.ShowSnackBar(
                                message = it.message ?: "Unexpected error occurred!",
                                action = "Ok"
                            )
                        )
                    }
                    is Resource.Loading -> {
                        _profileData.value = _profileData.value.copy(
                            isLoading = true
                        )
                    }
                }
            }
        }
    }

    fun delete(userProfile: UserProfile) {
        viewModelScope.launch {
            dataRepository.deleteAccount(userProfile).collect {
                when (it) {
                    is Resource.Success -> {
                        _profileData.value = _profileData.value.copy(
                            isLoading = false
                        )

                        _uiEvents.emit(
                            UiEvents.ShowSnackBar(
                                message = "Account is successfully deleted!",
                                action = "Ok"
                            )
                        )
                        _successfullyDeleted.value = true
                    }
                    is Resource.Error -> {
                        _successfullyDeleted.value = false
                        _profileData.value = _profileData.value.copy(
                            isLoading = false
                        )
                        _uiEvents.emit(
                            UiEvents.ShowSnackBar(
                                message = it.message
                                    ?: "Unexpected error occurred! Password is not changed.",
                                action = "Ok"
                            )
                        )
                    }
                    is Resource.Loading -> {
                        _successfullyDeleted.value = false
                        _profileData.value = _profileData.value.copy(
                            isLoading = true
                        )
                    }
                }
            }
        }
    }

    fun changePassword(oldPassword: String, newPassword: String, confirmedNewPassword: String) {
        viewModelScope.launch {
            dataRepository.changePassword(
                oldPassword,
                newPassword,
                confirmedNewPassword
            ).collect {
                when (it) {
                    is Resource.Success -> {
                        it.data?.let {
                            _profileData.value = _profileData.value.copy(
                                isLoading = false
                            )
                            _uiEvents.emit(
                                UiEvents.ShowSnackBar(
                                    message = "Password successfully updated",
                                    action = "Ok"
                                )
                            )
                        } ?: run {
                            _uiEvents.emit(
                                UiEvents.ShowSnackBar(
                                    message = "Unable to change password",
                                    action = "Ok"
                                )
                            )
                        }
                    }
                    is Resource.Error -> {
                        _profileData.value = _profileData.value.copy(
                            isLoading = false
                        )
                        _uiEvents.emit(
                            UiEvents.ShowSnackBar(
                                message = it.message
                                    ?: "Unexpected error occurred! Password is not changed.",
                                action = "Ok"
                            )
                        )
                    }
                    is Resource.Loading -> {
                        _profileData.value = _profileData.value.copy(
                            isLoading = true
                        )
                    }
                }
            }
        }
    }
}