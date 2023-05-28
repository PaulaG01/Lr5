package com.example.lr4

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.drawToBitmap
import com.example.lr4.malina.Vacancy
import com.example.lr4.malina.VacancyFilesHelper
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private lateinit var city: Spinner
    private lateinit var title: EditText
    private lateinit var days: EditText

    private val pickImage = 100
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        city = findViewById<Spinner>(R.id.spinner) as Spinner
        title = findViewById<EditText>(R.id.editTextText) as EditText
        days = findViewById<EditText>(R.id.editTextNumber2) as EditText

        setSelectedData()

        registerOnCameraBtnClick()
        registerNextButton()
        registerToListButton()
    }

    private fun registerToListButton() {
        val skipBtn = findViewById<Button>(R.id.toList)

        skipBtn.setOnClickListener {
            try {
                val intent = Intent(this, FinalActivity::class.java)
                startActivity(intent)
            } catch (ex: Exception) {
                Toast.makeText(this, "Создайте визитку!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registerNextButton() {
        val nextBtn = findViewById<Button>(R.id.nextButton)

        nextBtn.setOnClickListener {
            val intent = Intent(this, FinalActivity::class.java)

            val vacancy = application as Vacancy
            vacancy.vacancyVar.city = city.selectedItem as String
            vacancy.vacancyVar.title = (title.text.toString())
            vacancy.vacancyVar.days = (days.text.toString())

            try {
                val bitmap = findViewById<ImageView>(R.id.photo).drawToBitmap()
                VacancyFilesHelper.saveVacancy(vacancy.vacancyVar, this, bitmap)

                vacancy.vacancyVar.resetData()

                Toast.makeText(this, "Данные сохранены!", Toast.LENGTH_LONG).show()

                startActivity(intent)
                finish()
            } catch (ex: Exception) {
                Log.e("FILEE", ex.printStackTrace().toString())
                Toast.makeText(this, "Ой, ошибочка!!", Toast.LENGTH_SHORT).show()
            }

            startActivity(intent)
        }
    }

    private fun setSelectedData() {
        val vacancy = application as Vacancy

        val city = vacancy.vacancyVar.city
        val cityId = vacancy.vacancyVar.getCities()[city]

        this.title.setText(vacancy.vacancyVar.title)
        this.days.setText(vacancy.vacancyVar.days.toString())

        if (cityId != null) {
            this.city.setSelection(cityId)
        }

    }

    private fun registerOnCameraBtnClick() {
        val cameraBtn = findViewById<Button>(R.id.button3)

        cameraBtn.setOnClickListener {
            checkPermissions()
        }
    }

    private fun checkPermissions() {
        val button = findViewById<Button>(R.id.button3)
        button.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            findViewById<ImageView>(R.id.photo).setImageURI(imageUri)
        }
    }
}
