package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates


class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var widthSize = 0
    private var heightSize = 0


    private var _backgroundColor: Int by Delegates.notNull() //using delegates instead of lateinit because int is primitive
    private var _textColor: Int by Delegates.notNull()
    private var _loadingBackgroundColor: Int by Delegates.notNull()
    private var _loadingCircleColor: Int by Delegates.notNull()

    private var _downloadString: String by Delegates.notNull()
    private var _loadingString: String by Delegates.notNull()

    private var buttonText = ""


    private var valueAnimator = ValueAnimator()
    private var animationProgress = 0
    //TODO(maybe make it customizable attributes)
    private val valueAnimatorStart = 0
    private val valueAnimatorEnd = 250
    private val valueAnimatorDuration: Long = 2500
    private val loadingCircleStartAngel=270f
    private val fontSize=65f



    private val rect = RectF(
        720f,
        40f,
        790f,
        110f
    )



    var buttonState: ButtonState by Delegates.observable(ButtonState.Completed) { _, old, new ->
        when {
            new == ButtonState.Loading && old == ButtonState.Completed -> {
                buttonText = _loadingString
                valueAnimator.start()
            }
            new == ButtonState.Completed -> {
                buttonText = _downloadString
                valueAnimator.cancel().also {
                    animationProgress = 0
                }

            }

        }
        invalidate()
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = fontSize
        typeface = Typeface.create("", Typeface.NORMAL)
        blendMode=BlendMode.HARD_LIGHT
    }

    init {
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {

            _backgroundColor = getColor(R.styleable.LoadingButton_backgroundColor, 0)
            _textColor = getColor(R.styleable.LoadingButton_textColor, 0)
            _loadingBackgroundColor = getColor(R.styleable.LoadingButton_loadingBackgroundColor, 0)
            _loadingCircleColor = getColor(R.styleable.LoadingButton_loadingCircleColor, 0)
            _downloadString = getString(R.styleable.LoadingButton_downloadString)
                ?: context.getString(R.string.button_download)
            _loadingString = getString(R.styleable.LoadingButton_loadingString)
                ?: context.getString(R.string.button_running)

        }

        isClickable = true
        buttonState = ButtonState.Completed
        valueAnimator = ValueAnimator.ofInt(valueAnimatorStart, valueAnimatorEnd).apply {
            duration = valueAnimatorDuration
            repeatMode = ValueAnimator.REVERSE
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener {
                animationProgress = animatedValue as Int
                invalidate()
            }
        }

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

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        paint.color = _backgroundColor
        canvas?.drawRect(
            0f,
            0f,
            widthSize.toFloat(),
            heightSize.toFloat(),
            paint
        )

        //these two will draw only if there is an animation running
        paint.color = _loadingBackgroundColor
        canvas?.drawRect(
            0f,
            0f,
            (width * (animationProgress.toFloat() / valueAnimatorEnd.toFloat())),
            height.toFloat(),
            paint
        )


        paint.color = _loadingCircleColor
        canvas?.drawArc(
            rect,
            loadingCircleStartAngel,
            (360f * (animationProgress.toFloat() / valueAnimatorEnd.toFloat())),
            true,
            paint
        )
        //Toast.makeText(context, "${rect.left}", Toast.LENGTH_SHORT).show()

        //draw text here to appear on top of background
        paint.color = _textColor
        canvas?.drawText(
            buttonText,
            (widthSize / 2f),
            (heightSize / 2f) + 20f,
            paint
        )


    }


}