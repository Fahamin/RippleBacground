package com.fahamin.ripplelibrary

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat

class RippleBackground @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    private var rippleRadius = 0f
    private var rippleStrokeWidth = 0f
    private var paint: Paint? = null
    var isRippleAnimationRunning = false
        private set
    private var animatorSet: AnimatorSet? = null
    private val list = ArrayList<RippleView>()

    init {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        if (isInEditMode) return
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RippleBackground)
        val rippleColor = typedArray.getColor(
            R.styleable.RippleBackground_rb_color,
            ContextCompat.getColor(context, R.color.ripple_color)
        )
        rippleStrokeWidth = typedArray.getDimension(
            R.styleable.RippleBackground_rb_strokeWidth,
            resources.getDimension(R.dimen.rippleStrokeWidth)
        )
        rippleRadius = typedArray.getDimension(
            R.styleable.RippleBackground_rb_radius,
            resources.getDimension(R.dimen.rippleRadius)
        )
        val rippleDuration =
            typedArray.getInt(R.styleable.RippleBackground_rb_duration, DEFAULT_DURATION)
        val rippleDelay = typedArray.getInt(R.styleable.RippleBackground_rb_delay, DEFAULT_DURATION)
        val rippleAmount =
            typedArray.getInt(R.styleable.RippleBackground_rb_rippleAmount, DEFAULT_RIPPLE_COUNT)
        val rippleScale = typedArray.getFloat(R.styleable.RippleBackground_rb_scale, DEFAULT_SCALE)
        val rippleType = typedArray.getInt(R.styleable.RippleBackground_rb_type, DEFAULT_FILL_TYPE)
        typedArray.recycle()
        paint = Paint()
        paint!!.isAntiAlias = true
        if (rippleType == DEFAULT_FILL_TYPE) {
            rippleStrokeWidth = 0f
            paint!!.style = Paint.Style.FILL
        } else paint!!.style = Paint.Style.STROKE
        paint!!.color = rippleColor
        val params = LayoutParams(
            (2 * (rippleRadius + rippleStrokeWidth)).toInt(),
            (2 * (rippleRadius + rippleStrokeWidth)).toInt()
        )
        params.addRule(CENTER_IN_PARENT, TRUE)
        animatorSet = AnimatorSet()
        animatorSet!!.interpolator = AccelerateDecelerateInterpolator()
        val animators = ArrayList<Animator>()
        for (i in 0 until rippleAmount) {
            val rippleView = RippleView(context)
            addView(rippleView, params)
            list.add(rippleView)
            val scaleXAnimator = ObjectAnimator.ofFloat(rippleView, SCALE_X, 1.0f, rippleScale)
            scaleXAnimator.repeatCount = ObjectAnimator.INFINITE
            scaleXAnimator.repeatMode = ObjectAnimator.RESTART
            scaleXAnimator.startDelay = (i * rippleDelay).toLong()
            scaleXAnimator.duration = rippleDuration.toLong()
            scaleXAnimator.interpolator = AccelerateInterpolator()
            animators.add(scaleXAnimator)
            val scaleYAnimator = ObjectAnimator.ofFloat(rippleView, SCALE_Y, 1.0f, rippleScale)
            scaleYAnimator.repeatCount = ObjectAnimator.INFINITE
            scaleYAnimator.repeatMode = ObjectAnimator.RESTART
            scaleYAnimator.startDelay = (i * rippleDelay).toLong()
            scaleYAnimator.duration = rippleDuration.toLong()
            scaleXAnimator.interpolator = AccelerateInterpolator()
            animators.add(scaleYAnimator)
            val alphaAnimator = ObjectAnimator.ofFloat(rippleView, ALPHA, 1.0f, 0f)
            alphaAnimator.repeatCount = ObjectAnimator.INFINITE
            alphaAnimator.repeatMode = ObjectAnimator.RESTART
            alphaAnimator.startDelay = (i * rippleDelay).toLong()
            alphaAnimator.duration = rippleDuration.toLong()
            scaleXAnimator.interpolator = AccelerateInterpolator()
            animators.add(alphaAnimator)
        }
        animatorSet!!.playTogether(animators)
    }

    private inner class RippleView(context: Context?) : View(context) {
        init {
            this.visibility = INVISIBLE
        }

        override fun onDraw(canvas: Canvas) {
            canvas.drawCircle(rippleRadius, rippleRadius, rippleRadius - rippleStrokeWidth, paint!!)
        }
    }

    fun startRippleAnimation() {
        if (!isRippleAnimationRunning) {
            for (rippleView in list) {
                rippleView.visibility = VISIBLE
            }
            animatorSet!!.start()
            isRippleAnimationRunning = true
        }
    }

    fun stopRippleAnimation() {
        if (isRippleAnimationRunning) {
            animatorSet!!.end()
            isRippleAnimationRunning = false
        }
    }

    companion object {
        // default circle count
        private const val DEFAULT_RIPPLE_COUNT = 6

        // default animator duration
        private const val DEFAULT_DURATION = 3000
        private const val DEFAULT_SCALE = 2.0f
        private const val DEFAULT_FILL_TYPE = 0
    }
}