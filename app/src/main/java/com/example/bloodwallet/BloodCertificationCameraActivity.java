package com.example.bloodwallet;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class BloodCertificationCameraActivity extends Activity implements SurfaceHolder.Callback {

    private SurfaceView cameraView;
    private SurfaceHolder cameraHolder;
    private Camera camera;
    private ImageButton scanButton;

    private TessBaseAPI tess;
    String dataPath = "";
    String userID;
    String userName;
    String userBirthDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_certification_camera);

        Toast.makeText(this, "등록할 헌혈 증서를 카메라에 가져다주세요", Toast.LENGTH_LONG).show();

        cameraView = (SurfaceView)findViewById(R.id.blood_certificate_camera);

        userID = getIntent().getStringExtra("userID");
        userName = getIntent().getStringExtra("userName");
        userBirthDate = getIntent().getStringExtra("userBirthDate");

        camera = Camera.open();
        camera.setDisplayOrientation(90);
        Camera.Parameters param = camera.getParameters();
        param.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        camera.setParameters(param);

        cameraHolder = cameraView.getHolder();
        cameraHolder.addCallback(this);
        cameraHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        ImageView cameraBackground = (ImageView) findViewById(R.id.blood_certificate_camera_background);
        cameraBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.autoFocus(autoFocusCallback);
            }
        });

        scanButton = findViewById(R.id.blood_certificate_camera_scan);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        dataPath = getFilesDir()+ "/tesseract/";

        checkFile(new File(dataPath + "tessdata/"));

        String lang = "kor";
        tess = new TessBaseAPI();
        tess.init(dataPath, lang);
    }

    public void takePicture() {
        camera.takePicture(null, null, pictureCallback);
    }

    Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Intent intent = new Intent(BloodCertificationCameraActivity.this, BloodCertificationRegisterActivity.class);
            Bundle b = new Bundle();

            android.graphics.Matrix matrix = new android.graphics.Matrix();
            matrix.postRotate(90);
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap = cropBitmap(bitmap);
            bitmap = resizeBitmap(bitmap, 1000);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            float scale = (float) (1024/(float)bitmap.getWidth());
            int image_w = (int) (bitmap.getWidth() * scale);
            int image_h = (int) (bitmap.getHeight() * scale);
            Bitmap resize = Bitmap.createScaledBitmap(bitmap, image_w, image_h, true);
            resize.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            intent.putExtra("certificate", byteArray);

//            bitmap = overlay(bitmap);
            String ocrResult = processOcrFromBitmap(bitmap);
            Log.d("test", ocrResult);

            intent.putExtra("ocr", ocrResult);
            intent.putExtra("userID", userID);
            intent.putExtra("userName", userName);
            intent.putExtra("userBirthDate", userBirthDate);
            startActivity(intent);
            finish();
        }
    };

    public static Bitmap cropBitmap(Bitmap src) {
        if(src == null) {
            return null;
        }

        int width = src.getWidth();
        int height = src.getHeight();

        int x = (int)(width * 0.2);
        int y = (int)(height * 0.22);
        int cw = (int)(width * 0.6);
        int ch = (int)(height * 0.3);

        return Bitmap.createBitmap(src, x, y, cw, ch);
    }

    public static Bitmap resizeBitmap(Bitmap src, int max) {
        if(src == null) {
            return null;
        }

        int width = src.getWidth();
        int height = src.getHeight();

        float rate = max / (float) width;
        height = (int) (height * rate);
        width = max;

        return Bitmap.createScaledBitmap(src, width, height, true);
    }

    public static Bitmap overlay(Bitmap bitmap) {
        Bitmap white1 = getWhiteBitmap(bitmap, 0.45, 0.3);
        Bitmap white2 = getWhiteBitmap(bitmap, 1, 0.15);
        Bitmap white3 = getWhiteBitmap(bitmap, 1, 0.17);
        Bitmap white4 = getWhiteBitmap(bitmap, 0.24, 1);
        Bitmap white5 = getWhiteBitmap(bitmap, 1, 0.15);
        Bitmap white6 = getWhiteBitmap(bitmap, 0.5, 0.15);

        Bitmap bmOverlay = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bitmap, new Matrix(), null);
        canvas.drawBitmap(white1, 0, 0, null);
        canvas.drawBitmap(white2, 0, (int)(bitmap.getHeight() * 0.5), null);
        canvas.drawBitmap(white3, 0, bitmap.getHeight() - white3.getHeight(), null);
        canvas.drawBitmap(white4, 0, 0, null);
        canvas.drawBitmap(white5, 0, (int)(bitmap.getHeight() * 0.2), null);
        canvas.drawBitmap(white6, (int)(bitmap.getWidth() * 0.5), (int)(bitmap.getHeight() * 0.43), null);

        return bmOverlay;
    }

    private static Bitmap getWhiteBitmap(Bitmap source, double widthRatio, double heightRatio) {
        Bitmap white = Bitmap.createBitmap((int)(source.getWidth() * widthRatio), (int)(source.getHeight() * heightRatio), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(white);
        canvas.drawColor(Color.WHITE);
        return white;
    }

    private void checkFile(File dir) {
        //directory does not exist, but we can successfully create it
        if (!dir.exists()&& dir.mkdirs()){
            copyFiles();
        }
        //The directory exists, but there is no data file in it
        if(dir.exists()) {
            String datafilepath = dataPath + "/tessdata/kor.traineddata";
            File datafile = new File(datafilepath);
            if (!datafile.exists()) {
                copyFiles();
            }
        }
    }

    private void copyFiles() {
        try {
            //location we want the file to be at
            String filepath = dataPath + "/tessdata/kor.traineddata";

            //get access to AssetManager
            AssetManager assetManager = getAssets();

            //open byte streams for reading/writing
            InputStream instream = assetManager.open("tessdata/kor.traineddata");
            OutputStream outstream = new FileOutputStream(filepath);

            //copy the file to the location specified by filepath
            byte[] buffer = new byte[1024];
            int read;
            while ((read = instream.read(buffer)) != -1) {
                outstream.write(buffer, 0, read);
            }
            outstream.flush();
            outstream.close();
            instream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String processOcrFromBitmap(Bitmap image) {
        tess.setImage(image);
        return tess.getUTF8Text();
    }

    // surfaceholder 와 관련된 구현 내용
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            if (camera == null) {
                camera.setPreviewDisplay(holder);
                camera.startPreview();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // View 가 존재하지 않을 때
        if (cameraHolder.getSurface() == null) {
            return;
        }

        // 작업을 위해 잠시 멈춘다
        try {
            camera.stopPreview();
        } catch (Exception e) {
            // 에러가 나더라도 무시한다.
        }

        // 카메라 설정을 다시 한다.
        Camera.Parameters parameters = camera.getParameters();
        List<String> focusModes = parameters.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }
        camera.setParameters(parameters);

        // View 를 재생성한다.
        try {
            camera.setPreviewDisplay(cameraHolder);
            camera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback() {

        @Override
        public void onAutoFocus(boolean arg0, Camera arg1) {
            // TODO Auto-generated method stub
        }
    };
}