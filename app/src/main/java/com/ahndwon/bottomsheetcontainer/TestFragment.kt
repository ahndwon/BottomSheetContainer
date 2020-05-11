package com.ahndwon.bottomsheetcontainer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ahndwon.nestedfragmentbottomsheetdialog.NestedFragmentBottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_test.view.*

class TestFragment : BaseFragment() {

    var dialog: NestedFragmentBottomSheetDialog<TestFragment>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_test, container, false)

        val callback = object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                Log.d("onSlide", "offset : $slideOffset")
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                Log.d("onStateChanged", "offset : $newState")
            }

        }

        view.anotherDialogButton.setOnClickListener {
            dialog?.dismiss()
        }

//        view.anotherDialogButton.setOnClickListener {
//            val fragment = TestFragment()
//            val builder = NestedFragmentBottomSheetDialog.Builder(fragment)
//                .setCallback(callback)
//                .setExpandHandle(true)
//                .removeDim(true)
//                .setTopMargin(100f)
////                .setLayerMargin(100f)
////                .setPeekHeight(600f)
//                .showExpanded(true)
//                .setFullScreen(true)
//                .isHideable(true)
//                .setCloseButton(true)
//                .setTextCloseButton("닫기")
//                .setTitle("test title")
//                .setTitleTextAppearance(R.style.TestDialogTitle)
//                .setCloseButtonTextAppearance(R.style.TestDialogCancel)
//
//
//            val dialog = builder.build()
//
//            fragment.dialog = dialog
//
//            dialog.show(childFragmentManager, "")
//        }

        view.activityButton.setOnClickListener {
            startActivity(Intent(view.context, MainActivity::class.java))
        }


        return view
    }
}