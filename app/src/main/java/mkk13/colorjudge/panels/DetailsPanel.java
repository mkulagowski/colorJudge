package mkk13.colorjudge.panels;

import android.app.Activity;
import android.os.Bundle;
import android.text.Layout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mkk13.colorjudge.Color;
import mkk13.colorjudge.ColorBase;
import mkk13.colorjudge.ColorConversions;
import mkk13.colorjudge.R;

/**
 * Created by mkk-1 on 31/03/2017.
 */

public class DetailsPanel extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.detailspanel);
        Color col = ColorBase.getInstance().colors.get(getIntent().getStringExtra("colorId"));


        TextView colName = (TextView) findViewById(R.id.color_name);
        colName.setText(col.name_pl);

        LinearLayout colLayout = (LinearLayout) findViewById(R.id.color_layout);
        colLayout.setBackgroundColor(ColorConversions.hex2int(col.hex));

        setDetail(R.id.details_hex, "Hex", col.getHexStringDetails());
        setDetail(R.id.details_rgb, "RGB", col.getRgbStringDetails());
        setDetail(R.id.details_lab, "CIE*Lab", col.getLabStringDetails());
        setDetail(R.id.details_xyz, "XYZ", col.getXyzStringDetails());
        setDetail(R.id.details_hsv, "XYZ", col.getHsvStringDetails());
    }

    private void setDetail(int layoutId, String name, String[] vals) {
        LinearLayout detailHex = (LinearLayout) findViewById(layoutId);
        TextView detailName = (TextView) detailHex.findViewById(R.id.detail_name);
        List<TextView> textViews = new ArrayList<>();
        textViews.add((TextView) detailHex.findViewById(R.id.detail_val1));
        textViews.add((TextView) detailHex.findViewById(R.id.detail_val2));
        textViews.add((TextView) detailHex.findViewById(R.id.detail_val3));

        detailName.setText(name);
        for (int i = 0; i < 3; i++) {
            textViews.get(i).setText(vals[i]);
        }
    }
}
