package mkk13.colorjudge.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Range;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.webkit.PermissionRequest;
import android.widget.Button;
import android.widget.Toast;

import com.android.camera.CropImageIntentBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mkk13.colorjudge.Fragments.HorizontalGalleryListFragment;
import mkk13.colorjudge.R;
import mkk13.colorjudge.RequestPermissions;
import mkk13.colorjudge.Utils;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.RuntimePermissions;

import static java.text.DateFormat.getDateTimeInstance;


public class GuessColorActivity extends AppCompatActivity {
    private Size mPreviewSize;
    private Size mJpegSizes[] = null;
    private TextureView mTextureView;
    private Uri mPicUri, mCroppedUri;
    private CameraDevice mCameraDevice;
    private CaptureRequest.Builder mPreviewBuilder;
    private CameraCaptureSession mPreviewSession;
    private Range<Integer> mAERange = null;
    Button mGetPictureBtn;
    boolean mSurfaceTextureAvailable = false;
    boolean mPermissionsGranted = false;

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    static {
        ORIENTATIONS.append(Surface.ROTATION_0  ,  90);
        ORIENTATIONS.append(Surface.ROTATION_90 ,   0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guess_color_panel);

        mTextureView = (TextureView) findViewById(R.id.textureview);
        mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);

        mGetPictureBtn = (Button) findViewById(R.id.getpicture);
        mGetPictureBtn.setOnClickListener(mBtnOnClickListener);

        startCameraFragment();

    }

    private void startCameraFragment(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        HorizontalGalleryListFragment fragment = new HorizontalGalleryListFragment();
        transaction.replace(R.id.sample_content_fragment, fragment);
        transaction.commit();
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mCameraDevice != null)
            mCameraDevice.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSurfaceTextureAvailable && mPermissionsGranted)
            openCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCameraDevice != null)
            mCameraDevice.close();
    }

    private ImageReader createCameraImageReader(CameraCharacteristics characteristics) {
        if (characteristics != null) {
            StreamConfigurationMap camChars = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            if (camChars != null)
                mJpegSizes = camChars.getOutputSizes(ImageFormat.JPEG);
        }

        int width = 1920, height = 1080;
        if (mJpegSizes != null && mJpegSizes.length > 0) {
            int last = mJpegSizes.length - 1;
            if (mJpegSizes[last].getWidth() < width) {
                width = mJpegSizes[last].getWidth();
                height = mJpegSizes[last].getHeight();
            }
        }

        return ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);
    }

    private Range<Integer> getRange(CameraCharacteristics chars) {
        Range<Integer>[] ranges = chars.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES);
        Range<Integer> result = null;

        for (Range<Integer> range : ranges) {
            int upper = range.getUpper();
            // 10 - min range upper for my needs
            if (upper >= 10) {
                if (result == null || upper < result.getUpper()) {
                    result = range;
                }
            }
        }
        if (result == null) {
            result = ranges[0];
        }
        return result;
    }

    void getPicture() {
        if (mCameraDevice == null)
            return;

        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(mCameraDevice.getId());
            ImageReader reader = createCameraImageReader(characteristics);

            List<Surface> outputSurfaces = new ArrayList<Surface>();
            outputSurfaces.add(reader.getSurface());
            outputSurfaces.add(new Surface(mTextureView.getSurfaceTexture()));

            final CaptureRequest.Builder capturebuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            final int orientation = ORIENTATIONS.get(getWindowManager().getDefaultDisplay().getRotation());
            capturebuilder.addTarget(reader.getSurface());

            if (mAERange != null)
                capturebuilder.set(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, mAERange);
            capturebuilder.set(CaptureRequest.CONTROL_AWB_MODE, CameraMetadata.CONTROL_AWB_MODE_AUTO);
            capturebuilder.set(CaptureRequest.CONTROL_AE_LOCK, false);
            capturebuilder.set(CaptureRequest.CONTROL_AE_MODE, CameraMetadata.CONTROL_AE_MODE_ON);
            capturebuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            capturebuilder.set(CaptureRequest.JPEG_ORIENTATION, orientation);

            HandlerThread handlerThread = new HandlerThread("takePicture");
            handlerThread.start();

            final Handler handler = new Handler(handlerThread.getLooper());
            reader.setOnImageAvailableListener(mOnImageAvailableListener, handler);

            final CameraCaptureSession.StateCallback captureSessionStateCb = new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession session) {
                    try {
                        session.capture(capturebuilder.build(), mCaptureSessionCaptureCb, handler);
                    } catch (Exception err) {
                        err.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession session) {
                }
            };

            mCameraDevice.createCaptureSession(outputSurfaces, captureSessionStateCb, handler);
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public void openCamera() {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            String camId = manager.getCameraIdList()[0];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(camId);
            if (mAERange == null)
                mAERange = getRange(characteristics);

            StreamConfigurationMap streamConfMap = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            mPreviewSize = streamConfMap.getOutputSizes(SurfaceTexture.class)[0];
            manager.openCamera(camId, mDeviceStateCb, null);
        } catch (SecurityException | CameraAccessException err) {
            err.printStackTrace();
        }
    }

    private void startCamera() {
        if (mCameraDevice == null || !mTextureView.isAvailable() || mPreviewSize == null)
            return;

        SurfaceTexture texture = mTextureView.getSurfaceTexture();
        if (texture == null)
            return;

        texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
        Surface surface = new Surface(texture);
        try {
            mPreviewBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        } catch (Exception err) {
            err.printStackTrace();
        }

        try {
            mPreviewBuilder.addTarget(surface);
            mCameraDevice.createCaptureSession(Arrays.asList(surface), mCaptureSessionStateCb, null);
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    void getChangedPreview()
    {
        if (mCameraDevice == null)
            return;

        if (mAERange != null)
            mPreviewBuilder.set(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, mAERange);
        mPreviewBuilder.set(CaptureRequest.CONTROL_AWB_MODE, CameraMetadata.CONTROL_AWB_MODE_AUTO);
        mPreviewBuilder.set(CaptureRequest.CONTROL_AE_LOCK, false);
        mPreviewBuilder.set(CaptureRequest.CONTROL_AE_MODE, CameraMetadata.CONTROL_AE_MODE_ON);
        mPreviewBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);

        HandlerThread thread = new HandlerThread("changedPreview");
        thread.start();
        Handler handler = new Handler(thread.getLooper());
        try
        {
            mPreviewSession.setRepeatingRequest(mPreviewBuilder.build(), null, handler);
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    // LISTENERS
    private final TextureView.SurfaceTextureListener mSurfaceTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            mSurfaceTextureAvailable = true;
            //if (ContextCompat.checkSelfPermission(GuessColorActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
          //      ActivityCompat.requestPermissions(GuessColorActivity.this, new String[]{Manifest.permission.CAMERA}, RequestPermissions.REQUEST_PERMISSIONS);
         //  } else {
                mPermissionsGranted = true;

                // Execute some code after 500 milliseconds have passed
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                            openCamera();
                    }
                }, 500);


           // }
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] results) {
        if (requestCode == 1 && !mPermissionsGranted) {
            for (int i = 0; i < permissions.length; i++)
                if (permissions[i].equals("android.permission.CAMERA")) {
                    if (results.length > i && results[i] == PackageManager.PERMISSION_GRANTED) {
                        mPermissionsGranted = true;
                        if (mSurfaceTextureAvailable)
                            openCamera();
                    }
                }

        }
    }

    private File createImageFile() {
        File image = null;
        String timeStamp = getDateTimeInstance().toString();
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return image;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == Utils.IMAGE_CROP_INTENT) {
            if (mPicUri != null) {
                File bigImg = new File(mPicUri.getPath());
                bigImg.delete();
                mPicUri = null;
            }

            Intent intent = new Intent(this, GuessColorResolveActivity.class);
            intent.putExtra("photoUri", mCroppedUri);
            startActivity(intent);
        }
    }

    public void cropImage(Uri srcUri) {
        mPicUri = null;
        if (!srcUri.toString().startsWith("file://") && !srcUri.toString().startsWith("content://")) {
            String uri = "file://" + srcUri.toString();
            srcUri = Uri.parse(uri);
        }
        mCroppedUri = Uri.fromFile(createImageFile());
        CropImageIntentBuilder cropImage = new CropImageIntentBuilder(64, 64, mCroppedUri);
        cropImage.setOutlineColor(0xFF03A9F4);
        cropImage.setSourceImage(srcUri);
        try {
            startActivityForResult(cropImage.getIntent(getApplicationContext()), Utils.IMAGE_CROP_INTENT);
        } catch (ActivityNotFoundException ex) {
            ex.printStackTrace();
        }
    }


    private final ImageReader.OnImageAvailableListener mOnImageAvailableListener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader reader) {
            Image image = null;
            try {
                image = reader.acquireLatestImage();
                ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                byte[] bytes = new byte[buffer.capacity()];
                buffer.get(bytes);
                save(bytes);
                cropImage(mPicUri);
            } catch (Exception err) {
                err.printStackTrace();
            } finally {
                if (image != null)
                    image.close();
            }
        }

        private void save(byte[] bytes)
        {
            File image = createImageFile();
            OutputStream outputStream = null;
            try
            {
                outputStream = new FileOutputStream(image);
                outputStream.write(bytes);
            } catch (Exception err) {
                err.printStackTrace();
            } finally {
                try {
                    if (outputStream != null)
                        outputStream.close();
                } catch (Exception err) {
                    err.printStackTrace();
                }
            }
            mPicUri = FileProvider.getUriForFile(getApplicationContext(),
                    "com.example.android.fileprovider",
                    image);
        }

    };

    private final View.OnClickListener mBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getPicture();
        }
    };

    // CALLBACKS
    private final CameraCaptureSession.StateCallback mCaptureSessionStateCb = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(CameraCaptureSession session) {
            mPreviewSession = session;
            getChangedPreview();
        }

        @Override
        public void onConfigureFailed(CameraCaptureSession session) {
        }
    };

    private final CameraDevice.StateCallback mDeviceStateCb = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            mCameraDevice = camera;
            startCamera();
        }

        @Override
        public void onDisconnected(CameraDevice camera) {
        }

        @Override
        public void onError(CameraDevice camera, int error) {
        }
    };

    private final CameraCaptureSession.CaptureCallback mCaptureSessionCaptureCb = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureStarted(CameraCaptureSession session, CaptureRequest request, long timestamp, long frameNumber) {
            super.onCaptureStarted(session, request, timestamp, frameNumber);
        }

        @Override
        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);
            startCamera();
        }
    };
}