package jm.through.read;

import android.util.Log;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.mail.search.FlagTerm;

import java.security.Security;
import java.util.*;

import jm.through.send.JSSEProvider;
import jm.through.send.MailSender;

/**
 * MailReader = Pop3
 * */
public class MailReader extends javax.mail.Authenticator{
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
                Message msg = messages[i];

                String subject = MimeUtility.decodeText(msg.getSubject()); // 제목
                String from = MimeUtility.decodeText(String.valueOf(msg.getFrom()[0])); // 발신자 + <계정>
                String date = String.valueOf(msg.getSentDate()).split("G")[0];

                //Date tempdate = msg.getSentDate();

                // from 세분화 목록
//                String fromName = from.split("<")[0]; // 발신자
//                String fromAccount = from.substring(s+1,f-1); // 계정

                // date 세분화 목록
//                String date = String.valueOf(msg.getSentDate()).split("G")[0]; // 전체 내용
//                String date_time = date.split(" ")[3]; // 시간
//                int idx = date.indexOf(" ",8);
//                String date_weekend = date.substring(0, idx); // 시간 제외 날짜

                readList.add(new ReadData(from, subject, date,false));
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
