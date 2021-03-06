package com.townspriter.android.photobrowser.core.model.extension.engine;

import static android.view.MotionEvent.ACTION_CANCEL;
import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_UP;

import java.lang.ref.WeakReference;

import com.townspriter.android.photobrowser.core.api.bean.BrowserImageBean;
import com.townspriter.android.photobrowser.core.api.listener.OnGestureListener;
import com.townspriter.android.photobrowser.core.api.listener.OnPhotoTapListener;
import com.townspriter.android.photobrowser.core.api.listener.OnViewTapListener;
import com.townspriter.android.photobrowser.core.model.extension.IPhotoView;
import com.townspriter.android.photobrowser.core.model.extension.LongPhotoAnalysable;
import com.townspriter.android.photobrowser.core.model.gesture.DoubleTapDetector;
import com.townspriter.android.photobrowser.core.model.gesture.GestureDetectorFactory;
import com.townspriter.android.photobrowser.core.model.gesture.GestureService;
import com.townspriter.android.photobrowser.core.model.listener.OnMatrixChangedListener;
import com.townspriter.android.photobrowser.core.model.listener.OnScaleChangeListener;
import com.townspriter.android.photobrowser.core.model.listener.OnScrollListener;
import com.townspriter.base.foundation.utils.lang.AssertUtil;
import com.townspriter.base.foundation.utils.log.Logger;
import com.townspriter.base.foundation.utils.system.SystemInfo;
import com.townspriter.base.foundation.utils.ui.ResHelper;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Matrix.ScaleToFit;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLayoutChangeListener;
import android.view.View.OnLongClickListener;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

/******************************************************************************
 * @Path PhotoBrowserCore:PhotoViewEngine
 * @Describe ????????????????????????
 * @Name ??????
 * @Email zhangfei@townspriter.com
 * @Data 21-4-6
 * CopyRight(C)2021 ?????????????????????????????????
 * *****************************************************************************
 */
public class PhotoViewEngine implements IPhotoView,View.OnTouchListener,OnGestureListener,ViewTreeObserver.OnGlobalLayoutListener,OnLayoutChangeListener
{
    /** ????????????????????????????????????????????? */
    public static final int PHOTOxBGxALPHA=255;
    protected static final int EDGE_NONE=-1;
    protected static final int EDGE_LEFT=0;
    protected static final int EDGE_RIGHT=1;
    protected static final int EDGE_BOTH=2;
    public static float SCREENxHEIGHT;
    /**
     * ????????????????????????????????????????????????
     */
    public static float MINIMUMxQUITxDISTANCE;
    protected final Interpolator INTERPOLATOR=new AccelerateDecelerateInterpolator();
    /**
     * ????????????
     */
    protected final Matrix mSuppMatrix=new Matrix();
    private final String TAG="PhotoViewEngine";
    /**
     * ????????????
     */
    private final Matrix mBaseMatrix=new Matrix();
    /**
     * ?????????????????????
     */
    private final Matrix mDrawMatrix=new Matrix();
    private final RectF mDisplayRect=new RectF();
    private final float[] mMatrixValues=new float[9];
    public int mScrollEdge=EDGE_BOTH;
    protected int mZoomDuration=DEFAULTxZOOMxDURATION;
    protected boolean mAllowParentInterceptOnEdge=true;
    protected boolean mBlockParentIntercept=false;
    protected GestureService mScaleDragDetector;
    private int mImageType=0;
    private float mMinScale=DEFAULTxMINxSCALE;
    private float mMidScale=DEFAULTxMIDxSCALE;
    private float mMaxScale=DEFAULTxMAXxSCALE;
    private boolean mLastBlockParentInterceptStatus;
    private boolean mWasScaling;
    private boolean mWasDragging;
    private WeakReference<ImageView> mImageView;
    private GestureDetector mGestureDetector;
    /**
     * ?????????????????????????????????????????????
     */
    private LongPhotoAnalysable longPhotoAnalysable;
    private boolean mDisAllowParentInterceptTouchEvent=true;
    private OnMatrixChangedListener mMatrixChangeListener;
    private OnPhotoTapListener mPhotoTapListener;
    private OnViewTapListener mViewTapListener;
    private OnScrollListener mScrollListener;
    private OnLongClickListener mLongClickListener;
    private OnScaleChangeListener mScaleChangeListener;
    private FlingRunnable mCurrentFlingRunnable;
    private boolean mZoomEnabled;
    private ScaleType mScaleType=ScaleType.FIT_CENTER;
    
    public PhotoViewEngine(ImageView imageView)
    {
        init(imageView);
    }
    
    /**************************************** ???????????? ****************************************/
    public Matrix getDrawMatrix()
    {
        // ????????????
        mDrawMatrix.set(mBaseMatrix);
        // ??????(??????)??????(M'=T*M)
        mDrawMatrix.postConcat(mSuppMatrix);
        return mDrawMatrix;
    }
    
    public void setImageViewMatrix(Matrix matrix)
    {
        ImageView imageView=getImageView();
        if(null!=imageView)
        {
            inspectMatrixScaleType();
            imageView.setImageMatrix(matrix);
            if(null!=mMatrixChangeListener)
            {
                RectF displayRect=getDisplayRect(matrix);
                if(null!=displayRect)
                {
                    mMatrixChangeListener.onMatrixChanged(displayRect);
                }
                else
                {
                    Logger.w(TAG,"setImageViewMatrix-displayRect:NULL");
                }
            }
        }
        else
        {
            Logger.w(TAG,"setImageViewMatrix-imageView:NULL");
        }
        // ????????????????????????????????????????????????????????????????????????.???????????????????????????
        if(getValue(mSuppMatrix,Matrix.MTRANS_Y)==0.0||getScale()!=getMinimumScale())
        {
            onBackgroundAlphaChangingByGesture(PHOTOxBGxALPHA);
        }
    }
    
    /**
     * ????????????????????????????????????????????????.?????????????????????????????????
     *
     * @param dragDistanceY
     */
    public void checkMatrixRegionBounds(float dragDistanceY)
    {
        checkMatrixBounds();
        setImageViewMatrix(getDrawMatrix());
        ImageView imageView=getImageView();
        if(longPhotoAnalysable!=null)
        {
            longPhotoAnalysable.reloadBitmapIfNeeded(imageView,dragDistanceY/getScale());
        }
    }
    
    /**
     * ??????????????????????????????????????????
     *
     * @param matrix
     * ????????????
     * @param whichValue
     * ??????
     * @return float ????????????????????????????????????
     */
    public float getValue(Matrix matrix,int whichValue)
    {
        matrix.getValues(mMatrixValues);
        return mMatrixValues[whichValue];
    }
    
    /**
     * checkAndDisplayMatrix
     * ??????????????????????????????.???????????????????????????????????????
     */
    public void checkAndDisplayMatrix()
    {
        boolean checkMatrixBounds=checkMatrixBounds();
        if(checkMatrixBounds)
        {
            setImageViewMatrix(getDrawMatrix());
        }
    }
    
    /**************************************** IPhotoView ****************************************/
    @Override
    public ImageView getImageView()
    {
        ImageView imageView=null;
        if(null!=mImageView)
        {
            imageView=mImageView.get();
        }
        if(null==imageView)
        {
            clean();
        }
        return imageView;
    }
    
    @Override
    public IPhotoView getPhotoView()
    {
        return this;
    }
    
    @Override
    public int getImageType()
    {
        return mImageType;
    }
    
    @Override
    public void setImageType(int imageType)
    {
        mImageType=imageType;
    }
    
    @Override
    public void resetStatus()
    {
        Logger.i(TAG,"resetStatus");
        float draggedDistanceY=getValue(mSuppMatrix,Matrix.MTRANS_Y);
        if(draggedDistanceY==0)
        {
            return;
        }
        if(Math.abs(draggedDistanceY)>PhotoViewEngine.MINIMUMxQUITxDISTANCE)
        {
            if(draggedDistanceY>0)
            {
                // ??????
                onDragRelease(true,SCREENxHEIGHT-draggedDistanceY,0);
            }
            else
            {
                // ??????
                onDragRelease(true,-(SCREENxHEIGHT+draggedDistanceY),0);
            }
        }
        else
        {
            onDragRelease(false,mScaleDragDetector.getInvalidDraggedDistanceY()-draggedDistanceY,0);
        }
    }
    
    @Override
    public void update()
    {
        ImageView imageView=getImageView();
        if(null!=imageView)
        {
            if(mZoomEnabled)
            {
                setScaleTypeToMatrix(imageView);
                updateBaseMatrix(imageView.getDrawable());
            }
            else
            {
                resetMatrix();
            }
        }
    }
    
    @Override
    public void clean()
    {
        if(null==mImageView)
        {
            return;
        }
        final ImageView imageView=mImageView.get();
        if(null!=imageView)
        {
            // ??????????????????
            ViewTreeObserver observer=imageView.getViewTreeObserver();
            if(null!=observer&&observer.isAlive())
            {
                observer.removeGlobalOnLayoutListener(this);
            }
            imageView.setOnTouchListener(null);
            cancelFling();
        }
        if(null!=mGestureDetector)
        {
            mGestureDetector.setOnDoubleTapListener(null);
        }
        mMatrixChangeListener=null;
        mPhotoTapListener=null;
        mViewTapListener=null;
        mImageView=null;
    }
    
    @Override
    public int getTranslateY()
    {
        return (int)getValue(mSuppMatrix,Matrix.MTRANS_Y);
    }
    
    @Override
    public void setAllowParentInterceptOnEdge(boolean allow)
    {
        mAllowParentInterceptOnEdge=allow;
    }
    
    @Override
    public boolean canZoom()
    {
        return mZoomEnabled;
    }
    
    @Override
    public void setZoomable(boolean zoomable)
    {
        mZoomEnabled=zoomable;
        update();
    }
    
    @Override
    public void setZoomTransitionDuration(int milliseconds)
    {
        if(milliseconds<0)
        {
            milliseconds=DEFAULTxZOOMxDURATION;
        }
        this.mZoomDuration=milliseconds;
    }
    
    @Override
    public RectF getDisplayRect()
    {
        checkMatrixBounds();
        return getDisplayRect(getDrawMatrix());
    }
    
    @Override
    public Matrix getDisplayMatrix()
    {
        return new Matrix(getDrawMatrix());
    }
    
    @Override
    public boolean setDisplayMatrix(Matrix finalMatrix)
    {
        if(finalMatrix==null)
        {
            Logger.w(TAG,"setDisplayMatrix-finalMatrix:NULL");
            return false;
        }
        ImageView imageView=getImageView();
        if(null==imageView)
        {
            Logger.w(TAG,"setDisplayMatrix-imageView:NULL");
            return false;
        }
        Drawable drawable=imageView.getDrawable();
        if(null==drawable)
        {
            Logger.w(TAG,"setDisplayMatrix-drawable:NULL");
            return false;
        }
        mSuppMatrix.set(finalMatrix);
        setImageViewMatrix(getDrawMatrix());
        checkMatrixBounds();
        return true;
    }
    
    @Override
    public Bitmap getVisibleRectangleBitmap()
    {
        ImageView imageView=getImageView();
        return imageView==null?null:imageView.getDrawingCache();
    }
    
    @Override
    public float getMinimumScale()
    {
        return mMinScale;
    }
    
    @Override
    public void setMinimumScale(float minimumScale)
    {
        checkZoomLevels(minimumScale,mMidScale,mMaxScale);
        mMinScale=minimumScale;
    }
    
    @Override
    public float getMediumScale()
    {
        return mMidScale;
    }
    
    @Override
    public void setMediumScale(float mediumScale)
    {
        checkZoomLevels(mMinScale,mediumScale,mMaxScale);
        mMidScale=mediumScale;
    }
    
    @Override
    public float getMaximumScale()
    {
        return mMaxScale;
    }
    
    @Override
    public void setMaximumScale(float maximumScale)
    {
        checkZoomLevels(mMinScale,mMidScale,maximumScale);
        mMaxScale=maximumScale;
    }
    
    @Override
    public void setScaleTypeSafely(ScaleType scaleType)
    {
        boolean isSupportScale=isSupportScale(scaleType);
        if(isSupportScale&&scaleType!=mScaleType)
        {
            mScaleType=scaleType;
            update();
        }
    }
    
    @Override
    public void setScaleLevel(float minimumScale,float mediumScale,float maximumScale)
    {
        checkZoomLevels(minimumScale,mediumScale,maximumScale);
        mMinScale=minimumScale;
        mMidScale=mediumScale;
        mMaxScale=maximumScale;
    }
    
    @Override
    public float getScale()
    {
        return (float)Math.sqrt((float)Math.pow(getValue(mSuppMatrix,Matrix.MSCALE_X),2)+(float)Math.pow(getValue(mSuppMatrix,Matrix.MSKEW_Y),2));
    }
    
    @Override
    public void setScale(float scale)
    {
        setScale(scale,false);
    }
    
    @Override
    public void setScale(float scale,boolean animate)
    {
        ImageView imageView=getImageView();
        if(null!=imageView)
        {
            setScale(scale,(imageView.getRight())/2.0F,(imageView.getBottom())/2.0F,animate);
        }
        else
        {
            Logger.w(TAG,"setScale-imageView:NULL");
        }
    }
    
    @Override
    public void setScale(float scale,float focalX,float focalY,boolean animate)
    {
        Logger.i(TAG,"setScale:"+scale);
        ImageView imageView=getImageView();
        if(null!=imageView)
        {
            if(scale<mMinScale||scale>mMaxScale)
            {
                Logger.w(TAG,"setScale:???????????????");
                return;
            }
            if(animate)
            {
                imageView.post(new AnimatedZoomRunnable(getScale(),scale,focalX,focalY,PhotoViewEngine.this));
            }
            else
            {
                mSuppMatrix.setScale(scale,scale,focalX,focalY);
                checkAndDisplayMatrix();
            }
        }
        else
        {
            Logger.w(TAG,"setScale-imageView:NULL");
        }
    }
    
    @Override
    public void setRotationTo(float degrees)
    {
        mSuppMatrix.setRotate(degrees%360);
        checkAndDisplayMatrix();
    }
    
    @Override
    public void setRotationBy(float degrees)
    {
        mSuppMatrix.postRotate(degrees%360);
        checkAndDisplayMatrix();
    }
    
    @Override
    public LongPhotoAnalysable getLongPhotoAnalysator()
    {
        return longPhotoAnalysable;
    }
    
    @Override
    public void setLongPhotoAnalysator(LongPhotoAnalysable longPhotoAnalysable)
    {
        this.longPhotoAnalysable=longPhotoAnalysable;
    }
    
    @Override
    public void setGestureListener(OnGestureListener listener)
    {
        mScaleDragDetector.addGestureListener(listener);
    }
    
    @Override
    public void setLongClickListener(OnLongClickListener listener)
    {
        mLongClickListener=listener;
    }
    
    @Override
    public void setMatrixChangeListener(OnMatrixChangedListener listener)
    {
        mMatrixChangeListener=listener;
    }
    
    @Override
    public void setDoubleClickListener(GestureDetector.OnDoubleTapListener listener)
    {
        if(listener!=null)
        {
            mGestureDetector.setOnDoubleTapListener(listener);
        }
        else
        {
            mGestureDetector.setOnDoubleTapListener(new DoubleTapDetector(this));
        }
    }
    
    @Override
    public void setScaleChangeListener(OnScaleChangeListener onScaleChangeListener)
    {
        this.mScaleChangeListener=onScaleChangeListener;
    }
    
    @Override
    public void setScrollListener(OnScrollListener listener)
    {
        mScrollListener=listener;
    }
    
    @Override
    public OnViewTapListener getViewTapListener()
    {
        return mViewTapListener;
    }
    
    @Override
    public void setViewTapListener(OnViewTapListener listener)
    {
        mViewTapListener=listener;
    }
    
    @Override
    public OnPhotoTapListener getPhotoTapListener()
    {
        return mPhotoTapListener;
    }
    
    @Override
    public void setPhotoTapListener(OnPhotoTapListener listener)
    {
        mPhotoTapListener=listener;
    }
    
    /**************************************** OnTouchListener ****************************************/
    @Override
    public boolean onTouch(View view,MotionEvent motionEvent)
    {
        if(motionEvent.getAction()!=MotionEvent.ACTION_DOWN)
        {
            if(!mDisAllowParentInterceptTouchEvent)
            {
                return false;
            }
        }
        else
        {
            mDisAllowParentInterceptTouchEvent=true;
        }
        boolean handled=false;
        if(mZoomEnabled&&hasDrawable((ImageView)view))
        {
            ViewParent parent=view.getParent();
            switch(motionEvent.getAction())
            {
                case ACTION_DOWN:
                    // ?????????????????????
                    mLastBlockParentInterceptStatus=false;
                    // ???????????????????????????????????????????????????
                    if(null!=parent)
                    {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                    // ??????????????????????????????????????????????????????
                    cancelFling();
                    break;
                case ACTION_CANCEL:
                case ACTION_UP:
                    // ???????????????????????????.???????????????????????????
                    if(getScale()<mMinScale)
                    {
                        RectF rect=getDisplayRect();
                        if(null!=rect)
                        {
                            view.post(new AnimatedZoomRunnable(getScale(),mMinScale,rect.centerX(),rect.centerY(),PhotoViewEngine.this));
                            handled=true;
                        }
                    }
                    break;
                default:
                    break;
            }
            // ???????????????????????????
            if(null!=mScaleDragDetector)
            {
                mWasScaling=mScaleDragDetector.isScaling();
                mWasDragging=mScaleDragDetector.isDragging();
                handled=mScaleDragDetector.onTouchEvent(motionEvent);
            }
            // ????????????????????????
            if(null!=mGestureDetector&&mGestureDetector.onTouchEvent(motionEvent))
            {
                handled=true;
            }
        }
        return handled;
    }
    
    /**************************************** OnGestureListener ****************************************/
    @Override
    public void onDrag(float mDraggedDistanceY,float dx,float dy)
    {
        if(mScaleDragDetector.isScaling())
        {
            return;
        }
        ImageView imageView=getImageView();
        if(imageView==null)
        {
            return;
        }
        ViewParent parent=imageView.getParent();
        mDisAllowParentInterceptTouchEvent=parentDisallowInterceptTouchEvent(dx,dy);
        if(null!=parent)
        {
            parent.requestDisallowInterceptTouchEvent(mDisAllowParentInterceptTouchEvent);
        }
        if(!mDisAllowParentInterceptTouchEvent)
        {
            return;
        }
        if(getScale()==getMinimumScale()&&getImageType()!=BrowserImageBean.IMAGExLONG)
        {
            int alphaValue=PHOTOxBGxALPHA-(int)((mDraggedDistanceY/SCREENxHEIGHT)*PHOTOxBGxALPHA>PHOTOxBGxALPHA?PHOTOxBGxALPHA:(mDraggedDistanceY/SCREENxHEIGHT)*PHOTOxBGxALPHA);
            onBackgroundAlphaChangingByGesture(alphaValue);
        }
        else
        {
            mScaleDragDetector.setIsReleasing(false);
        }
        float distanceY;
        if(!mLastBlockParentInterceptStatus)
        {
            if(dy<0)
            {
                mDraggedDistanceY=-1*mDraggedDistanceY;
            }
            mLastBlockParentInterceptStatus=true;
            mSuppMatrix.postTranslate(dx,mDraggedDistanceY);
            distanceY=mDraggedDistanceY;
        }
        else
        {
            mSuppMatrix.postTranslate(dx,dy);
            distanceY=dy;
        }
        if(longPhotoAnalysable!=null)
        {
            checkMatrixRegionBounds(distanceY);
        }
        else
        {
            checkAndDisplayMatrix();
        }
    }
    
    @Override
    public void onDragRelease(boolean willExit,float mDragDistance,float dx)
    {
        if(mScaleDragDetector!=null&&mScaleDragDetector.isScaling())
        {
            return;
        }
        if(getScale()==getMinimumScale()&&getImageType()!=BrowserImageBean.IMAGExLONG)
        {
            mScaleDragDetector.setIsReleasing(true);
            ImageView imageView=getImageView();
            if(imageView==null)
            {
                return;
            }
            imageView.post(new SmoothScrollToOriginalRunnable(willExit,mDragDistance,dx,imageView,PhotoViewEngine.this));
        }
        else
        {
            mScaleDragDetector.setIsReleasing(false);
        }
    }
    
    @Override
    public void onFling(float startX,float startY,float velocityX,float velocityY)
    {
        ImageView imageView=getImageView();
        mCurrentFlingRunnable=new FlingRunnable(imageView.getContext(),PhotoViewEngine.this);
        mCurrentFlingRunnable.fling(getImageViewWidth(imageView),getImageViewHeight(imageView),(int)velocityX,(int)velocityY);
        imageView.post(mCurrentFlingRunnable);
    }
    
    @Override
    public void onScale(float scaleFactor,float focusX,float focusY)
    {
        if(getScale()<mMaxScale||scaleFactor<1.0F)
        {
            if(scaleFactor<=0)
            {
                scaleFactor=1.0F;
            }
            if(null!=mScaleChangeListener)
            {
                mScaleChangeListener.onScaleChange(scaleFactor,focusX,focusY);
            }
            mSuppMatrix.postScale(scaleFactor,scaleFactor,focusX,focusY);
            checkAndDisplayMatrix();
        }
    }
    
    @Override
    public int getBackgroundAlphaByGesture()
    {
        if(mScrollListener!=null)
        {
            return mScrollListener.getBackgroundAlphaByScroll();
        }
        else
        {
            return PHOTOxBGxALPHA;
        }
    }
    
    @Override
    public void onBackgroundAlphaChangingByGesture(int alphaValue)
    {
        if(mScrollListener!=null)
        {
            mScrollListener.onBackgroundAlphaChangingByScroll(alphaValue);
        }
    }
    
    @Override
    public void onExit()
    {
        if(mScrollListener!=null)
        {
            mScrollListener.onScrollExit();
        }
    }
    
    /**************************************** OnGlobalLayoutListener ****************************************/
    @Override
    public void onGlobalLayout()
    {
        ImageView imageView=getImageView();
        if(null!=imageView)
        {
            if(!mZoomEnabled)
            {
                updateBaseMatrix(imageView.getDrawable());
            }
        }
    }
    
    /**************************************** OnLayoutChangeListener ****************************************/
    @Override
    public void onLayoutChange(View view,int left,int top,int right,int bottom,int oldLeft,int oldTop,int oldRight,int oldBottom)
    {
        ImageView imageView=getImageView();
        if(imageView!=null)
        {
            if(mZoomEnabled)
            {
                if(left!=oldLeft||top!=oldTop||right!=oldRight||bottom!=oldBottom)
                {
                    updateBaseMatrix(imageView.getDrawable());
                }
            }
        }
    }
    
    /**************************************** ???????????? ****************************************/
    private void cancelFling()
    {
        if(null!=mCurrentFlingRunnable)
        {
            mCurrentFlingRunnable.cancelFling();
            mCurrentFlingRunnable=null;
        }
    }
    
    private boolean parentDisallowInterceptTouchEvent(float dx,float dy)
    {
        ImageView imageView=getImageView();
        if(imageView==null)
        {
            return false;
        }
        ViewParent parent=imageView.getParent();
        if(parent==null)
        {
            return true;
        }
        boolean didScale=mWasScaling&&mScaleDragDetector.isScaling();
        boolean didDrag=mWasDragging&&mScaleDragDetector.isDragging();
        mBlockParentIntercept=didScale||didDrag;
        if(mAllowParentInterceptOnEdge&&!mScaleDragDetector.isScaling()&&!mBlockParentIntercept)
        {
            if(mScrollEdge==EDGE_BOTH||(mScrollEdge==EDGE_LEFT&&dx>=1.0F)||(mScrollEdge==EDGE_RIGHT&&dx<=-1.0F))
            {
                // ???X??????????????????????????????|dy|<|dx|???
                return !(Math.abs(dy)<Math.abs(dx));
            }
        }
        else
        {
            return true;
        }
        return true;
    }
    
    private void inspectMatrixScaleType()
    {
        ImageView imageView=getImageView();
        if(null==imageView)
        {
            return;
        }
        if(!(imageView instanceof IPhotoView))
        {
            if(!ScaleType.MATRIX.equals(imageView.getScaleType()))
            {
                throw new IllegalStateException("??????:?????????????????????");
            }
        }
    }
    
    private boolean checkMatrixBounds()
    {
        final ImageView imageView=getImageView();
        if(null==imageView)
        {
            Logger.w(TAG,"checkMatrixBounds-imageView:NULL");
            return false;
        }
        final RectF rect=getDisplayRect(getDrawMatrix());
        if(null==rect)
        {
            Logger.w(TAG,"checkMatrixBounds-rect:NULL");
            return false;
        }
        final float height=rect.height(),width=rect.width();
        float deltaX=0,deltaY=0;
        final int viewHeight=getImageViewHeight(imageView);
        if(height<=viewHeight)
        {
            switch(mScaleType)
            {
                case FIT_START:
                    deltaY=-rect.top;
                    break;
                case FIT_END:
                    deltaY=viewHeight-height-rect.top;
                    break;
                default:
                    deltaY=(viewHeight-height)/2.0F-rect.top;
                    break;
            }
        }
        else if(rect.top>0)
        {
            deltaY=-rect.top;
        }
        else if(rect.bottom<viewHeight)
        {
            deltaY=viewHeight-rect.bottom;
        }
        final int viewWidth=getImageViewWidth(imageView);
        if(width<=viewWidth)
        {
            switch(mScaleType)
            {
                case FIT_START:
                    deltaX=-rect.left;
                    break;
                case FIT_END:
                    deltaX=viewWidth-width-rect.left;
                    break;
                default:
                    deltaX=(viewWidth-width)/2.0F-rect.left;
                    break;
            }
            mScrollEdge=EDGE_BOTH;
        }
        else if(rect.left>=0)
        {
            mScrollEdge=EDGE_LEFT;
            deltaX=-rect.left;
        }
        else if(rect.right<=viewWidth)
        {
            deltaX=viewWidth-rect.right;
            mScrollEdge=EDGE_RIGHT;
        }
        else
        {
            mScrollEdge=EDGE_NONE;
        }
        // ??????????????????????????????????????????????????????
        if(getScale()==getMinimumScale()&&getImageType()!=BrowserImageBean.IMAGExLONG)
        {
            mSuppMatrix.postTranslate(deltaX,0);
        }
        else
        {
            mSuppMatrix.postTranslate(deltaX,deltaY);
        }
        return true;
    }
    
    private RectF getDisplayRect(Matrix matrix)
    {
        ImageView imageView=getImageView();
        if(null!=imageView)
        {
            Drawable drawable=imageView.getDrawable();
            if(null!=drawable)
            {
                mDisplayRect.set(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
                matrix.mapRect(mDisplayRect);
                return mDisplayRect;
            }
        }
        return null;
    }
    
    // ???????????????????????????
    private void resetMatrix()
    {
        if(longPhotoAnalysable!=null)
        {
            mSuppMatrix.reset();
        }
        setImageViewMatrix(getDrawMatrix());
        checkMatrixBounds();
    }
    
    private void updateBaseMatrix(Drawable drawable)
    {
        ImageView imageView=getImageView();
        if(null==imageView)
        {
            return;
        }
        if(null==drawable)
        {
            return;
        }
        final float viewWidth=getImageViewWidth(imageView);
        final float viewHeight=getImageViewHeight(imageView);
        final int drawableWidth=drawable.getIntrinsicWidth();
        final int drawableHeight=drawable.getIntrinsicHeight();
        // ????????????????????????
        mBaseMatrix.reset();
        final float widthScale=viewWidth/drawableWidth;
        final float heightScale=viewHeight/drawableHeight;
        if(mScaleType==ScaleType.CENTER)
        {
            mBaseMatrix.postTranslate((viewWidth-drawableWidth)/2.0F,(viewHeight-drawableHeight)/2.0F);
        }
        else if(mScaleType==ScaleType.CENTER_CROP)
        {
            float scale=Math.max(widthScale,heightScale);
            mBaseMatrix.postScale(scale,scale);
            mBaseMatrix.postTranslate((viewWidth-drawableWidth*scale)/2.0F,0);
        }
        else if(mScaleType==ScaleType.CENTER_INSIDE)
        {
            float scale=Math.min(1.0F,Math.min(widthScale,heightScale));
            mBaseMatrix.postScale(scale,scale);
            mBaseMatrix.postTranslate((viewWidth-drawableWidth*scale)/2.0F,(viewHeight-drawableHeight*scale)/2.0F);
        }
        else
        {
            RectF mTempSrc=new RectF(0,0,drawableWidth,drawableHeight);
            RectF mTempDst=new RectF(0,0,viewWidth,viewHeight);
            // ??????????????????????????????????????????
            switch(mScaleType)
            {
                case FIT_CENTER:
                    mBaseMatrix.setRectToRect(mTempSrc,mTempDst,ScaleToFit.CENTER);
                    break;
                case FIT_START:
                    mBaseMatrix.setRectToRect(mTempSrc,mTempDst,ScaleToFit.START);
                    break;
                case FIT_END:
                    mBaseMatrix.setRectToRect(mTempSrc,mTempDst,ScaleToFit.END);
                    break;
                case FIT_XY:
                    mBaseMatrix.setRectToRect(mTempSrc,mTempDst,ScaleToFit.FILL);
                    break;
                default:
                    break;
            }
        }
        if(longPhotoAnalysable!=null)
        {
            checkAndDisplayMatrix();
        }
        else
        {
            resetMatrix();
        }
    }
    
    private int getImageViewWidth(ImageView imageView)
    {
        if(null==imageView)
        {
            Logger.w(TAG,"getImageViewWidth-imageView:NULL");
            return 0;
        }
        return imageView.getWidth()-imageView.getPaddingLeft()-imageView.getPaddingRight();
    }
    
    private int getImageViewHeight(ImageView imageView)
    {
        if(null==imageView)
        {
            Logger.w(TAG,"getImageViewHeight-imageView:NULL");
            return 0;
        }
        return imageView.getHeight()-imageView.getPaddingTop()-imageView.getPaddingBottom();
    }
    
    private void checkZoomLevels(float minZoom,float midZoom,float maxZoom)
    {
        // ?????????????????????.???????????????????????????
        if(minZoom>=maxZoom)
        {
            Logger.w(TAG,"checkZoomLevels:DO NOTHING");
        }
    }
    
    private boolean hasDrawable(ImageView imageView)
    {
        return null!=imageView&&null!=imageView.getDrawable();
    }
    
    private boolean isSupportScale(final ScaleType scaleType)
    {
        if(null==scaleType)
        {
            Logger.w(TAG,"isSupportScale-scaleType:NULL");
            return false;
        }
        switch(scaleType)
        {
            case MATRIX:
                AssertUtil.fail("???????????????????????????");
                return false;
            default:
                return true;
        }
    }
    
    private void setScaleTypeToMatrix(ImageView imageView)
    {
        if(null!=imageView)
        {
            if(imageView instanceof IPhotoView)
            {
                if(!ScaleType.MATRIX.equals(imageView.getScaleType()))
                {
                    imageView.setScaleType(ScaleType.MATRIX);
                }
            }
        }
    }
    
    private void init(ImageView imageView)
    {
        SCREENxHEIGHT=SystemInfo.INSTANCE.getDeviceHeight(imageView.getContext());
        MINIMUMxQUITxDISTANCE=ResHelper.dpToPxF(80);
        mImageView=new WeakReference<>(imageView);
        imageView.setDrawingCacheEnabled(true);
        imageView.setOnTouchListener(this);
        imageView.addOnLayoutChangeListener(this);
        ViewTreeObserver observer=imageView.getViewTreeObserver();
        if(observer.isAlive())
        {
            observer.addOnGlobalLayoutListener(this);
        }
        // ???????????????????????????:????????????
        setScaleTypeToMatrix(imageView);
        if(imageView.isInEditMode())
        {
            return;
        }
        // ????????????
        mScaleDragDetector=GestureDetectorFactory.newInstance(imageView.getContext(),this);
        mGestureDetector=new GestureDetector(imageView.getContext(),new GestureDetector.SimpleOnGestureListener()
        {
            @Override
            public void onLongPress(MotionEvent motionEvent)
            {
                if(null!=mLongClickListener)
                {
                    mLongClickListener.onLongClick(getImageView());
                }
            }
            
            @Override
            public boolean onScroll(MotionEvent motionEventBefore,MotionEvent motionEventAfter,float distanceX,float distanceY)
            {
                return super.onScroll(motionEventBefore,motionEventAfter,distanceX,distanceY);
            }
            
            @Override
            public boolean onFling(MotionEvent motionEventBefore,MotionEvent motionEventAfter,float velocityX,float velocityY)
            {
                if(Math.abs(velocityY)>Math.abs(velocityX))
                {
                    // ????????????
                    if(velocityY<0)
                    {
                        // ??????
                        if(mScrollListener!=null)
                        {
                            mScrollListener.onFlingUp(velocityX,velocityY);
                        }
                    }
                    else
                    {
                        // ??????
                        if(mScrollListener!=null)
                        {
                            mScrollListener.onFlingDown(velocityX,velocityY);
                        }
                    }
                }
                return super.onFling(motionEventBefore,motionEventAfter,velocityX,velocityY);
            }
        });
        mGestureDetector.setOnDoubleTapListener(new DoubleTapDetector(this));
        setZoomable(true);
    }
}
