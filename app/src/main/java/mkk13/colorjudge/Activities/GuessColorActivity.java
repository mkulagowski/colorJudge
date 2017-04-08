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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.camera.CropImageIntentBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mkk13.colorjudge.Adapters.ScoreAdapter;
import mkk13.colorjudge.Color;
import mkk13.colorjudge.ColorDatabase;
import mkk13.colorjudge.ColorConversions;
import mkk13.colorjudge.ColorUtils;
import mkk13.colorjudge.R;
import mkk13.colorjudge.Score;
import mkk13.colorjudge.Utils;

import static java.text.DateFormat.getDateTimeInstance;


/**
 * Created by mkk-1 on 26/03/2017.
 */

public class GuessColorActivity extends Activity implements View.OnClickListener {
    private Uri mPicUri;
    private ScoreAdapter mCustomAdapter;
    private String mCurrentPhotoPath;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.colorpanel);
        mCustomAdapter = new ScoreAdapter(this, R.layout.scoretemplate);

        ListView list = (ListView) findViewById(R.id.listview);
        list.setAdapter(mCustomAdapter);

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
            switch(requestCode) {
                case Utils.IMAGE_CAPTURE_INTENT:
                    promptCrop();
                    break;
                case Utils.IMAGE_GALLERY_INTENT:
                    mPicUri = data.getData();
                    mCurrentPhotoPath = mPicUri.getPath();
                    promptCrop();
                    break;
                case Utils.IMAGE_CROP_INTENT:
                    Bitmap image = BitmapFactory.decodeFile(mCurrentPhotoPath);
                    ImageView imageView = (ImageView) findViewById(R.id.imageView);
                    imageView.setImageBitmap(image);

                    image = Bitmap.createScaledBitmap(image, 1, 1, false);
                    int imageColor = image.getPixel(0,0);

                    findViewById(R.id.search_layout).setBackgroundColor(imageColor);

                    String hexVal = ColorConversions.int2hex(imageColor);
                    ((TextView) findViewById(R.id.hex_searchtext)).setText(hexVal.toUpperCase());

                    Color search = new Color(hexVal, "?", "?", "");
                    ArrayList<Score> results = ColorUtils.getMatchingColors(search, ColorDatabase.getInstance().getColors().values());
                    mCustomAdapter.clear();
                    mCustomAdapter.addAll(results.subList(0, Utils.COLORS_IN_LIST));
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
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = createImageFile();
            if (photoFile != null) {
                mPicUri = Uri.fromFile(photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mPicUri);
                startActivityForResult(takePictureIntent, Utils.IMAGE_CAPTURE_INTENT);
            }
        }
    }

    private void promptImageGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        try {
            startActivityForResult(Intent.createChooser(intent, getString(R.string.camera_chooser)),
                                                        Utils.IMAGE_GALLERY_INTENT);
        }
        catch(ActivityNotFoundException ex){
            Toast.makeText(this, getString(R.string.gallery_not_supported), Toast.LENGTH_SHORT).show();
        }
    }

    private void promptCrop(){
        Uri srcUri = mPicUri;
        Uri dstUri = Uri.fromFile(createImageFile());
        CropImageIntentBuilder cropImage = new CropImageIntentBuilder(64, 64, dstUri);
        cropImage.setOutlineColor(0xFF03A9F4);
        cropImage.setSourceImage(srcUri);

        try {
            startActivityForResult(cropImage.getIntent(this), Utils.IMAGE_CROP_INTENT);
        }
        catch(ActivityNotFoundException ex){
            Toast.makeText(this, getString(R.string.crop_not_supported), Toast.LENGTH_SHORT).show();
        }
    }

    private File createImageFile() {
        File image = null;
        String timeStamp = getDateTimeInstance().toString();
                //new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException ex) {
            Toast.makeText(this, getString(R.string.image_file_create_error), Toast.LENGTH_SHORT).show();
        }
        if (image != null) {
            mCurrentPhotoPath = image.getAbsolutePath();
        }
        return image;
    }
}