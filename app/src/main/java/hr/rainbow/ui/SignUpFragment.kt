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
import hr.rainbow.databinding.FragmentSignUpBinding
import hr.rainbow.domain.DownloadDataService
import hr.rainbow.domain.model.UserProfile
import hr.rainbow.domain.view_models.ProfileViewModel
import hr.rainbow.util.UiEvents
import hr.rainbow.util.isOnline
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding: FragmentSignUpBinding get() = _binding!!

    private val viewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        subscribeForLodgedInObservable()
        subscribeForErrorObservable()
    }

    private fun subscribeForLodgedInObservable() {
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

    private fun subscribeForErrorObservable() {
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
        binding.btnCancel.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.signIn)
        }
        binding.btnRegister.setOnClickListener {
            if (isFormValid() && requireContext().isOnline()) {
                toggleProgressBar()
                viewModel.register(
                    userProfile = UserProfile(
                        email = binding.etEmail.editText?.text.toString(),
                        firstName = binding.etFirstName.editText?.text.toString(),
                        lastName = binding.etLastName.editText?.text.toString(),
                        nickName = binding.etNickName.editText?.text.toString(),
                        bio = binding.etBio.editText?.text.toString()
                    ),
                    password = binding.etPassword.editText?.text?.trim().toString(),
                    confirmPassword = binding.etConfirmPassword.editText?.text?.trim().toString()
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
    }

    private fun isFormValid(): Boolean {
        var ok = true

        binding.etFirstName.editText?.let {
            if (it.text.isNullOrEmpty()) {
                binding.etFirstName.error = getString(R.string.you_have_to_enter_first_name)
                ok = false
            } else {
                binding.etFirstName.error = null
            }
        }

        binding.etLastName.editText?.let {
            if (it.text.isNullOrEmpty()) {
                binding.etLastName.error = getString(R.string.you_have_to_enter_last_name)
                ok = false
            } else {
                binding.etLastName.error = null
            }
        }

        binding.etNickName.editText?.let {
            if (it.text.isNullOrEmpty()) {
                binding.etNickName.error = getString(R.string.you_have_to_enter_nickname)
                ok = false
            } else {
                binding.etNickName.error = null
            }
        }

        binding.etEmail.editText?.let {
            if (it.text.isNullOrEmpty()) {
                binding.etEmail.error = getString(R.string.you_must_enter_email)
                ok = false
            } else {
                binding.etEmail.error = null
            }
        }

        binding.etPassword.editText?.let {
            if (it.text.isNullOrEmpty()) {
                binding.etPassword.error = getString(R.string.you_must_enter_password)
                ok = false
            } else {
                if (it.text.length < 6) {
                    binding.etPassword.error = getString(R.string.password_not_valid)
                    ok = false
                } else {
                    binding.etPassword.error = null
                }
            }
        } ?: run { ok = false }

        binding.etConfirmPassword.editText?.let {
            val psw = binding.etPassword.editText?.text?.trim() ?: ""

            if (it.text.trim().toString() == psw.toString()) {
                binding.etConfirmPassword.error = ""
            } else {
                binding.etConfirmPassword.error = getString(R.string.confirm_password_error)
                ok = false
            }

        } ?: run { ok = false }

        return ok
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun toggleProgressBar() {
        if (binding.progressBar.visibility == View.GONE) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}