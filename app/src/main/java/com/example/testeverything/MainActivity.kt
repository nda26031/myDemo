package com.example.testeverything

import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.testeverything.camerax.CameraxActivity
import com.example.testeverything.charginginfo.ChargingInfoActivity
import com.example.testeverything.databinding.ActivityMainBinding
import com.example.testeverything.googlemap.GoogleMapActivity
import com.example.testeverything.rotateandcrop.GetPhotoActivity
import com.example.testeverything.rvmultitype.MultipleTypeRcvActivity
import com.example.testeverything.saveimageofview.SaveImageGalleryActivity
import com.example.testeverything.patternLockView.custom.PatternLockActivity2
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.GoogleMap

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var geo: Geocoder


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        navigate()


        val player = ExoPlayer.Builder(this).build()
        binding.videoView.player = player
        // Build the media item.
        val mediaItem = MediaItem.fromUri("asset:///Cute/charging_32.mp4")
// Set the media item to be played.
        player.setMediaItem(mediaItem)
// Prepare the player.
        player.prepare()
        player.repeatMode = Player.REPEAT_MODE_ALL
        binding.videoView.useController = false
// Start the playback.
        player.play()
    }

    private fun navigate() {
        binding.btnRequestPermissions.setOnClickListener {
//            val intent = Intent(this, RequestPermissionActivity::class.java)
//            startActivity(intent)
//            requestCamPermissions()
            startActivity(Intent(this, GetPhotoActivity::class.java))
        }

        binding.btnSingleSelectionRv.setOnClickListener {
//            val intent = Intent(this, SecondActivity::class.java)
//            startActivity(intent)
            startActivity(Intent(this, PatternLockActivity2::class.java))

        }

        binding.btnRvMultiType.setOnClickListener {
            val intent = Intent(this, MultipleTypeRcvActivity::class.java)
            startActivity(intent)
        }

        binding.btnCameraX.setOnClickListener {
            val intent = Intent(this, CameraxActivity::class.java)
            startActivity(intent)
        }

        binding.btnSaveGallery.setOnClickListener {
            val intent = Intent(this, SaveImageGalleryActivity::class.java)
            startActivity(intent)
        }

        binding.btnGoogleMap.setOnClickListener {
            val intent = Intent(this, GoogleMapActivity::class.java)
            startActivity(intent)
        }

        binding.btnChargeInfo.setOnClickListener {
            val intent = Intent(this, ChargingInfoActivity::class.java)
            startActivity(intent)
        }
    }


//    private fun requestCamPermissions() {
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                android.Manifest.permission.ACCESS_FINE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            Toast.makeText(this, "Camera permission is granted", Toast.LENGTH_SHORT).show()
//        } else if (ActivityCompat.shouldShowRequestPermissionRationale(
//                this,
//                android.Manifest.permission.ACCESS_FINE_LOCATION
//            )
//        ) {
//            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
//            builder.setTitle("Camera Permission")
//            builder.setMessage("This app requires camera permission to take photos.")
//            builder.setPositiveButton("Settings") { dialog, _ ->
//
//                val intent =
//                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
//                        Uri.fromParts("package", packageName, null))
//                startActivity(intent)
//                dialog.dismiss()
//            }
//            builder.setNegativeButton("Cancel") { dialog, _ ->
//                dialog.dismiss()
//            }
//            builder.show()
//        } else {
//            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
//        }
//    }
//
//    fun startTextLoopAnimation(textView: TextView) {
//        val viewWidth = textView.width.toFloat()
//
//        val animator = ObjectAnimator.ofFloat(
//            textView,
//            "translationX",
//            0f,
//            -viewWidth
//        ).apply {
//            duration = 3000L              // thời gian chạy 1 vòng
//            repeatCount = ValueAnimator.INFINITE
//            repeatMode = ValueAnimator.RESTART
//            interpolator = LinearInterpolator()
//            start()
//        }
//    }
}