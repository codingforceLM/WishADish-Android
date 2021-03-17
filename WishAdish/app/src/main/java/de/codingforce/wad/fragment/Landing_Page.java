package de.codingforce.wad.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import de.codeingforce.wad.R;
import de.codingforce.wad.activity.MainActivity;
import de.codingforce.wad.fragment.adapter.TabFragmentAdapter;

public class Landing_Page extends NameAwareFragment {

    private static final String LOG_TAG = "Landing Page";
    private final String[] tabTexts = new String[]{"Gerichte Heute", "Aktive Einkaufslisten"};

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private TabLayoutMediator mediator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        MainActivity.main.change_title("Startseite");
        Log.e(LOG_TAG, "--onCreatedView--");
        return inflater.inflate(R.layout.fragment_landing_page, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.e(LOG_TAG, "--onViewCreated--");

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager2 = view.findViewById(R.id.landing_page_pager);

        TabFragmentAdapter adapter = new TabFragmentAdapter(getChildFragmentManager(), this.getLifecycle(), (MainActivity) getActivity());
        viewPager2.setAdapter(adapter);

        TabLayoutMediator.TabConfigurationStrategy strategy = new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                // stub
            }
        };

        mediator = new TabLayoutMediator(tabLayout, viewPager2, strategy);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.e(LOG_TAG, "--onTabSelected--");
                MainActivity activity = (MainActivity) getActivity();
                switch (tab.getPosition()) {
                    case 0:
                        // Songs
                        Log.e(LOG_TAG, "set current tab: Dishes_Today");
                        //activity.setCurrentTab(MainActivity.TAB_SONGS);
                        activity.invalidateOptionsMenu();
                        break;
                    case 1:
                        // Albums
                        Log.e(LOG_TAG, "set current tab: Open_Shoppinglists");
                        //activity.setCurrentTab(MainActivity.TAB_ALBUMS);
                        activity.invalidateOptionsMenu();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mediator.attach();
        this.initializeTabTexts();
    }

    private void initializeTabTexts() {
        for (int i = 0; i < tabTexts.length; i++) {
            tabLayout.getTabAt(i).setText(tabTexts[i]);
        }
    }
}
