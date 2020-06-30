package com.codepath.apps.restclienttemplate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class ComposeDialog extends AppCompatDialogFragment {

    public EditText etComposeBody;
    public onSubmitListener listener;
    public Button btnTweet;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view  = inflater.inflate(R.layout.compose_layout,null);

        builder.setView(view);

        etComposeBody = view.findViewById(R.id.etComposeBody);
        btnTweet = view.findViewById(R.id.btnTweet);

        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.submitTweet(etComposeBody.getText().toString());

            }
        });



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
