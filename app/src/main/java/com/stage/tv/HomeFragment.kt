package com.stage.tv

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class HomeFragment : Fragment() {

    lateinit var txtTitle: TextView
    lateinit var txtSubTitle: TextView
    lateinit var txtDescription: TextView

    lateinit var imgBanner: ImageView
    lateinit var listFragment: ListFragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)

        // Add this inside onViewCreated() after init(view)
        /*view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                // When user presses LEFT, open the menu
                (requireActivity() as? MainActivity)?.apply {
                    openMenu()
                }
                true
            } else {
                false
            }
        }
*/
    }

     fun init(view: View) {

        imgBanner = view.findViewById(R.id.img_banner)
        txtTitle = view.findViewById(R.id.title)
        txtSubTitle = view.findViewById(R.id.subtitle)
        txtDescription = view.findViewById(R.id.description)


        listFragment = ListFragment()
        val transaction = childFragmentManager.beginTransaction()
        transaction.add(R.id.list_fragment, listFragment)
        transaction.commit()


        val gson = Gson()
        val i: InputStream = requireContext().assets.open("movies.json")
        val br = BufferedReader(InputStreamReader(i))
        val dataList: DataModel = gson.fromJson(br, DataModel::class.java)

        listFragment.bindData(dataList)

        listFragment.setOnContentSelectedListener {
            updateBanner(it)
        }

         listFragment.setOnItemClickListener {
             val intent  = Intent(requireContext(), PlayerActivity::class.java)
             intent.putExtra("id", it.id)
             intent.putExtra("promo_url", it.promo_url)
             startActivity(intent)
         }
    }

    fun updateBanner(dataList: DataModel.Result.Detail) {
        txtTitle.text = dataList.title
        txtDescription.text = dataList.overview

        val url1 = "https://dbcmsassets.docubay.com/featured-images/1760806704-antarctica-searching-for-adaptation-1024x576-768x432.jpg"

        Glide.with(this).load(url1).into(imgBanner)
    }
}