package com.allexandresantos.custombutton

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.allexandresantos.R
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var loadingWidth = 0f
    private var loadingAngle = 0f

    private var buttonText = context.getString(R.string.download)

    private var colorPrimary = context.getColor(R.color.colorPrimary)
    private var colorWhite = context.getColor(R.color.white)
    private var colorAccent = context.getColor(R.color.colorAccent)

    private var buttonAnimator = ValueAnimator()
    private var circleAnimator = ValueAnimator()

    private var buttonPaint = Paint().apply { color = colorPrimary }
    private var buttonAnimationPaint = Paint()
    private var circlePaint = Paint().apply { color = colorWhite }
    private var buttonTextPaint = Paint().apply {
        textSize = 40f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create("", Typeface.BOLD)
        color = colorWhite
    }

    private var buttonState: ButtonState by Delegates.observable(ButtonState.InitialState) { _, _, new ->
        when (new) {
            ButtonState.Loading -> {
                buttonText = context.getString(R.string.loading_file)
                buttonAnimationPaint.color = colorAccent
                buttonAnimator = ValueAnimator.ofFloat(0f, measuredWidth.toFloat()).apply {
                    duration = 2000
                    repeatMode = ValueAnimator.RESTART
                    repeatCount = ValueAnimator.INFINITE

                    addUpdateListener {
                        loadingWidth = animatedValue as Float
                        this@LoadingButton.invalidate()
                    }
                    start()
                }

                circleAnimator = ValueAnimator.ofFloat(0f, 360f).apply {
                    duration = 2000
                    repeatMode = ValueAnimator.RESTART
                    repeatCount = ValueAnimator.INFINITE

                    addUpdateListener {
                        loadingAngle = animatedValue as Float
                        this@LoadingButton.invalidate()
                    }
                    start()
                }
            }

            ButtonState.Completed -> {
                buttonText = context.getString(R.string.download)

                if (buttonAnimator.isRunning) buttonAnimator.end()
                if (circleAnimator.isRunning) circleAnimator.end()

                loadingAngle = 0f
                buttonAnimationPaint.color = colorPrimary
                this@LoadingButton.invalidate()
            }

            else -> this@LoadingButton.invalidate()

        }

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
//        Log.d("oi", "onDraw: buttonanimator " + buttonAnimator.isRunning)
//        Log.d("oi", "onDraw: loadingwidth " + loadingWidth)
//        Log.d("oi", "onDraw: color " + buttonAnimationPaint.color)
//        Log.d("oi", "onDraw: cor original " + colorAccent)

        Log.d("oi", "onDraw: " + measuredWidth)


        canvas!!.drawRect(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), buttonPaint)

        canvas.drawRect(0f, 0f, loadingWidth, measuredHeight.toFloat(), buttonAnimationPaint)

        canvas.drawText(buttonText,measuredWidth.toFloat() / 2,measuredHeight / 1.7f, buttonTextPaint)

        canvas.drawArc(measuredWidth - 100f,(measuredHeight / 2) - 30f,measuredWidth - 50f,(measuredHeight / 2) + 30f,0f, loadingAngle, true, circlePaint)

    }

    override fun performClick(): Boolean {
        invalidate()
        return super.performClick()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

    fun setState(state: ButtonState?) {
        state?.let { buttonState = it }
    }

}