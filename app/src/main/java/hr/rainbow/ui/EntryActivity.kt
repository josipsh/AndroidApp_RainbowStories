package hr.rainbow.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import dagger.hilt.android.AndroidEntryPoint
import hr.rainbow.databinding.ActivityEntryBinding
import javax.inject.Inject

@AndroidEntryPoint
class EntryActivity : AppCompatActivity() {

    companion object {
        fun startActivity(context: Context) {
            ActivityCompat.startActivity(
                context,
                Intent(
                    context,
                    EntryActivity::class.java
                ),
                null
            )
        }
    }

    private lateinit var binding: ActivityEntryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
    }
}