package cs408.purdueclassassistant;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;

public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    // Fragment managing the behaviors, interactions and presentation of the navigation drawer.
    private NavigationDrawerFragment mNavigationDrawerFragment;
    // Used to store the last screen title. For use in {@link #restoreActionBar()}.
    private CharSequence mTitle;

    private static final String ARGS_SECTION_NUMBER = "sectionNumber";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = "Planner";

        // Set up the navigation drawer.
        DrawerLayout dLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, dLayout);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // Update the main content by replacing fragments.

        if (position == 6) {
            new ScheduleDialogFragment(this).show(getFragmentManager(), "ScheduleDialog");
        } else {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            PlaceholderFragment newPFrag = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARGS_SECTION_NUMBER, position + 1);
            newPFrag.setArguments(args);
            transaction.replace(R.id.container, newPFrag).commit();
        }
    }

}
