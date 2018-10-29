package jm.through.account;


import android.util.Log;

import com.sun.mail.imap.IMAPFolder;

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;

public class Authenticator {
    static int count = 0;

    boolean authen(String host, String id, String pass) {
        final String userId = id;
        final String userPass = pass;
        final String hostName = "imap."+host+".com";
        final String port = "993"; //imap port


        try {
            Properties props = new Properties();
            //Properties props = System.getProperties();
            props.setProperty("mail.store.protocol", "imap");
            props.put("mail.imap.host", hostName);
            props.put("mail.imap.port", port);//port
            // SSL setting
            props.setProperty("mail.imap.socketFactory.class",
                    "javax.net.ssl.SSLSocketFactory");
            props.setProperty("mail.imap.socketFactory.fallback", "false");
            props.setProperty("mail.imap.socketFactory.port",
                    String.valueOf(port));

            javax.mail.Authenticator auth = new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(userId, userPass);
                }
            };

            Session session = Session.getInstance(props, auth);
            Store store = session.getStore("imap");
            store.connect(hostName, userId, userPass);
            IMAPFolder folder = (IMAPFolder) store.getFolder("INBOX"); // This doesn't work for other email account
            folder.open(Folder.READ_ONLY);
            count = folder.getMessageCount();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


}
