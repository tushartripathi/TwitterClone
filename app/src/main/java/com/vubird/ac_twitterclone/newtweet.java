package com.vubird.ac_twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class newtweet extends AppCompatActivity {
    EditText tweetBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newtweet);

        tweetBox = findViewById(R.id.newTweeteditText);
    }
    public void sharetweet(final View view)
    {
        ParseObject parseObject = new ParseObject("MyTweet");
        parseObject.put("tweet",tweetBox.getText().toString().trim());
        parseObject.put("user", ParseUser.getCurrentUser().getUsername());

        final ProgressDialog progressDialog =  new ProgressDialog(this);
        progressDialog.setMessage("Twetting.......");
        progressDialog.show();

        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e)
            {
                if(e==null)
                {
                    Toast.makeText(newtweet.this,"done", Toast.LENGTH_SHORT).show();
                    Snackbar.make(view, "Done", Snackbar.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });
    }
}
