package com.example.usuario.gcmgrupo6;


import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.ArrayList;

public class ConectedUsersActivity extends AppCompatActivity {
    ListView userList;
    String TAG ="ConectedUsersActivity";
    ArrayList lista = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conected_users);
        userList=(ListView) findViewById(R.id.UserList);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        new GetAllNames().execute();

        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int posicion, long id) {
                String item = (String) userList.getItemAtPosition(posicion);
                Intent intent = new Intent(ConectedUsersActivity.this, ChatActivity.class);
                intent.putExtra("select_username", item);
                startActivity(intent);
            }
        });
    }

    private class GetAllNames extends AsyncTask<String, Void,String> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = Util.getAllUsers;
            Log.i(TAG, "url: "+url);
            OkHttpClient client_for_getMyFriends = new OkHttpClient();
            String response = null;

            try {
                url = url.replace(" ", "820");
                response = callOkHttpRequest(new URL(url), client_for_getMyFriends);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            JSONArray jsonResponse;
            String outputData;
            try {
                jsonResponse = new JSONArray(result);
                for (int i = 0; i < jsonResponse.length(); i++) {
                    outputData = jsonResponse.getJSONObject(i).optString("name");
                    lista.add(outputData);
                }
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }
            ArrayAdapter<String> adaptador = new ArrayAdapter<String>(ConectedUsersActivity.this, android.R.layout.simple_list_item_1, lista);
            userList.setAdapter(adaptador);
        }
    }
        String callOkHttpRequest(URL url , OkHttpClient tempClient) throws  IOException{
            Request request = new Request.Builder().url(url).build();
            Response response = tempClient.newCall(request).execute();
            return response.body().string();
        }
}


