package com.example.telematica.anywall_grx;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class PostActivity extends AppCompatActivity {

    private EditText postEditText;
    private TextView characterCountTextView;
    private Button postButton;
    private int maxCharacterCount=140;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        postEditText= (EditText) findViewById(R.id.post_editText);

        postEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updatePostButtonState();
                updateCharacterCountTextViewText();
            }
        });

        characterCountTextView=(TextView)  findViewById(R.id.character_count_textview);
        postButton= (Button) findViewById(R.id.post_button);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Post();
            }
        });
        updatePostButtonState();
        updateCharacterCountTextViewText();
    }

    private void Post(){
        String text= postEditText.getText().toString().trim();
        final ProgressDialog dialog= new ProgressDialog(PostActivity.this);
        dialog.setMessage("Post en progreso...");
        dialog.show();

        AnywallPost post= new AnywallPost();

        post.setText(text);
        Log.d("Comentariooooooooooo", ParseUser.getCurrentUser().toString());
        post.setUser(ParseUser.getCurrentUser());
        ParseACL acl= new ParseACL();

        acl.setPublicReadAccess(true);
        post.setACL(acl);

        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                dialog.dismiss();
                finish();
            }
        });

    }

    private String getPostEditTextText(){
        return postEditText.getText().toString().trim();
    }
    private void updatePostButtonState(){
        int length= getPostEditTextText().length();
        boolean enabled=length>0 && length<140;
        postButton.setEnabled(enabled);
    }
    private void updateCharacterCountTextViewText(){
        String characterCountString=String.format("%d/%d", postEditText.length(),maxCharacterCount);
        characterCountTextView.setText(characterCountString);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
