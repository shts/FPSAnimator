package com.daasuu.FPSAnimator.record;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.daasuu.FPSAnimator.util.UIUtil;
import com.daasuu.library.InputSurfaceView;

import java.io.File;

/**
 * Created by shotasaitou on 2017/11/07.
 */

public class MultipleInputSurfaceView extends InputSurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = MultipleInputSurfaceView.class.getSimpleName();

    private boolean isRecordable;
    private boolean isFinish;

    private SurfaceHolder surfaceHolder;
    private Recorder recorder;

    public MultipleInputSurfaceView(Context context) {
        super(context);
        initSurface();
    }

    public MultipleInputSurfaceView(Context context, int fps) {
        super(context, fps);
        initSurface();
    }

    public MultipleInputSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSurface();
    }

    public MultipleInputSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initSurface();
    }

    private void initSurface() {
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
    }

    public void startAnimation() {
        isRecordable = false;
        tickStart();
    }

    public void finishAnimation() {
        isFinish = true;
        tickStop();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void startRecord(File outputFile) {
        isRecordable = true;

        int bitRate = 4000000;
        int iFrameInterval = 5;
        int displayWidth = UIUtil.getWindowWidth(getContext());
        int displayHeight = UIUtil.getWindowHeight(getContext()) - UIUtil.getStatusBarHeight(getContext());
        recorder = new Recorder(displayWidth, displayHeight, bitRate, getFps(), iFrameInterval);
        recorder.prepare(outputFile);

        tickStart();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void finishRecord() {
        tickStop();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onStopped() {
        if (isRecordable && recorder != null) {
            recorder.finish();
            recorder.release();
            isRecordable = false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onPreDraw() {
        if (isFinish) {
            return;
        }
        if (isRecordable) {
            recorder.capture();
        }
    }

    @Override
    public Surface getInputSurface() {
        if (isFinish) {
            return null;
        }
        if (isRecordable) {
            return recorder != null ? recorder.getInputSurface() : null;
        } else {
            return surfaceHolder.getSurface();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }
}
