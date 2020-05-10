package com.vubird.ac_twitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class userListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private ArrayList<String> userArrayList;
    private ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        if(ParseUser.getCurrentUser()==null)
        {
            Intent i = new Intent(this, login.class);
            startActivity(i);
        }

        listView = findViewById(R.id.Userlistview);
        listView.setOnItemClickListener(userListActivity.this);
        userArrayList = new ArrayList();
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_checked, userArrayList);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e==null && objects.size() > 0)
                {
                    for(ParseUser user : objects)
                    {
                        userArrayList.add(user.getUsername());
                    }
                    listView.setAdapter(arrayAdapter);
                    for(String twitterUser : userArrayList)
                    {
                        if(ParseUser.getCurrentUser().getList("Following")!=null) {
                            if (ParseUser.getCurrentUser().getList("Following").contains(twitterUser)) {
                                listView.setItemChecked(userArrayList.indexOf(twitterUser), true);
                            }
                        }
                    }
                }

            }
        });


    }



    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        CheckedTextView checkedTextView = (CheckedTextView)view;
        if(checkedTextView.isChecked())
        {
            FancyToast.makeText(this,userArrayList.get(i) + " Followed",Toast.LENGTH_SHORT, FancyToast.SUCCESS,true).show();
            ParseUser.getCurrentUser().add("Following", userArrayList.get(i));

            }
        else
        {
            ParseUser.getCurrentUser().getList("Following").remove(userArrayList.get(i));
            List currentUserFollowing = ParseUser.getCurrentUser().getList("Following");
            ParseUser.getCurrentUser().remove("Following");
            ParseUser.getCurrentUser().put("Following",currentUserFollowing);
        }


        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e)
            {
                    if(e==null)
                    {

                    }
            }
        });


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
