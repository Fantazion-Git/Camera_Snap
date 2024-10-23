package com.example.camerasnap

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.mylibrary.MainFragment

class AppActivity : AppCompatActivity() {

//    companion object {
//        private val REQUIRED_PERMISSIONS =
//            mutableListOf(Manifest.permission.CAMERA).apply {
//                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
//                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                }
//            }.toTypedArray()
//    }

//    private val mainComponent: com.example.mylibrary.di.MainComponent by lazy {
//        DaggerMainComponent.factory().create(this)
//    }

//    private val viewModel: com.example.mylibrary.presentation.MainViewModel by lazy {
//        ViewModelProvider(
//            this,
//            mainComponent.getMainViewModelFactory()
//        )[com.example.mylibrary.presentation.MainViewModel::class.java]
//    }

//    private val activityResultLauncher = registerForActivityResult(
//        ActivityResultContracts.RequestMultiplePermissions()
//    ) { permissions ->
//        handlePermissionsResult(permissions)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_app)

        val libraryFragment = MainFragment() // Создаем экземпляр библиотечного фрагмента
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerView, libraryFragment)
        transaction.commit()
    }
//        timestampTV = findViewById(R.id.time_stamp)
//        btn = findViewById(R.id.make_photo)

     //   requestPermissions()
       // setupWindowInsets()
       // observeViewModel()
        //setupButtonClickListener()
    //}

//    private fun setupWindowInsets() {
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { _, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            btn.y -= systemBars.bottom
//            timestampTV.y += systemBars.top
//            insets
//        }
//    }

//    private fun observeViewModel() {
//        viewModel.state
//            .onEach { timestampTV.text = it.timeStamp }
//            .launchIn(lifecycleScope)
//    }

//    private fun setupButtonClickListener() {
//        btn.setOnClickListener {
//            viewModel.takeAndSavePhoto()
//        }
//    }

//    private fun requestPermissions() {
//        activityResultLauncher.launch(REQUIRED_PERMISSIONS)
//    }

//    private fun handlePermissionsResult(permissions: Map<String, Boolean>) {
//        val permissionGranted = permissions.entries.all { it.value }
//        if (!permissionGranted) {
//            Toast.makeText(this, "Permission request denied", Toast.LENGTH_SHORT).show()
//        } else {
//   //         viewModel.initCamera(this, findViewById(R.id.preview))
//        }
//    }
}