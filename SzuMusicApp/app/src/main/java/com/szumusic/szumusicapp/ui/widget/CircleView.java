package com.szumusic.szumusicapp.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.ImageView;
import com.szumusic.szumusicapp.R;
import com.szumusic.szumusicapp.utils.ImageUtils;

/**
 * Created by kobe_xuan on 2017/2/5.
 */
public class CircleView extends ImageView {
    private int width;
    private int mradius;
    private BitmapShader mbitmapShader;
    private Matrix matrix;
    private Paint mBitmapPaint;
    private Paint mBorderPaint;
    private float mstrokeWidth;
    private int mstrokeColor;
    private Bitmap mDiscBitmap;

    public CircleView(Context context) {
        super(context);
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs,0);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs,defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle){
         matrix=new Matrix();
        mBitmapPaint=new Paint();
        mBorderPaint=new Paint();
        mBitmapPaint.setAntiAlias(true);
        mDiscBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.play_page_default_cover);
        System.out.println("图片的宽度为"+mDiscBitmap.getWidth());
        System.out.println("屏幕的宽度为"+getScreenWidth());
//        mDiscBitmap = ImageUtils.resizeImage(mDiscBitmap, getScreenWidth(),
//                getScreenWidth());
        //获取属性
        TypedArray typedArray=getContext().obtainStyledAttributes(attrs,R.styleable.CircleView);
        mstrokeWidth=typedArray.getDimension(R.styleable.CircleView_strokeWidth,0);
        mstrokeColor=typedArray.getColor(R.styleable.CircleView_strokeColor,Color.WHITE);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width=Math.min(getMeasuredHeight(),getMeasuredWidth());
        mradius=width/2;
        mradius= (int) (mradius-mstrokeWidth/2);
        setMeasuredDimension(width,width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
       /* int r=getMeasuredWidth()/2;
        int centerX=getLeft()+r;
        int centerY=getTop()+r;
        Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(4);

        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        //canvas.drawCircle(centerX,centerY,r/2,paint);
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.default_artist);

        //canvas.drawBitmap(bitmap,0,0,paint);

        //canvas.drawText("fasdfaf",centerX,centerY,paint);
        System.out.println("半径为"+r+"坐标为"+centerX+"===="+centerY);*/

        if (getDrawable()==null)
            return;
        setBitmapShader();
        canvas.drawCircle(width/2,width/2,mradius,mBitmapPaint);
        setmBorderPaint();
        canvas.drawCircle(width/2,width/2,mradius,mBorderPaint);
        //下面画专辑
        canvas.drawBitmap(mDiscBitmap,0,0,null);
    }

    private void setBitmapShader(){

        Drawable drawable=getDrawable();
        if(drawable==null)
            return ;
        BitmapDrawable bd= (BitmapDrawable) drawable;
        Bitmap bitmap=bd.getBitmap();
        mbitmapShader=new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float scale=1.0f;
        int bitmapsize=Math.min(bitmap.getWidth(),bitmap.getHeight());
        scale=width*1.0f/bitmapsize;
        matrix.setScale(scale,scale);
        mbitmapShader.setLocalMatrix(matrix);
        mBitmapPaint.setShader(mbitmapShader);
    }

    private void setmBorderPaint(){
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setColor(mstrokeColor);
        mBorderPaint.setStrokeCap(Paint.Cap.ROUND);
        mBorderPaint.setStrokeWidth(mstrokeWidth);

    }

    private int getScreenWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }
}
