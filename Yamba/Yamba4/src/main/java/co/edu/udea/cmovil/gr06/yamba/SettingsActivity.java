package co.edu.udea.cmovil.gr06.yamba;

import android.os.Bundle;

/**
 * Created by Usuario on 29/09/2015.
 */
public class SettingsActivity extends SubActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if this activity was created before
        if (savedInstanceState == null) {
            // Create a fragment
            SettingFragment fragment = new SettingFragment();
            getFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, fragment,
                            fragment.getClass().getSimpleName()).commit();
        }
    };
}

