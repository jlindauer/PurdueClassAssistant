package cs408.purdueclassassistant;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;


/**
 * Created by frickm on 2/19/14.
 * edited by Travis
 */
public class AddActivityDialogFragment extends DialogFragment {

    private Activity activity;
    private Fragment parent;

    public AddActivityDialogFragment(Activity a, Fragment f) {
        activity = a;
        parent = f;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.dialog_add)
                .setItems(R.array.activities, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Intent add_intent = new Intent(activity, EditActivity.class);

                        int type = -1;
                        switch (which) {
                            case 0:
                                type = 0;
                                break;
                            case 1:
                                type = 1;
                                break;
                            case 2:
                                type = 2;
                                break;
                            case 3:
                                type = 3;
                                break;
                        }

                        add_intent.putExtra("INT_TYPE", type);
                        parent.startActivityForResult(add_intent, 4321);

                    }
                });
        return builder.create();

    }

}
