package de.codingforce.wad.activity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import java.util.Date;
import java.util.List;

import de.codeingforce.wad.R;
import de.codingforce.wad.fragment.User;
import de.codingforce.wad.fragment.Calendar;
import de.codingforce.wad.fragment.Dishes;
import de.codingforce.wad.fragment.Groups;
import de.codingforce.wad.fragment.Login;
import de.codingforce.wad.fragment.NameAwareFragment;
import de.codingforce.wad.fragment.OnManualDetachListener;
import de.codingforce.wad.fragment.LandingPage;
import de.codingforce.wad.fragment.Shoppinglists;
import de.codingforce.wad.fragment.Ingredients;
import de.codingforce.wad.fragment.WishesFromDate;
import de.codingforce.wad.fragment.add.AddGroup;
import de.codingforce.wad.fragment.add.AddIngredient;
import de.codingforce.wad.fragment.add.AddShoppinglist;
import de.codingforce.wad.fragment.add.CreatWish;
import de.codingforce.wad.fragment.add.CreateDish;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity";

    private final  String Ingredients = "de.codingforce.wad.fragment.Ingredients";
    private final  String Dishes = "de.codingforce.wad.fragment.Dishes";
    private final  String Groupes = "de.codingforce.wad.fragment.Groups";
    private final  String Shoppinglistes = "de.codingforce.wad.fragment.Shoppinglists";
    private final  String WishFromDate = "de.codingforce.wad.fragment.WishesFromDate";
    private final  String Login = "de.codingforce.wad.fragment.Login";
    private final  String Register = "de.codingforce.wad.fragment.add.Register";

    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;

    private ActionBarDrawerToggle drawerToggle;
    private String currentTab;
    private String currentFragment = "";

    public static MainActivity main;

    public static String token;
    public static String username;
    public static final String URL = "http://10.0.2.2:3000/api/";
    public static String userID;
    public static String shoppinglistID;
    public static String shoppinglistName;
    public static Date tag;
    public static String dishID;
    public static String dishName;
    public static String groupID;
    public static String groupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*// This will display an Up icon (<-)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        // ...From section above...
        // Find our drawer view
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        // Setup drawer view
        setupDrawerContent(nvDrawer);*/

        main = this;

        //Login
        Class Login = Login.class;
        placeFragment(Login, R.id.mainFrame);

    }

    /**
     * places an instance of a given Fragment.class on a given layout id
     * @param fragmentClass FragmentClass to display
     * @param frameId       layoutFrame to place Fragment on
     */
    public void placeFragment(Class fragmentClass, int frameId) {
        Log.e(LOG_TAG, "--placeFragment--");
        currentFragment = fragmentClass.getName();

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
                fragmentClass = LandingPage.class;
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
                fragmentClass = Ingredients.class;
                break;
            case R.id.nav_groups:
                fragmentClass = Groups.class;
                break;
            case R.id.nav_user:
                fragmentClass = User.class;
                break;
            case R.id.nav_logout:
                resetStatics();
                fragmentClass = Login.class;
                break;
            default:
                fragmentClass = LandingPage.class;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
            case R.id.ma_action_add :
                if(currentFragment.equals(Ingredients)) {
                    Class AddIngr = AddIngredient.class;
                    placeFragment(AddIngr, R.id.mainFrame);
                }else if(currentFragment.equals(Dishes)){
                    Class CreateDish = CreateDish.class;
                    placeFragment(CreateDish, R.id.mainFrame);
                }else if(currentFragment.equals(Groupes)){
                    Class AddGroup = AddGroup.class;
                    placeFragment(AddGroup, R.id.mainFrame);
                }else if(currentFragment.equals(Shoppinglistes)){
                    Class AddShoppinglist = AddShoppinglist.class;
                    placeFragment(AddShoppinglist, R.id.mainFrame);
                }else if(currentFragment.equals(WishFromDate)){
                    Class CreateWish = CreatWish.class;
                    placeFragment(CreateWish, R.id.mainFrame);
                }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(!currentFragment.equals(Login) && !currentFragment.equals(Register)){
            // This will display an Up icon (<-)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

           // Find our drawer view
            mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

            // ...From section above...
            // Find our drawer view
            nvDrawer = (NavigationView) findViewById(R.id.nvView);
            // Setup drawer view
            setupDrawerContent(nvDrawer);
        }
        else{
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        MenuInflater inflater = getMenuInflater();

        int m = -1;
        switch (currentFragment) {
            case  Ingredients:
            case Dishes:
            case Groupes:
            case Shoppinglistes:
            case WishFromDate:
                m = R.menu.add_menu;
                break;
            default :
                break;
        }
        if (m >= 0) {
            inflater.inflate(m, menu);
        } else {
            menu.clear();
        }
        return true;
    }
    /**
     * Sets Titel
     * @param titel
     */
    public void change_title(String titel){
        setTitle(titel);
    }

    /**
     * Sets current tab
     * @param tab tab
     */
    public void setCurrentTab(String tab) {
        this.currentTab = tab;
    }

    private void resetStatics(){
        token = null;
        username= null;
        userID= null;
        shoppinglistID= null;
        shoppinglistName= null;
        tag= null;
        dishID= null;
        dishName= null;
        groupID= null;
        groupName= null;
    }

    private void clearBackStack() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }
}