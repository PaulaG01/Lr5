package com.example.lr4

import android.content.Intent
import android.content.res.Configuration
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
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import com.example.lr4.malina.VacancyFilesHelper
import com.example.lr4.malina.VacancyVar


class FinalActivity : AppCompatActivity() {
    private lateinit var searchView: SearchView

    private lateinit var listFragment : ListFragment

    private val CONTEXT_SHOW: Int = 1
    private val CONTEXT_DELETE: Int = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_final)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        if (savedInstanceState == null) {
            listFragment = ListFragment()
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(R.id.fragmentContainerItems, listFragment)
            }
        }
        else
        {
            val fragmentContainerItems = findViewById<FragmentContainerView>(R.id.fragmentContainerItems)
            listFragment = fragmentContainerItems.getFragment()
        }
    }

    fun showItem(id: Int) {
        val orientation = getResources().getConfiguration().orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            val viewFragment = ItemViewFragment()
            viewFragment.arguments = Bundle()
            viewFragment.arguments?.putInt("vacancyId", id)
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(R.id.fragmentContainerDetails, viewFragment)
                addToBackStack(null)
            }
        } else {
            val intent = Intent(this, VacancyDetailsActivity::class.java)
            intent.putExtra("vacancyId", id)
            startActivity(intent)
            finish()
        }
    }

    private fun cofigureSearch() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val text = newText!!
                listFragment.customAdapter.filter(text)
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
            listFragment.customAdapter.sort(0)
        }
        if (id == R.id.sortDesc) {
            listFragment.customAdapter.sort(1)
        }

        return super.onOptionsItemSelected(item);
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View?, menuInfo: ContextMenuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val vacancy = menuInfo as AdapterContextMenuInfo
        val title: String = (listFragment.customAdapter.getItem(vacancy.position) as VacancyVar).title
        val id: Int = (listFragment.customAdapter.getItem(vacancy.position) as VacancyVar).id
        menu.setHeaderTitle("$title --- $id")
        menu.add(Menu.NONE, CONTEXT_DELETE, Menu.NONE, "Удалить")
        menu.add(Menu.NONE, CONTEXT_SHOW, Menu.NONE, "Редактировать")
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val intent = Intent(this, VacancyDetailsActivity::class.java)
        val vacancy = item.menuInfo as AdapterContextMenuInfo
        val id: Int = (listFragment.customAdapter.getItem(vacancy.position) as VacancyVar).id
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

}