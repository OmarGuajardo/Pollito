package com.codepath.apps.restclienttemplate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.codepath.apps.restclienttemplate.R;

public class ComposeDialog extends AppCompatDialogFragment {

    public EditText etComposeBody;
    public onSubmitListener listener;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view  = inflater.inflate(R.layout.layout_dialog,null);

        builder.setView(view);

        etComposeBody = view.findViewById(R.id.etComposeBody);

        return builder.create();
    };

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (onSubmitListener)context;
        } catch (Exception e) {
            Log.e("ComposeDialog", "compose dialog error : ", e);
        }
    }

    public interface onSubmitListener{
        void submitTweet(String body);
    }
}
