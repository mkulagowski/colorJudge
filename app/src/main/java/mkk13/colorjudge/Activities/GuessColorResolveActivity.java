package mkk13.colorjudge.Activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import mkk13.colorjudge.Adapters.ScoreAdapter;
import mkk13.colorjudge.Color;
import mkk13.colorjudge.ColorConversions;
import mkk13.colorjudge.ColorDatabase;
import mkk13.colorjudge.ColorDetailsListener;
import mkk13.colorjudge.ColorUtils;
import mkk13.colorjudge.R;
import mkk13.colorjudge.Score;
import mkk13.colorjudge.Utils;

import static java.text.DateFormat.getDateTimeInstance;


/**
 * Created by mkk-1 on 26/03/2017.
 */

public class GuessColorResolveActivity extends Activity {
    private ScoreAdapter mCustomAdapter;
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
        setContentView(R.layout.guess_color_resolve_panel);

        mCustomAdapter = new ScoreAdapter(this, R.layout.score_adapter);

        ListView list = (ListView) findViewById(R.id.listview);
        list.setAdapter(mCustomAdapter);
        list.setOnItemClickListener(new ColorDetailsListener(this));

        if (savedInstanceState != null)
            mCurrentPhotoPath = savedInstanceState.getString("fileName");
        else {
            Uri picUri = getIntent().getParcelableExtra("photoUri");
            mCurrentPhotoPath = picUri.getPath();
        }

        calculateColors();
    }

    private void calculateColors() {
        Bitmap image = BitmapFactory.decodeFile(mCurrentPhotoPath);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageBitmap(image);
        imageView.setAlpha(1f);

        image = Bitmap.createScaledBitmap(image, 1, 1, false);
        int imageColor = image.getPixel(0,0);

        findViewById(R.id.search_layout).setBackgroundColor(imageColor);

        String hexVal = ColorConversions.int2hex(imageColor);
        ((TextView) findViewById(R.id.hex_searchtext)).setText(hexVal.toUpperCase());

        Color search = new Color(hexVal, "?", "?", "");
        ArrayList<Score> results = ColorUtils.getMatchingColors(search, ColorDatabase.getInstance().getColors().values());
        mCustomAdapter.clear();
        mCustomAdapter.addAll(results.subList(0, Utils.COLORS_IN_LIST));
    }
}