package com.daasuu.FPSAnimator.record;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.Surface;

import com.daasuu.FPSAnimator.util.UIUtil;
import com.daasuu.library.FPSInputSurfaceView;

import java.io.File;

/**
 * Created by shotasaitou on 2017/10/25.
 */
public class RecordableFPSSurfaceView extends FPSInputSurfaceView {

    private Recorder recorder;
    private boolean isFinish;

    public RecordableFPSSurfaceView(Context context) {
        super(context);
    }

    public RecordableFPSSurfaceView(Context context, int fps) {
        super(context, fps);
    }

    public RecordableFPSSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void startRecord(File outputFile) {
        if (recorder != null) {
            return;
        }

        int bitRate = 4000000;
        int iFrameInterval = 5;
        int displayWidth = UIUtil.getWindowWidth(getContext());
        int displayHeight = UIUtil.getWindowHeight(getContext()) - UIUtil.getStatusBarHeight(getContext());

        recorder = new Recorder(displayWidth, displayHeight, bitRate, getFps(), iFrameInterval);
        recorder.prepare(outputFile);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void stopRecord() {
        if (isFinish) {
            return;
        }
        isFinish = true;
        recorder.finish();
        recorder.release();
    }


    @Override
    public Surface getInputSurface() {
        return recorder.getInputSurface();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onPreDraw() {
        recorder.capture();
    }
}
