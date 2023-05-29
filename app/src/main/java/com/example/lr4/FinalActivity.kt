package com.example.lr4

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.ContextMenu
import android.view.ContextMenu.ContextMenuInfo
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView.AdapterContextMenuInfo
import android.widget.LinearLayout
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuItemCompat
import com.example.lr4.malina.VacancyFilesHelper
import com.example.lr4.malina.VacancyVar
import java.io.File


class FinalActivity : AppCompatActivity() {
    private lateinit var linLayout: LinearLayout

    private var customAdapter: CustomAdapter? = null
    lateinit var vacanciesLV: ListView
    lateinit var vacanciesList: ArrayList<VacancyVar>
    lateinit var imagesList: ArrayList<Bitmap?>
    private lateinit var searchView: SearchView

    private val CONTEXT_SHOW: Int = 1
    private val CONTEXT_DELETE: Int = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_final)

        linLayout = findViewById<LinearLayout>(R.id.ll)
        vacanciesLV = findViewById(R.id.vacLv)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        vacanciesList = populateList()
        imagesList = populateImagesList()
        customAdapter = CustomAdapter(this, vacanciesList!!, imagesList!!)
        vacanciesLV!!.adapter = customAdapter
        vacanciesLV.setOnItemClickListener { adapterView, view, position, l ->
            val id: Int = (customAdapter?.getItem(position) as VacancyVar).id
            val intent = Intent(this, VacancyDetailsActivity::class.java)
            intent.putExtra("vacancyId", id)
            startActivity(intent)
            finish()
        }

        registerForContextMenu(vacanciesLV)
    }

    private fun populateImagesList(): ArrayList<Bitmap?> {
        val list = ArrayList<Bitmap?>()

        val vacancies = VacancyFilesHelper.readVacancies(this)
        for (i in 0 until vacancies.count()) {
            val imgFile = File(this.filesDir, "img_${vacancies[i].id}.png")
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

        val vacancies = VacancyFilesHelper.readVacancies(this)
        for (i in 0 until vacancies.count()) {
            val vacancy = VacancyVar()
            vacancy.title = vacancies[i].title
            vacancy.photo = vacancies[i].photo
            vacancy.id = vacancies[i].id
            list.add(vacancy)
        }

        return list
    }

    private fun cofigureSearch() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val text = newText!!
                customAdapter?.filter(text)
                return false
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_final, menu)

        val searchViewItem: MenuItem? = menu?.findItem(R.id.appSearchBar)
        searchView = MenuItemCompat.getActionView(searchViewItem) as SearchView

        cofigureSearch()

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId;

        if (id == R.id.action_add_item) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        if (id == R.id.sortAsc) {
            customAdapter?.sort(0)
        }
        if (id == R.id.sortDesc) {
            customAdapter?.sort(1)
        }

        return super.onOptionsItemSelected(item);
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View?, menuInfo: ContextMenuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val vacancy = menuInfo as AdapterContextMenuInfo
        val title: String = (customAdapter?.getItem(vacancy.position) as VacancyVar).title
        val id: Int = (customAdapter?.getItem(vacancy.position) as VacancyVar).id
        menu.setHeaderTitle("$title --- $id")
        menu.add(Menu.NONE, CONTEXT_DELETE, Menu.NONE, "Удалить")
        menu.add(Menu.NONE, CONTEXT_SHOW, Menu.NONE, "Редактировать")
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val intent = Intent(this, VacancyDetailsActivity::class.java)
        val vacancy = item.menuInfo as AdapterContextMenuInfo
        val id: Int = (customAdapter?.getItem(vacancy.position) as VacancyVar).id
        intent.putExtra("vacancyId", id)

        when (item.itemId) {
            CONTEXT_SHOW -> {
                startActivity(intent)
                finish()
            }

            CONTEXT_DELETE -> deleteVacancy(id)
        }

        return super.onContextItemSelected(item)
    }

    private fun deleteVacancy(vacancyId: Int) {
        val vacancy: VacancyVar? =
            VacancyFilesHelper.readVacancies(this).find { it.id == vacancyId }
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Подтвердите правки")
            .setCancelable(false)
            .setPositiveButton("Да") { dialog, _ ->
                if (vacancy != null) {
                    VacancyFilesHelper.deleteVacancy(vacancy, this)
                }
                finish()
                startActivity(intent)
            }
            .setNegativeButton("Нет") { dialog, _ ->
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }

//    private fun displayVacancies() {
//        val vacancies = VacancyFilesHelper.readVacancies(this)
//
//        for (i in 0 until vacancies.count()) {
//            val card = ElementsCreationHelper.createCard(this, linLayout, vacancies[i])
//            card.id = vacancies[i].id
//            registerForContextMenu(card)
//        }
//    }

}