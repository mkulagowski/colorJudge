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
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.camera.CropImageIntentBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import mkk13.colorjudge.Color;
import mkk13.colorjudge.ColorConversions;
import mkk13.colorjudge.ColorDatabase;
import mkk13.colorjudge.R;
import mkk13.colorjudge.Utils;

import static java.text.DateFormat.getDateTimeInstance;

/**
 * Created by mkk-1 on 12/04/2017.
 */

public class JudgeActivity extends Activity implements View.OnClickListener{
    private AutoCompleteTextView mTextEdit1, mTextEdit2;
    private Boolean mImageSet = false, mButtonSet = false;
    private Button mJudgeButton;
    private Uri mPicUri;
    private int mCurrentColorDest = 0;
    private String mCurrentPhotoPath;

    @Override
    public void onSaveInstanceState(Bundle bundle)
    {
        super.onSaveInstanceState(bundle);
        bundle.putString("fileName", mCurrentPhotoPath);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.judge_panel);

        findViewById(R.id.judge_capture_btn1).setOnClickListener(this);
        findViewById(R.id.judge_capture_btn2).setOnClickListener(this);

        mTextEdit1 = (AutoCompleteTextView) findViewById(R.id.judge_textedit1);
        Map<String, Color> cols = ColorDatabase.getInstance().getColors();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, cols.keySet().toArray(new String[cols.size()]));
        mTextEdit1.setAdapter(adapter);
        mTextEdit1.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                toggleButton();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence text, int start,
                                      int before, int count) {}
        });
        mTextEdit2 = (AutoCompleteTextView) findViewById(R.id.judge_textedit2);
        mTextEdit2.setAdapter(adapter);
        mTextEdit2.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                toggleButton();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {}
        });

        mJudgeButton = (Button) findViewById(R.id.judge_btn);
        mJudgeButton.setOnClickListener(this);
        mJudgeButton.setAlpha(0.33f);
        mJudgeButton.setClickable(false);

        if (savedInstanceState != null)
            mCurrentPhotoPath = savedInstanceState.getString("fileName");
        else {
            Uri picUri = getIntent().getParcelableExtra("photoUri");
            mCurrentPhotoPath = picUri.getPath();
        }

        Bitmap image = BitmapFactory.decodeFile(mCurrentPhotoPath);
        ImageView imageView = (ImageView) findViewById(R.id.judge_image);
        imageView.setImageBitmap(image);
        imageView.setAlpha(1f);

        image = Bitmap.createScaledBitmap(image, 1, 1, false);
        mCurrentColorDest = image.getPixel(0, 0);

        findViewById(R.id.judge_layout3).setBackgroundColor(mCurrentColorDest);
        mImageSet = true;
    }

    @Override
    public void onClick(View v) {
        toggleButton();

        switch (v.getId()) {
            case R.id.judge_capture_btn1:
            case R.id.judge_capture_btn2:
                promptSpeechInput();
                break;
            case R.id.judge_btn:
                Intent intent = new Intent(this, JudgeResolveActivity.class);
                intent.putExtra("color1", mTextEdit1.getText().toString());
                intent.putExtra("color2", mTextEdit2.getText().toString());
                intent.putExtra("colorDest", ColorConversions.int2hex(mCurrentColorDest).toUpperCase());
                startActivity(intent);
                break;
        }
    }

    private void toggleButton() {
        if (mTextEdit1.getText().length() > 0 && mTextEdit2.getText().length() > 0 && mImageSet) {
            if (!mButtonSet) {
                mJudgeButton.setAlpha(1f);
                mJudgeButton.setClickable(true);
                mButtonSet = true;
            }
        } else if (mButtonSet) {
            mJudgeButton.setAlpha(0.33f);
            mJudgeButton.setClickable(false);
            mButtonSet = false;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Utils.JUDGE_SOUND_CAPTURE_INTENT1:
                    if (data != null) {
                        ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        mTextEdit1.setText(result.get(0));
                    }
                    break;
                case Utils.JUDGE_SOUND_CAPTURE_INTENT2:
                    if (data != null) {
                        ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        mTextEdit2.setText(result.get(0));
                    }
                    break;
                case Utils.IMAGE_CAPTURE_INTENT:
                    promptCrop();
                    break;
                case Utils.IMAGE_CROP_INTENT:
                    Bitmap image = BitmapFactory.decodeFile(mCurrentPhotoPath);
                    ImageView imageView = (ImageView) findViewById(R.id.judge_image);
                    imageView.setImageBitmap(image);
                    imageView.setAlpha(1f);

                    image = Bitmap.createScaledBitmap(image, 1, 1, false);
                    mCurrentColorDest = image.getPixel(0, 0);

                    findViewById(R.id.judge_layout3).setBackgroundColor(mCurrentColorDest);
                    mImageSet = true;
                    toggleButton();
                    //String hexVal = ColorConversions.int2hex(imageColor);
                    //((TextView) findViewById(R.id.hex_searchtext)).setText(hexVal.toUpperCase());
                    break;
            }
        }
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Utils.LANGUAGE.getLocale());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, Utils.SOUND_CAPTURE_INTENT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT
            ).show();
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