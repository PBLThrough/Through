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
//    static Folder localFolder;
//    static int count;
    public ArrayList<ReadData> readMail(String userName, String password) {

        final String id = userName;
        final String pass = password;
        final String host = "pop.naver.com";
        int count = 0;
        int position;

        try {
            Log.v("start success", "success");
            Properties props = new Properties();
            props.put("mail.pop3.host",host);
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

            store.connect(host, id, pass);


            Folder localFolder = store.getFolder("INBOX");
            localFolder.open(Folder.READ_ONLY);


            count = localFolder.getMessageCount();
            // 메일 확인 , 메일 확인 누를시 중복 메일 발생
            Message messages[] = localFolder.getMessages(count - 15, count);


            for (int i = 15; i > 0; i--) {
                String subject = messages[i].getSubject();
                String from = String.valueOf(messages[i].getFrom()[0]);
//                String messages = String.valueOf()
                readList.add(new ReadData(from, subject, false));
            }


            localFolder.close(false);
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
