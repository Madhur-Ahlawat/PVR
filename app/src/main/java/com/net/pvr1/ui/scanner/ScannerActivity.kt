package com.net.pvr1.ui.scanner

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.net.pvr1.databinding.ActivityScannerBinding
import com.net.pvr1.di.preference.AppPreferences
import com.net.pvr1.ui.dailogs.LoaderDialog
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.ExecutorService

@AndroidEntryPoint
class ScannerActivity : AppCompatActivity() {
    private lateinit var preferences: AppPreferences
    private var binding: ActivityScannerBinding? = null
    private var loader: LoaderDialog? = null

    //
//    private val cameraPermissionRequestCode = 1
//    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
//    /** Blocking camera operations are performed using this executor */
    private lateinit var cameraExecutor: ExecutorService
    private var flashEnabled = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScannerBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
//        startScanning()
    }

//    private fun startScanning() {
//        if (ContextCompat.checkSelfPermission(
//                this,
//                Manifest.permission.CAMERA
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            openCameraWithScanner()
//        } else {
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(Manifest.permission.CAMERA),
//                cameraPermissionRequestCode
//            )
//        }
//    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == cameraPermissionRequestCode && grantResults.isNotEmpty()) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                openCameraWithScanner()
//            } else if (!ActivityCompat.shouldShowRequestPermissionRationale(
//                    this,
//                    Manifest.permission.CAMERA
//                )
//            ) {
//                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//                val uri: Uri = Uri.fromParts("package", packageName, null)
//                intent.data = uri
//                startActivityForResult(intent, cameraPermissionRequestCode)
//            }
//        }
//    }
//
//    private fun openCameraWithScanner() {
//        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
//        // Initialize our background executor
//        cameraExecutor = Executors.newSingleThreadExecutor()
//
//        cameraProviderFuture.addListener({
//            val cameraProvider = cameraProviderFuture.get()
//            bindPreview(cameraProvider)
//        }, ContextCompat.getMainExecutor(this))
//
//        binding?.overlay?.post {
//            binding?.overlay?.setViewFinder()
//        }
//    }
//    private fun bindPreview(cameraProvider: ProcessCameraProvider?) {
//
//        if (isDestroyed || isFinishing) {
//            //This check is to avoid an exception when trying to re-bind use cases but user closes the activity.
//            //java.lang.IllegalArgumentException: Trying to create use case mediator with destroyed lifecycle.
//            return
//        }
//
//        cameraProvider?.unbindAll()
//
//        val preview: Preview = Preview.Builder()
//            .build()
//
//        val cameraSelector: CameraSelector = CameraSelector.Builder()
//            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
//            .build()
//
//        val imageAnalysis = ImageAnalysis.Builder()
//            .setTargetResolution(Size(
//                binding?.cameraPreview?.width!!,
//                binding?.cameraPreview!!.height
//            ))
//            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
//            .build()
//
//        val orientationEventListener = object : OrientationEventListener(this as Context) {
//            override fun onOrientationChanged(orientation : Int) {
//                // Monitors orientation values to determine the target rotation value
//                val rotation : Int = when (orientation) {
//                    in 45..134 -> Surface.ROTATION_270
//                    in 135..224 -> Surface.ROTATION_180
//                    in 225..314 -> Surface.ROTATION_90
//                    else -> Surface.ROTATION_0
//                }
//
//                imageAnalysis.targetRotation = rotation
//            }
//        }
//        orientationEventListener.enable()
//
//        //switch the analyzers here, i.e. MLKitBarcodeAnalyzer, ZXingBarcodeAnalyzer
//        class ScanningListener : ScanningResultListener {
//            override fun onScanned(result: String) {
//                runOnUiThread {
//                    imageAnalysis.clearAnalyzer()
//                    cameraProvider?.unbindAll()
//                    ScannerResultDialog.newInstance(
//                        result,
//                        object : ScannerResultDialog.DialogDismissListener {
//                            override fun onDismiss() {
//                                bindPreview(cameraProvider)
//                            }
//                        })
//                        .show(supportFragmentManager, ScannerResultDialog::class.java.simpleName)
//                }
//            }
//        }
//
//        val analyzer = ZXingBarcodeAnalyzer(ScanningListener())
//
//        imageAnalysis.setAnalyzer(cameraExecutor, analyzer)
//
//        preview.setSurfaceProvider(binding?.cameraPreview?.surfaceProvider)
//
//        val camera =
//            cameraProvider?.bindToLifecycle(this, cameraSelector, imageAnalysis, preview)
//
//        if (camera?.cameraInfo?.hasFlashUnit() == true) {
//            binding?.ivFlashControl?.show()
//
//            binding?.ivFlashControl?.setOnClickListener {
//                camera.cameraControl.enableTorch(!flashEnabled)
//            }
//
//            camera.cameraInfo.torchState.observe(this) {
//                it?.let { torchState ->
//                    if (torchState == TorchState.ON) {
//                        flashEnabled = true
//                        binding?.ivFlashControl?.setImageResource(R.drawable.ic_round_flash_on)
//                    } else {
//                        flashEnabled = false
//                        binding?.ivFlashControl?.setImageResource(R.drawable.ic_round_flash_off)
//                    }
//                }
//            }
//        }
//
//
//    }
//
//
//    @Deprecated("Deprecated in Java")
//    @Override
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if (requestCode == cameraPermissionRequestCode) {
//            if (ContextCompat.checkSelfPermission(
//                    this,
//                    Manifest.permission.CAMERA
//                ) == PackageManager.PERMISSION_GRANTED
//            ) {
//                openCameraWithScanner()
//            }
//        }
//    }

}