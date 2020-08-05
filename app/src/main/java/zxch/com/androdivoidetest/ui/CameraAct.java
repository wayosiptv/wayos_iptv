package zxch.com.androdivoidetest.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.WindowManager;

import java.io.File;
import java.io.IOException;

import zxch.com.androdivoidetest.R;

public class CameraAct extends NewBaseAct implements SurfaceHolder.Callback{
    private SurfaceView cameraView;
    private SurfaceHolder surfaceHolder;
    private static Camera mCamera;
    private Uri imageUri;
    private File outputImage;
    public static final int TAKE_PHOTO = 1;
    private TextureView textureView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        cameraView = findViewById(R.id.cameraView);
        startPreview();
    }

    /**
     * 开始预览
     */
    private void startPreview() {
        //检查访问摄像头权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
        } else { //拥有权限
            initCameraView();
        }
    }
    private void initCameraView() {
        surfaceHolder = cameraView.getHolder();
        surfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(final SurfaceHolder holder) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mCamera == null) mCamera = Camera.open();
                    mCamera.setPreviewDisplay(holder);
                    mCamera.setPreviewCallback(new Camera.PreviewCallback() {
                        @Override
                        public void onPreviewFrame(byte[] data, Camera camera) {
                        }
                    });
                    refreshCamera(); // 这一步是否多余？在以后复杂的使用场景下，此步骤是必须的。
                    int rotation = getDisplayOrientation(); //获取当前窗口方向
                    mCamera.setDisplayOrientation(rotation); //设定相机显示方向
                    mCamera.startPreview();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }
    //    @Override
//    protected void onStart() {
//        super.onStart();
//        if (checkCameraHardware(CameraAct.this)) {
//            initData();
//        } else {
//            T.show(CameraAct.this, "暂未找到摄像头", 1);
//        }
//
//    }
//
//    private void initData() {
//        surfaceHolder = cameraView.getHolder();
//        surfaceHolder.addCallback(this);
//    }
//
//    private boolean checkCameraHardware(Context context) {
//        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
////             this device has a camera
//            Log.e(TAG, "checkCameraHardware: this device has a camera");
//            return true;
//        } else {
////             no camera on this device
//            Log.e(TAG, "checkCameraHardware: no camera on this device");
//            return false;
//        }
//    }
//
//    @Override
//    public void surfaceCreated(SurfaceHolder surfaceHolder) {
//        mCamera = getCameraInstance();
//        try {
//            if (mCamera != null) {
//                mCamera.setPreviewDisplay(surfaceHolder);
//                mCamera.startPreview();
//            }
//
//        } catch (IOException e) {
//            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
//        }
//
//    }
//
//    @Override
//    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
//        refreshCamera(); // 这一步是否多余？在以后复杂的使用场景下，此步骤是必须的。
//        int rotation = getDisplayOrientation(); //获取当前窗口方向
//        mCamera.setDisplayOrientation(rotation); //设定相机显示方向
//    }
//
//    @Override
//    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
//        surfaceHolder.removeCallback(this);
//        mCamera.setPreviewCallback(null);
//        mCamera.stopPreview();
//        mCamera.release();
//        mCamera = null;
//    }
//
//    // 获取camera实例
//    public static Camera getCameraInstance() {
//        try {
//            mCamera = Camera.open();
//        } catch (Exception e) {
//            Log.d("TAG", "camera is not available");
//        }
//        return mCamera;
//    }
//
    // 获取当前窗口管理器显示方向
    private int getDisplayOrientation() {
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int rotation = display.getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        android.hardware.Camera.CameraInfo camInfo =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, camInfo);
//        Camera.CameraInfo camInfo = new Camera.CameraInfo();
//        Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, camInfo);

        // 这里其实还是不太懂：为什么要获取camInfo的方向呢？相当于相机标定？？
        int result = (camInfo.orientation - degrees + 360) % 360;

        return result;
    }

    // 刷新相机
    private void refreshCamera() {
        if (surfaceHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here
        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.startPreview();
        } catch (Exception e) {

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        finish();
        return super.onKeyDown(keyCode, event);

    }
}
