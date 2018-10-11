
package jm.through.read;

import java.net.URL;
import java.security.Security;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.URLDataSource;
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
            Message[] messages = folder.getMessages();
            System.out.println("No of Messages : " + folder.getMessageCount());
            System.out.println("No of Unread Messages : " + folder.getUnreadMessageCount());
            System.out.println(messages.length);

            System.out.println("FolderFetchItem on!!");


            int states = 10; // 새로고침 할 때 + 30개 해주기
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
//                System.out.println("Body: \n"+ msg.getContent());
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

//package jm.through.read;
//
//import android.os.AsyncTask;
//import android.util.Log;
//
//import java.io.*;
//import java.net.URL;
//import java.security.Security;
//import java.util.*;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import javax.activation.DataHandler;
//import javax.activation.DataSource;
//import javax.activation.URLDataSource;
//import javax.mail.*;
//import javax.mail.Flags.Flag;
//import javax.mail.internet.*;
//
//import com.sun.mail.imap.IMAPFolder;
//import com.sun.mail.imap.IMAPMessage;
//
//import jm.through.activity.MailActivity;
//import jm.through.send.JSSEProvider;
//
///**
// * IMAP 하는 곳
// */
//public class FolderFetchImap extends javax.mail.Authenticator {
//    private static final Pattern COMPILED_PATTERN_SRC_URL_SINGLE = Pattern.compile("src='([^']*)'", Pattern.CASE_INSENSITIVE);
//    private static final Pattern COMPILED_PATTERN_SRC_URL_DOUBLE = Pattern.compile("src=\"([^\"]*)\"", Pattern.CASE_INSENSITIVE);
//
//
//    static {
//        Security.addProvider(new JSSEProvider());
//
//    }
//
//    public Multipart build(String messageText, String messageHtml, List<URL> messageHtmlInline, List<URL> attachments) throws MessagingException {
//        final Multipart mpMixed = new MimeMultipart("mixed");
//        {
//            // alternative
//            final Multipart mpMixedAlternative = newChild(mpMixed, "alternative");
//            {
//                // Note: MUST RENDER HTML LAST otherwise iPad mail client only renders the last image and no email
//                addTextVersion(mpMixedAlternative, messageText);
//                addHtmlVersion(mpMixedAlternative, messageHtml, messageHtmlInline);
//            }
//            // attachments
//            addAttachments(mpMixed, attachments);
//        }
//
//        //msg.setText(message, "utf-8");
//        //msg.setContent(message,"text/html; charset=utf-8");
//        return mpMixed;
//    }
//
//    private void addAttachments(Multipart parent, List<URL> attachments) throws MessagingException {
//        if (attachments != null) {
//            for (URL attachment : attachments) {
//                final MimeBodyPart mbpAttachment = new MimeBodyPart();
//                DataSource htmlPartImgDs = new URLDataSource(attachment);
//                mbpAttachment.setDataHandler(new DataHandler(htmlPartImgDs));
//                String fileName = attachment.getFile();
//                fileName = getFileName(fileName);
//                mbpAttachment.setDisposition(BodyPart.ATTACHMENT);
//                mbpAttachment.setFileName(fileName);
//                parent.addBodyPart(mbpAttachment);
//            }
//        }
//    }
//
//    private Multipart newChild(Multipart parent, String alternative) throws MessagingException {
//        MimeMultipart child = new MimeMultipart(alternative);
//        final MimeBodyPart mbp = new MimeBodyPart();
//        parent.addBodyPart(mbp);
//        mbp.setContent(child);
//        return child;
//    }
//
//    private void addTextVersion(Multipart mpRelatedAlternative, String messageText) throws MessagingException {
//        final MimeBodyPart textPart = new MimeBodyPart();
//        textPart.setContent(messageText, "text/plain");
//        mpRelatedAlternative.addBodyPart(textPart);
//    }
//
//    private void addHtmlVersion(Multipart parent, String messageHtml, List<URL> embeded) throws MessagingException {
//        // HTML version
//        final Multipart mpRelated = newChild(parent, "related");
//
//        // Html
//        final MimeBodyPart htmlPart = new MimeBodyPart();
//        HashMap<String, String> cids = new HashMap<String, String>();
//        htmlPart.setContent(replaceUrlWithCids(messageHtml, cids), "text/html");
//        mpRelated.addBodyPart(htmlPart);
//
//        // Inline images
//        addImagesInline(mpRelated, embeded, cids);
//    }
//
//    private void addImagesInline(Multipart parent, List<URL> embeded, HashMap<String, String> cids) throws MessagingException {
//        if (embeded != null) {
//            for (URL img : embeded) {
//                final MimeBodyPart htmlPartImg = new MimeBodyPart();
//                DataSource htmlPartImgDs = new URLDataSource(img);
//                htmlPartImg.setDataHandler(new DataHandler(htmlPartImgDs));
//                String fileName = img.getFile();
//                fileName = getFileName(fileName);
//                String newFileName = cids.get(fileName);
//                boolean imageNotReferencedInHtml = newFileName == null;
//                if (imageNotReferencedInHtml) continue;
//                // Gmail requires the cid have <> around it
//                htmlPartImg.setHeader("Content-ID", "<" + newFileName + ">");
//                htmlPartImg.setDisposition(BodyPart.INLINE);
//                parent.addBodyPart(htmlPartImg);
//            }
//        }
//    }
//
//    public String replaceUrlWithCids(String html, HashMap<String, String> cids) {
//        html = replaceUrlWithCids(html, COMPILED_PATTERN_SRC_URL_SINGLE, "src='cid:@cid'", cids);
//        html = replaceUrlWithCids(html, COMPILED_PATTERN_SRC_URL_DOUBLE, "src=\"cid:@cid\"", cids);
//        return html;
//    }
//
//    private String replaceUrlWithCids(String html, Pattern pattern, String replacement, HashMap<String, String> cids) {
//        Matcher matcherCssUrl = pattern.matcher(html);
//        StringBuffer sb = new StringBuffer();
//        while (matcherCssUrl.find()) {
//            String fileName = matcherCssUrl.group(1);
//            // Disregarding file path, so don't clash your filenames!
//            fileName = getFileName(fileName);
//            // A cid must start with @ and be globally unique
//            String cid = "@" + UUID.randomUUID().toString() + "_" + fileName;
//            if (cids.containsKey(fileName))
//                cid = cids.get(fileName);
//            else
//                cids.put(fileName, cid);
//            matcherCssUrl.appendReplacement(sb, replacement.replace("@cid", cid));
//        }
//        matcherCssUrl.appendTail(sb);
//        html = sb.toString();
//        return html;
//    }
//
//    private String getFileName(String fileName) {
//        if (fileName.contains("/"))
//            fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
//        return fileName;
//    }
//
//
//    public static ArrayList<ReadData> readList = new ArrayList<>();
//
//    //String[] args
//    public ArrayList<ReadData> readImapMail(String username, String password) {
//        final String id = username;
//        final String pass = password;
//        final String host = "imap." + username.split("@")[1];
//
//        final String port = "993";
//
//        System.out.println("id = "+username);
//        try {
//            Properties props = new Properties();
//            //Properties props = System.getProperties();
//            props.setProperty("mail.store.protocol", "imap");
//            props.put("mail.imap.host", host);
//            props.put("mail.imap.port", port);//port
//            // SSL setting
//            props.setProperty("mail.imap.socketFactory.class",
//                    "javax.net.ssl.SSLSocketFactory");
//            props.setProperty("mail.imap.socketFactory.fallback", "false");
//            props.setProperty("mail.imap.socketFactory.port",
//                    String.valueOf(port));
//
//            Authenticator auth = new Authenticator() {
//                @Override
//                protected PasswordAuthentication getPasswordAuthentication() {
//                    return new PasswordAuthentication(id, pass);
//                }
//            };
//
//            Session session = Session.getDefaultInstance(props, auth);
//
//            Store store = session.getStore("imap");
//            store.connect(host, username, password);
//            //IMAPFolder
//            IMAPFolder folder = (IMAPFolder) store.getFolder("INBOX"); // This doesn't work for other email account
//            folder.open(Folder.READ_WRITE);
//            //folder = (IMAPFolder) store.getFolder("inbox"); This works for both email account
//
//            Message[] messages = folder.getMessages();
//            //Message[] messages = folder.getMessages(count - 15, count);
//            System.out.println("No of Messages : " + folder.getMessageCount());
//            System.out.println("No of Unread Messages : " + folder.getUnreadMessageCount());
//            System.out.println(messages.length);
//
//            int states = 5; // 새로고침 할 때 + 30개 해주기
//            for (int i = messages.length - 1; i > messages.length - states; i--) {
//                System.out.println("*****************************************************************************");
//                System.out.println("MESSAGE " + (i + 1) + ":");
//                Message msg = messages[i];
//
//                String subject = MimeUtility.decodeText(msg.getSubject());
//                String from = MimeUtility.decodeText(String.valueOf(msg.getFrom()[0]));
//                Date date = msg.getSentDate();
//                String contenttype = msg.getContentType();
//                int size = msg.getSize();
//                Object content = msg.getContent();
//
//                System.out.println("Subject: " + subject);
////                System.out.println("from :" + from);
////                System.out.println("From: " + from);
////                System.out.println("To: "+msg.getAllRecipients()[0]);
////                System.out.println("Date: " + date);
////                System.out.println("Size: "+ msg.getSize());
////                System.out.println(msg.getFlags());
//                //  System.out.println("Body: \n"+ msg.getContent());
//                System.out.println("\n" + messages[i] + "의 타입은 " + msg.getContentType());
//
//                readList.add(new ReadData(from, subject, date, contenttype, content, false));
//            }
//            if (folder != null && folder.isOpen()) {
//                folder.close(true);
//            }
//            if (store != null) {
//                store.close();
//            }
//        } catch (NoSuchProviderException e) {
//            e.printStackTrace();
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.getMessage();
//        }
//        return readList;
//    }
//
//
//}
//
//
