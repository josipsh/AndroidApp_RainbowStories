package hr.rainbow.domain.view_models


import com.google.common.truth.Truth.assertThat
import hr.rainbow.data.FakeDataRepository
import hr.rainbow.domain.model.UserProfile
import hr.rainbow.util.MainCoroutineRule
import hr.rainbow.util.UiEvents
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ProfileViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: ProfileViewModel
    private lateinit var repository: FakeDataRepository

    @Before
    fun setup() {
        repository = FakeDataRepository()
        viewModel = ProfileViewModel(repository)
    }

    @After
    fun teardown() {
    }


//PROFILE TEST

    @Test
    fun `profile - isLoading should be true when fetching is started`() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = true, isErrorOn = false)

        var isLoading = false
        val job = launch {
            viewModel.profileData.collect {
                isLoading = it.isLoading
            }
        }

        viewModel.fetchProfileData()
        assertThat(isLoading).isTrue()
        job.cancel()
    }

    @Test
    fun `profile - isLoading should be false when fetching is done`() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = false, isErrorOn = false)

        var isLoading = true
        val job = launch {
            viewModel.profileData.collect {
                isLoading = it.isLoading
            }
        }

        viewModel.fetchProfileData()
        assertThat(isLoading).isFalse()
        job.cancel()
    }

    @Test
    fun `profile - should get data when all is good`() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = false, isErrorOn = false)

        var profile: UserProfile? = null
        val job = launch {
            viewModel.profileData.collect {
                profile = it.profileData
            }
        }

        viewModel.fetchProfileData()
        assertThat(profile).isNotNull()
        job.cancel()
    }

    @Test
    fun `profile - if there is error while fetching profile snackBar event should be emitted`() =
        runBlockingTest {
            repository.configureFakeRepository(isLoadingOn = false, isErrorOn = true)

            var event: UiEvents? = null
            val job = launch {
                viewModel.uiEvents.collect {
                    event = it
                }
            }

            viewModel.fetchProfileData()
            assertThat(event).isNotNull()
            job.cancel()
        }


//PROFILE UPDATE

    @Test
    fun `profile - isLoading should be true when update is started`() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = true, isErrorOn = false)

        var isLoading = false
        val job = launch {
            viewModel.profileData.collect {
                isLoading = it.isLoading
            }
        }

        viewModel.updateProfile(UserProfile(0, "", "", "", "", ""))
        assertThat(isLoading).isTrue()
        job.cancel()
    }

    @Test
    fun `profile - isLoading should be false when update is done`() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = false, isErrorOn = false)

        var isLoading = true
        val job = launch {
            viewModel.profileData.collect {
                isLoading = it.isLoading
            }
        }

        viewModel.updateProfile(UserProfile(0, "", "", "", "", ""))
        assertThat(isLoading).isFalse()
        job.cancel()
    }

    @Test
    fun `profile - after successful update we should get new data`() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = false, isErrorOn = false)

        val newProfile = UserProfile(
            id = 10,
            firstName = "Iva",
            lastName = "Ivi",
            bio = "This is bio",
            email = "iva.ivi@gmail.com",
            nickName = "iva"
        )
        var profile: UserProfile? = null
        val job = launch {
            viewModel.profileData.collect {
                profile = it.profileData
            }
        }

        viewModel.updateProfile(newProfile)
        assertThat(profile).isEqualTo(newProfile)
        job.cancel()
    }

    @Test
    fun `profile - if there is error while updating profile snackBar event should be emitted`() =
        runBlockingTest {
            repository.configureFakeRepository(isLoadingOn = false, isErrorOn = true)

            var event: UiEvents? = null
            val job = launch {
                viewModel.uiEvents.collect {
                    event = it
                }
            }

            viewModel.updateProfile(UserProfile(0, "", "", "", "", ""))
            assertThat(event).isNotNull()
            job.cancel()
        }


//REGISTER

    @Test
    fun `register - when registration is started loggedIn should be false`() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = true, isErrorOn = false)

        var loggedIn = true
        val job = launch {
            viewModel.loggedIn.collect {
                loggedIn = it
            }
        }

        viewModel.register(
            UserProfile(0, "", "", "", "", ""),
            "",
            ""
        )
        assertThat(loggedIn).isFalse()
        job.cancel()
    }

    @Test
    fun `register - if register is successful loggedIn should be true`() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = false, isErrorOn = false)

        var loggedIn = false
        val job = launch {
            viewModel.loggedIn.collect {
                loggedIn = it
            }
        }

        viewModel.register(
            UserProfile(0, "", "", "", "", ""),
            "",
            ""
        )
        assertThat(loggedIn).isTrue()
        job.cancel()
    }

    @Test
    fun `register - if register is not successful loggedIn should be false`() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = false, isErrorOn = true)

        var loggedIn = true
        val job = launch {
            viewModel.loggedIn.collect {
                loggedIn = it
            }
        }

        viewModel.register(
            UserProfile(0, "", "", "", "", ""),
            "",
            ""
        )
        assertThat(loggedIn).isFalse()
        job.cancel()
    }

    @Test
    fun `register - if there is error while registering profile snackBar event should be emitted`() =
        runBlockingTest {
            repository.configureFakeRepository(isLoadingOn = false, isErrorOn = true)

            var event: UiEvents? = null
            val job = launch {
                viewModel.uiEvents.collect {
                    event = it
                }
            }

            viewModel.register(
                UserProfile(0, "", "", "", "", ""),
                "",
                ""
            )

            assertThat(event).isNotNull()
            job.cancel()
        }


//LOG In

    @Test
    fun `login - when logIn() is started loggedIn should be false`() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = true, isErrorOn = false)

        var loggedIn = true
        val job = launch {
            viewModel.loggedIn.collect {
                loggedIn = it
            }
        }

        viewModel.login(
            "",
            ""
        )
        assertThat(loggedIn).isFalse()
        job.cancel()
    }

    @Test
    fun `login - if logIn() is successful loggedIn should be true`() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = false, isErrorOn = false)

        var loggedIn = false
        val job = launch {
            viewModel.loggedIn.collect {
                loggedIn = it
            }
        }

        viewModel.login(
            "",
            ""
        )
        assertThat(loggedIn).isTrue()
        job.cancel()
    }

    @Test
    fun `login - if login() is not successful loggedIn should be false`() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = false, isErrorOn = true)

        var loggedIn = true
        val job = launch {
            viewModel.loggedIn.collect {
                loggedIn = it
            }
        }

        viewModel.login(
            "",
            ""
        )
        assertThat(loggedIn).isFalse()
        job.cancel()
    }

    @Test
    fun `log in - if there is error while log in-a snackBar event should be emitted`() =
        runBlockingTest {
            repository.configureFakeRepository(isLoadingOn = false, isErrorOn = true)

            var event: UiEvents? = null
            val job = launch {
                viewModel.uiEvents.collect {
                    event = it
                }
            }

            viewModel.login(
                "",
                ""
            )

            assertThat(event).isNotNull()
            job.cancel()
        }


//CHANGE PASSWORD

    @Test
    fun `changing password - isLoading should be true when changePassword() is started`() =
        runBlockingTest {
            repository.configureFakeRepository(isLoadingOn = true, isErrorOn = false)

            var isLoading = false
            val job = launch {
                viewModel.profileData.collect {
                    isLoading = it.isLoading
                }
            }

            viewModel.changePassword(
                "",
                "",
                ""
            )
            assertThat(isLoading).isTrue()
            job.cancel()
        }

    @Test
    fun `changing password - isLoading should be false when changePassword() is done`() =
        runBlockingTest {
            repository.configureFakeRepository(isLoadingOn = false, isErrorOn = false)

            var isLoading = true
            val job = launch {
                viewModel.profileData.collect {
                    isLoading = it.isLoading
                }
            }

            viewModel.changePassword(
                "",
                "",
                ""
            )
            assertThat(isLoading).isFalse()
            job.cancel()
        }

    @Test
    fun `changing password - should get data when all is good`() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = false, isErrorOn = false)

        var profile: UserProfile? = null
        val job = launch {
            viewModel.profileData.collect {
                profile = it.profileData
            }
        }

        viewModel.changePassword(
            "",
            "",
            ""
        )
        assertThat(profile).isNotNull()
        job.cancel()
    }

    @Test
    fun `changing password - if there is error while changing password, iLoading is false`() =
        runBlockingTest {
            repository.configureFakeRepository(isLoadingOn = false, isErrorOn = true)

            var isLoading = true
            val job = launch {
                viewModel.profileData.collect {
                    isLoading = it.isLoading
                }
            }

            viewModel.changePassword(
                "",
                "",
                ""
            )

            assertThat(isLoading).isFalse()
            job.cancel()
        }

    @Test
    fun `changing password - if there is error while changing password, snackBar event should be emitted`() =
        runBlockingTest {
            repository.configureFakeRepository(isLoadingOn = false, isErrorOn = true)

            var event: UiEvents? = null
            val job = launch {
                viewModel.uiEvents.collect {
                    event = it
                }
            }

            viewModel.changePassword(
                "",
                "",
                ""
            )

            assertThat(event).isNotNull()
            job.cancel()
        }


//DELETE ACCOUNT

    @Test
    fun `delete account - isLoading should be true when deleteAccount() is started`() =
        runBlockingTest {
            repository.configureFakeRepository(isLoadingOn = true, isErrorOn = false)

            var isLoading = false
            val job = launch {
                viewModel.profileData.collect {
                    isLoading = it.isLoading
                }
            }

            viewModel.delete(
                UserProfile(
                    0, "", "", "", "", ""
                )
            )
            assertThat(isLoading).isTrue()
            job.cancel()
        }

    @Test
    fun `delete account - isLoading should be false when deleteAccount() is done`() =
        runBlockingTest {
            repository.configureFakeRepository(isLoadingOn = false, isErrorOn = false)

            var isLoading = true
            val job = launch {
                viewModel.profileData.collect {
                    isLoading = it.isLoading
                }
            }

            viewModel.delete(
                UserProfile(
                    0, "", "", "", "", ""
                )
            )
            assertThat(isLoading).isFalse()
            job.cancel()
        }

    @Test
    fun `delete account - if there is error while deleting, isLoading is false`() =
        runBlockingTest {
            repository.configureFakeRepository(isLoadingOn = false, isErrorOn = true)

            var isLoading = true
            val job = launch {
                viewModel.profileData.collect {
                    isLoading = it.isLoading
                }
            }

            viewModel.delete(
                UserProfile(
                    0, "", "", "", "", ""
                )
            )
            assertThat(isLoading).isFalse()
            job.cancel()
        }

    @Test
    fun `delete account - if there is error, snackBar event should be emitted`() =
        runBlockingTest {
            repository.configureFakeRepository(isLoadingOn = false, isErrorOn = true)

            var event: UiEvents? = null
            val job = launch {
                viewModel.uiEvents.collect {
                    event = it
                }
            }

            viewModel.delete(
                UserProfile(
                    0, "", "", "", "", ""
                )
            )
            assertThat(event).isNotNull()
            job.cancel()
        }


}