package com.example.expandablebottomsheet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottom = ExpandableBottomSheet.Builder(TestFragment(), coordinator)
            .isCloseable(true)
            .setPeekHeight(200)
            .build()

        bottomSheetButton.setOnClickListener {
            bottom.show(supportFragmentManager)
        }

    }
}
