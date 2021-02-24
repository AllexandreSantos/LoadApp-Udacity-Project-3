package com.allexandresantos.custombutton

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
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

    private val buttonPaint = Paint().apply { color = colorPrimary }
    private val circlePaint = Paint().apply { color = colorWhite }
    private val buttonTextPaint = Paint().apply {
        textSize = 50f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create("", Typeface.BOLD)
        color = colorWhite
    }

    private var buttonState: ButtonState by Delegates.observable(ButtonState.InitialState) { _, _, new ->
        when (new) {
            ButtonState.Loading -> {
                buttonText = context.getString(R.string.loading_file)
                buttonAnimator = ValueAnimator.ofFloat(0f, measuredWidth.toFloat()).apply {
                    duration = 1500
                    repeatMode = ValueAnimator.RESTART
                    repeatCount = ValueAnimator.INFINITE

                    interpolator = AccelerateInterpolator(1f)


                    addUpdateListener {
                        loadingWidth = animatedValue as Float
                        this@LoadingButton.invalidate()
                    }
                    start()
                }

                circleAnimator = ValueAnimator.ofFloat(0f, 360f).apply {
                    duration = 1500
                    repeatMode = ValueAnimator.RESTART
                    repeatCount = ValueAnimator.INFINITE

                    interpolator = AccelerateInterpolator(1f)

                    addUpdateListener {
                        loadingAngle = animatedValue as Float
                        this@LoadingButton.invalidate()
                    }
                    start()
                }
            }

            ButtonState.Completed -> {
                buttonText = context.getString(R.string.download_again)
                loadingAngle = 360f
                if (buttonAnimator.isRunning) buttonAnimator.end()
                if (circleAnimator.isRunning) circleAnimator.end()
            }

            else -> return@observable
        }

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (buttonState == ButtonState.Completed) buttonPaint.color = colorAccent else buttonPaint.color = colorPrimary
        canvas!!.drawRect(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), buttonPaint)

        buttonPaint.color = colorAccent
        canvas.drawRect(0f, 0f, loadingWidth, measuredHeight.toFloat(), buttonPaint)

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