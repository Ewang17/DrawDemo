package com.example.draw2.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.net.Uri;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.draw2.activities.DrawActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2017/8/12.
 */

public class MainDrawView extends View{
    private Context context;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mBitmapPaint;// 画布的画笔
    private Paint mPaint;// 真实的画笔
    private float mX, mY;// 临时点坐标
    private static final float TOUCH_TOLERANCE = 4;
    // 保存Path路径的集合,用List集合来模拟栈
    private static List<DrawPath> savePath;
    // 保存已删除Path路径的集合
    private static List<DrawPath> deletePath;
    // 记录Path路径的对象
    private DrawPath dp;
    private int screenWidth, screenHeight;
    private int currentColor = Color.RED;
    private int clickPencilTimes=0;
    private int clickEraserTimes=0;
    private boolean isFill=false;
    private boolean isPencil=true;
    //设置画图样式
    private static final int DRAW_PATH = 0;
    private static final int DRAW_CIRCLE = 01;
    private static final int DRAW_RECTANGLE = 02;
    private static final int DRAW_ARROW = 03;
    private static final int DRAW_TRIANGLE=04;
    private int[] graphics = new int[]{
            DRAW_PATH,DRAW_CIRCLE,DRAW_RECTANGLE,DRAW_ARROW,DRAW_TRIANGLE};
    private int currentDrawGraphics = graphics[0];//默认画线
    private float startX;
    private float startY;
    private Bitmap srcBitmap;//传递过来的背景图转换成的bitmap
    //存储贴纸列表
    private StickerView mCurrentView;

    public boolean isPencil() {
        return isPencil;
    }

    public void setPencil(boolean pencil) {
        isPencil = pencil;
    }

    public void setmCurrentView(StickerView mCurrentView) {
        this.mCurrentView = mCurrentView;
    }

    public void setClickPencilTimes(int clickPencilTimes) {
        this.clickPencilTimes = clickPencilTimes;
    }

    public void setClickEraserTimes(int clickEraserTimes) {
        this.clickEraserTimes = clickEraserTimes;
    }

    public int getClickPencilTimes() {
        return clickPencilTimes;
    }

    public int getClickEraserTimes() {
        return clickEraserTimes;
    }

    public int getCurrentSize() {
        return currentSize;
    }
    public int getEraserCurrentSize() {
        return currentEraserSize;
    }
    public int getCurrentAlpha() {
        return currentAlpha;
    }

    private int currentEraserSize=20;
    private int currentSize = 20;
    private int currentAlpha=255;
    private int[] paintColor;//颜色集合
    private Bitmap contentBitmap;

    public void setContentBitmap(Bitmap contentBitmap) {
        this.contentBitmap = contentBitmap;
    }

    private class DrawPath {
        public Path path;// 路径
        public Paint paint;// 画笔
        public Bitmap bitmap;
        public Matrix matrix;
    }

    public MainDrawView(Context context, int w, int h) {
        super(context);
        this.context = context;
        screenWidth = w;
        screenHeight = h;
        paintColor = new int[]{
                Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.BLACK,Color.WHITE, Color.GRAY, Color.CYAN
        };
        setLayerType(LAYER_TYPE_SOFTWARE,null);//设置默认样式，去除dis-in的黑色方框以及clear模式的黑线效果
        initCanvas();
        savePath = new ArrayList<DrawPath>();
        deletePath = new ArrayList<DrawPath>();
    }


    public void initCanvas() {
        setPencilStyle();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        //画布大小
        mBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
        mBitmap.eraseColor(Color.argb(0, 0, 0, 0));
        mCanvas = new Canvas(mBitmap);  //所有mCanvas画的东西都被保存在了mBitmap中
        mCanvas.drawColor(Color.TRANSPARENT);
    }

    private void setPencilStyle() {
        mPaint = new Paint();
        //mPaint.setStyle(Paint.Style.STROKE);
        if(isFill){
            mPaint.setStyle(Paint.Style.FILL);
        }else{
            mPaint.setStyle(Paint.Style.STROKE);
        }
        mPaint.setStrokeJoin(Paint.Join.ROUND);// 设置外边缘
        mPaint.setStrokeCap(Paint.Cap.ROUND);// 形状
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(currentColor);

        //mPaint.setStrokeWidth(currentSize);

        //手动设置画笔样式
        mPaint.setAlpha(currentAlpha);
        mPaint.setStrokeWidth(currentSize);
        //硬度设置
    }
    private void setEraserStyle(){
        mPaint = new Paint();
        //mPaint.setStyle(Paint.Style.STROKE);
        if(isFill){
            mPaint.setStyle(Paint.Style.FILL);
        }else{
            mPaint.setStyle(Paint.Style.STROKE);
        }
        mPaint.setStrokeJoin(Paint.Join.ROUND);// 设置外边缘
        mPaint.setStrokeCap(Paint.Cap.ROUND);// 形状
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);

        mPaint.setAlpha(0);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mPaint.setColor(Color.TRANSPARENT);
        mPaint.setStrokeWidth(currentEraserSize);
    }

    /*private void resetPecil(int reset){
        if (reset == 1) {
            mPaint.setStrokeWidth(20);
            mPaint.setAlpha(255);
            //硬度设置
        }
    }*/

    @Override
    public void onDraw(Canvas canvas) {
        //canvas.drawColor(0xFFAAAAAA);
        // 将前面已经画过得显示出来
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        if (mPath != null) {
            // 实时的显示
            canvas.drawPath(mPath, mPaint);
        }
    }
    private void touch_start(float x, float y) {
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }
    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(mY - y);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            // 从x1,y1到x2,y2画一条贝塞尔曲线，更平滑(直接用mPath.lineTo也可以)
            switch(currentDrawGraphics){
                case 0:
                    mPath.quadTo(mX, mY,(x + mX) / 2, (y + mY) / 2);
                    //mPath.lineTo(mX,mY);
                    break;
                case 1:
                    mPath.reset();
                    RectF rectF=new RectF(startX,startY,x,y);
                    mPath.addOval(rectF, Path.Direction.CCW);//画椭圆
                    break;
                case 2:
                    mPath.reset();
                    RectF rectF2 = new RectF(startX,startY,x,y);
                    mPath.addRect(rectF2, Path.Direction.CCW);
                    break;
                case 3:
                    mPath.reset();
                    drawAL((int) startX,(int) startY,(int) x,(int) y);
                    break;
                case 4:
                    mPath.reset();
                    drawTA((int) startX,(int) startY,(int) x,(int) y);
                    break;
                default:
                    break;
            }

            mX = x;
            mY = y;
        }
    }
    private void touch_up() {
        if(currentDrawGraphics==DRAW_PATH)
            mPath.lineTo(mX, mY);
        mCanvas.drawPath(mPath, mPaint);
        //将一条完整的路径保存下来(相当于入栈操作)
        mPath = null;// 重新置空
        savePath.add(dp);

    }
    /**
     * 撤销
     * 撤销的核心思想就是将画布清空，
     * 将保存下来的Path路径最后一个移除掉，
     * 重新将路径画在画布上面。
     */
    public void undo() {
        if (savePath != null && savePath.size() > 0) {
            DrawPath drawPath = savePath.get(savePath.size() - 1);
            deletePath.add(drawPath);
            Log.e("TAG","");
            savePath.remove(savePath.size() - 1);
            redrawOnBitmap();
        }
    }
    /**
     * 重做
     */
    public void redo() {
        if (savePath != null && savePath.size() > 0) {
            savePath.clear();
            redrawOnBitmap();
        }
    }
    private void redrawOnBitmap() {
        initCanvas();
        Iterator<DrawPath> iter = savePath.iterator();
        while (iter.hasNext()) {
            DrawPath drawPath = iter.next();
            if(drawPath.bitmap!=null){
                mCanvas.drawBitmap(drawPath.bitmap,drawPath.matrix,drawPath.paint);
            }
            else
                mCanvas.drawPath(drawPath.path, drawPath.paint);
        }
        invalidate();// 刷新
    }
    /**
     * 恢复，恢复的核心就是将删除的那条路径重新添加到savepath中重新绘画即可
     */
    public void recover() {
        if (deletePath.size() > 0) {
            //将删除的路径列表中的最后一个，也就是最顶端路径取出（栈）,并加入路径保存列表中
            DrawPath dp = deletePath.get(deletePath.size() - 1);
            savePath.add(dp);
            //将取出的路径重绘在画布上
            if(dp.bitmap!=null){
                mCanvas.drawBitmap(dp.bitmap,dp.matrix,dp.paint);
            }
            else
                mCanvas.drawPath(dp.path, dp.paint);
            //将该路径从删除的路径列表中去除
            deletePath.remove(deletePath.size() - 1);

            invalidate();
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                startX=event.getX();
                startY=event.getY();



                Log.e("TAG","ACTION_DOWN");
                if(mCurrentView!=null){
                    dp = new DrawPath();
                    dp.bitmap=contentBitmap;
                    Log.e("TAG","mCurrentView is not null");
                    drawPic(contentBitmap);
                    Log.e("TAG","drawPic");


                }else{
                    // 每次down下去重新new一个Path
                    mPath = new Path();
                    //每一次记录的路径对象是不一样的
                    dp = new DrawPath();
                    dp.path = mPath;
                    dp.paint = mPaint;
                    touch_start(x, y);
                }


                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                if(mCurrentView==null){
                    touch_move(x, y);
                    invalidate();
                }

                break;
            case MotionEvent.ACTION_UP:
                if(mCurrentView==null){
                    touch_up();
                    invalidate();
                }else{
                    Log.e("TAG","I'm up,but mCurrentView is still not null");
                    savePath.add(dp);
                    mCurrentView=null;
                    invalidate();
                }

                break;
        }
        return true;
    }
    //保存到sd卡
    public void saveToSDCard() {
        //获得系统当前时间，并以该时间作为文件名
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        Log.e("TAG", "当前时间"+formatter.format(curDate));
        String str = formatter.format(curDate) + "paint.png";
        Log.e("TAG", "当前文件名"+str);
        File file = new File("sdcard/Pictures/" + str);
        //String directory=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath();
        //File file = new File(directory + str);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            Log.e("TAG", "尝试写入file");
        } catch (Exception e) {
            e.printStackTrace();
        }
        mBitmap.compress(CompressFormat.PNG, 100, fos);
        Log.e("TAG", "压缩为PNG格式");
        //发送Sd卡的就绪广播,要不然在手机图库中不存在
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Log.e("TAG", "发送就绪广播");
        intent.setData(Uri.fromFile(file));
        context.sendBroadcast(intent);
        Log.e("TAG", "图片已保存");
        Toast.makeText(context,"图片已保存",Toast.LENGTH_LONG).show();
    }
    //以下为样式修改内容

   public void selectPencilStyle(int reset,int which1,int which2) {
        if(reset==1){
            setPencilStyle();
        }else{
            if(which1!=0){
                currentSize = which1;
            }
            if(which2!=0){
                currentAlpha=which2;
            }

            //isPencil=true;
            setPencilStyle();
        }
    }
    public void selectEraserStyle(int reset,int which){
        if(reset==1){
            setEraserStyle();
        }else{
            if(which!=0){
                currentEraserSize=which;
            }

            //isPencil=false;
            setEraserStyle();
        }
    }
    /**
     * 设置画笔样式
     * @param reset 是否重置,若为0则不改变
     * @param which1 当前尺寸大小,若为0则不改
     * @param which2 当前透明度,若为0则不改
     */
    public void selectPaintStyle(int reset,int which1,int which2){
        //橡皮
        if(isPencil==false){
            selectEraserStyle(reset,which1);
        }else{
            selectPencilStyle(reset,which1,which2);
        }
    }


    //设置画笔颜色
    public void selectPaintColor(int which) {
        currentColor = paintColor[which];
        setPencilStyle();
    }
    public void drawGraphics(int which){
        currentDrawGraphics = graphics[which];
    }
    public void isFill(boolean isFill){
        this.isFill=isFill;
    }

    public int getColor(int which){
        return paintColor[which];
    }

    /**
     * 画三角形
     * @param startX 开始位置x坐标
     * @param startY 开始位置y坐标
     * @param endX 结束位置x坐标
     * @param endY 结束位置y坐标
     */
    public void drawTA(int startX,int startY,int endX, int endY){
        //double lineLength = Math.sqrt(Math.pow(Math.abs(endX-startX),2) + Math.pow(Math.abs(endY-startY),2));//线当前长度
        double dx=Math.abs(endX-startX);
        double dy=Math.abs(endY-startY);
        double x1,y1;
        double lineLength = Math.sqrt(Math.pow(dx,2) + Math.pow(dy,2));//线当前长度
        if(endX>startX){
            x1=Math.abs(endX-2*dx);
            y1=endY;
        }else{
            x1=Math.abs(endX+2*dx);
            y1=endY;
        }


        mPath.moveTo(startX,startY);
        mPath.lineTo(endX,endY);
        mPath.lineTo((int) x1,(int) y1);
        mPath.close();
    }
    /**
     * 画箭头
     * @param startX 开始位置x坐标
     * @param startY 开始位置y坐标
     * @param endX 结束位置x坐标
     * @param endY 结束位置y坐标
     */
    public void drawAL(int startX, int startY, int endX, int endY)
    {
        double lineLength = Math.sqrt(Math.pow(Math.abs(endX-startX),2) + Math.pow(Math.abs(endY-startY),2));//线当前长度
        double H = 0;// 箭头高度
        double L = 0;// 箭头长度
        if(lineLength < 320){//防止箭头开始时过大
            H = lineLength/4 ;
            L = lineLength/6;
        }else { //超过一定线长箭头大小固定
            H = 80;
            L = 50;
        }

        double arrawAngle = Math.atan(L / H); // 箭头角度
        double arraowLen = Math.sqrt(L * L + H * H); // 箭头的长度
        double[] pointXY1 = rotateVec(endX - startX, endY - startY, arrawAngle, true, arraowLen);
        double[] pointXY2 = rotateVec(endX - startX, endY - startY, -arrawAngle, true, arraowLen);
        int x3 = (int) (endX - pointXY1[0]);//(x3,y3)为箭头一端的坐标
        int y3 = (int) (endY - pointXY1[1]);
        int x4 = (int) (endX - pointXY2[0]);//(x4,y4)为箭头另一端的坐标
        int y4 = (int) (endY - pointXY2[1]);
        // 画线
        mPath.moveTo(startX,startY);
        mPath.lineTo(endX,endY);
        mPath.moveTo(x3, y3);
        mPath.lineTo(endX, endY);
        mPath.lineTo(x4, y4);
        // mPath.close();闭合线条
    }

    /**
     * 矢量旋转函数，计算末点的位置
     * @param x  x分量
     * @param y  y分量
     * @param ang  旋转角度
     * @param isChLen  是否改变长度
     * @param newLen   箭头长度长度
     * @return    返回末点坐标
     */
    public double[] rotateVec(int x, int y, double ang, boolean isChLen, double newLen) {
        double pointXY[] = new double[2];
        double vx = x * Math.cos(ang) - y * Math.sin(ang);
        double vy = x * Math.sin(ang) + y * Math.cos(ang);
        if (isChLen) {
            double d = Math.sqrt(vx * vx + vy * vy);
            pointXY[0] = vx / d * newLen;
            pointXY[1] = vy / d * newLen;
        }
        return pointXY;
    }
    //设置背景图
    public void setSrcBitmap(Bitmap bitmap){
        this.srcBitmap = bitmap;
    }
    public void drawPic(Bitmap bitmap){

        Matrix matrix=mCurrentView.getMatrix();
        dp.matrix=matrix;
        mCanvas.drawBitmap(bitmap,matrix,mBitmapPaint);

        DrawActivity.frameLayout.removeView(mCurrentView);

    }
}
