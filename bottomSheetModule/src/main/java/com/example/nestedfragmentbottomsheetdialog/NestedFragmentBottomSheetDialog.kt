package com.example.nestedfragmentbottomsheetdialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Point
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.StyleRes
import androidx.core.view.updateLayoutParams
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_layout.*
import kotlinx.android.synthetic.main.bottom_sheet_layout.view.*

//
//  NestedFragmentBottomSheetDialog
//
//  Created by Dongwon Ahn on 2020-03-30
//  Copyright © 2020 Shinhan. All rights reserved.
//

/**
 * 외부에서 프래그먼트를 받아서 띄워주는 BottomSheetDialogFragment
 *
 * 기본적으로 BottomSheetDialogFragment 를 상속
 * @see BottomSheetDialogFragment
 *
 * @param T 표시할 Fragment
 * @constructor
 *  빌더 패턴을 사용했기 떄문에 Builder 를 통해 인스턴스 생성해야됨
 *
 * @param builder
 */
class NestedFragmentBottomSheetDialog<T : Fragment> private constructor(builder: Builder<T>) :
    BottomSheetDialogFragment() {
    val fragment: T

    var isExpandHandle: Boolean = false

    var isCloseable: Boolean = false

    var isHideable: Boolean = true

    var isFullScreen: Boolean = false

    var showExpanded: Boolean = false

    var closeButtonText: String? = null

    var title: String? = null

    var isRemoveToolbar: Boolean = false

    var isLayerDetectionOn: Boolean = false

    var isRemoveDim: Boolean = false

    var peekHeight: Float? = null

    var topMargin: Float = 0f

    var layerMargin: Float = DEFAULT_LAYER_MARGIN

    @StyleRes
    var titleTextStyle: Int? = null

    @StyleRes
    var closeButtonTextStyle: Int? = null

    var bottomSheet: FrameLayout? = null

    var sheetBehavior: BottomSheetBehavior<FrameLayout>? = null

    var callback: BottomSheetBehavior.BottomSheetCallback? = null


    private var isDebug = false

    init {
        this.fragment = builder.fragment
        this.isExpandHandle = builder.isExpandHandle
        this.peekHeight = builder.peekHeightDp
        this.topMargin = builder.topMargin
        this.layerMargin = builder.layerMargin
        this.isCloseable = builder.isCloseButton
        this.isHideable = builder.isHideable
        this.isFullScreen = builder.isFullScreen
        this.showExpanded = builder.showExpanded
        this.closeButtonText = builder.closeButtonText
        this.title = builder.title
        this.titleTextStyle = builder.titleTextStyle
        this.closeButtonTextStyle = builder.closeButtonTextStyle
        this.isRemoveToolbar = builder.isRemoveToolbar
        this.isLayerDetectionOn = builder.isLayerDetectionOn
        this.isRemoveDim = builder.isRemoveDim
        this.callback = builder.callback
    }

    /**
     * 다이얼로그의 background 를 style 통해 적용함
     *
     * @return
     */
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_layout, container, false)

        childFragmentManager.beginTransaction().add(R.id.fragmentContainer, fragment).commit()

        if (layers < 0) layers = 0

        return view
    }

    /**
     * fragmentContainer 의 높이를 화면 전체로 변경
     *
     * fragment 의 root view 의 height 가 match_parent 여도 화면을 가득 채우지 않기 때문에 따로 화면 크기를 계산하여
     * fragment 를 담고 있는 R.id.fragmentContainer 의 height 를 조정
     */
    private fun setFullscreenWithMargin() {
        if (isFullScreen.not()) return

        val display = dialog?.ownerActivity?.windowManager?.defaultDisplay ?: return

        val size = Point()
        display.getSize(size)

        val toolbarHeight = if (isRemoveToolbar) {
            0
        } else {
            view?.toolbar?.layoutParams?.height ?: 0
        }

        view?.let { view ->
            val layerDistance = if (isLayerDetectionOn) {
                convertDpToPx(view.context, layerMargin) * (layers)
            } else {
                0
            }

            view.fragmentContainer?.updateLayoutParams {
                this.height =
                    size.y - toolbarHeight - convertDpToPx(
                        view.context,
                        topMargin
                    ) - layerDistance
            }
        }

    }

    override fun onResume() {
        super.onResume()

        if (isRemoveDim) {
            removeDim()
        }

        bottomSheet =
            dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout

        bottomSheet?.let {
            sheetBehavior = BottomSheetBehavior.from(it)
        }

        setFullscreenWithMargin()

        this@NestedFragmentBottomSheetDialog.view?.let {
            applySettings(it)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog: BottomSheetDialog =
            super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        setDialogShowState(dialog)

        return dialog
    }

    private fun setDialogShowState(dialog: BottomSheetDialog) {
        dialog.setOnShowListener { dialogInterface ->
            if (showExpanded.not()) return@setOnShowListener

            val shownDialog: BottomSheetDialog = dialogInterface as BottomSheetDialog
            val bottomSheet =
                shownDialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
                    ?: return@setOnShowListener

            val behavior = BottomSheetBehavior.from(bottomSheet)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED

            behavior.isHideable = isHideable
            layers++
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        layers--
    }

    private fun addExpandHandle() {
        view?.expandHandleStub?.inflate()
    }

    /**
     * 빌더 사용 시 설정한 설정들을 적용
     *
     * 모든 설정들이 여기서 적용되지는 않음.
     *
     * @param inflated 생성된 프래그먼트 다이얼로그의 뷰
     */
    private fun applySettings(inflated: View) {
        if (isExpandHandle) {
            addExpandHandle()
        }

        setupToolbar(inflated)
        setupPeekHeight(inflated)
        setupCallbacks()
    }

    /**
     * 다이얼로그의 툴바 설정
     *
     * 제목, hideable, 닫기 버튼을 설정
     *
     * @param inflated 생성된 프래그먼트 다이얼로그의 뷰
     */
    private fun setupToolbar(inflated: View) {
        inflated.toolbar.visibility = if (isRemoveToolbar) View.GONE else View.VISIBLE

        if (!title.isNullOrEmpty()) {
            showTitle()
        }

        if (isHideable) {
            sheetBehavior?.isHideable = isHideable
        }

        when {
            isCloseable && closeButtonText == null -> addIconCloseButton()
            isCloseable && closeButtonText != null -> addTextCloseButton()
        }
    }

    /**
     * peekHeight 설정
     *
     * 빌더 사용 시 입력한 peekHeight 를 설정함
     * 입력 값을 dp 값으로 변환해서 적용
     *
     * @param inflated 생성된 프래그먼트 다이얼로그의 뷰
     */
    private fun setupPeekHeight(inflated: View) {
        peekHeight?.let { peekHeight ->
            sheetBehavior?.peekHeight = convertDpToPx(inflated.context, peekHeight)
            sheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    /**
     * BottomSheetBehavior callback 들을 적용함
     *
     * 콜백을 여러개 추가 가능
     */
    private fun setupCallbacks() {
        callback?.let { callback ->
            sheetBehavior?.let { behavior ->
                behavior.addBottomSheetCallback(callback)
                addCollapseOnSwipeDown(behavior)
            }
        }
    }

    /**
     * BottomSheet 확장 되었을 시 단 한번에 숨김기
     *
     * 기본 스와이프 다운 작동/
     * STATE_EXPANDED -> STATE_COLLAPSED -> STATE_HIDDEN
     *
     * 바로 STATE_EXPANDED -> STATE_HIDDEN 으로 가기 위해
     * 다이얼로그가 STATE_EXPANDED 되고 난 후 STATE를 저장
     *
     *
     * @param behavior 현재 bottom sheet의 BottomSheetBehavior<FrameLayout>
     */
    private fun addCollapseOnSwipeDown(behavior: BottomSheetBehavior<FrameLayout>) {
        var beforeState = behavior.state
        var tempState = behavior.state
        var hasExpanded = false

        behavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if (isHideable &&
                    hasExpanded &&
                    beforeState == BottomSheetBehavior.STATE_DRAGGING &&
                    tempState == BottomSheetBehavior.STATE_SETTLING &&
                    slideOffset < 0.9
                ) {
                    hide()
                }
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    hasExpanded = true
                }

                beforeState = tempState

                if (isHideable &&
                    hasExpanded &&
                    beforeState == BottomSheetBehavior.STATE_SETTLING &&
                    newState == BottomSheetBehavior.STATE_COLLAPSED
                ) {
                    hide()
                }

                tempState = newState
            }

        })
    }

    /**
     * 아이콘 닫기 버튼 추가
     *
     * iconCloseButtonStub ViewStub 을 inflate
     */
    private fun addIconCloseButton() {
        iconCloseButtonStub ?: return

        val view = iconCloseButtonStub.inflate()
        view.setOnClickListener { dismiss() }
    }

    /**
     * 아이콘 닫기 버튼 추가
     *
     * iconCloseButtonStub ViewStub 을 inflate
     * closeButtonTextStyle 적용
     */
    private fun addTextCloseButton() {
        textCloseButtonStub ?: return

        val view = textCloseButtonStub.inflate() as? TextView? ?: return
        view.setOnClickListener { dismiss() }

        view.text = closeButtonText

        TextViewCompat.setTextAppearance(
            view,
            closeButtonTextStyle ?: return
        )
    }

    /**
     * BottomSheet 축소 된 상태로 전환
     *
     */
    fun collapse() {
        sheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    /**
     * BottomSheet 를 완전히 안 보이는 상태로 전환
     *
     */
    fun hide() {
        if (isHideable.not()) return
        sheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
    }

    /**
     * BottomSheet 를 최대 사이즈로 확장
     *
     */
    fun expand() {
        sheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    /**
     * BottomSheet 를 화면의 절반 크기로 확장
     *
     * 적용하기 위해선 mFitToContents 가 true 여야함
     *
     */
    fun halfExpand() {
        sheetBehavior?.state = BottomSheetBehavior.STATE_HALF_EXPANDED
    }

    /**
     * BottomSheet 의 타이틀을 표시함
     *
     * title 과 titleTextStyle 적용
     *
     */
    private fun showTitle() {
        val titleTextView = view?.toolbar?.titleStub?.inflate() as TextView? ?: return
        titleTextView.apply {
            text = title
            TextViewCompat.setTextAppearance(this, titleTextStyle ?: return)
        }
    }

    /**
     * 백그라운드 dim 제거
     *
     */
    private fun removeDim() {
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    }

    private fun convertDpToPx(context: Context, dp: Float): Int {
        return (dp * context.resources.displayMetrics.density).toInt()
    }

    class Builder<T : Fragment>(val fragment: T) {

        var isExpandHandle: Boolean = false
            private set

        var isCloseButton: Boolean = false
            private set

        var isHideable: Boolean = true
            private set

        var isFullScreen: Boolean = false
            private set

        var showExpanded: Boolean = false
            private set

        var closeButtonText: String? = null
            private set

        var title: String? = null
            private set

        var isRemoveToolbar: Boolean = false
            private set

        var isLayerDetectionOn: Boolean = false
            private set

        var isRemoveDim: Boolean = false
            private set

        var peekHeightDp: Float? = null
            private set

        var topMargin: Float = 0f
            private set

        var layerMargin: Float = DEFAULT_LAYER_MARGIN
            private set

        @StyleRes
        var titleTextStyle: Int? = null
            private set

        @StyleRes
        var closeButtonTextStyle: Int? = null
            private set

        var callback: BottomSheetBehavior.BottomSheetCallback? = null
            private set

        fun setExpandHandle(isExpandHandle: Boolean) =
            apply { this.isExpandHandle = isExpandHandle }

        fun setCloseButton(isButton: Boolean) = apply {
            this.isCloseButton = isButton
            this.closeButtonText = null
        }

        /**
         * BottomSheet 의 hideable 여부 결정
         *
         * @param isHideable false 시 STATE_COLLAPSE 까지만 축소.
         * dismiss 를 위해선 백버튼 이나 배경 클릭해야함
         */
        fun isHideable(isHideable: Boolean) = apply { this.isHideable = isHideable }

        /**
         * BottomSheet 크기 최대화
         *
         * 전달된 fragment 의 root view 의 height 가 match_parent 임에도 화면을 가득 채우지 않을 때 사용
         * topMargin 과 layerMargin 적용됨
         *
         * @param isFullScreen true 시 전체화면 크기 적용
         */
        fun setFullScreen(isFullScreen: Boolean) = apply { this.isFullScreen = isFullScreen }

        /**
         * BottomSheet show 시 STATE_EXPANDED 로 설정
         *
         * show 시 STATE_COLLAPSE 가 아닌 STATE_EXPANDED 로 설정
         *
         * @param showExpanded true 시 다이얼로그 크기 최대화
         */
        fun showExpanded(showExpanded: Boolean) = apply { this.showExpanded = showExpanded }

        /**
         * BottomSheet 상단에 텍스트 닫기 버튼 추가
         *
         * @param text 설정할 닫기 버튼의 텍스트
         */
        fun setTextCloseButton(text: String) = apply {
            this.isCloseButton = true
            this.closeButtonText = text
        }

        /**
         * BottomSheet 좌측 상단에 제목 추가
         *
         * @param title 설정할 제목
         */
        fun setTitle(title: String) = apply {
            this.title = title
        }

        /**
         * BottomSheet 상단 toolbar 제거
         *
         * visibility 변경을 통해 제거함
         *
         * @param isRemove true 시 toolbar 제거
         */
        fun removeToolbar(isRemove: Boolean) = apply { this.isRemoveToolbar = isRemove }

        /**
         * Layer 인식 사용
         *
         * BottomSheet 여러 개 생성 시 자동으로 간격을 주어 먼저 호출된 BottomSheet 들이 조금 드러나게함용
         * 단, showExpanded 와 isFullScreen 이 true 여야 하고 fragment 의 root view 가 match_parent 여야 한다.
         *
         * @see setLayerMargin 으로 조정 가능
         */
        fun useLayerDetection() = apply {
            this.isLayerDetectionOn = true
            this.showExpanded = true
        }

        /**
         * 백그라운드 dim 제거
         *
         * @param isRemove true 시 dim 제거
         */
        fun removeDim(isRemove: Boolean) = apply { this.isRemoveDim = isRemove }

        /**
         * peekHeight 설정
         *
         * 입력한 peekHeight 가 pixel 수로 계산된 뒤 적용
         *
         * @param dp 설정하고자 하는 peekHeight
         */
        fun setPeekHeight(dp: Float) =
            apply { this.peekHeightDp = dp }

        /**
         * BottomSheet 상단 margin
         *
         * 입력한 topMargin 이 pixel 수로 계산된 뒤 적용
         *
         * @param dp 설정하고자 하는 topMargin
         */
        fun setTopMargin(dp: Float) = apply { this.topMargin = dp }

        /**
         * LayerMargin 값 변경
         *
         * 각 layer(다이얼로그) 간의 간격 조정
         * 입력한 dp 가 pixel 수로 계산된 뒤 적용
         *
         * default : 56f
         * @see DEFAULT_LAYER_MARGIN
         *
         * @param dp 설정하고자 하는 layerMargin
         */
        fun setLayerMargin(dp: Float) = apply { this.layerMargin = dp }

        /**
         * Title 에 style 적용
         *
         * 설정하고자 하는 style 을 xml 에 정의해야함
         * 이때 반드시 TextAppearance 를 상속해야함
         *
         * @sample
         *  <style name="DialogTitle" parent="TextAppearance.AppCompat">
        <item name="android:textSize">22sp</item>
        <item name="android:textColor">#00FFFF</item>
        <item name="android:textStyle">bold</item>
        </style>
         *
         * @param styleId  설정하고자 하는 style의 ResId
         */
        fun setTitleTextAppearance(@StyleRes styleId: Int) = apply { this.titleTextStyle = styleId }

        /**
         * TextCloseButton 에 style 적용
         *
         * 설정하고자 하는 style 을 xml 에 정의해야함
         * 이때 반드시 TextAppearance 를 상속해야함
         *
         * @sample
         *  <style name="DialogTitle" parent="TextAppearance.AppCompat">
        <item name="android:textSize">22sp</item>
        <item name="android:textColor">#00FFFF</item>
        <item name="android:textStyle">bold</item>
        </style>
         *
         * @param styleId  설정하고자 하는 style의 ResId
         */
        fun setCloseButtonTextAppearance(@StyleRes styleId: Int) =
            apply { this.closeButtonTextStyle = styleId }

        /**
         * BottomSheetCallback 추가
         *
         * BottomSheetBehavior.BottomSheetCallback 을 set
         *
         * @param callback
         */
        fun setCallback(callback: BottomSheetBehavior.BottomSheetCallback) =
            apply { this.callback = callback }

        /**
         * ExpandableBottomSheetDialog 빌드
         *
         * Builder 를 통해 설정들을 한 후 마지막에 build 하여 사
         *
         * @sample
         * ExpandableBottomSheetDialog.Builder(TestFragment())
        .setCallback(callback)
        .setExpandHandle(true)
        .removeDim(true)
        .setTopMargin(100f)
        .removeToolbar(true)
        .setLayerMargin(100f)
        .setPeekHeight(300f)
        .showExpanded(true)
        .isFullScreen(true)
        .isCloseable(true)
        .setTitle("test title")
        .isHideable(false)
        .useLayerDetection()
        .build()
         *
         */
        fun build() = NestedFragmentBottomSheetDialog(this)
    }

    companion object {
        private const val DEFAULT_LAYER_MARGIN = 56f
        var layers = 0
    }
}

