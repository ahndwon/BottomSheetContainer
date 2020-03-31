package com.example.expandablebottomsheet

import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.bottom_sheet_layout.view.*

class ExpandableBottomSheet<T : Fragment> private constructor(builder: Builder<T>) {
    var fragment: T

    var coordinatorLayout: CoordinatorLayout

    var isExpandable: Boolean = false

    var isCloseable: Boolean = false

    var isHideable: Boolean = false

    var closeButtonText: String? = null

    var isRemoveToolbar: Boolean = false

    var peekHeight: Int? = null

    var bottomSheetView: View? = null

    var sheetBehavior: BottomSheetBehavior<LinearLayout>? = null


    init {
        this.fragment = builder.fragment
        this.coordinatorLayout = builder.coordinatorLayout
        this.isExpandable = builder.isExpandable
        this.peekHeight = builder.peekHeight
        this.isCloseable = builder.isCloseable
        this.closeButtonText = builder.closeButtonText
        this.isRemoveToolbar = builder.isRemoveToolbar
    }

    fun show(fragmentManager: FragmentManager) {
        bottomSheetView = LayoutInflater.from(coordinatorLayout.context)
            .inflate(R.layout.bottom_sheet_layout, coordinatorLayout, false)
        bottomSheetView?.let { inflated ->
            fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit()
            sheetBehavior = BottomSheetBehavior.from(inflated.sheet)
            applySettings(inflated)
        }
        coordinatorLayout.addView(bottomSheetView)
    }

    private fun applySettings(inflated: View) {
        inflated.toolbar.visibility = if (isRemoveToolbar) View.GONE else View.VISIBLE
        inflated.toolbar.closeButton.visibility = if (isCloseable) View.GONE else View.VISIBLE
        peekHeight?.let { peekHeight ->
            sheetBehavior?.peekHeight = peekHeight
            sheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    class Builder<T : Fragment>(val fragment: T, val coordinatorLayout: CoordinatorLayout) {

        var isExpandable: Boolean = false
            private set

        var isCloseable: Boolean = false
            private set

        var closeButtonText: String? = null
            private set

        var isRemoveToolbar: Boolean = false
            private set

        var peekHeight: Int? = null

        fun isExpandable(isExpandable: Boolean) = apply { this.isExpandable = isExpandable }

        fun isCloseable(isCloseable: Boolean) = apply {
            this.isCloseable = isCloseable
            this.closeButtonText = null
        }

        fun setTextCloseButton(text: String) = apply {
            this.isCloseable = true
            this.closeButtonText = text
        }

        fun removeToolbar(isRemove: Boolean) = apply { this.isRemoveToolbar = isRemove }

        fun setPeekHeight(peekHeight: Int) = apply { this.peekHeight = peekHeight }

        fun build() = ExpandableBottomSheet(this)
    }
}
