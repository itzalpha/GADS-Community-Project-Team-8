package com.example.gadshealthteam8.support

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.gadshealthteam8.databinding.FragmentAboutDeveloperBinding


class AboutDeveloperFragment : Fragment() {

    private var _binding : FragmentAboutDeveloperBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAboutDeveloperBinding.inflate(inflater, container, false)

//
//        _binding!!.twitterCeo.setOnClickListener {
//
//            val uri = Uri.parse("https://twitter.com/KoredeAhmed1")
//            val likeIng = Intent(Intent.ACTION_VIEW, uri)
//
//            likeIng.setPackage("com.twitter.android")
//
//            try {
//                startActivity(likeIng)
//            } catch (e: ActivityNotFoundException) {
//                startActivity(
//                    Intent(
//                        Intent.ACTION_VIEW,
//                        Uri.parse("https://twitter.com/KoredeAhmed1")
//                    )
//                )
//            }
//
//        }
//
//        _binding!!.linkedInCeo.setOnClickListener {
//
//            val uri = Uri.parse("https://www.linkedin.com/in/ahmed-mukhtar-7245851b5")
//            val likeIng = Intent(Intent.ACTION_VIEW, uri)
//
//            likeIng.setPackage("com.linkedin.android")
//
//            try {
//                startActivity(likeIng)
//            } catch (e: ActivityNotFoundException) {
//                startActivity(
//                    Intent(
//                        Intent.ACTION_VIEW,
//                        Uri.parse("https://www.linkedin.com/in/ahmed-mukhtar-7245851b5")
//                    )
//                )
//            }
//
//
//        }
//
//        _binding!!.instagramCeo.setOnClickListener {
//            val uri = Uri.parse("https://www.instagram.com/itzlonelyalphamk/")
//            val likeIng = Intent(Intent.ACTION_VIEW, uri)
//
//            likeIng.setPackage("com.instagram.android")
//
//            try {
//                startActivity(likeIng)
//            } catch (e: ActivityNotFoundException) {
//                startActivity(
//                    Intent(
//                        Intent.ACTION_VIEW,
//                        Uri.parse("https://www.instagram.com/itzlonelyalphamk/")
//                    )
//                )
//            }
//        }

        return _binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}