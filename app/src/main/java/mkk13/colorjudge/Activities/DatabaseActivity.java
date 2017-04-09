package mkk13.colorjudge.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import mkk13.colorjudge.Adapters.ColorAdapter;
import mkk13.colorjudge.ColorDatabase;
import mkk13.colorjudge.ColorDetailsListener;
import mkk13.colorjudge.ColorUtils;
import mkk13.colorjudge.R;

/**
 * Created by mkk-1 on 31/03/2017.
 */

public class DatabaseActivity extends Activity  {

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dbpanel);
        ColorAdapter customAdapter = new ColorAdapter(this, R.layout.colortemplate);

        ListView list = (ListView) findViewById(R.id.listview);
        list.setAdapter(customAdapter);
        list.setOnItemClickListener(new ColorDetailsListener(this));

        customAdapter.addAll(ColorUtils.getSortedColors(ColorDatabase.getInstance().getColors().values()));
    }


}
