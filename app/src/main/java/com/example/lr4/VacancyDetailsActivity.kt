package com.example.lr4

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.drawToBitmap
import com.example.lr4.malina.Skill
import com.example.lr4.malina.Vacancy
import com.example.lr4.malina.VacancyFilesHelper
import com.example.lr4.malina.VacancyVar
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.io.File

class VacancyDetailsActivity : AppCompatActivity() {
    private lateinit var vacancy: VacancyVar

    private lateinit var title: EditText
    private lateinit var city: Spinner
    private lateinit var days: EditText
    private lateinit var chipGroup: ChipGroup
    private lateinit var photo: ImageView
    private lateinit var saveBtn: Button
    private lateinit var photoBtn: Button

    private val pickImage = 100
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vacancy_details)

        try {
            initData()
            displayData()
            registerSaveButton()
            registerPhotoButton()
            registerPevButton()
        } catch (ex: Exception) {
            Log.e("EERRRR", ex.printStackTrace().toString())
            Toast.makeText(this, "Ой, ошибочка!!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initData() {
        vacancy =
            VacancyFilesHelper.readVacancies(this).find {
                it.id == intent.getIntExtra("vacancyId", 1)
            }!!

        title = findViewById(R.id.title)
        city = findViewById(R.id.city)
        days = findViewById(R.id.days)
        chipGroup = findViewById(R.id.chipGroup)
        photo = findViewById(R.id.imageView2)
        saveBtn = findViewById(R.id.saveBtn)
        photoBtn = findViewById(R.id.photoBtn)
    }

    private fun displayData() {
        title.setText(vacancy.title)
        days.setText(vacancy.days.toString())

        val cityId = vacancy.getCities()[vacancy.city]
        if (cityId != null) {
            city.setSelection(cityId)
        }

        val imgFile = File(this.filesDir, "img_${vacancy.id}.png")
        if (imgFile.exists()) {
            val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
            photo.setImageBitmap(myBitmap)
        }
    }

    private fun registerPevButton() {
        val prevBtn = findViewById<Button>(R.id.backBtn)

        prevBtn.setOnClickListener {
            val intent = Intent(this, FinalActivity::class.java)

            startActivity(intent)
            finish()
        }
    }

    private fun registerSaveButton() {
        saveBtn.setOnClickListener {
            val intent = Intent(this, VacancyDetailsActivity::class.java)
            intent.putExtra("vacancyId", vacancy.id)

            val builder = AlertDialog.Builder(this)
            builder.setMessage("Подтвердите правки")
                .setCancelable(false)
                .setPositiveButton("Да") { dialog, id ->
                    storeData()
                    startActivity(intent)
                    finish()
                }
                .setNegativeButton("Нет") { dialog, id ->
                    // Dismiss the dialog
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }
    }

    private fun registerPhotoButton() {
        photoBtn.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            photo.setImageURI(imageUri)
        }
    }

    private fun storeData() {
        vacancy.title = title.text.toString()
        vacancy.days = days.text.toString()
        vacancy.city = city.selectedItem as String

        val bitmap = photo.drawToBitmap()
        VacancyFilesHelper.saveVacancy(vacancy, this, bitmap, 1)
    }
}