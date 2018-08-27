//package jm.through.read;
// // pop3 하는 곳
//import android.content.Intent;
//import android.util.Log;
//
//import javax.activation.DataHandler;
//import javax.mail.*;
//import javax.mail.internet.MimeMessage;
//import javax.mail.internet.MimeUtility;
//import javax.mail.search.FlagTerm;
//
//import java.io.BufferedInputStream;
//import java.io.BufferedReader;
//import java.io.ByteArrayInputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.URL;
//import java.security.Security;
//import java.util.*;
//
//import jm.through.send.JSSEProvider;
//import jm.through.send.MailSender;
//
///**
// * MailReader = Pop3
// * */
//public class MailReader extends javax.mail.Authenticator{
//    static {
//        Security.addProvider(new JSSEProvider());
//
//    }
//
//    static ArrayList<ReadData> readList = new ArrayList<>();
////    static Folder localFolder;
////    static int count;
//    public ArrayList<ReadData> readMail(String userName, String password) {
//
//        final String id = userName;
//        final String pass = password;
//        final String host = "pop.naver.com";
//        int count = 0;
//
//        try {
//            Log.v("start success", "success");
//            Properties props = new Properties();
//            props.put("mail.pop3.host",host);
//            props.put("mail.pop3.port", "995");
//            props.put("mail.pop3.socketFactory.port", "995");
//            props.put("mail.pop3.socketFactory.class",
//                    "javax.net.ssl.SSLSocketFactory");
//            props.put("mail.pop3.socketFactory.fallback", "false");
//            props.put("mail.pop3.auth", "true");
//            props.setProperty("mail.pop3.quitwait", "false");
//
//
//            // 2. Creates a javax.mail.Authenticator object.
//            Authenticator auth = new Authenticator() {
//                @Override
//                protected PasswordAuthentication getPasswordAuthentication() {
//                    return new PasswordAuthentication(id, pass);
//                }
//            };
//
//
//            Session session = Session.getDefaultInstance(props, auth);
//            session.setDebug(true); //디버깅 로그
//
//            Store store = session.getStore("pop3s");
//
//            store.connect(host, id, pass);
//
//
//            Folder localFolder = store.getFolder("INBOX");
//            localFolder.open(Folder.READ_ONLY);
//
//
//            count = localFolder.getMessageCount();
//            // 메일 확인 , 메일 확인 누를시 중복 메일 발생
//
//
//            //MimeMessageParser parser = new MimeMessageParser()
//
////            String ip =msg.toString();
////            ip = MimeUtility.decodeText(ip);
////            InputStream is = new ByteArrayInputStream(ip.getBytes("ISO-8859-1"));
////            MimeMessage mi = new MimeMessage(s, is);
////
////            Object ms = mi.getContent();
////            String test = mi.getContent().toString();
////            test.toString();
//
//
////            InputStream mailFileInputStream = new FileInputStream();
////            MimeMessage ms = new MimeMessage(session, mailFileInputStream);
//            Message messages[] = localFolder.getMessages(count - 15, count);
//
//            for (int i = 15; i > 0; i--) {
//                Message msg = messages[i];
//
//                String subject = MimeUtility.decodeText(msg.getSubject()); // 제목
//                String from = MimeUtility.decodeText(String.valueOf(msg.getFrom()[0])); // 발신자 + <계정>
//                Date date = msg.getSentDate(); // 날짜
//
//
//                DataHandler dataHandler = msg.getDataHandler();
//                String contentType = dataHandler.getContentType();
//
////                if(contentType.indexOf("multipart/mixed") != -1){
////                    Multipart multipart = (Multipart)msg.getContent();// javax.mail.internet.MimeMultipart@8d463af
////                    try{
////                        //System.out.println("multipart.getCount():"+multipart.getCount());
////                        for(int j=0;j<multipart.getCount();j++){
////                            BodyPart part = multipart.getBodyPart(j);
////                            //System.out.println(part.getContent());
////                            String disp = part.getDisposition();
////                            if (disp == null || disp.equalsIgnoreCase(Part.ATTACHMENT)){
////                                //첨부 파일 이라는 얘기임
////
////                                String filename =part.getFileName();
////
////                                System.out.println("첨부된 파일 이름 ="+filename);
////
//////                                if (filename.startsWith("=?ks_c_5601-1987?B?")) //BASE64 로 인코딩 되있는 경우
//////                                {
//////                                    filename = filename.substring(19) ;
//////                                    if (filename.indexOf('?') != -1) filename = filename.substring( 0 ,filename.indexOf('?') ) ;
//////                                    filename = new String(MimeUtility.decodeText(filename)) ;
//////                                }else if(filename.startsWith("=?ISO-8859-1?B?")){
//////                                    filename = filename.substring(15) ;
//////                                    if (filename.indexOf('?') != -1) filename = filename.substring( 0 ,filename.indexOf('?') ) ;
////                                    filename = new String(MimeUtility.decodeText(filename)) ;
////                                    Log.v("파일명 ","파일명:"+filename);
//////                                }
////
////
////                                try{
////
////                                    System.out.println("첨부된 파일 이름[인코딩]:"+ filename);
////                                    File f = new File("C:"+File.separator+"Temp"+File.separator+"mail"+File.separator+filename);
////                                    FileOutputStream fos = new FileOutputStream(f);
////
////                                    byte[] buffer = new byte[1024];
////
////                                    BufferedInputStream in = new BufferedInputStream(part.getInputStream());
////                                    Log.v("여기야!! InputStream = ",in.toString());
////
////                                    int n = 0;
////                                    while((n=in.read(buffer, 0, 1024)) != -1) {
////                                        fos.write(buffer, 0, n);
////                                    }
////                                    fos.close();
////                                    in.close();
////                                }catch(Exception e1){
////                                    System.out.println("[SUB_ERROR]:"+e1.getMessage());
////                                }
////                            }//end attech
////
////
////                            //System.out.println("["+disp+"]"+part.getContentType());
////                        }
////                    }catch(Exception e){
////                        System.out.println("[ERROR]:"+e.getMessage());
////                    }
////                }
////
////
////                if ( (contentType.indexOf("text/html") != -1) || (contentType.indexOf("text/plain") != -1) ) {
////                    BufferedReader br = new BufferedReader(new InputStreamReader(dataHandler.getInputStream()));
//////                    char[] buff = new char[512];
//////                    int len;
//////                    while ( (len = br.read(buff)) != -1) {
//////                        System.out.print(new String(buff, 0, len));
//////                    }
////                    Log.v("여기야!! reading html...",br.toString());
////                    br.close();
////                }
////                System.out.println("\n=====================================================\n");
//
////                 from 세분화 목록
////                String fromName = from.split("<")[0]; // 발신자
////                String fromAccount = from.substring(s+1,f-1); // 계정
////
////                 date 세분화 목록
////                String date = String.valueOf(msg.getSentDate()).split("G")[0]; // 전체 내용
////                String date_time = date.split(" ")[3]; // 시간
////                int idx = date.indexOf(" ",8);
////                String date_weekend = date.substring(0, idx); // 시간 제외 날짜
//
//                readList.add(new ReadData(from, subject, date, false));
//            }
//
//
//            localFolder.close(false);
//            store.close();
//        } catch (NoSuchProviderException e) {
//            e.printStackTrace();
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.getMessage();
//        }
//        return readList;
//    }
//}
