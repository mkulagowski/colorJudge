package mkk13.colorjudge.Activities;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.camera.CropImageIntentBuilder;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mkk13.colorjudge.Adapters.ScoreAdapter;
import mkk13.colorjudge.Color;
import mkk13.colorjudge.ColorBase;
import mkk13.colorjudge.ColorConversions;
import mkk13.colorjudge.Comparator;
import mkk13.colorjudge.R;
import mkk13.colorjudge.Score;
import mkk13.colorjudge.Utils;


/**
 * Created by mkk-1 on 26/03/2017.
 */

public class ColorPanel extends Activity implements View.OnClickListener {
    private Uri picUri;
    private ScoreAdapter customAdapter;
    private String mCurrentPhotoPath;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.colorpanel);
        customAdapter = new ScoreAdapter(this, R.layout.scoretemplate);

        ListView list = (ListView) findViewById(R.id.listview);
        list.setAdapter(customAdapter);

        List<Button> buttonList = new ArrayList<>();
        buttonList.add((Button) findViewById(R.id.image_capture_btn));
        buttonList.add((Button) findViewById(R.id.gallery_btn));
        for (Button btn : buttonList) {
            btn.setOnClickListener(this);
        }
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.image_capture_btn:
                promptImageCapture();
                break;
            case R.id.gallery_btn:
                promptImageGallery();
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            //File croppedImageFile = new File(getFilesDir(), "test.jpg");
            //user is returning from capturing an image using the camera
            switch(requestCode) {
                case Utils.IMAGE_CAPTURE_INTENT:
                    performCrop();
                    break;
                case Utils.IMAGE_GALLERY_INTENT:
                    picUri = data.getData();
                    mCurrentPhotoPath = picUri.getPath();
                    performCrop();
                    break;
                case Utils.IMAGE_CROP_INTENT:
                    //Bundle extras = data.getExtras();
                    Bitmap image = BitmapFactory.decodeFile(mCurrentPhotoPath);//extras.getParcelable("data");
                    ImageView imageView = (ImageView) findViewById(R.id.imageView);
                    imageView.setImageBitmap(image);

                    image = Bitmap.createScaledBitmap(image, 1, 1, false);
                    int pixel = image.getPixel(0,0);

                    LinearLayout layout = (LinearLayout) findViewById(R.id.search_layout);
                    layout.setBackgroundColor(pixel);

                    String hexVal = ColorConversions.int2hex(pixel);
                    TextView hex = (TextView) findViewById(R.id.hex_searchtext);
                    hex.setText(hexVal.toUpperCase());

                    Color search = new Color(hexVal, "?", "?");
                    ArrayList<Score> results = Comparator.findColors(search, ColorBase.getInstance().colors.values());
                    customAdapter.clear();
                    customAdapter.addAll(results.subList(0, Utils.COLORS_IN_LIST));
                    break;
            }
        }
    }

    protected void promptImageCapture() {
        PackageManager packageManager = getPackageManager();
        if(packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA) == false){
            Toast.makeText(this, getString(R.string.camera_not_supported), Toast.LENGTH_SHORT).show();
            return;
        }

        // Camera exists? Then proceed...
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go.
            // If you don't do this, you may get a crash in some devices.
            File photoFile = createImageFile();
            // Continue only if the File was successfully created
            if (photoFile != null) {
                picUri = Uri.fromFile(photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
                startActivityForResult(takePictureIntent, Utils.IMAGE_CAPTURE_INTENT);
            }
        }
    }

    private void promptImageGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        try {
            startActivityForResult(Intent.createChooser(intent, "Select picture"), Utils.IMAGE_GALLERY_INTENT);
        }
        catch(ActivityNotFoundException ex){
            Toast.makeText(this, "Gallery not supported? waat?", Toast.LENGTH_SHORT).show();
        }
    }

    private void performCrop(){
        Uri oldUri = picUri;
        CropImageIntentBuilder cropImage = new CropImageIntentBuilder(64, 64, Uri.fromFile(createImageFile()));
        cropImage.setOutlineColor(0xFF03A9F4);
        cropImage.setSourceImage(oldUri);

        try {
            startActivityForResult(cropImage.getIntent(this), Utils.IMAGE_CROP_INTENT);
        }
        catch(ActivityNotFoundException ex){
            Toast.makeText(this, getString(R.string.crop_not_supported), Toast.LENGTH_SHORT).show();
        }
    }

    private File createImageFile() {//} throws IOException {
        // Create an image file name
        File image = null;
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            //File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
            //File image = new File(storageDir, imageFileName+System.currentTimeMillis()+".jpg");
        } catch (IOException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        if (image != null) {
            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = image.getAbsolutePath();
        }
        return image;
    }
}