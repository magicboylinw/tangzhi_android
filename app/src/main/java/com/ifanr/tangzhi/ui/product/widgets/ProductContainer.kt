package com.ifanr.tangzhi.ui.product.widgets

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.NestedScrollingParent3
import androidx.customview.widget.ViewDragHelper
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.canScrollDown
import com.ifanr.tangzhi.ext.dp2px
import com.ifanr.tangzhi.ui.getStatusBarHeight
import com.ifanr.tangzhi.util.axesToString
import com.ifanr.tangzhi.util.typeToString
import java.lang.Math.abs
import kotlin.math.min

/**
 * percent: 0f(bottom) - 1f(top)
 */
typealias BottomPanelDraggedListener = (percent: Float) -> Unit

class ProductContainer: ViewGroup, NestedScrollingParent3 {

    enum class State {
        FOLD,
        EXPAND,
        DRAGGING
    }

    companion object {
        private const val TAG = "ProductContainer"
    }

    private lateinit var productView: ProductPanel

    private lateinit var reviewsView: View

    var state = State.FOLD
        private set

    private var actionDownPointerId = 0

    private var actionLastY = 0f

    private val bottomPanelDraggedListeners
            = mutableListOf<BottomPanelDraggedListener>()

    // 点评面板露出高度
    private val reviewExposeHeight = context.dp2px(50)

    // 点评面板距离 container top 的高度
    private val reviewPaddingTop =
        context.getStatusBarHeight() + resources.getDimensionPixelSize(R.dimen.app_tool_bar_height)

    // 点评面板被 drag 时，top 的上限（底部）
    private val reviewDraggedTopMax: Int
        get() = measuredHeight - reviewExposeHeight

    // 点评面板被 drag 时，top 的下限（顶部）
    private val reviewDraggedTopMin: Int
        get() = reviewPaddingTop

    // 点评面板可以被 drag 的距离
    private val reviewDraggedDistance: Int
        get() = reviewDraggedTopMax - reviewDraggedTopMin

    private val dragCallback: ViewDragHelper.Callback = object: ViewDragHelper.Callback() {

        override fun onViewPositionChanged(
            changedView: View,
            left: Int,
            top: Int,
            dx: Int,
            dy: Int
        ) {
            bottomPanelDraggedListeners.forEach {
                it.invoke(1f - (top - reviewDraggedTopMin).toFloat().div(reviewDraggedDistance))
            }
        }

        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            return child == reviewsView
        }

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            return top.coerceIn(reviewDraggedTopMin, reviewDraggedTopMax)
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            // 往上 fling
            if (yvel < 0 || (yvel == 0f && releasedChild.top <= reviewDraggedTopMax * 0.7)) {
                if (dragHelper.settleCapturedViewAt(0, reviewDraggedTopMin)) {
                    invalidate()
                }
            } else {

                // 往下 fling
                if (dragHelper.settleCapturedViewAt(0, reviewDraggedTopMax)) {
                    invalidate()
                }
            }
        }

        override fun onViewDragStateChanged(s: Int) {
            state = when (s) {
                ViewDragHelper.STATE_SETTLING, ViewDragHelper.STATE_DRAGGING -> State.DRAGGING
                else -> if (reviewsView.top == reviewDraggedTopMin) State.EXPAND else State.FOLD
            }

            if (s == ViewDragHelper.STATE_IDLE) {
                requestLayout()
            }

            // 只有当点评面板收起时，产品面板才能收到 touch event
            productView.freeze = when (state) {
                State.FOLD -> false
                else -> true
            }
        }
    }
    private val dragHelper = ViewDragHelper.create(this, dragCallback)

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onFinishInflate() {
        super.onFinishInflate()
        productView = getChildAt(0) as ProductPanel
        reviewsView = getChildAt(1)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        Log.d(TAG, "onMeasure")
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        measureChild(
            productView,
            MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(measuredHeight - reviewExposeHeight, MeasureSpec.EXACTLY))

        measureChild(
            reviewsView,
            MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(measuredHeight - reviewPaddingTop, MeasureSpec.EXACTLY)
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        Log.d(TAG, "onLayout")
        var left = 0
        var top = 0
        var right = productView.measuredWidth
        var bottom = productView.measuredHeight
        productView.layout(left, top, right, bottom)

        if (state == State.FOLD) {
            top = measuredHeight - reviewExposeHeight
            right = reviewsView.measuredWidth
            bottom = top + reviewsView.measuredHeight
        } else {
            top = reviewsView.top
            right = reviewsView.right
            bottom = reviewsView.bottom
        }
        reviewsView.layout(left, top, right, bottom)
    }

    override fun onDraw(canvas: Canvas?) {
        Log.d(TAG, "onDraw")
        super.onDraw(canvas)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val action = ev.actionMasked
        val pointerId = ev.getPointerId(ev.actionIndex)
        dragHelper.shouldInterceptTouchEvent(ev)

        // review 展开的时候，滚动到顶部继续往下拉，可以把 review 收起来
        /*if (state == State.EXPAND && reviewsPanel.scrollY <= 2) {
            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    actionDownPointerId = pointerId
                    actionLastY = ev.y
                    dragHelper.shouldInterceptTouchEvent(ev)
                }

                MotionEvent.ACTION_MOVE -> {
                    if (pointerId == actionDownPointerId) {
                        val distance = ev.y - actionLastY
                        if (distance >= dragHelper.touchSlop) {
                            dragHelper.shouldInterceptTouchEvent(ev)
                            dragHelper.captureChildView(reviewsPanel, actionDownPointerId)
                            return true
                        }
                    }
                }
            }
        }*/

        // review 面板收起时，info 面板滚动到底部，继续往下拉可以拉起 review 面板
        if (state == State.FOLD && !productView.canScrollDown()) {
            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    actionDownPointerId = pointerId
                    actionLastY = ev.y
                    dragHelper.shouldInterceptTouchEvent(ev)
                }

                MotionEvent.ACTION_MOVE -> {
                    if (pointerId == actionDownPointerId) {
                        val distance = actionLastY - ev.y
                        if (distance >= dragHelper.touchSlop) {
                            dragHelper.shouldInterceptTouchEvent(ev)
                            dragHelper.captureChildView(reviewsView, actionDownPointerId)
                            return true
                        }
                    }
                }
            }
        }

        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        dragHelper.processTouchEvent(event)
        return true
    }

    override fun computeScroll() {
        super.computeScroll()
        if (dragHelper.continueSettling(true)) {
            invalidate()
        }
    }

    /************************** NestedScrollingParent3 start ******************************/

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {

        // info 往下滚动，review 先消费，info 后消费
        /*if (target == infoPanel) {
            val maxDy = -min(abs(dy), scrollY)
            if (maxDy < 0) {
                scrollBy(0, maxDy)
                consumed[1] = maxDy
            }
        }*/

        Log.d(TAG, "onNestedPreScroll, $dy, ${consumed.joinToString()}, ${typeToString(type)}")
    }

    override fun onStopNestedScroll(target: View, type: Int) {
        Log.d(TAG, "onStopNestedScroll, ${typeToString(type)}")
    }

    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        Log.d(TAG, "onStartNestedScroll, ${axesToString(axes)}, ${typeToString(type)}")
        /*return axes.and(ViewCompat.SCROLL_AXIS_VERTICAL) == ViewCompat.SCROLL_AXIS_VERTICAL*/
        return false
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {
        Log.d(TAG, "onNestedScrollAccepted, ${axesToString(axes)}, ${typeToString(type)}")
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {

        // info 往上滚动，info 先消费，review 后消费
        /*if (target == infoPanel && dyUnconsumed > 0) {
            val maxDy = min(dyUnconsumed, reviewsPanel.measuredHeight - scrollY - reviewExposeHeight)
            if (maxDy > 0) {
                scrollBy(0, maxDy)
                consumed[1] = maxDy
            }
        }*/

        // review 往下滚动，review 先消费，container 后消费
        if (dyUnconsumed < 0 && scrollY > 0) {
            val maxDy = -min(abs(dyUnconsumed), scrollY)
            if (maxDy < 0) {
                scrollBy(0, maxDy)
                consumed[1] = maxDy
            }
        }

        Log.d(TAG, "onNestedScroll, $dyConsumed, $dyUnconsumed, " +
                "${consumed.joinToString()}, ${typeToString(type)}")
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {
        this.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed,
            dyUnconsumed, type, IntArray(2) { 0 })
    }

    /************************** NestedScrollingParent3 end ******************************/


    fun addBottomPanelDraggedListener(l: BottomPanelDraggedListener) {
        bottomPanelDraggedListeners.add(l)
    }

    fun removeBottomPanelDraggedListener(l: BottomPanelDraggedListener) {
        bottomPanelDraggedListeners.remove(l)
    }

    /**
     * 展开点评面板
     */
    fun expand() {
        if (state == State.FOLD) {
            if (dragHelper.smoothSlideViewTo(reviewsView, 0, reviewDraggedTopMin)) {
                invalidate()
            }
        }
    }

    /**
     * 收起点评面板
     */
    fun fold() {
        if (state == State.EXPAND) {
            if (dragHelper.smoothSlideViewTo(reviewsView, 0, reviewDraggedTopMax)) {
                invalidate()
            }
        }
    }

}