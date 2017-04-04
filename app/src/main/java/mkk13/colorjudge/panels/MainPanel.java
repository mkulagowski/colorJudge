package mkk13.colorjudge.panels;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import mkk13.colorjudge.ColorBase;
import mkk13.colorjudge.DbPanel;
import mkk13.colorjudge.R;

public class MainPanel extends Activity implements View.OnClickListener
{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpanel);
        ColorBase.setContext(getApplicationContext());
        /*final SeekBar red = (SeekBar) findViewById(R.id.seekBarR);
        final SeekBar green = (SeekBar) findViewById(R.id.seekBarG);
        final SeekBar blue = (SeekBar) findViewById(R.id.seekBarB);
        final ListView res = (ListView) findViewById(R.id.listview);
        final LinearLayout lo1 = (LinearLayout) findViewById(R.id.linearLayout);
        final LinearLayout lo2 = (LinearLayout) findViewById(R.id.linearLayout2);
        final ColorAdapter customAdapter = new ColorAdapter(this, R.layout.scoretemplate);*/
        List<Button> buttonList = new ArrayList<>();
        buttonList.add((Button) findViewById(R.id.act_button_color));
        buttonList.add((Button) findViewById(R.id.act_button_name));
        buttonList.add((Button) findViewById(R.id.act_button_judge));
        buttonList.add((Button) findViewById(R.id.act_button_db));

        for (Button btn : buttonList) {
            btn.setOnClickListener(this);
        }
        /*
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
                ArrayList<Score> results = Comparator.findColors(search, ColorBase.getInstance().colors.values());
                customAdapter.clear();
                customAdapter.addAll(results.subList(0, 13));
            }
        };
        red.setOnSeekBarChangeListener(ocl);
        green.setOnSeekBarChangeListener(ocl);
        blue.setOnSeekBarChangeListener(ocl);
        */
    }

    @Override
    public void onClick(View v) {
        Class intentClass = null;
        switch(v.getId()){
            case(R.id.act_button_color):
                intentClass = ColorPanel.class;
                break;
            case(R.id.act_button_name):
                intentClass = NamePanel.class;
                break;
            case(R.id.act_button_judge):
                break;
            case(R.id.act_button_db):
                intentClass = DbPanel.class;
                break;
        }

        if (intentClass != null) {
            Intent myIntent = new Intent(MainPanel.this, intentClass);
            MainPanel.this.startActivity(myIntent);
        }
    }


}