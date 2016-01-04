package com.example.usuario.gcmgrupo6;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class ChatActivity extends AppCompatActivity {
    EditText editText_mail_id;
    EditText editText_chat_message;
    ListView listView_chat_messages;
    Button button_send_chat;
    List<ChatObject> chat_list;
    String TAG="ChatActivity";
    Context context;
    String select_username;

    BroadcastReceiver recieve_chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editText_mail_id= (EditText) findViewById(R.id.editText_email_id);
        editText_chat_message=(EditText) findViewById(R.id.editText_chat_message);
        listView_chat_messages=(ListView) findViewById(R.id.listView_chat_messages);
        button_send_chat=(Button) findViewById(R.id.button_send_chat);
        context=getApplicationContext();
        Bundle bundle=getIntent().getExtras();
        select_username=bundle.getString("select_username");
        if(!isUserRegistered(context)){
            startActivity(new Intent(ChatActivity.this, MainActivity.class));
        }
        System.out.println("select_username: "+select_username);

        button_send_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message=editText_chat_message.getText().toString();
                showChat("Sent", message);
                new SendMessage().execute();
                editText_chat_message.setText("");
            }
        });

        recieve_chat=  new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String message=intent.getStringExtra("message");

                Log.d("pavan","in local braod "+message);
                showChat("receive",message);
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(recieve_chat, new IntentFilter("message_receiver"));
    }

    private void showChat(String type,String message){
        if(chat_list==null || chat_list.size()==0){
            chat_list=new ArrayList<ChatObject>();
        }
        chat_list.add(new ChatObject(message, type));
        ChatAdapter chatAdapter= new ChatAdapter(ChatActivity.this,R.layout.chat_view,chat_list);
        listView_chat_messages.setAdapter(chatAdapter);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    private SharedPreferences getGCMPreferences(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    private boolean isUserRegistered(Context context){
        final SharedPreferences prefs= getGCMPreferences(context);
        String user_name= prefs.getString(Util.USER_NAME, "");
        if(user_name.isEmpty()){
            Log.i(TAG, "no hay usuario logueado");
            return false;
        }
        return true;
    }

    public class SendMessage extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            String url =Util.send_chat_url+"?name="+select_username+
                    "&message="+editText_chat_message.getText().toString();
            Log.i("pavan", "url" + url);

            OkHttpClient client_for_getMyFriends=new OkHttpClient();
            String response=null;

            try{
                url=url.replace(" ", "%20");
                response= callOkHttpRequest(new URL(url),client_for_getMyFriends);
                for (String subString:response.split("<script",2)){
                    System.out.println("split:" +subString );
                    response=subString;
                    break;
                }
            }catch(MalformedURLException e){
               e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
        }
    }


    String callOkHttpRequest(URL url , OkHttpClient tempClient) throws  IOException{
        Request request = new Request.Builder().url(url).build();
        Response response = tempClient.newCall(request).execute();
        return response.body().string();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case R.id.action_settings:
                startActivity(new Intent(ChatActivity.this, Preferens.class));
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onRestart(){
        super.onRestart();
        if(!isUserRegistered(context)){
            startActivity(new Intent(ChatActivity.this, MainActivity.class));
        }
    }
}
