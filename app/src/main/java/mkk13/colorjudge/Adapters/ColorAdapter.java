package mkk13.colorjudge.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

import mkk13.colorjudge.Color;
import mkk13.colorjudge.ColorConversions;
import mkk13.colorjudge.R;
import mkk13.colorjudge.Score;


/**
 * Created by mkk-1 on 26/03/2017.
 */

public class ColorAdapter extends ArrayAdapter<Score> {

    public ColorAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ColorAdapter(Context context, int resource, List<Score> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.colortemplate, null);
        }

        Score score = getItem(position);

        if (score != null) {
            Color col = score.mColor;
            TextView hex = (TextView) v.findViewById(R.id.hex);
            TextView name = (TextView) v.findViewById(R.id.name);

            if (hex != null && name != null) {
                hex.setText(col.getHex());
                name.setText(col.getName());

                Integer colVal = ColorConversions.hex2int(col.getHex());

                hex.setBackgroundColor(colVal);
                name.setBackgroundColor(colVal);
            }
        }

        return v;
    }

}