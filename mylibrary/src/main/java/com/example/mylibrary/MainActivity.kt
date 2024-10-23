package com.example.mylibrary

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.mylibrary.di.DaggerMainComponent
import com.example.mylibrary.di.MainComponent
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainFragment : Fragment() {

    companion object {
        private val REQUIRED_PERMISSIONS =
            mutableListOf(Manifest.permission.CAMERA).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }

    private val mainComponent: MainComponent by lazy {
        DaggerMainComponent.factory().create(requireActivity())
    }

    private val viewModel: com.example.mylibrary.presentation.MainViewModel by lazy {
        ViewModelProvider(
            this,
            mainComponent.getMainViewModelFactory()
        )[com.example.mylibrary.presentation.MainViewModel::class.java]
    }

    private val activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        handlePermissionsResult(permissions)
    }

    private lateinit var timestampTV: TextView
    private lateinit var btn: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_main, container, false)

        timestampTV = view.findViewById(R.id.time_stamp)
        btn = view.findViewById(R.id.make_photo)

        requestPermissions()
        setupWindowInsets(view)
        observeViewModel()
        setupButtonClickListener()

        return view
    }

    private fun setupWindowInsets(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main)) { _, insets ->
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
            Toast.makeText(requireContext(), "Permission request denied", Toast.LENGTH_SHORT).show()
        } else {
            viewModel.initCamera(requireActivity(), requireView().findViewById(R.id.preview))
        }
    }
}
