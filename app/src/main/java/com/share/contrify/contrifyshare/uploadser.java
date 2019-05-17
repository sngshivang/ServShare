package com.share.contrify.contrifyshare;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class uploadser  {
    public static FtpServer server;
    public static void ftpsstart() {
        final File WEB_ROOT = Environment.getExternalStorageDirectory();
        try {
            new File(WEB_ROOT, "servshare_files").mkdirs();
            PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
            UserManager userManager = userManagerFactory.createUserManager();
            BaseUser user = new BaseUser();
            user.setName(universals.usrnme);
            user.setPassword(universals.spass);
            user.setHomeDirectory(WEB_ROOT.getPath() + "/servshare_files");
            List<Authority> auths = new ArrayList<>();
            Authority auth = new WritePermission();
            auths.add(auth);
            user.setAuthorities(auths);
            userManager.save(user);

            ListenerFactory listenerFactory = new ListenerFactory();
            listenerFactory.setPort(2221);

            FtpServerFactory factory = new FtpServerFactory();
            factory.setUserManager(userManager);
            factory.addListener("default", listenerFactory.createListener());


            server = factory.createServer();
            server.start();

        } catch (Exception e) {
            Log.e("Apache FTP", e.toString());
        }

    }
    public static void stopser()
    {
        server.stop();
    }
    public static boolean getstat()
    {
        if (server!=null)
        return server.isStopped();
        else
            return true;
    }
}