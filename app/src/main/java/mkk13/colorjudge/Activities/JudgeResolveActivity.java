package mkk13.colorjudge.Activities;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import mkk13.colorjudge.Color;
import mkk13.colorjudge.ColorConversions;
import mkk13.colorjudge.ColorDatabase;
import mkk13.colorjudge.ColorUtils;
import mkk13.colorjudge.R;


/**
 * Created by mkk-1 on 14/04/2017.
 */

public class JudgeResolveActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.judge_resolve_panel);

        String col1 = getIntent().getStringExtra("color1");
        String col2 = getIntent().getStringExtra("color2");
        String colDest = getIntent().getStringExtra("colorDest");
        findViewById(R.id.judge_layout2).setBackgroundColor(ColorConversions.hex2int(colDest));
        Color col11 = ColorUtils.getMatchingNames(col1, ColorDatabase.getInstance().getColors().values()).get(0).mColor;
        Color col22 = ColorUtils.getMatchingNames(col2, ColorDatabase.getInstance().getColors().values()).get(0).mColor;

        findViewById(R.id.judge_layout1).setBackgroundColor(ColorConversions.hex2int(col11.getHex()));
        ((TextView)findViewById(R.id.judge_colortext1)).setText(col11.getName());
        ((TextView)findViewById(R.id.judge_colortext1)).setTextColor(ColorConversions.hex2int(ColorConversions.hex2invert(col11.getHex())));
        float[] labDest = ColorConversions.rgb2lab(ColorConversions.hex2rgb(colDest));
        int dist1 = ColorUtils.deltaEInt(col11.getLAB(), labDest);
        ((TextView)findViewById(R.id.judge_colorscore1)).setText(String.valueOf(dist1));
        ((TextView)findViewById(R.id.judge_colorscore1)).setTextColor(ColorConversions.hex2int(ColorConversions.hex2invert(col11.getHex())));

        findViewById(R.id.judge_layout3).setBackgroundColor(ColorConversions.hex2int(col22.getHex()));
        ((TextView)findViewById(R.id.judge_colortext2)).setText(col22.getName());
        ((TextView)findViewById(R.id.judge_colortext2)).setTextColor(ColorConversions.hex2int(ColorConversions.hex2invert(col22.getHex())));
        labDest = ColorConversions.rgb2lab(ColorConversions.hex2rgb(colDest));
        int dist2 = ColorUtils.deltaEInt(col22.getLAB(), labDest);
        ((TextView)findViewById(R.id.judge_colorscore2)).setText(String.valueOf(dist2));
        ((TextView)findViewById(R.id.judge_colorscore2)).setTextColor(ColorConversions.hex2int(ColorConversions.hex2invert(col22.getHex())));

        LinearLayout.LayoutParams looserParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.5f);

        if (dist1 > dist2) {
            findViewById(R.id.crown11).setVisibility(View.VISIBLE);
            findViewById(R.id.crown12).setVisibility(View.VISIBLE);

            ((AnimationDrawable)findViewById(R.id.crown11).getBackground()).start();
            ((AnimationDrawable)findViewById(R.id.crown12).getBackground()).start();

            findViewById(R.id.judge_layout2).setLayoutParams(looserParams);
            findViewById(R.id.judge_layout3).setLayoutParams(looserParams);
        } else {
            findViewById(R.id.crown21).setVisibility(View.VISIBLE);
            findViewById(R.id.crown22).setVisibility(View.VISIBLE);

            ((AnimationDrawable)findViewById(R.id.crown21).getBackground()).start();
            ((AnimationDrawable)findViewById(R.id.crown22).getBackground()).start();

            findViewById(R.id.judge_layout1).setLayoutParams(looserParams);
            findViewById(R.id.judge_layout2).setLayoutParams(looserParams);
        }
    }
}
