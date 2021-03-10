package de.codingforce.wad.fragment.adapter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import de.codingforce.wad.activity.MainActivity;
import de.codingforce.wad.fragment.tab.Dishes_Today;
import de.codingforce.wad.fragment.tab.Open_Shoppinglists;

/**
 * Adapter class to retrieve fragments for tabs
 */
public class TabFragmentAdapter extends FragmentStateAdapter {
    private static final String LOG_TAG = "TabFragmentAdapter";
    private final int numOfTabs;
    private final MainActivity activity;

    /**
     * Default constructor
     * @param fragmentManager fragmentManager
     * @param lifecycle lifecycle
     * @param activity activity
     */
    public TabFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, MainActivity activity) {
        super(fragmentManager, lifecycle);
        numOfTabs = 3;
        this.activity = activity;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Log.e(LOG_TAG, "-- createFragment --");
        switch (position) {
            case 0:
                // Wünsche heute
                Log.e(LOG_TAG, "position:"+position+", creating "+Dishes_Today.class.getSimpleName());
                return new Dishes_Today();
            /*case 1:
                //Einkauflisten
                Log.e(LOG_TAG, "position:"+position+", creating "+Open_Shoppinglists.class.getSimpleName());
                return new Dishes_Today();*/
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return numOfTabs;
    }
}
