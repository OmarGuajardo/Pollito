package com.codepath.apps.restclienttemplate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ComposeDialog extends AppCompatDialogFragment {

    public String TAG = "ComposeDialog";
    public TextInputLayout etComposeBodyContainer;
    public TextInputEditText etComposeBody;
    public onSubmitListener listener;
    public Button btnTweet;
    public String userHandle;
    public long tweetID;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view  = inflater.inflate(R.layout.compose_layout,null);

        builder.setView(view);

        etComposeBody = view.findViewById(R.id.etComposeBody);
        etComposeBodyContainer = view.findViewById(R.id.etComposeBodyContainer);
        btnTweet = view.findViewById(R.id.btnTweet);
        Bundle mArgs = getArguments();

        if(mArgs == null){
            userHandle= mArgs.getString("userHandle");
            tweetID= mArgs.getLong("tweetID");
            Log.d(TAG, "onCreateDialog: this is what I get for userHandle " +userHandle);
            Log.d(TAG, "onCreateDialog: this is what I get for tweetID " +tweetID);
            etComposeBodyContainer.setHint("Replying to @"+userHandle);
            etComposeBody.requestFocus();
            btnTweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.submitTweet("@"+userHandle+" "+etComposeBody.getText().toString(),tweetID);
                }
            });
        }else {
            Log.d(TAG, "onCreateDialog: no argumetns passed ");
            btnTweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.submitTweet(etComposeBody.getText().toString());
                }
            });
        }



        return builder.create();
    };


    //Attaches the function from TimeLineActivity to this Fragment
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
        void submitTweet(String body,long ID);
    }
}
