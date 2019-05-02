package com.share.contrify.contrifyshare;

public class universals {
    public static int port=53000;
    public static String usrnme = "username";
    public static String spass="password";

    public static void chport(int prt)
    {
        port = prt;
    }
    public static void chusrpass(String usr, String pass)
    {
        usrnme=usr;
        spass=pass;
    }
}
