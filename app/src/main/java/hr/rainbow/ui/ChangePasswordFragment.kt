package hr.rainbow.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import hr.rainbow.R
import hr.rainbow.databinding.FragmentChangePasswordBinding
import hr.rainbow.domain.view_models.ProfileViewModel

class ChangePasswordFragment : Fragment() {

    private lateinit var binding: FragmentChangePasswordBinding

    private val viewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnCancel.setOnClickListener {
            Navigation.findNavController(binding.root).popBackStack()
        }
        binding.btnSave.setOnClickListener {
            if (formIsOk()) {
                viewModel.changePassword(
                    binding.etOldPassword.editText?.text.toString(),
                    binding.etNewPassword.editText?.text.toString(),
                    binding.etConfirmNewPassword.editText?.text.toString()
                )
                Navigation.findNavController(binding.root).popBackStack()
            }
        }
    }

    private fun formIsOk(): Boolean {
        var ok = true

        binding.etOldPassword.editText?.let {
            if (it.text.isNullOrEmpty()) {
                ok = false
                binding.etOldPassword.error = getString(R.string.you_must_enter_password)
            } else {
                binding.etOldPassword.error = null
            }
        } ?: run { ok = false }

        binding.etNewPassword.editText?.let {
            if (it.text.isNullOrEmpty()) {
                ok = false
                binding.etNewPassword.error = getString(R.string.you_must_enter_password)
            } else {
                if (it.text.length < 6) {
                    ok = false
                    binding.etNewPassword.error = getString(R.string.password_not_valid)
                } else {
                    binding.etNewPassword.error = null
                }
            }
        } ?: run { ok = false }

        binding.etConfirmNewPassword.editText?.let {
            val psw = binding.etNewPassword.editText?.text?.trim() ?: ""

            if (it.text.trim().toString() == psw.toString()) {
                binding.etConfirmNewPassword.error = ""
            } else {
                binding.etConfirmNewPassword.error = getString(R.string.confirm_password_error)
                ok = false
            }

        } ?: run { ok = false }

        return ok
    }
}