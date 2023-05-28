package com.example.lr4.malina

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Paths

class VacancyFilesHelper {
    companion object {
        private const val fileName: String = "vithitochki.json"

        private const val MODECREATE: Int = 0
        private const val MODEEDIT: Int = 1

        fun saveVacancy(vacancy: VacancyVar, context: Context, bitmap: Bitmap, mode: Int = 0) {
            val path: File = context.filesDir

            var vacancies: MutableList<VacancyVar> = mutableListOf()

            if (File(path, fileName).exists()) {
                vacancies = readVacancies(context)
            }

            when (mode) {
                MODECREATE -> {
                    vacancies.sortBy { it.id }
                    val lastId: Int = vacancies.last().id
                    vacancy.id = lastId + 1
                    vacancies.add(vacancy)
                    saveImgBitmap(vacancy, context, bitmap)
                }
                MODEEDIT -> {
                    val vac = vacancies.find { it.id == vacancy.id }
                    if (vac != null) {
                        vacancy.id = vac.id
                        vacancies.remove(vac)
                    }
                    saveImgBitmap(vacancy, context, bitmap)
                    vacancies.add(vacancy)
                }
            }

            storeJsonToFile(vacancies, path)
        }

        fun deleteVacancy(vacancy: VacancyVar, context: Context) {
            val path: File = context.filesDir

            var vacancies: MutableList<VacancyVar> = mutableListOf()

            if (File(path, fileName).exists()) {
                vacancies = readVacancies(context)
            }

            val vac = vacancies.find { it.id == vacancy.id }
            vacancies.remove(vac)

            storeJsonToFile(vacancies, path)
        }
        private fun saveImgBitmap(vacancy: VacancyVar, context: Context, bitmap: Bitmap): VacancyVar {
            val imgName = "img_${vacancy.id}.png"
            val file = File(context.filesDir, imgName)

            file.outputStream().use {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
            }

            vacancy.photo = imgName

            return vacancy
        }

        private fun storeJsonToFile(
            vacancies: MutableList<VacancyVar>,
            path: File,
        ) {
            val mapper = ObjectMapper().writerWithDefaultPrettyPrinter()
            val jsonVacancy = mapper.writeValueAsString(vacancies)

            val writer = FileOutputStream(File(path, fileName))

            writer.write(jsonVacancy.toByteArray())

            writer.close()
        }

        fun readVacancies(context: Context): MutableList<VacancyVar> {
            val mapper = ObjectMapper()

            val path: File = context.filesDir
            val file: File = File(path, fileName)
            var vacs = mutableListOf<VacancyVar>()

            if (file.exists()) {
                vacs = mutableListOf(*mapper.readValue(file, Array<VacancyVar>::class.java))
                vacs.sortBy { it.id }
            }

            return vacs
        }

    }
}
