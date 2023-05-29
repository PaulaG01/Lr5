package com.example.lr4

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import com.example.lr4.malina.ElementsCreationHelper
import com.example.lr4.malina.VacancyFilesHelper

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ItemViewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ItemViewFragment : Fragment(R.layout.fragment_item_view) {
    private lateinit var context: Context

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context = view.context

        val itemId = arguments?.getInt("vacancyId")!!

        val vacancy =
            VacancyFilesHelper.readVacancies(context).find {
                it.id == itemId
            }!!
        val textViewName = view.findViewById<TextView>(R.id.textViewName)
        val textViewDescription = view.findViewById<TextView>(R.id.textViewDescription)
        val textViewDate = view.findViewById<TextView>(R.id.textViewDate)
        val textViewTime = view.findViewById<TextView>(R.id.textViewTime)
        val photoHolder = view.findViewById<LinearLayout>(R.id.photoHolder)

        textViewName.text = vacancy.title
        textViewDescription.text = vacancy.days as CharSequence?
        textViewDate.text = vacancy.city
//        textViewTime.text = vacancy.

        val dp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1f, this.getResources().getDisplayMetrics()).toInt()
        val img = ElementsCreationHelper.createImageView(context, "img_${vacancy.id}.png")
        img.layoutParams = ViewGroup.LayoutParams(200 * dp, 200 * dp)
        photoHolder.addView(img)
    }
}