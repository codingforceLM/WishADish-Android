package de.codingforce.wad.activity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

import de.codeingforce.wad.R;
import de.codingforce.wad.fragment.Calendar;
import de.codingforce.wad.fragment.CreatWish;
import de.codingforce.wad.fragment.Dishes;
import de.codingforce.wad.fragment.Groups;
import de.codingforce.wad.fragment.NameAwareFragment;
import de.codingforce.wad.fragment.OnManualDetachListener;
import de.codingforce.wad.fragment.Landing_Page;
import de.codingforce.wad.fragment.Shoppinglists;
import de.codingforce.wad.fragment.ingredients;
import de.codingforce.wad.fragment.tab.Dishes_Today;
import de.codingforce.wad.fragment.tab.Open_Shoppinglists;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity";

    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;

    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // This will display an Up icon (<-), we will replace it with hamburger later
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        // ...From section above...
        // Find our drawer view
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        // Setup drawer view
        setupDrawerContent(nvDrawer);

        //Startseite
        Class Landing_Page = CreatWish.class;
        placeFragment(Landing_Page, R.id.mainFrame);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * places an instance of a given Fragment.class on a given layout id
     * @param fragmentClass FragmentClass to display
     * @param frameId       layoutFrame to place Fragment on
     */
    public void placeFragment(Class fragmentClass, int frameId) {
        Log.e(LOG_TAG, "--placeFragment--");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // detach fragments
        String simpleName = fragmentClass.getSimpleName();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment f : fragments) {
                if (f != null && !f.isDetached()) {
                    fragmentTransaction.detach(f);
                    Log.e(LOG_TAG, f.getClass().getSimpleName() + " detatched");
                    if (f instanceof OnManualDetachListener) {
                        Log.e(LOG_TAG, "Calling onManualDetach");
                        ((OnManualDetachListener) f).onManualDetach();
                    }
                    Log.e(LOG_TAG, f.getClass().getSimpleName() + " detatched");
                }

            }
        }

        // add/attach fragments
        if (fragmentManager.findFragmentByTag(simpleName) != null) {
            fragmentTransaction.attach(fragmentManager.findFragmentByTag(simpleName));
            Log.e(LOG_TAG, simpleName + " attached");
        } else {
            Fragment fragment = null;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                Log.e(LOG_TAG, "Exception creating Fragment instance\n" + e.getMessage());
            }
            fragmentTransaction.add(frameId, fragment, simpleName);
            Log.e(LOG_TAG, simpleName + " added");
        }
        fragmentTransaction.addToBackStack(simpleName);
        fragmentTransaction.commit();
    }

    /**
     * places a given Fragment on a given layout id
     * @param fragment Fragment to display
     * @param frameId  layoutFrame to place Fragment on
     */
    public void placeFragment(Fragment fragment, int frameId) {
        Log.e(LOG_TAG, "--placeFragment--");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // detach fragments
        String simpleName = fragment.getClass().getSimpleName();
        if (fragment instanceof NameAwareFragment) {
            simpleName = ((NameAwareFragment) fragment).getFragmentname();
        }
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment f : fragments) {
                if (f != null && !f.isDetached())
                    fragmentTransaction.detach(f);
                Log.e(LOG_TAG, f.getClass().getSimpleName() + " detatched");
            }
        }

        // add/attach fragments
        if (fragmentManager.findFragmentByTag(simpleName) != null) {
            Fragment f = fragmentManager.findFragmentByTag(simpleName);
            fragmentTransaction.attach(f);
            Log.e(LOG_TAG, simpleName + " attached");
        } else {
            fragmentTransaction.add(frameId, fragment, simpleName);
            Log.e(LOG_TAG, simpleName + " added");
        }
        fragmentTransaction.addToBackStack(simpleName);
        fragmentTransaction.commit();
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.nav_landing_page:
                fragmentClass = Landing_Page.class;
                break;
            case R.id.nav_calender:
                fragmentClass = Calendar.class;
                break;
            case R.id.nav_shoppinglist:
                fragmentClass = Shoppinglists.class;
                break;
            case R.id.nav_dishes:
                fragmentClass = Dishes.class;
                break;
            case R.id.nav_ingredients:
                fragmentClass = ingredients.class;
                break;
            case R.id.nav_groups:
                fragmentClass = Groups.class;
                break;
            default:
                fragmentClass = Landing_Page.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        Log.e(LOG_TAG, fragmentClass.getSimpleName() + " selected");
        placeFragment(fragmentClass, R.id.mainFrame);

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }
}