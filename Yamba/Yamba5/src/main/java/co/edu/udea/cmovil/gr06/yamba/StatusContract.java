package co.edu.udea.cmovil.gr06.yamba;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Usuario on 11/10/2015.
 */
public class StatusContract {
    public static final String DB_NAME="timeline3.db";
    public static final int DB_VERSION=1;
    public static final String TABLE= "status";
    public  static final String DEFAULT_SORT= Column.CREATED_AT+"DESC";

    public static final String AUTHORITY="co.edu.udea.compumovil.yamba.StatusProvider";
    public static final Uri CONTENT_URI=Uri.parse("content://"+AUTHORITY+"/"+TABLE);
    public static final int STATUS_ITEM=1;
    public static final int STATUS_DIR=2;
    public static final String STATUS_TYPE_ITEM="vnd.android.cursor.item/vnd.co.edu.udea.compumovil.yamba.provider.status";
    public static final String STATUS_TYPE_DIR="vnd.android.cursor.dir/vnd.co.edu.udea.compumovil.yamba.provider.status";


    public class Column{
        public static final String ID= BaseColumns._ID;
        public static final String USER= "user";
        public static final String MESSAGE= "message";
        public static final String CREATED_AT= "created_at";

    }
}
