package cs408.purdueclassassistant;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class EditActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        Intent intent = getIntent();

        int activity_type = intent.getIntExtra("INT_TYPE", 0);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        switch (activity_type) {
            case 0:
                //ADD CLASS
                // Create new fragment and transaction
                Fragment classFragment = new AddClassFragment();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(R.id.container, classFragment);

                // Commit the transaction
                transaction.commit();
                break;
            case 1:
                //ADD HOMEWORK
                // Create new fragment and transaction
                Fragment homeworkFragment = new AddHomeworkFragment();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(R.id.container, homeworkFragment);

                // Commit the transaction
                transaction.commit();
                break;
            case 2:
                //ADD EXAM
                // Create new fragment and transaction
                Fragment examFragment = new AddExamFragment();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(R.id.container, examFragment);

                // Commit the transaction
                transaction.commit();
                break;
            case 3:
                //ADD TASK
                // Create new fragment and transaction
                Fragment taskFragment = new AddTaskFragment();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(R.id.container, taskFragment);

                // Commit the transaction
                transaction.commit();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_edit, container, false);
        }
    }

}
