package hr.rainbow.ui

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import hr.rainbow.R
import hr.rainbow.databinding.FragmentProfileBinding
import hr.rainbow.domain.model.UserProfile
import hr.rainbow.domain.view_models.ProfileViewModel
import hr.rainbow.util.UiEvents
import hr.rainbow.util.showConfirmDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogOut.setOnClickListener { handleLogOut() }
        binding.btnChangePassword.setOnClickListener { handleChangePassword() }
        binding.btnUpdateProfile.setOnClickListener { handleUpdate() }
        binding.btnDeleteAccount.setOnClickListener { handleDeleteAccount() }
        binding.btnSaveProfileChanges.setOnClickListener { handleSaveChanges() }
        binding.btnCancel.setOnClickListener { handleCancel() }

        viewModel.fetchProfileData()
        subscribeForDataObservable()
        subscribeForErrorObservable()
        subscribeForDeletedDataObservable()
    }

    private fun handleLogOut() {
        EntryActivity.startActivity(this.requireContext())
        this.requireActivity().finish()
    }


    private fun handleChangePassword() {
        Navigation.findNavController(binding.root).navigate(R.id.menuChangePassword)
    }

    private fun handleUpdate() {
        toggleEditTextFields()
        toggleButtonVisibility()
    }

    private fun handleDeleteAccount() {
        val userProfile = UserProfile(
            email = binding.etEmail.editText?.text.toString().trim(),
            firstName = binding.etFirstName.editText?.text.toString().trim(),
            lastName = binding.etLastName.editText?.text.toString().trim(),
            nickName = binding.etNickName.editText?.text.toString().trim(),
            bio = binding.etBio.editText?.text.toString().trim(),
        )

        requireContext().showConfirmDialog(
            title = "Delete account?",
            message = "Are you sure? You want be able to recover stories that you created",
            DialogInterface.OnClickListener { _, _ ->
                viewModel.delete(userProfile)
            }
        )
    }

    private fun handleSaveChanges() {
        val userProfile = UserProfile(
            email = binding.etEmail.editText?.text.toString().trim(),
            firstName = binding.etFirstName.editText?.text.toString().trim(),
            lastName = binding.etLastName.editText?.text.toString().trim(),
            nickName = binding.etNickName.editText?.text.toString().trim(),
            bio = binding.etBio.editText?.text.toString().trim(),
        )

        viewModel.updateProfile(userProfile)
        toggleEditTextFields()
        toggleButtonVisibility()
    }

    private fun handleCancel() {
        lifecycleScope.launchWhenStarted {
            renderData(viewModel.profileData.value.profileData)
            toggleButtonVisibility()
            toggleEditTextFields()
        }
    }

    private fun subscribeForDataObservable() {
        lifecycleScope.launchWhenStarted {
            viewModel.profileData.collect {
                renderData(it.profileData)

                if (it.isLoading) {
                    binding.progressBar.visibility = View.VISIBLE
                } else {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun subscribeForDeletedDataObservable() {
        lifecycleScope.launchWhenStarted {
            viewModel.successfullyDeleted.collect {
                if (it) {
                    delay(1500)
                    EntryActivity.startActivity(this@ProfileFragment.requireContext())
                }
            }
        }
    }

    private fun subscribeForErrorObservable() {
        lifecycleScope.launchWhenStarted {
            viewModel.uiEvents.collect {
                when (it) {
                    is UiEvents.ShowSnackBar -> {
                        Snackbar.make(binding.root, it.message, Snackbar.LENGTH_INDEFINITE)
                            .setAction(it.action) {

                            }.show()
                    }
                }
            }
        }

    }


    private fun toggleEditTextFields() {
        binding.etFirstName.editText?.isEnabled = !binding.etFirstName.editText?.isEnabled!!
        binding.etLastName.editText?.isEnabled = !binding.etLastName.editText?.isEnabled!!
        binding.etNickName.editText?.isEnabled = !binding.etNickName.editText?.isEnabled!!
        binding.etBio.editText?.isEnabled = !binding.etBio.editText?.isEnabled!!
    }

    private fun toggleButtonVisibility() {
        if (binding.btnSaveProfileChanges.visibility == Button.GONE)
            binding.btnSaveProfileChanges.visibility = Button.VISIBLE
        else
            binding.btnSaveProfileChanges.visibility = Button.GONE

        if (binding.btnCancel.visibility == Button.GONE)
            binding.btnCancel.visibility = Button.VISIBLE
        else
            binding.btnCancel.visibility = Button.GONE

        if (binding.btnChangePassword.visibility == Button.GONE)
            binding.btnChangePassword.visibility = Button.VISIBLE
        else
            binding.btnChangePassword.visibility = Button.GONE

        if (binding.btnUpdateProfile.visibility == Button.GONE)
            binding.btnUpdateProfile.visibility = Button.VISIBLE
        else
            binding.btnUpdateProfile.visibility = Button.GONE

        if (binding.btnLogOut.visibility == Button.GONE)
            binding.btnLogOut.visibility = Button.VISIBLE
        else
            binding.btnLogOut.visibility = Button.GONE

        if (binding.btnDeleteAccount.visibility == Button.GONE)
            binding.btnDeleteAccount.visibility = Button.VISIBLE
        else
            binding.btnDeleteAccount.visibility = Button.GONE

    }

    private fun renderData(profileData: UserProfile) {
        binding.etEmail.editText?.setText(profileData.email)
        binding.etFirstName.editText?.setText(profileData.firstName)
        binding.etLastName.editText?.setText(profileData.lastName)
        binding.etNickName.editText?.setText(profileData.nickName)
        binding.etBio.editText?.setText(profileData.bio)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}