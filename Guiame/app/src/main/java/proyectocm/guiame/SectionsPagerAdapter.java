package proyectocm.guiame;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.Menu;

import java.util.Locale;

import static android.provider.Settings.Global.getString;


/**
 * Created by Usuario on 30/09/2015.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment =new Fragment();
        Bundle args = new Bundle();
        switch(position) {
            case 0:
                fragment = new favoritosFragment();
                args.putInt(favoritosFragment.ARG_SECTION_NUMBER,position+1);
                break;
            case 1:
                fragment = new famososFragment();
                args.putInt(favoritosFragment.ARG_SECTION_NUMBER, position+1);
                break;
            default:
                break;
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    public CharSequence getPageTitle(int position) {
        if (position==0)
            return "Favoritos";
        else
            return "Famosos";
    }




}
