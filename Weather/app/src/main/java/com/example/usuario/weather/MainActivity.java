package com.example.usuario.weather;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity{
    EditText city;
    static TextView description;
    static TextView temp;
    Button button;
    String findCity;
    ImageView imagenClima;
    String URL_IMAGEN="http://openweathermap.org/img/w/";
    Drawable drawable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        city=(EditText) findViewById(R.id.cityText);
        description= (TextView) findViewById(R.id.condDescr);
        temp= (TextView) findViewById(R.id.temp);
        button= (Button) findViewById(R.id.button);
        imagenClima= (ImageView) findViewById(R.id.imagenClima);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WeatherTaskToday task=new WeatherTaskToday();
                findCity=city.getText().toString();
                task.execute(findCity);
            }
        });
    }


    private class WeatherTaskToday extends AsyncTask<String, Void, Void>{
        private static final  String TAG="WeatherTask";
        private String error=null;
        private ProgressDialog dialog= new ProgressDialog((MainActivity.this));
        String data="";
        @Override
        protected void onPreExecute(){
            dialog.setMessage("Por favor espere");
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            Log.d(TAG, "doInBackground");
            try{
                URL url;
                JSON json;
                weather weather;
                Gson gson=new Gson();
                data=((new WeatherHTTPClient()).getWeatherDataToday(params[0],true));
                JSONObject jsonResponse= new JSONObject(data);
                json = gson.fromJson(String.valueOf(jsonResponse), JSON.class);
                weather=json.getWeather().get(0);
                url= new  URL(URL_IMAGEN+weather.getIcon()+".png");
                InputStream is = (InputStream) url.getContent();
                drawable = Drawable.createFromStream(is, "src name");
            }catch(Exception e){error=e.getMessage();}
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dialog.dismiss();
            if(error!=null){
            }else{
                JSONObject jsonResponse;
                JSON json;
                weather weather;
                Gson gson;
                try{
                    gson= new Gson();
                    jsonResponse= new JSONObject(data);
                    json = gson.fromJson(String.valueOf(jsonResponse), JSON.class);
                    city.setText(json.getName());
                    temp.setText(Math.rint(json.getMain().getTemp()-273.15)+" °C");
                    weather=json.getWeather().get(0);
                    description.setText(weather.getMain()+","+weather.getDescription());
                    imagenClima.setImageDrawable(drawable);
                }catch (JSONException e){
                    e.printStackTrace();

                }
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
