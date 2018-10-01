package jm.through.read;

import java.security.Security;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import com.sun.mail.imap.IMAPFolder;
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

            Store store = session.getStore("imap");
            store.connect(host, username, password);
            //IMAPFolder
            IMAPFolder folder = (IMAPFolder) store.getFolder("INBOX"); // This doesn't work for other email account
            folder.open(Folder.READ_WRITE);
            //folder = (IMAPFolder) store.getFolder("inbox"); This works for both email account


            //if(!folder.isOpen()) folder.open(Folder.READ_WRITE);
            int count = folder.getMessageCount();

            Message[] messages = folder.getMessages();
            //Message[] messages = folder.getMessages(count - 15, count);
            System.out.println("No of Messages : " + folder.getMessageCount());
            System.out.println("No of Unread Messages : " + folder.getUnreadMessageCount());
            System.out.println(messages.length);

            System.out.println("FolderFetchItem on!!");


            int states = 1; // 새로고침 할 때 + 30개 해주기
            for (int i= messages.length-1; i > messages.length - states -1 ;i--)
            {
                System.out.println("*****************************************************************************");
                System.out.println("MESSAGE " + (i + 1) + ":");
                Message msg =  messages[i];
                //System.out.println(msg.getMessageNumber());
                //System.out.println(folder.getUID(msg)

                String subject =  MimeUtility.decodeText(msg.getSubject());
                String from = MimeUtility.decodeText(String.valueOf(msg.getFrom()[0]));
                Date date = msg.getSentDate();
                String contenttype = msg.getContentType();
                int size = msg.getSize();
                Object content = msg.getContent();

                System.out.println("Subject: " + subject);
//                System.out.println("from :" + from);
//                System.out.println("From: " + from);
//                System.out.println("To: "+msg.getAllRecipients()[0]);
//                System.out.println("Date: "+ date);
//                System.out.println("Size: "+ msg.getSize());
//                System.out.println(msg.getFlags());
                //System.out.println("Body: \n"+ msg.getContent());
                System.out.println("\n"+messages[i] + "의 타입은 "+ msg.getContentType());

                readList.add(new ReadData(from, subject, date, contenttype, content, false));
            }
            if (folder != null && folder.isOpen()) { folder.close(true); }
            if (store != null) { store.close(); }
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

