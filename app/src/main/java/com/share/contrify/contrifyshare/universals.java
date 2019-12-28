package com.share.contrify.contrifyshare;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.InputStream;
import java.net.URI;

public class universals {
    public static int port=53000;
    public static String usrnme = "username";
    public static String spass="password";
    public static File uplpth = Environment.getExternalStorageDirectory();
    public static Uri univuri;
    public static Context fbr;
    public static InputStream ist;

    public static void chport(int prt)
    {
        port = prt;
    }
    public static void chusrpass(String usr, String pass)
    {
        usrnme=usr;
        spass=pass;
    }
    public static void chuplpth(File fl)
    {
        uplpth = fl;
    }
    public static void crcontext(Context ct)
    {
        fbr = ct;
    }
    public static void crunivuri(Uri ur) {univuri = ur;}
}
