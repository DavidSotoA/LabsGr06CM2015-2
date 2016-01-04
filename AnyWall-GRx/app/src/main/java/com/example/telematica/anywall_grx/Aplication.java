package com.example.telematica.anywall_grx;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by telematica on 27/11/15.
 */

public class Aplication extends android.app.Application{
    public static final boolean APPDEBUG = false;

    public  static  final  String APPTAG="AnyWall";

    public  Aplication(){
    }

    @Override
    public void onCreate(){
        super.onCreate();

        ParseObject.registerSubclass(AnywallPost.class);
        Parse.initialize(this,"yncn1HSyQNqCCuGiOGNCcttkQoVgpUmhtZ7Viuao","7GivjSNQdkDlwlRSJZICyBhK6zDyN2OkZmpyyj0V");

        ParseUser.enableAutomaticUser();
        ParseACL defaulAcl = new ParseACL();
        defaulAcl.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaulAcl,true);
    }


}
