package hr.rainbow.ui

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.work.*
import com.canhub.cropper.CropImageView
import dagger.hilt.android.AndroidEntryPoint
import hr.rainbow.R
import hr.rainbow.databinding.ActivityUploadImageHandlerBinding
import hr.rainbow.domain.workers.UploadImageWorker
import hr.rainbow.domain.workers.UploadUrlWorker
import hr.rainbow.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val START_TYPE = "hr.rainbow.ui.upload.image.handler.start.type"

@AndroidEntryPoint
class UploadImageHandlerActivity : AppCompatActivity() {

    companion object {
        fun startActivityForCamera(context: Context) {
            ActivityCompat.startActivity(
                context,
                Intent(context, UploadImageHandlerActivity::class.java).apply {
                    putExtra(START_TYPE, 1)
                },
                null
            )
        }

        fun startActivityForGallery(context: Context) {
            ActivityCompat.startActivity(
                context,
                Intent(context, UploadImageHandlerActivity::class.java).apply {
                    putExtra(START_TYPE, 2)
                },
                null
            )
        }
    }


    private lateinit var binding: ActivityUploadImageHandlerBinding
    private lateinit var imageUriFromCamera: Uri

    @Inject
    lateinit var workManager: WorkManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadImageHandlerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        configureImageCropView()

        supportActionBar?.hide()

        when (intent.getIntExtra(START_TYPE, 1)) {
            1 -> {
                handleTakeImage()
            }
            2 -> {
                handleImportFromGallery()
            }
        }
    }

    private fun configureImageCropView() {
        with(binding.cropImageView) {
            guidelines = CropImageView.Guidelines.ON
            setAspectRatio(aspectRatioX = 1, aspectRatioY = 1)
            setFixedAspectRatio(true)
            setMinCropResultSize(minCropResultWidth = 1080, minCropResultHeight = 1080)
        }
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener { onBackPressed() }
        binding.btnUpload.setOnClickListener { uploadImage() }
    }

    private fun uploadImage() {
        try {
            val bitmap = binding.cropImageView.croppedImage
            bitmap?.let {
                lifecycleScope.launch {
                    notifyProgressNotification(
                        this@UploadImageHandlerActivity,
                        UPLOAD_NOTIFICATION_ID,
                        CHANNEL_ID_MEDIA,
                        this@UploadImageHandlerActivity.getString(R.string.Media_upload),
                        this@UploadImageHandlerActivity.getString(R.string.uploading),
                        0,
                        0,
                        true
                    )

                    val imageUri = writeBitmapToTmpFile(it)

                    val constraints = Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()

                    val workers = workManager.beginUniqueWork(
                        UPLOAD_IMAGE_WORK_NAME,
                        ExistingWorkPolicy.KEEP,
                        OneTimeWorkRequestBuilder<UploadImageWorker>()
                            .setConstraints(constraints)
                            .setInputData(
                                Data.Builder()
                                    .putString(KEY_UPLOAD_IMAGE_URI, imageUri.toString())
                                    .build()
                            )
                            .build()
                    )

                    workers.then(
                        OneTimeWorkRequestBuilder<UploadUrlWorker>()
                            .setConstraints(constraints)
                            .build()
                    ).enqueue()

                }
                onBackPressed()
            }
        } catch (e: Throwable) {
            this.showErrorDialog(
                getString(R.string.error_occurred),
                e.message ?: getString(R.string.unexpected_error),
                null
            )
        }
    }

    private val importPictureIntentHandler = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        try {
            val imageURI: Uri? = result.data?.data
            imageURI?.let {
                binding.cropImageView.setImageUriAsync(it)
                binding.btnUpload.isEnabled = true
            }
        } catch (e: Throwable) {
            this.showErrorDialog(
                getString(R.string.error_occurred),
                e.message ?: getString(R.string.unexpected_error)
            ) { _, _ -> onBackPressed() }
        }
    }

    private fun handleImportFromGallery() {
        if (permissionGranted(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                READ_EXTERNAL_STORAGE_PERMISSION_KEY
            )
        ) {
            importPictureIntentHandler.launch(
                Intent(Intent.ACTION_PICK).apply {
                    type = "image/*"
                }
            )
        }
    }

    private val takePictureIntentHandler = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        try {
            if (result.resultCode == RESULT_OK && this::imageUriFromCamera.isInitialized) {
                imageUriFromCamera.let {
                    binding.cropImageView.setImageUriAsync(it)
                    binding.btnUpload.isEnabled = true
                }
            }
        } catch (e: Throwable) {
            this.showErrorDialog(
                getString(R.string.error_occurred),
                e.message ?: getString(R.string.unexpected_error)
            ) { _, _ -> onBackPressed() }
        }
    }

    private fun handleTakeImage() {
        if (permissionGranted(
                this,
                android.Manifest.permission.CAMERA,
                CAMERA_PERMISSION_KEY
            )
        ) {
            try {
                imageUriFromCamera = createTmpFile()

                takePictureIntentHandler.launch(
                    Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                        putExtra(MediaStore.EXTRA_OUTPUT, imageUriFromCamera)
                    }
                )
            } catch (e: Throwable) {
                this.showErrorDialog(
                    getString(R.string.error_occurred),
                    e.message ?: getString(R.string.unexpected_error)
                ) { _, _ -> onBackPressed() }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when {
            requestCode == READ_EXTERNAL_STORAGE_PERMISSION_KEY && grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
            -> handleImportFromGallery()
            requestCode == CAMERA_PERMISSION_KEY && grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
            -> handleTakeImage()
        }
    }
}