package com.example.lr4

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class InfoActivity : AppCompatActivity() {
    private val cameraRequest = 1888
    private lateinit var imageView: ImageView
    private lateinit var backBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        backBtn = findViewById(R.id.back)
        imageView = findViewById<ImageView>(R.id.imageView3)

        registerOnCameraBtnClick()
        registerOnPhoneBtnClick()
        registerOnEmailBtnClick()
        registerOnInstBtnClick()
        registerBackBtn()
    }

    private fun registerOnInstBtnClick() {
        val instBtn = findViewById<Button>(R.id.button2)
        instBtn.setOnClickListener {
            val uri = Uri.parse("http://instagram.com/_u/pa1n_a")
            val likeIng = Intent(Intent.ACTION_VIEW, uri)

            likeIng.setPackage("com.instagram.android")

            try {
                startActivity(likeIng)
            } catch (e: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://instagram.com/pa1na")
                    )
                )
            }
        }
    }

    private fun registerOnEmailBtnClick() {
        val mailBtn = findViewById<TextView>(R.id.textView14)
        mailBtn.setOnClickListener {
            startActivity(Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:paula.g01@gmail.com")))
        }
    }

    private fun registerBackBtn() {
        backBtn.setOnClickListener {
            val intent = Intent(this, FinalActivity::class.java)
            startActivity(intent)
        }
    }

    private fun registerOnPhoneBtnClick() {
        val phoneBtn = findViewById<TextView>(R.id.textView13)

        phoneBtn.setOnClickListener {
            call()
        }
    }

    private fun call() {
        val dialIntent = Intent(Intent.ACTION_DIAL)
        dialIntent.data = Uri.parse("tel:" + "+375298679268")
        startActivity(dialIntent)
    }

    private fun registerOnCameraBtnClick() {
        val cameraBtn = findViewById<Button>(R.id.button)

        cameraBtn.setOnClickListener {
            checkPermissions()
        }
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), cameraRequest)
        val photoButton: Button = findViewById(R.id.button)
        photoButton.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, cameraRequest)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == cameraRequest) {
            val photo: Bitmap = data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(photo)
        }
    }
}