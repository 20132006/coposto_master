package com.coposto.inner.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;

import com.coposto.R;

/**
 * Created by netlab on 1/11/16.
 */
public class MyAlertDialogFragment extends DialogFragment {

    private static View view_;
        public MyAlertDialogFragment() {
            // Empty constructor required for DialogFragment
        }

        public static MyAlertDialogFragment newInstance(String title, View view) {
            view_ = view;
            MyAlertDialogFragment frag = new MyAlertDialogFragment();
            Bundle args = new Bundle();
            args.putString("title", title);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String title = getArguments().getString("title");
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            alertDialogBuilder.setTitle(title);
            alertDialogBuilder.setMessage("Are you sure?");
            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // on success
                    view_.setBackgroundResource(R.drawable.background_send);
                }
            });
            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    view_.setBackgroundResource(R.drawable.background_send);
                }
            });

            return alertDialogBuilder.create();
        }

}
