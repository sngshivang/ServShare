package com.share.contrify.contrifyshare;

import android.os.AsyncTask;

public class sv_monitor extends Thread{
    public void run()
    {
        while (!isInterrupted()) {
            network ntw = new network();
            if (ntw.getStatus() == AsyncTask.Status.RUNNING) {

            }
        }
    }
}
