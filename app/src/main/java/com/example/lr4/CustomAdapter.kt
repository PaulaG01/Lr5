package com.example.lr4

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.lr4.malina.VacancyVar
import java.io.File

class CustomAdapter(
    private val context: Context,
    private val vacancies: ArrayList<VacancyVar>,
    private val imgs: ArrayList<Bitmap?>,
) : BaseAdapter()  {

    var tempNameVersionList = ArrayList(vacancies)
    var tempImagesList = ArrayList(imgs)

    override fun getViewTypeCount(): Int {
        return count
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getCount(): Int {
        return vacancies.size
    }

    override fun getItem(position: Int): Any {
        return vacancies[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val holder: ViewHolder

        if (convertView == null) {
            holder = ViewHolder()
            val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.lv_item, null, true)

            holder.tvname = convertView!!.findViewById(R.id.name) as TextView
            holder.iv = convertView!!.findViewById(R.id.imgView) as ImageView

            convertView.tag = holder
        } else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = convertView.tag as ViewHolder
        }

        holder.tvname!!.setText(vacancies[position].title)
        val imgFile = File(context.filesDir, "img_${vacancies[position].id}.png")
        if (imgFile.exists()) {
            val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
            holder.iv!!.setImageBitmap(myBitmap)
        }

        convertView.isLongClickable = false
        convertView.isFocusable = false

        return convertView
    }

    private inner class ViewHolder {
        val id: Int = 0
        var tvname: TextView? = null
        var iv: ImageView? = null
    }

    fun filter(text: String) {
        val text = text.lowercase()


        //Here We Clear Both ArrayList because We update according to Search query.
        imgs.clear()
        vacancies.clear()


        if (text.isEmpty()) {

            imgs.addAll(tempImagesList)
            vacancies.addAll(tempNameVersionList)
        } else {
            for (i in 0 until tempNameVersionList.size) {

                /*
                If our Search query is not empty than we Check Our search keyword in Temp ArrayList.
                if our Search Keyword in Temp ArrayList than we add to our Main ArrayList
                */

                if (tempNameVersionList[i].title!!.lowercase().contains(text)) {

                    imgs.add(tempImagesList[i])
                    vacancies.add(tempNameVersionList[i])
                }
            }
        }

        //This is to notify that data change in Adapter and Reflect the changes.
        notifyDataSetChanged()
    }

    fun sort(sortMode: Int) {
        when(sortMode) {
            1 -> {
                vacancies.sortBy { it.id }
            }
            0 -> {
                vacancies.sortByDescending { it.id }
            }
        }

        notifyDataSetChanged()
    }
}

