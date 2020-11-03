package com.ahndwon.bottomsheetcontainer

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.TypefaceSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ahndwon.nestedfragmentbottomsheetdialog.NestedFragmentBottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_test.view.*

class TestFragment : BaseFragment(), DialogInterface.OnShowListener {

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

        view.anotherDialogButton.setOnClickListener {
            val fragment = TestFragment()
            val builder = NestedFragmentBottomSheetDialog.Builder(fragment)
                    .setCallback(callback)
                    .setExpandHandle(true)
                    .removeDim(true)
                    .setTopMargin(100f)
//                .setLayerMargin(100f)
//                .setPeekHeight(600f)
                    .showExpanded(true)
                    .setFullScreen(true)
                    .isHideable(true)
                    .setCloseButton(true)
                    .setTextCloseButton("닫기")
                    .setTitle("test title")
                    .setTitleTextAppearance(R.style.TestDialogTitle)
                    .setCloseButtonTextAppearance(R.style.TestDialogCancel)


            val dialog = builder.build()

            fragment.dialog = dialog

            dialog.show(childFragmentManager, "")
        }

        view.activityButton.setOnClickListener {
            startActivity(Intent(view.context, MainActivity::class.java))
        }

        spanTest(view.title, "test", "desc")

        return view
    }

    private fun spanTest(textView: TextView, name: String, description: String) {
        val ssb = SpannableString("$name $description")

        ssb.setSpan(
                ForegroundColorSpan(Color.parseColor("#5F74FB")),
                0,
                name.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        ssb.setSpan(
                ForegroundColorSpan(Color.parseColor("#333333")),
                name.length,
                name.length + description.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        ssb.setSpan(TypefaceSpan("serif"), 0, name.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        ssb.setSpan(
                TypefaceSpan("monospace"),
                name.length,
                name.length + description.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        textView.text = ssb
    }

    override fun onShow(dialog: DialogInterface?) {
        Log.d(TAG, "onShow: $dialog")
    }

    companion object {
        const val TAG = "TestFragment"
    }
}