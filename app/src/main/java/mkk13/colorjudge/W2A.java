package mkk13.colorjudge;

import android.app.Activity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class W2A extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final SeekBar red = (SeekBar) findViewById(R.id.seekBarR);
        final SeekBar green = (SeekBar) findViewById(R.id.seekBarG);
        final SeekBar blue = (SeekBar) findViewById(R.id.seekBarB);
        final ListView res = (ListView) findViewById(R.id.listview);
        final LinearLayout lo1 = (LinearLayout) findViewById(R.id.linearLayout);
        final LinearLayout lo2 = (LinearLayout) findViewById(R.id.linearLayout2);
        final ColorAdapter customAdapter = new ColorAdapter(this, R.layout.colortemplate);
        res .setAdapter(customAdapter);
        final HashMap<String, Color> colors;
        {
            JSONParser parser = new JSONParser();
            Object obj = new Object();

            try {
                AssetManager assetManager = getApplicationContext().getAssets();
                InputStream file = assetManager.open("colors.json");
                InputStreamReader fileReader = new InputStreamReader(file);
                //System.out.print(fs);
                obj = parser.parse(fileReader);
            } catch (Exception e) {
                e.printStackTrace();
            }
            JSONArray jsonObject = (JSONArray) obj;

            HashMap<String, Color> colMap = new HashMap<>();

            for (Object i : jsonObject) {
                JSONObject jo = (JSONObject) i;
                Color col = new Color(jo);
                colMap.put(col.name_pl, col);
            }
            colors = colMap;
        }


        //ImageView androidImage = (ImageView) findViewById(R.id.android);
        //androidImage.setBackgroundResource(R.drawable.android_animate);
        //androidAnimation = (AnimationDrawable) androidImage.getBackground();
        //final Button btnAnimate = (Button) findViewById(R.id.animate);
        SeekBar.OnSeekBarChangeListener ocl;
        ocl = new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar s, int progress, boolean fromUser)
            {
                int rVal = red.getProgress();
                int gVal = green.getProgress();
                int bVal = blue.getProgress();
                String hexVal = "#" + Integer.toHexString(0x100 | rVal).substring(1) + Integer.toHexString(0x100 | gVal).substring(1) + Integer.toHexString(0x100 | bVal).substring(1);
                Integer colVal = android.graphics.Color.parseColor(hexVal);
                lo1.setBackgroundColor(colVal);
                lo2.setBackgroundColor(colVal);
            }

            @Override
            public void onStartTrackingTouch(SeekBar bar)
            {}

            @Override
            public void onStopTrackingTouch(SeekBar bar)
            {
                int rVal = red.getProgress();
                int gVal = green.getProgress();
                int bVal = blue.getProgress();
                String hexVal = "#" + Integer.toHexString(0x100 | rVal).substring(1) + Integer.toHexString(0x100 | gVal).substring(1) + Integer.toHexString(0x100 | bVal).substring(1);
                Color search = new Color(hexVal, "?", "?");
                ArrayList<Score> results = Comparator.findColors(search, colors.values());
                customAdapter.clear();
                customAdapter.addAll(results.subList(0, 13));
            }
        };
        red.setOnSeekBarChangeListener(ocl);
        green.setOnSeekBarChangeListener(ocl);
        blue.setOnSeekBarChangeListener(ocl);
    }
}