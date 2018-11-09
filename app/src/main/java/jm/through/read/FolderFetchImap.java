
package jm.through.read;

import com.sun.mail.imap.IMAPFolder;

import java.security.Security;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeUtility;
import jm.through.send.JSSEProvider;

/**
 * IMAP 하는 곳
 * */
public class FolderFetchImap extends javax.mail.Authenticator{

    static {
        Security.addProvider(new JSSEProvider());

    }

    public static ArrayList<ReadData> readList = new ArrayList<>();
    //String[] args
    public ArrayList<ReadData> readImapMail(String username, String password) {
        final String id = username;
        final String pass = password;
        final String host = "imap.naver.com"; // port = 993
        final String port = "993";


        try
        {
            Properties props = new Properties();
            //Properties props = System.getProperties();
            props.setProperty("mail.store.protocol", "imap");
            props.put("mail.imap.host",host);
            props.put("mail.imap.port",port);//port
            // SSL setting
            props.setProperty("mail.imap.socketFactory.class",
                    "javax.net.ssl.SSLSocketFactory");
            props.setProperty("mail.imap.socketFactory.fallback", "false");
            props.setProperty("mail.imap.socketFactory.port",
                    String.valueOf(port));

            Authenticator auth = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(id, pass);
                }
            };

            Session session = Session.getDefaultInstance(props, auth);
            // TODO comment by jiran 디버깅 필요하면 아래 코드 주석 해제 후 개발
            //session.setDebug(true);

            Store store = session.getStore("imap");
            store.connect(host, username, password);
            //IMAPFolder
            IMAPFolder folder = (IMAPFolder) store.getFolder("INBOX"); // This doesn't work for other email account
            folder.open(Folder.READ_WRITE);
            //folder = (IMAPFolder) store.getFolder("inbox"); This works for both email account


            //if(!folder.isOpen()) folder.open(Folder.READ_WRITE);
            Message[] messages = folder.getMessages();
            System.out.println("No of Messages : " + folder.getMessageCount());
            System.out.println("No of Unread Messages : " + folder.getUnreadMessageCount());
            System.out.println(messages.length);

            System.out.println("FolderFetchItem on!!");


            int states = 20; // 새로고침 할 때 + 30개 해주기
            for (int i= messages.length-1; i > messages.length - states -1 ;i--)
            {
                System.out.println("*****************************************************************************");
                System.out.println("MESSAGE " + (i + 1) + ":");
                Message msg =  messages[i];

                String subject =  MimeUtility.decodeText(msg.getSubject());
                String from = MimeUtility.decodeText(String.valueOf(msg.getFrom()[0]));
                Date date = msg.getSentDate();
                String contenttype = msg.getContentType();
                int size = msg.getSize();
                Object content = msg.getContent();

                System.out.println("Subject: " + subject);
                System.out.println("Body : " + content);
                System.out.println("\n"+messages[i] + "의 타입은 "+ msg.getContentType());

                readList.add(new ReadData(from, subject, date, contenttype, content, false));
            }

            /**
             * 컨텐츠(ex:html) 획득을 위해 세션 종료를 주석처리.
             * (* 기존의 경우, MessageActivity에서 메일 컨텐츠 로드 시도 시(getContent()) 소켓이 이미 닫혀 있기 때문에 소켓에서 컨텐츠를 읽어올 수 없었음.)
             *
             * 우선은 아래 주석처리로 정상동작하겠지만, 시간이 되신다면 기존 피드백대로 수정하는 걸 권유드립니다.
             *
             * 메일 컨텐츠 로드가 필요할 때,
             * 1. 커넥션을 유지한 상태에서 앱을 계속 사용
             * 2. 커넥션이 필요한 시점마다 커넥션 재연결 후 사용
             * 3.내부 Sql에 컨텐츠 저장해놓고 필요한 시점에 불러와서 쓰도록 구현
             *
             */
//            if (folder != null && folder.isOpen()) { folder.close(true); }
//            if (store != null) { store.close(); }
        }
        catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.getMessage();
        }
        return readList;
    }
}
