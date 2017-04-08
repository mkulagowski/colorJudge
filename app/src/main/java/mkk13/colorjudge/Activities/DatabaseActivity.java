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
import mkk13.colorjudge.ColorUtils;
import mkk13.colorjudge.R;

/**
 * Created by mkk-1 on 31/03/2017.
 */

public class DatabaseActivity extends Activity implements AdapterView.OnItemClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dbpanel);
        ColorAdapter customAdapter = new ColorAdapter(this, R.layout.colortemplate);

        ListView list = (ListView) findViewById(R.id.listview);
        list.setAdapter(customAdapter);
        list.setOnItemClickListener(this);

        customAdapter.addAll(ColorUtils.getSortedColors(ColorDatabase.getInstance().getColors().values()));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(DatabaseActivity.this, DetailsActivity.class);
        TextView txt = (TextView) view.findViewById(R.id.name);
        intent.putExtra("colorId", txt.getText());
        startActivity(intent);
        overridePendingTransition(R.anim.slide_up, R.anim.stay);
    }
}
