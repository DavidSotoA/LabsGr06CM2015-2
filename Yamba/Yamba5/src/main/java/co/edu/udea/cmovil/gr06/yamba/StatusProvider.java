package co.edu.udea.cmovil.gr06.yamba;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class StatusProvider extends ContentProvider {
    private static final  String TAG=StatusProvider.class.getSimpleName();
    private DbHelper dbhelper;
    private static final UriMatcher sURIMatcher=new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(StatusContract.AUTHORITY,StatusContract.TABLE,StatusContract.STATUS_DIR);
        sURIMatcher.addURI(StatusContract.AUTHORITY,StatusContract.TABLE+"/#",StatusContract.STATUS_ITEM);
    }

    public StatusProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        switch (sURIMatcher.match(uri)){
            case StatusContract.STATUS_DIR:
                Log.d(TAG, "gotType: "+StatusContract.STATUS_TYPE_DIR);
                return  StatusContract.STATUS_TYPE_DIR;
            case StatusContract.STATUS_ITEM:
                Log.d(TAG,"gotType: " +StatusContract.STATUS_TYPE_ITEM);
                return StatusContract.STATUS_TYPE_ITEM;
            default:
                throw new IllegalArgumentException("Illegal uri: " +uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri ret=null;
        if(sURIMatcher.match(uri)!=StatusContract.STATUS_DIR){
            throw new IllegalArgumentException("Illegal uri: "+uri);
        }
        SQLiteDatabase db=dbhelper.getWritableDatabase();
        long rowId =db.insertWithOnConflict(StatusContract.TABLE,null,values,SQLiteDatabase.CONFLICT_IGNORE);
        if(rowId!=-1){
            long id=values.getAsLong(StatusContract.Column.ID);
            ret= ContentUris.withAppendedId(uri,id);
            Log.d(TAG, "inserted uri: " +ret );
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return ret;
    }

    @Override
    public boolean onCreate() {
        Log.d(TAG,"onCreated");
        dbhelper= new DbHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb= new SQLiteQueryBuilder();
        qb.setTables(StatusContract.TABLE);
        switch(sURIMatcher.match(uri)){
            case StatusContract.STATUS_DIR:
                break;
            case StatusContract.STATUS_ITEM:
                qb.appendWhere(StatusContract.Column.ID+ "="+uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Illegal uri: " +uri);
        }
        String orderBy= (TextUtils.isEmpty(sortOrder)) ? StatusContract.DEFAULT_SORT: sortOrder;
        SQLiteDatabase db= dbhelper.getReadableDatabase();
        Cursor cursor= qb.query(db,projection,selection,selectionArgs,null,null,orderBy);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        Log.d(TAG, "queried records: " +cursor.getCount() );
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String where;
        switch (sURIMatcher.match(uri)) {
            case StatusContract.STATUS_DIR:
                where = selection;
                break;
            case StatusContract.STATUS_ITEM:
                long id = ContentUris.parseId(uri);
                where = StatusContract.Column.ID
                        + "="
                        + id
                        + (TextUtils.isEmpty(selection) ? "" : " and ("
                        + selection + " )");
                break;
            default:
                throw new IllegalArgumentException("Illegal uri: " + uri);
        }
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        int ret = db.update(StatusContract.TABLE, values, where, selectionArgs);
        if (ret > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        Log.d(TAG,"updated records: " +ret);
        return ret;
    }
}
