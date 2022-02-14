package hr.rainbow.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import hr.rainbow.R
import hr.rainbow.databinding.FragmentSignInBinding
import hr.rainbow.domain.DownloadDataService
import hr.rainbow.domain.view_models.ProfileViewModel
import hr.rainbow.util.UiEvents
import hr.rainbow.util.isOnline
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding

    private val viewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        checkIfUserCredentialIsSaved()
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun checkIfUserCredentialIsSaved() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        subscribeForLogInObservables()
        subscribeForErrorObservables()
    }

    private fun subscribeForLogInObservables() {
        lifecycleScope.launchWhenStarted {
            viewModel.loggedIn.collect {
                if (it) {
                    toggleProgressBar()
                    DownloadDataService.enqueueService(requireContext())
                    Navigation.findNavController(binding.root).navigate(R.id.splashScreen)
                }
            }
        }
    }

    private fun subscribeForErrorObservables() {
        lifecycleScope.launchWhenStarted {
            viewModel.uiEvents.collectLatest {
                when (it) {
                    is UiEvents.ShowSnackBar -> {
                        toggleProgressBar()
                        Snackbar.make(binding.root, it.message, Snackbar.LENGTH_INDEFINITE)
                            .setAction(it.action) {

                            }.show()
                    }
                }
            }
        }

    }

    private fun setupListeners() {
        binding.btnLogIn.setOnClickListener {
            logIn()
        }

        binding.btnRegister.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.signUp)
        }
    }

    private fun logIn() {
        if (isFormValid() && requireContext().isOnline()) {
            toggleProgressBar()
            viewModel.login(
                binding.etUserName.editText?.text?.trim().toString(),
                binding.etPassword.editText?.text?.trim().toString()
            )
        } else {
            toggleProgressBar()
            Toast.makeText(
                requireContext(),
                requireContext().getString(R.string.you_must_be_connected_to_internet),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun isFormValid(): Boolean {
        var ok = true

        binding.etUserName.editText?.let {
            if (it.text.isNullOrEmpty()) {
                ok = false
                binding.etUserName.error = getString(R.string.you_must_enter_username)
            } else {
                binding.etUserName.error = null
            }

        } ?: run { ok = false }

        binding.etPassword.editText?.let {
            if (it.text.isNullOrEmpty()) {
                ok = false
                binding.etPassword.error = getString(R.string.you_must_enter_password)
            } else {
                if (it.text.length < 6) {
                    ok = false
                    binding.etPassword.error = getString(R.string.password_not_valid)
                } else {
                    binding.etPassword.error = null
                }
            }
        } ?: run { ok = false }

        return ok
    }

    private fun toggleProgressBar() {
        if (binding.progressBar.visibility == View.GONE) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}