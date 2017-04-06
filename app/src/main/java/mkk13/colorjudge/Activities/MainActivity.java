package mkk13.colorjudge.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import mkk13.colorjudge.ColorBase;
import mkk13.colorjudge.Fragments.MainSlideFragment;
import mkk13.colorjudge.R;

/**
 * Created by mkk-1 on 05/04/2017.
 */

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ColorBase.setContext(this);
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            MainSlideFragment fragment = new MainSlideFragment();
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }
    }
}
