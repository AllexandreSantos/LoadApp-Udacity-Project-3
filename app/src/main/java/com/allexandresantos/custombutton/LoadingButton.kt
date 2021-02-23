package com.allexandresantos.custombutton

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
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
    private var buttonText = ""

    private var buttonAnimator = ValueAnimator()
    private var circleAnimator = ValueAnimator()


    private val buttonPaint = Paint()
    private val circlePaint = Paint()
    private val buttonTextPaint = Paint().apply {
        textSize = 50f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create("", Typeface.BOLD)
    }
    private var buttonColor = 0
    private var buttonTextColor = 0
    private var circleColor = 0

    init {
        initialize(attrs)
    }

    private fun initialize(attrs: AttributeSet? = null){

        isClickable = true
        buttonText = context.getString(R.string.download)
        attrs?.let {
            context.theme.obtainStyledAttributes(it, R.styleable.LoadingButton, 0, 0).apply {
                buttonColor = getColor(R.styleable.LoadingButton_buttonColor, 0)
                buttonTextColor = getColor(R.styleable.LoadingButton_buttonTextColor, 0)
                circleColor = getColor(R.styleable.LoadingButton_circleColor, 0)

            }
        }

        buttonPaint.color = buttonColor
        buttonTextPaint.color = buttonTextColor
        circlePaint.color = circleColor

    }

    private var buttonState: ButtonState by Delegates.observable(ButtonState.Completed) { _, _, new ->
        when (new) {
            ButtonState.Loading -> {
                buttonText = context.getString(R.string.loading_file)
                buttonAnimator = ValueAnimator.ofFloat(0f, measuredWidth.toFloat()).apply {
                    duration = 2000
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
                    duration = 2000
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
                initialize()
                buttonText = context.getString(R.string.download)
                buttonColor = context.getColor(R.color.colorPrimary)
                loadingWidth = 0f
                loadingAngle = 0f
                buttonAnimator.end()
                circleAnimator.end()
                this@LoadingButton.invalidate()
            }

            ButtonState.InitialState -> Log.d("oi", ": teste")

        }

    }

    override fun onDraw(canvas: Canvas?) {
        buttonPaint.color = buttonColor

        super.onDraw(canvas)
        canvas!!.drawRect(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), buttonPaint)

        buttonPaint.color = context.getColor(R.color.colorAccent)
        canvas.drawRect(0f, 0f, loadingWidth, measuredHeight.toFloat(), buttonPaint)

        canvas.drawText(
            buttonText,
            measuredWidth.toFloat() / 2,
            measuredHeight / 1.7f,
            buttonTextPaint
        )
        canvas.drawArc(
            measuredWidth - 100f,
            (measuredHeight / 2) - 30f,
            measuredWidth - 50f,
            (measuredHeight / 2) + 30f,
            0f, loadingAngle, true, circlePaint
        )

    }

    override fun performClick(): Boolean {
        if (super.performClick()) return true
        invalidate()
        return true
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