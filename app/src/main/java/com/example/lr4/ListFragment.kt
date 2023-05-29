package com.example.lr4

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.example.lr4.malina.VacancyFilesHelper
import com.example.lr4.malina.VacancyVar
import java.io.File

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListFragment : Fragment(R.layout.fragment_list) {
    lateinit var customAdapter: CustomAdapter
    lateinit var vacanciesLV: ListView
    lateinit var vacanciesList: ArrayList<VacancyVar>
    lateinit var imagesList: ArrayList<Bitmap?>

    private lateinit var contextt: Context

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        contextt = view.context

        vacanciesLV = view.findViewById(R.id.listViewItems)
        vacanciesList = populateList()
        imagesList = populateImagesList()
        customAdapter = CustomAdapter(contextt, vacanciesList!!, imagesList!!)
        vacanciesLV.adapter = customAdapter
        vacanciesLV.setOnItemClickListener { adapterView, view, position, l ->
            if(contextt is FinalActivity)
            {
                val idd: Int = (customAdapter.getItem(position) as VacancyVar).id
                (contextt as FinalActivity).showItem(idd)
            }

        }

        registerForContextMenu(vacanciesLV)
    }
    private fun populateImagesList(): ArrayList<Bitmap?> {
        val list = ArrayList<Bitmap?>()

        val vacancies = VacancyFilesHelper.readVacancies(contextt)
        for (i in 0 until vacancies.count()) {
            val imgFile = File(contextt.filesDir, "img_${vacancies[i].id}.png")
            var myBitmap: Bitmap? = null
            if (imgFile.exists()) {
                myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
            }
            list.add(myBitmap)
        }
        return list
    }

    private fun populateList(): ArrayList<VacancyVar> {
        val list = ArrayList<VacancyVar>()

        val vacancies = VacancyFilesHelper.readVacancies(contextt)
        for (i in 0 until vacancies.count()) {
            val vacancy = VacancyVar()
            vacancy.title = vacancies[i].title
            vacancy.photo = vacancies[i].photo
            vacancy.id = vacancies[i].id
            list.add(vacancy)
        }

        return list
    }

}