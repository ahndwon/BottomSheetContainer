import android.view.MotionEvent
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior

class LockableBottomSheetBehavior<V : View> : BottomSheetBehavior<V>() {

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun <V : View> from(view: V): LockableBottomSheetBehavior<V> {
            val params = view.layoutParams as? CoordinatorLayout.LayoutParams
                    ?: throw IllegalArgumentException("The view is not a child of CoordinatorLayout")
            params.behavior as? BottomSheetBehavior<V>
                    ?: throw IllegalArgumentException("The view is not associated with BottomSheetBehavior")
            params.behavior = LockableBottomSheetBehavior<V>()
            return params.behavior as LockableBottomSheetBehavior<V>
        }
    }

    override fun onInterceptTouchEvent(parent: CoordinatorLayout, child: V, event: MotionEvent): Boolean {
        return false
    }

    override fun onTouchEvent(parent: CoordinatorLayout, child: V, event: MotionEvent): Boolean {
        return false
    }

    override fun onStartNestedScroll(
            coordinatorLayout: CoordinatorLayout,
            child: V,
            directTargetChild: View,
            target: View,
            axes: Int,
            type: Int
    ): Boolean {
        return false
    }

    override fun onNestedPreScroll(
            coordinatorLayout: CoordinatorLayout,
            child: V,
            target: View,
            dx: Int,
            dy: Int,
            consumed: IntArray,
            type: Int
    ) {
    }

    override fun onStopNestedScroll(
            coordinatorLayout: CoordinatorLayout,
            child: V,
            target: View,
            type: Int
    ) {
    }

    override fun onNestedPreFling(
            coordinatorLayout: CoordinatorLayout,
            child: V,
            target: View,
            velocityX: Float,
            velocityY: Float
    ): Boolean {
        return false
    }
}