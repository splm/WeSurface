package me.splm.wesurface;  

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
  
public class WeSurface extends SurfaceView implements Callback,Runnable {
    
    private State state;
    
    /**
     * 默认步长
     */
    private static final int DEF_STEP=1;
    
    /**
     * 默认图片资源缩放比例
     */
    private static final float DEF_SCALE=1.5f;
    
    private Canvas mCanvas;
    
    private Bitmap bitmap_bg;
    
    private boolean isFlag;
    
    private int mSurface_width;
    
    private int mSurface_height;
    
    private int bg_height;
    
    private int bg_width;
    
    private int mBitPosX;
    
    private int mBitPosY;
    
    private SurfaceHolder surfaceHolder;
    
    /*******************************/
    //属性定义
    private int speed;
    
    private Drawable drawable;
    
    private float scale;
    
    private int step;
    
    private OnRuningListener onRuningListener;
    
    private boolean isRunning;
    
    private enum State{
        
        LEFT_UP,RIGHT_UP,LEFT_DOWN,RIGHT_DOWN
        
    }
    
    private enum Speed{
        
        SLOW(1000),
        
        MIDDLE(500),
        
        FAST(100);
        
        private int value;
        
        private Speed(int value){
            
            this.value=value;
            
        }

        public int getValue() {
            return value;
        }

    }
    
    public void setOnRuningListener(OnRuningListener onRuningListener) {
        
        this.onRuningListener = onRuningListener;
        
    }
    
    public boolean isRunning(){
        
        return isRunning;
        
    }
    
    public WeSurface(Context context, AttributeSet attrs) {

        super(context, attrs);  
    
        isFlag=true;
        
        init(context, attrs);
        
        setFocusable(true);  
        
        setFocusableInTouchMode(true);
        
        surfaceHolder=getHolder();
        
        surfaceHolder.addCallback(this);
        
    }
    
    public WeSurface(Context context, AttributeSet attrs, int defStyle) {
        
        super(context, attrs, defStyle);  
        
        init(context, attrs);
    
    }

    private void init(Context context, AttributeSet attrs){
        
        TypedArray typedArray=context.obtainStyledAttributes(attrs,R.styleable.WeSurface);
        
        int state_ordinal=typedArray.getInt(R.styleable.WeSurface_state, State.LEFT_UP.ordinal());
        
        state=State.values()[state_ordinal];
        
        int speed_ordinal=typedArray.getInt(R.styleable.WeSurface_speed, Speed.FAST.ordinal());
        
        speed=Speed.values()[speed_ordinal].getValue();
        
        drawable=typedArray.getDrawable(R.styleable.WeSurface_src);
        
        scale=typedArray.getFloat(R.styleable.WeSurface_scale, DEF_SCALE);
        
        step=typedArray.getInteger(R.styleable.WeSurface_step,DEF_STEP);
        
        typedArray.recycle();
        
        Log.e("--------------->", "执行了~~~init方法"+state.name()+"--------speed="+speed);
        
    }

    public WeSurface(Context context) {
        super(context);  
        // TODO Auto-generated constructor stub
    }

    public void updateBg(){
        
        switch(state){
        
        case LEFT_UP:
            
            mBitPosX-=step;
            
            mBitPosY-=step;
            
            break;
            
        case RIGHT_DOWN:
            
            mBitPosX+=step;
            
            mBitPosY+=step;
            
            break;
            
        case LEFT_DOWN:
            
            mBitPosX-=step;
            
            mBitPosY+=step;
            
            break;
            
        case RIGHT_UP:
            
            mBitPosX+=step;
            
            mBitPosY-=step;
            
            break;
            
        }
        
        if(mBitPosX<=-mSurface_width/2){
            
            state=State.RIGHT_DOWN;
            
            onRuningListener.onToBounds();
            
        }
        
        if(mBitPosX>=0){
            
            state=State.LEFT_UP;
            
            onRuningListener.onToBounds();
            
        }
        
       /* if(mBitPosX>=mSurface_width/2){
            
            state=State.LEFT_DOWN;
            
        }
        
        if(mBitPosX<0){
            
            state=State.RIGHT_UP;
            
        }*/
        
    }
    
    public void drawBg(){
        
        mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);// 清屏幕.  
        
        mCanvas.drawBitmap(bitmap_bg, mBitPosX, mBitPosY, null);// 绘制当前屏幕背景
        
    }
    
    @Override
    public void run() {
        
        while(isFlag){
            
            synchronized(surfaceHolder){
                
                mCanvas=surfaceHolder.lockCanvas();
                
                onDrawing();
                
                surfaceHolder.unlockCanvasAndPost(mCanvas);
                
                try {
                    
                    Thread.sleep(speed);
                    
                    isRunning=true;
                
                } catch (InterruptedException e) {
                    
                    e.printStackTrace();
                
                }
                
            }
            
        }
        
    }
    
    protected void onDrawing() {
        
        drawBg();
        
        updateBg();
        
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        
        mSurface_width=this.getWidth();
        
        mSurface_height=this.getHeight();
        
        bg_height=(int)(mSurface_height*scale);
        
        bg_width=(int)(mSurface_width*scale);
        
        bitmap_bg=loadById(drawable, bg_width, bg_height);
        
        Thread thread=new Thread(this);
        
        thread.start();
        
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // TODO Auto-generated method stub  
        
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        
        isFlag=false;
        
    }
    
    public Bitmap loadById(Drawable drawable,int w,int h){
        
        Bitmap bitmap = Bitmap.createBitmap(

         drawable.getIntrinsicWidth(),

         drawable.getIntrinsicHeight(),

         drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888: Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(bitmap);

        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

        drawable.draw(canvas);

        int width=bitmap.getWidth();
        
        int height=bitmap.getHeight();
        
        float scale_w=(float)w/width;
        
        float scale_h=(float)h/height;
        
        Matrix matrix=new Matrix();
        
        matrix.postScale(scale_w, scale_h);
        
        Bitmap nBitmap=Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        
        return nBitmap;
        
    }
    
    public interface OnRuningListener{
        
        void onToBounds();
        
    }

}
