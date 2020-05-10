package com.vubird.ac_twitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class viewTweets extends AppCompatActivity {


    ListView listView;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tweets);
        listView = findViewById(R.id.tweetListView);


        if(ParseUser.getCurrentUser()==null)
        {
            Intent i = new Intent(this,login.class);
            startActivity(i);
            finish();
        }
        setTitle(ParseUser.getCurrentUser().getUsername() + "-Twitter");

        textView = findViewById(R.id.textView);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displaytweets();
            }
        });


        displaytweets();

    }

    void displaytweets()
    {
        final ArrayList<HashMap<String, String>> tweetList = new ArrayList<>();
        final SimpleAdapter adapter = new SimpleAdapter(this,tweetList,android.R.layout.simple_list_item_2, new String[]{"twitterUser","twitterValue"}, new int[]{android.R.id.text1, android.R.id.text2});
        final ProgressDialog dialog = new ProgressDialog(this);
        try {
            ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("MyTweet");
            parseQuery.whereContainedIn("user", ParseUser.getCurrentUser().getList("Following"));

            dialog.setMessage("loding tweets");
            dialog.show();
            parseQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {

                    if(objects.size() > 0&& e==null) {
                        for (ParseObject tweetObj : objects) {

                                HashMap<String , String> userTweetsHashMap = new HashMap<>();
                            userTweetsHashMap.put("twitterUser",  tweetObj.getString("user"));
                            userTweetsHashMap.put("twitterValue",  tweetObj.getString("tweet"));
                            Toast.makeText(viewTweets.this,tweetObj.getString("tweet"),Toast.LENGTH_SHORT).show();
                            tweetList.add(userTweetsHashMap);
                        }
                    }

                    dialog.dismiss();
                    listView.setAdapter(adapter);
                }
            });

            }
        catch (Exception e)
        {
            dialog.dismiss();
            e.printStackTrace();
        }

    }

    public void gotoNewTweets(View view)
    {
        Intent i = new Intent(this, newtweet.class);
        startActivity(i);
    }
    public void gotoUser(View view)
    {
        Intent i = new Intent(this, userListActivity.class);
        startActivity(i);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.my_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }


    //called when menu is clicked
    //swtich case to check which menu item is clicked
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {

        switch (item.getItemId())
        {
            case R.id.postImageItem:

                break;
            case R.id.logOutUser:
                ParseUser.logOut();
                finish();
                Intent i = new Intent(this, login.class);
                startActivity(i);

                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
