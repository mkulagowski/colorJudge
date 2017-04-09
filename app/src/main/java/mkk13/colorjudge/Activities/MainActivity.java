package mkk13.colorjudge.Activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import java.util.Arrays;
import java.util.Locale;

import mkk13.colorjudge.Adapters.TabsPagerAdapter;
import mkk13.colorjudge.ColorDatabase;
import mkk13.colorjudge.Fragments.MainSlideFragment;
import mkk13.colorjudge.R;
import mkk13.colorjudge.Utils;

/**
 * Created by mkk-1 on 05/04/2017.
 */

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ColorDatabase.setContext(this);

        String locale = getResources().getConfiguration().locale.getDisplayLanguage();
        if (locale.equals("polski")) {
            Utils.LANGUAGE = Utils.Language.POLISH;
        }

        TabsPagerAdapter.setTabsNames(Arrays.asList(getString(R.string.tab_guess), getString(R.string.tab_learn)));

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            MainSlideFragment fragment = new MainSlideFragment();
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }
    }
}
