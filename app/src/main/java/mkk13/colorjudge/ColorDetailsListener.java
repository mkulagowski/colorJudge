package mkk13.colorjudge;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import mkk13.colorjudge.Activities.DetailsActivity;

/**
 * Created by mkk-1 on 09/04/2017.
 */

public class ColorDetailsListener implements AdapterView.OnItemClickListener {
    Activity mCurrentActivity;

    public ColorDetailsListener(Activity currActivity){
        mCurrentActivity = currActivity;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(mCurrentActivity, DetailsActivity.class);
        TextView txt = (TextView) view.findViewById(R.id.name);
        intent.putExtra("colorId", txt.getText());
        mCurrentActivity.startActivity(intent);
        mCurrentActivity.overridePendingTransition(R.anim.slide_up, R.anim.stay);
    }
}
