package jm.through.send;

import android.util.Log;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Security;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import jm.through.attachment.AttachData;

public class MailSender extends javax.mail.Authenticator {
    private String mailhost = "smtp.naver.com";
    private String user;
    private String password;
    private Session session;

    static {
        Security.addProvider(new JSSEProvider());
    }

    public MailSender(String user, String password) {
        this.user = user;
        this.password = password;

        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", mailhost);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.quitwait", "false");

        session = Session.getInstance(props, this);
        //port 25로 default
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, password);
    }

    public synchronized Boolean sendMail(String subject, String sender, List<String> recipients,
                                      String body, ArrayList<AttachData> attachment_PathList) throws Exception {

        Boolean flag = true;

        try {

            //TODO chips 사용

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender)); //full주소가 들어가야 함
            message.setSubject(subject);
            message.setSentDate(new Date());

            int numOfRecipient = recipients.size();

            Log.v("sizesize", Integer.toString(numOfRecipient));
            Log.v("contentcontent", recipients.toString());

            InternetAddress[] toAddr = new InternetAddress[recipients.size()];


            //다중 수신자
            if(numOfRecipient > 1){
                for (int i = 0; i < recipients.size(); i++) {
                    toAddr[i] = new InternetAddress(recipients.get(i));
                }
            }else {
                toAddr[0] = new InternetAddress(recipients.get(0));
            }

            Log.v("convertadress1", toAddr[0].toString());

            message.setRecipients(Message.RecipientType.TO, toAddr);

            Multipart multipart = new MimeMultipart();

            //메시지
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(body);
            multipart.addBodyPart(messageBodyPart); //본문 메세지

            //첨부파일
            if (attachment_PathList.size() != 0) {
                for (AttachData data : attachment_PathList) {
                    messageBodyPart = new MimeBodyPart();
                    DataSource source = new FileDataSource(data.getFileUri());
                    messageBodyPart.setDataHandler(new DataHandler(source));
                    messageBodyPart.setFileName(MimeUtility.encodeText(source.getName()));
                    multipart.addBodyPart(messageBodyPart);
                }
            }

            Log.v("messageDate", message.getSentDate().toString());
            message.setContent(multipart);
            Transport.send(message);

        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
            Log.v("functionFlag", flag.toString());

        }
        return flag;
    }

    public class ByteArrayDataSource implements DataSource {
        private byte[] data;
        private String type;

        public ByteArrayDataSource(byte[] data, String type) {
            super();
            this.data = data;
            this.type = type;
        }

        public ByteArrayDataSource(byte[] data) {
            super();
            this.data = data;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContentType() {
            if (type == null)
                return "application/octet-stream";
            else
                return type;
        }

        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(data);
        }

        public String getName() {
            return "ByteArrayDataSource";
        }

        public OutputStream getOutputStream() throws IOException {
            throw new IOException("Not Supported");
        }
    }
}