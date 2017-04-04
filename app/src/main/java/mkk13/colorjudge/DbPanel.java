package mkk13.colorjudge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import mkk13.colorjudge.panels.DetailsPanel;

/**
 * Created by mkk-1 on 31/03/2017.
 */

public class DbPanel extends Activity implements AdapterView.OnItemClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dbpanel);
        ColorAdapter customAdapter = new ColorAdapter(this, R.layout.colortemplate);

        ListView list = (ListView) findViewById(R.id.listview);
        list.setAdapter(customAdapter);
        list.setOnItemClickListener(this);

        customAdapter.addAll(Comparator.getColors(ColorBase.getInstance().colors.values()));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(DbPanel.this, DetailsPanel.class);
        TextView txt = (TextView) view.findViewById(R.id.name);
        intent.putExtra("colorId", txt.getText());
        startActivity(intent);
    }
}
