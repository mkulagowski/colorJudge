package mkk13.colorjudge.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import java.util.Arrays;
import java.util.List;

import mkk13.colorjudge.Fragments.GuessMainFragment;
import mkk13.colorjudge.Fragments.LearnMainFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    private static final List<Class<? extends Fragment>> tabs = Arrays.<Class<? extends Fragment>>asList(GuessMainFragment.class, LearnMainFragment.class);
    private static final int tabCount = tabs.size();
    private static final List<String> tabNames = Arrays.asList( "Guess", "Learn" );
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {
        try {
            if (index < tabCount) {
                return tabs.get(index).getConstructor().newInstance();
            }
        } catch (Exception ex) {
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabNames.get(position);
    }


}