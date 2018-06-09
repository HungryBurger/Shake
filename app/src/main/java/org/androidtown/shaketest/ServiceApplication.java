package org.androidtown.shaketest;

import android.app.Application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ServiceApplication extends Application {

    public static ArrayList<String> myContactList;
    public static boolean isShaking;
    public static HashMap<String, ContactData> person;

}
