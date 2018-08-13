package jm.through.read;

import android.util.Log;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import javax.mail.search.FlagTerm;

import java.security.Security;
import java.util.*;

import jm.through.send.JSSEProvider;


public class MailReader {
    static {
        Security.addProvider(new JSSEProvider());

    }

    static ArrayList<ReadData> readList = new ArrayList<>();

    public ArrayList<ReadData> readMail(String userName, String password) {

        final String id = userName;
        final String pass = password;
        int count = 0;
        int position;

        try {
            Log.v("start success", "success");
            Properties props = new Properties();
            props.put("mail.pop3.host", "pop.gmail.com");
            props.put("mail.pop3.port", "995");
            props.put("mail.pop3.socketFactory.port", "995");
            props.put("mail.pop3.socketFactory.class",
                    "javax.net.ssl.SSLSocketFactory");
            props.put("mail.pop3.socketFactory.fallback", "false");
            props.put("mail.pop3.auth", "true");
            props.setProperty("mail.pop3.quitwait", "false");


            // 2. Creates a javax.mail.Authenticator object.
            Authenticator auth = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(id, pass);
                }
            };


            Session session = Session.getDefaultInstance(props, auth);
            session.setDebug(true); //디버깅 로그


            Store store = session.getStore("pop3s");

            store.connect("pop.gmail.com", id, pass);

            Folder folder = store.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);


           Message messages[] = folder.getMessages(1,11);
            count=folder.getMessageCount();

            Log.v("count", Integer.toString(count));

            FetchProfile fetchProfile = new FetchProfile();
            fetchProfile.add(FetchProfile.Item.ENVELOPE);
            fetchProfile.add(FetchProfile.Item.FLAGS);
            fetchProfile.add(FetchProfile.Item.CONTENT_INFO);
            fetchProfile.add("X-mailer");
            folder.fetch(messages, fetchProfile);


            for (int i = 1; i < 11; i++) {
                String subject = messages[i].getSubject();
                String from = String.valueOf(messages[i].getFrom()[0]);
                readList.add(new ReadData(from, subject, false));
            }

            folder.close(false);
            store.close();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.getMessage();
        }
        return readList;
    }
}
