package com.inspect.ocr

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.VelocityTracker
import android.widget.ImageView



class OcrImageView : ImageView {
//    private var bitmap: Bitmap? = null
    public var angle:Int = 0
    private var step:Int = 5
    private var mVelocityTracker: VelocityTracker? = null
    private var offset:Float = 0.0f
    private var touchStatus:Int = 0

    constructor( context:Context ):super( context ){}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    fun getBitmap():Bitmap?{
        var drawable: BitmapDrawable = drawable as BitmapDrawable
        var bitmap:Bitmap = drawable.bitmap as Bitmap
        bitmap = ImageProcess.rotate( bitmap, angle )
        return bitmap
    }
    public fun startAnimation() {
/*
        angle += offset.toInt()
        setPivotX( (width/2).toFloat() );
        setPivotY( (this.height/2).toFloat() );
*/
        angle += 90
        if( angle >= 360 ){
            angle = 0
        }
        setRotation( angle.toFloat() );
    }
    public fun initialize(){
        angle = 0
        setPivotX( (width/2).toFloat() );
        setPivotY( (this.height/2).toFloat() );
        setRotation( 0.0f );
    }
/*    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                touchStatus = 0
                // Reset the velocity tracker back to its initial state.
                mVelocityTracker?.clear()
                // If necessary retrieve a new VelocityTracker object to watch the
                // velocity of a motion.
                mVelocityTracker = mVelocityTracker ?: VelocityTracker.obtain()
                // Add a user's movement to the tracker.
                mVelocityTracker?.addMovement(event)
                mVelocityTracker?.apply {
                    val pointerId: Int = event.getPointerId(event.actionIndex)
                    Log.d("", "X velocity: ${getXVelocity(pointerId)}")
                    Log.d("", "Y velocity: ${getYVelocity(pointerId)}")
                }
            }
            MotionEvent.ACTION_MOVE -> {
                touchStatus = 1
                mVelocityTracker?.apply {
                    val pointerId: Int = event.getPointerId(event.actionIndex)
                    addMovement(event)
                    // When you want to determine the velocity, call
                    // computeCurrentVelocity(). Then call getXVelocity()
                    // and getYVelocity() to retrieve the velocity for each pointer ID.
                    computeCurrentVelocity(100 )
                    // Log velocity of pixels per second
                    // Best practice to use VelocityTrackerCompat where possible.
                    offset = getXVelocity(pointerId)
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                // Return a VelocityTracker object back to be re-used by others.
                mVelocityTracker?.recycle()
                mVelocityTracker = null
                startAnimation()
            }
        }
        return true
    }*/
}