package com.example.camerasnap

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.camerasnap.di.DaggerMainComponent
import com.example.camerasnap.di.MainComponent
import com.example.camerasnap.presentation.MainViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainActivity : AppCompatActivity() {

    companion object {
        private val REQUIRED_PERMISSIONS =
            mutableListOf(Manifest.permission.CAMERA).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }

    private val mainComponent: MainComponent by lazy {
        DaggerMainComponent.factory().create(this)
    }

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(
            this,
            mainComponent.getMainViewModelFactory()
        )[MainViewModel::class.java]
    }

    private val activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        handlePermissionsResult(permissions)
    }

    private lateinit var timestampTV: TextView
    private lateinit var btn: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        timestampTV = findViewById(R.id.time_stamp)
        btn = findViewById(R.id.make_photo)

        requestPermissions()
        setupWindowInsets()
        observeViewModel()
        setupButtonClickListener()
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            btn.y -= systemBars.bottom
            timestampTV.y += systemBars.top
            insets
        }
    }

    private fun observeViewModel() {
        viewModel.state
            .onEach { timestampTV.text = it.timeStamp }
            .launchIn(lifecycleScope)
    }

    private fun setupButtonClickListener() {
        btn.setOnClickListener {
            viewModel.takeAndSavePhoto()
        }
    }

    private fun requestPermissions() {
        activityResultLauncher.launch(REQUIRED_PERMISSIONS)
    }

    private fun handlePermissionsResult(permissions: Map<String, Boolean>) {
        val permissionGranted = permissions.entries.all { it.value }
        if (!permissionGranted) {
            Toast.makeText(this, "Permission request denied", Toast.LENGTH_SHORT).show()
        } else {
            viewModel.initCamera(this, findViewById(R.id.preview))
        }
    }
}