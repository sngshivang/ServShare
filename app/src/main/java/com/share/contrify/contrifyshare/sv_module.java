package com.share.contrify.contrifyshare;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import java.net.URL;
import java.net.URLConnection;

public class sv_module {
    private static AsyncTask atsk;
    private static network ntw;
    public static void ststart()
    {
        sv_module sm = new sv_module();
        if (ntw==null||ntw.getStatus()==AsyncTask.Status.FINISHED)
            ntw = new network();

        if (ntw.getStatus() == AsyncTask.Status.RUNNING)
            sm.stop();
        else
            sm.svstart();

    }
    private void svstart()
    {
        atsk=ntw.execute();
    }
    private void stop()
    {
        Log.i("Cancel","CI");
        atsk.cancel(true);
        mockc();
    }
    private void mockc()
    {
        Thread t = new Thread(new Runnable() {
            public void run() {
                String ip = Utils.getIPAddress(true);
                ip = "http://" + ip + ":" + String.valueOf(universals.port);
                try

                {
                    URL url = new URL(ip);
                    URLConnection conection = url.openConnection();
                    conection.connect();
                } catch (Exception e)

                {
                    Log.i("MockConnection", e.toString());
                }
            }
        });
        t.start();
    }
    public static boolean getstat()
    {
        if (ntw!=null)
        return (ntw.getStatus()==AsyncTask.Status.RUNNING);
        else
            return false;
    }

}
