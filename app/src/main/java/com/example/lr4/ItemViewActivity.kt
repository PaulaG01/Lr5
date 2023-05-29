package com.example.lr4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit

class ItemViewActivity : AppCompatActivity() {
    private lateinit var viewFragment : ItemViewFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_view)

        val imageButtonBack = findViewById<Button>(R.id.imageButtonBack)
        imageButtonBack.setOnClickListener { view ->
            finish()
        }

        val itemId = getIntent().extras?.getInt("vacancyId")!!

        if (savedInstanceState == null) {
            viewFragment = ItemViewFragment()
            viewFragment.arguments = Bundle()
            viewFragment.arguments?.putInt("vacancyId", itemId)
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(R.id.fragmentContainerDetails, viewFragment)
                addToBackStack(null)
            }
        }
        else
        {
            val fragmentContainerItems = findViewById<FragmentContainerView>(R.id.fragmentContainerDetails)
            viewFragment = fragmentContainerItems.getFragment()
        }
    }
}