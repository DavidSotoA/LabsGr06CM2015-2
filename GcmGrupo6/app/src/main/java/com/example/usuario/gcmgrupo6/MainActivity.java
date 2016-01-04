package com.example.usuario.gcmgrupo6;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {

    EditText editText_user_name;
    EditText editText_email;
    Button button_login;

    static final String TAG ="pavan";

    TextView mDisplay;
    GoogleCloudMessaging gcm;
    AtomicInteger msgId= new AtomicInteger();
    SharedPreferences prefs;
    Context context;
    String regId;
    String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context=getApplicationContext();

        if(isUserRegistered(context)){
            startActivity(new Intent(MainActivity.this,ConectedUsersActivity.class));
            finish();
        }else{
            editText_user_name=(EditText) findViewById(R.id.editText_user_name);
            editText_email=(EditText) findViewById(R.id.editText_email);
            button_login=(Button) findViewById(R.id.button_login);

            button_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendRegistartionIdToBackend();
                }
            });

            if(checkPlayServices()){
                System.out.println("checkPlayServicesssssssssssssssssssssssssssssssssssssss");
                gcm=GoogleCloudMessaging.getInstance(this);
                regId= getRegistrationId(context);

                if(regId.isEmpty()){
                    System.out.println("regId emptry");
                    new registerInBackground().execute();
                }
            }else{
                Log.i(TAG, "no valid google play services APK found.");
            }
        }
    }

    private boolean checkPlayServices(){
        int resultCode= GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if(resultCode != ConnectionResult.SUCCESS){
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)){
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        Util.PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }else{
                Log.i(TAG,"this device is not supported");
            }
              return false;
        }
        return true;
    }

    private String getRegistrationId(Context context){
        final SharedPreferences prefs= getGCMPreferences(context);
        String registrationId= prefs.getString(Util.PROPERTY_REG_ID,"");
        if(registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found");
            return "";
        }
        // verificar si la aplicacion fue actualizada, en ese caso se debe
        // limpiar el ID registro

        int registeredVersion= prefs.getInt(Util.PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion= getAppVersion(context);
        if(registeredVersion!=currentVersion){
            Log.i(TAG, "App version changed");
            return "";
        }
        return registrationId;
    }

    private boolean isUserRegistered(Context context){
        final SharedPreferences prefs= getGCMPreferences(context);
        String user_name= prefs.getString(Util.USER_NAME, "");
        if(user_name.isEmpty()){
            Log.i(TAG, "Registration not found");
            return false;
        }
        return true;
    }

    private class registerInBackground extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... params) {
            try {
                if(gcm==null){
                    gcm= GoogleCloudMessaging.getInstance(MainActivity.this);
                }
                regId=gcm.register(Util.SENDER_ID);
                System.out.println("regIdddddddddddddddddddd: "+regId);
                msg="Devide registeres, registration ID="+ regId;
            }catch(IOException e){
                msg= "Error :" +e.getMessage();
            }
            return msg;
        }
    }

   private static int getAppVersion(Context context){
       try{
           PackageInfo packageInfo= context.getPackageManager().getPackageInfo(context.getPackageName(),0);
        return packageInfo.versionCode;
       } catch (PackageManager.NameNotFoundException e) {
           throw new RuntimeException("Could not get package name "+ e);
       }
   }

    private void storeRegistrationId(Context context, String regId){
        final SharedPreferences prefs= getGCMPreferences(context);
        int appVersion= getAppVersion(context);
        Log.i(TAG, "Saving regId on app version "+ appVersion);
        SharedPreferences.Editor editor=prefs.edit();
        editor.putString(Util.PROPERTY_REG_ID, regId);
        editor.putInt(Util.PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    private void storeUserDetails(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion =getAppVersion(context);
        Log.i(TAG,"Saving regId on app version "+appVersion);
        SharedPreferences.Editor editor=prefs.edit();
        editor.putString(Util.EMAIL, editText_email.getText().toString());
        editor.putString(Util.USER_NAME, editText_user_name.getText().toString());
        editor.commit();
    }

    private SharedPreferences getGCMPreferences(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    private void sendRegistartionIdToBackend(){
        new getUsersGCM().execute();
    }

    private class getUsersGCM extends AsyncTask<String, Void,String>{
        @Override
        protected String doInBackground(String... params) {
            String url= Util.getUsers+"?name="+editText_user_name.getText().toString();
            Log.i(TAG, "getUsersGCM: "+ url);
            OkHttpClient client_for_getMyFriends= new OkHttpClient();
            String response=null;
            try{
                url= url.replace(" ", "820");
                response= callOkHttpRequest(new URL(url), client_for_getMyFriends);
                response.replaceAll("\n", "");
                Log.i(TAG, "response: "+ response);
            }catch(MalformedURLException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
            Log.i(TAG, "response2: " + response);
            return response;
        }
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            char[] c=result.toCharArray();
            if(c[2]=='e'){
                Toast.makeText(context, "This username already exist", Toast.LENGTH_LONG).show();
            }else{
                new SendGcmToServer().execute();
            }
        }
    }

    private class SendGcmToServer extends AsyncTask<String, Void,String>{

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String url= Util.register_url+"?name="+editText_user_name.getText().toString()
                    +"&email="+editText_email.getText().toString()+"&regId="+regId;
            Log.i(TAG, "url: "+ url);
            OkHttpClient client_for_getMyFriends= new OkHttpClient();
            String response=null;

            try{
                url= url.replace(" ", "820");
                response= callOkHttpRequest(new URL(url), client_for_getMyFriends);
                System.out.println(response);
                Log.i(TAG, "response: "+ response);
                char[] c=response.toCharArray();
                if(c[40]=='s'){
                    response= "success";
                }else{
                    response = "failure";
                }
            }catch(MalformedURLException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
            Log.i(TAG, "response2: " + response);
            return response;
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            if(result!=null){
                if(result.equals("success")){
                    storeUserDetails(context);
                    startActivity(new Intent(MainActivity.this, ConectedUsersActivity.class));
                    finish();
                }else{
                    Toast.makeText(context,"Try again: "+result, Toast.LENGTH_LONG).show();
                    Log.i(TAG,"Try again: **"+result);
                }
            }else{
                Toast.makeText(context, "Check net comunication", Toast.LENGTH_LONG).show();
            }
        }
    }

    String callOkHttpRequest(URL url, OkHttpClient tempClient)
        throws IOException{
        Request request = new Request.Builder().url(url).build();
        Response response = tempClient.newCall(request).execute();
        return response.body().string();
    }

    byte[] readFully (InputStream in) throws  IOException{
        ByteArrayOutputStream out= new ByteArrayOutputStream();
        byte[] buffer= new byte[1024];
        for (int count; (count=in.read(buffer)) != -1;){
            out.write(buffer,0,count);
        }
        return  out.toByteArray();
    }
}
