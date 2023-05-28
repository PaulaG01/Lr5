package com.example.lr4.malina

import android.R
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.lr4.VacancyDetailsActivity
import java.io.File


class ElementsCreationHelper {
    companion object {
        fun createCard(context: Context, rootLayout: LinearLayout, vacancy: VacancyVar): CardView {
            val cardView = CardView(context)
            cardView.id = vacancy.id

            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, // CardView width
                LinearLayout.LayoutParams.WRAP_CONTENT // CardView height
            )

            // Set margins for card view
            layoutParams.setMargins(20, 20, 20, 20)

            // Set the card view layout params
            cardView.layoutParams = layoutParams

            // Set the card view corner radius
            cardView.radius = 16F

            // Set the card view content padding
            cardView.setContentPadding(25,25,25,25)

            // Set the card view background color
            cardView.setCardBackgroundColor(Color.rgb(243, 145, 160))

            // Set card view elevation
            cardView.cardElevation = 8F

            // Set card view maximum elevation
            cardView.maxCardElevation = 12F

            val dataStr = "${vacancy.id}. ${vacancy.title}, ${vacancy.city} \n" +
                    "${vacancy.days}"

            cardView.addView(createImageView(context, vacancy.photo))
            cardView.addView(createTextView(context, vacancy.id, dataStr))
            rootLayout.addView(cardView)

            addCardOnClickListener(cardView, context, vacancy)

            return cardView
        }

        private fun addCardOnClickListener(cardView: CardView, context: Context, vacancy: VacancyVar) {
            cardView.setOnClickListener {
                val intent = Intent(context, VacancyDetailsActivity::class.java)
                intent.putExtra("vacancyId", vacancy.id)
                context.startActivity(intent)
            }
        }

        private fun createTextView(context: Context, id: Int, str: String): TextView {
            val line = TextView(context)
            line.id = id
            line.text = str
            line.textSize = 14F
            line.setTextColor(Color.WHITE)
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(250, 20, 20, 20)
            line.layoutParams = params
            return line
        }

         fun createImageView(context: Context, fileName: String): ImageView {
            val img = ImageView(context)
            val params = LinearLayout.LayoutParams(
                200,
                200
            )
            val imgFile = File(context.filesDir, fileName)
            if (imgFile.exists()) {
                val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                img.setImageBitmap(myBitmap)
            } else {
                img.setImageResource(R.drawable.ic_menu_upload)
            }
            params.setMargins(20, 20, 20, 20)
            img.layoutParams = params
            return img
        }
    }
}