package com.example.telematica.anywall_grx;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;


public class Welcome extends AppCompatActivity {
    Button logout;
    Button post;
    private static final int MAX_POST_SEARCH_RESULTS=20;
    private ParseQueryAdapter <AnywallPost> postQueryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ParseQueryAdapter.QueryFactory<AnywallPost> factory=
                new ParseQueryAdapter.QueryFactory<AnywallPost>(){
                    @Override
                    public ParseQuery<AnywallPost> create() {
                        ParseQuery<AnywallPost> query =AnywallPost.getQuery();
                        query.include("user");
                        query.orderByAscending("createdAt");
                        query.setLimit(MAX_POST_SEARCH_RESULTS);
                        return query;
                    }
                };

        postQueryAdapter= new ParseQueryAdapter<AnywallPost>(this, factory){

        @Override
        public View getItemView(AnywallPost post, View view, ViewGroup parent){
              if(view==null){
                  view=View.inflate(getContext(), R.layout.anywall_post_item,null);
              }

              TextView contentView= (TextView) view.findViewById(R.id.content_view);
              TextView usernameView= (TextView) view.findViewById(R.id.username_view);
              contentView.setText(post.getText());
              //usernameView.setText(post.getUser().getUsername());
              return view;
          }
        };
        Boolean adapterEmpty= postQueryAdapter.isEmpty();
        Log.i("DEBUG","Adapter is empty "+adapterEmpty);
        postQueryAdapter.setAutoload(false);
        postQueryAdapter.setPaginationEnabled(false);

        ListView postsListView=(ListView) findViewById(R.id.post_listView);
        postsListView.setAdapter(postQueryAdapter);

        logout = (Button) findViewById(R.id.logout);
        post= (Button) findViewById(R.id.post_button);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                Intent intent = new Intent(Welcome.this,LoginSignupActivity.class);
                startActivity(intent);
                finish();
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                Intent intent = new Intent(Welcome.this,PostActivity.class);
                startActivity(intent);
            }
        });
    }

    private void doListQuery(){
        postQueryAdapter.loadObjects();
    }

    @Override
    protected void onResume(){
        super.onResume();
        doListQuery();
    }

    @Override
    protected void  onActivityResult(int requestCode, int resultCode, Intent intent){

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
