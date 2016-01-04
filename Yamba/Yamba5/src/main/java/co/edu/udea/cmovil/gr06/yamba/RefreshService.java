package co.edu.udea.cmovil.gr06.yamba;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.thenewcircle.yamba.client.YambaClient;
import com.thenewcircle.yamba.client.YambaClientException;
import com.thenewcircle.yamba.client.YambaStatus;

import java.util.List;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class RefreshService extends IntentService {
    private static final String TAG = RefreshService.class.getSimpleName();
    boolean isEmpty;

    public RefreshService() {
        super("RefreshService");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(isEmpty){
            Toast.makeText(this, "Please update your usernama and password", Toast.LENGTH_LONG).show();
            isEmpty=false;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreated");
    }

    @Override
    protected void onHandleIntent(Intent intent){
        if(intent!=null) {
            Log.d(TAG, "onStarted");
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            final String username = prefs.getString("username", "");
            final String password = prefs.getString("password", "");
           // DbHelper dbHelper= new DbHelper(this);
           // SQLiteDatabase db= dbHelper.getWritableDatabase();
            ContentValues values= new ContentValues();

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                isEmpty = true;
                return;
            }

            YambaClient cloud = new YambaClient(username, password);
            try {
                List<YambaStatus> timeline = cloud.getTimeline(20);
                for (YambaStatus status : timeline) {
                    values.clear();
                    values.put(StatusContract.Column.ID, status.getId());
                    values.put(StatusContract.Column.USER, status.getUser());
                    values.put(StatusContract.Column.MESSAGE, status.getMessage());
                    values.put(StatusContract.Column.CREATED_AT, status.getCreatedAt().getTime());
                   //db.insertWithOnConflict(StatusContract.TABLE,null,values,SQLiteDatabase.CONFLICT_IGNORE);
                    Uri uri= getContentResolver().insert(StatusContract.CONTENT_URI, values);
                    if(uri!=null){
                        Log.d(TAG,String.format("%s: %s", status.getUser(),status.getMessage()));
                    }
                }

            } catch (YambaClientException e) {
                Log.d(TAG, "Failed to fetch the timeline", e);
                e.printStackTrace();
            }
            return;
        }
    }
}
