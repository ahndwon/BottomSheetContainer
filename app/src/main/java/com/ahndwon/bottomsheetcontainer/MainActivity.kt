package com.ahndwon.bottomsheetcontainer

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.ahndwon.nestedfragmentbottomsheetdialog.NestedFragmentBottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val callback = object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                Log.d("onSlide", "offset : $slideOffset")
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                Log.d("onStateChanged", "offset : $newState")
            }
        }

        Log.d("TEST", "안동원동".toByteArray().size.toString())
        Log.d("TEST", "testest".toByteArray().size.toString())

        bottomSheetDialogButton.setOnClickListener {
            val fragment = TestFragment()
            val builder = NestedFragmentBottomSheetDialog.Builder(fragment)
                    .setCallback(callback)
                    .setExpandHandle(true)
                    .removeDim(false)
                    .showExpanded(true)
                    .setExpandHandle(true)
                    .setFullScreen(true)
                    .setCloseButton(true)
                    .setTitle("test title")

            val dialog = builder.build()

            fragment.dialog = dialog

            dialog.show(supportFragmentManager, "")
        }

        scrollTestButton.setOnClickListener {
            NestedFragmentBottomSheetDialog.Builder(TestScrollViewFragment())
                    .setCallback(callback)
                    .setExpandHandle(true)
//                .removeDim(true)
                    .setCloseButton(true)
                    .setExpandHandle(true)
//                .showExpanded(true)
                    .setTitle("테스트")
                    .isHideable(true)
                    .build()
                    .show(supportFragmentManager, "")
        }

        recyclerViewTestButton.setOnClickListener {
            NestedFragmentBottomSheetDialog.Builder(TestRecyclerViewFragment())
                    .setCallback(callback)
                    .setExpandHandle(true)
//                .removeDim(true)
                    .setCloseButton(true)
                    .setExpandHandle(true)
                    .setTopMargin(50f)
                    .setTitle("테스트")
                    .setPeekHeight(320f)
                    .build()
                    .show(supportFragmentManager, "")
        }

        noExpandHandleButton.setOnClickListener {
            NestedFragmentBottomSheetDialog.Builder(TestFragment())
                    .setCallback(callback)
                    .setExpandHandle(false)
                    .build().show(supportFragmentManager, "")
        }

        noDimButton.setOnClickListener {
            NestedFragmentBottomSheetDialog.Builder(TestFragment())
                    .setCallback(callback)
                    .setPeekHeight(200f)
                    .setExpandHandle(true)
                    .removeDim(true)
                    .build().show(supportFragmentManager, "")
        }

        noToolbarButton.setOnClickListener {
            NestedFragmentBottomSheetDialog.Builder(TestFragment())
                    .setCallback(callback)
                    .setPeekHeight(300f)
                    .setExpandHandle(true)
                    .removeToolbar(true)
                    .build().show(supportFragmentManager, "")
        }

        topMargin200Button.setOnClickListener {
            NestedFragmentBottomSheetDialog.Builder(TestFragment())
//                .setCallback(callback)
                    .setTopMargin(100f)
//                .showExpanded(true)
                    .setFullScreen(true)
                    .setExpandHandle(true)
                    .build()
                    .show(supportFragmentManager, "")
        }

        peekHeight100Button.setOnClickListener {
            NestedFragmentBottomSheetDialog.Builder(TestFragment())
                    .setCallback(callback)
                    .setPeekHeight(100f)
                    .setExpandHandle(true)
                    .build().show(supportFragmentManager, "")
        }

        expandedButton.setOnClickListener {
            NestedFragmentBottomSheetDialog.Builder(TestFragment())
                    .setCallback(callback)
                    .showExpanded(true)
                    .setExpandHandle(true)
                    .removeToolbar(true)
                    .setFullScreen(true) // Fragment의 child view들이 충분할 경우 필요없음
                    .build().show(supportFragmentManager, "")
        }

        fullscreenButton.setOnClickListener {
            NestedFragmentBottomSheetDialog.Builder(TestFragment())
                    .setCallback(callback)
                    .showExpanded(true)
                    .setFullScreen(true)
                    .build().show(supportFragmentManager, "")
        }

        closeButton.setOnClickListener {
            NestedFragmentBottomSheetDialog.Builder(TestFragment())
                    .setCallback(callback)
                    .setCloseButton(true)
                    .setCloseButtonDrawable(
                            ContextCompat.getDrawable(
                                    this,
                                    R.drawable.ic_close_wh
                            )
                    )
                    .setFullScreen(true)
                    .build().show(supportFragmentManager, "")
        }

        titleButton.setOnClickListener {
            NestedFragmentBottomSheetDialog.Builder(TestFragment())
                    .setCallback(callback)
                    .setTitle("전체 계좌")
                    .setToolbarBackground(
                            ContextCompat.getDrawable(
                                    this,
                                    R.drawable.bg_bottom_sheet_toolbar_gradient
                            )
                    )
                    .setCloseButton(true)
                    .setCloseButtonDrawable(
                            ContextCompat.getDrawable(
                                    this,
                                    R.drawable.ic_close_wh
                            )
                    )
                    .setTitleTextAppearance(R.style.TestDialogTitle)
                    .build().show(supportFragmentManager, "")
        }

        textCloseButton.setOnClickListener {
            NestedFragmentBottomSheetDialog.Builder(TestFragment())
                    .setCallback(callback)
                    .setTextCloseButton("닫기버튼")
                    .showExpanded(true)
                    .setCloseButtonTextAppearance(R.style.TestDialogCancel)
                    .build().show(supportFragmentManager, "")
        }

        notHideableButton.setOnClickListener {
            NestedFragmentBottomSheetDialog.Builder(TestFragment())
                    .setCallback(callback)
                    .isHideable(false)
                    .showExpanded(true)
                    .setFullScreen(true)
                    .build().show(supportFragmentManager, "")
        }

        softKeyResizeButton.setOnClickListener {
            NestedFragmentBottomSheetDialog.Builder(TestFragment())
                    .setCallback(callback)
                    .setTextCloseButton("닫기버튼")
                    .setSoftKeyMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                    .setCloseButtonTextAppearance(R.style.TestDialogCancel)
                    .build().show(supportFragmentManager, "")
        }

        outerViewButton.setOnClickListener {
            NestedFragmentBottomSheetDialog.Builder(TestFragment())
                    .setCallback(callback)
                    .setExpandHandle(true)
                    .setFullScreen(true)
                    .setTitle("전체 계좌")
                    .setToolbarBackground(
                            ContextCompat.getDrawable(
                                    this,
                                    R.drawable.bg_bottom_sheet_toolbar_gradient
                            )
                    )
                    .setCloseButton(true)
                    .setCloseButtonDrawable(
                            ContextCompat.getDrawable(
                                    this,
                                    R.drawable.ic_close_wh
                            )
                    )
                    .setOuterView(R.layout.item_outer_view) {
                        Toast.makeText(this, "outer view", Toast.LENGTH_SHORT).show()
                    }
                    .build().show(supportFragmentManager, "")
        }

        toolbar_background.setOnClickListener {
            NestedFragmentBottomSheetDialog.Builder(TestFragment())
                    .setCallback(callback)
                    .setExpandHandle(true)
                    .setTitle("전체 계좌")
                    .setToolbarBackground(
                            ContextCompat.getDrawable(
                                    this,
                                    R.drawable.bg_bottom_sheet_toolbar_gradient
                            )
                    )
                    .setCloseButton(true)
                    .setCloseButtonDrawable(
                            ContextCompat.getDrawable(
                                    this,
                                    R.drawable.ic_close_wh
                            )
                    )
                    .build().show(supportFragmentManager, "")
        }

        fix_height.setOnClickListener {
            NestedFragmentBottomSheetDialog.Builder(TestFragment())
                    .setTopMargin(50f)
                    .setPeekHeight(500f)
                    .fixBottomSheet()
                    .showExpanded(true)
//                    .setFullScreen(true)
//                    .showExpanded(true)
//                    .setPeekHeight(400f)
//                    .setExpandHandle(true)
                    .build().show(supportFragmentManager, "")
        }

        transparent.setOnClickListener {
            NestedFragmentBottomSheetDialog.Builder(TestFragment())
                    .setTransparentBackground()
                    .build().show(supportFragmentManager, "")
        }

        webView.setOnClickListener {
            NestedFragmentBottomSheetDialog.Builder(WebViewFragment())
//                    .setTopMargin(50f)
//                    .setPeekHeight(500f)
                    .removeToolbar(true)
                    .fixBottomSheet()
                    .showExpanded(true)
                    .setTransparentBackground()
                    .build().show(supportFragmentManager, "")
        }
    }
}
