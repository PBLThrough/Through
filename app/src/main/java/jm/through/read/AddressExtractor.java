//package jm.through.read;
//
//import javax.mail.*;
//import javax.mail.internet.*;
//import javax.mail.search.SearchTerm;
//
//import java.security.Security;
//import java.util.*;
//import java.io.*;
//
//import jm.through.send.JSSEProvider;
//
///**
// * imap 참고 코드
// * */
//public class AddressExtractor extends javax.mail.Authenticator
//{
//    static {
//        Security.addProvider(new JSSEProvider());
//    }
//    static ArrayList<ReadData> readList = new ArrayList<>();
//
//    public static void main(String[] args) {
//        String host = "imap.gmail.com";
//        String port = "993";
//        String userName = "your_email";
//        String password = "your_password";
//        AddressExtractor searcher = new AddressExtractor();
//        String keyword = "JavaMail";
//        searcher.searchEmail(host, port, userName, password, keyword);
//    }
//
//    public void searchEmail(String host, String port, String userName,
//                            String password, final String keyword)
//    {
//        String hosts      = host;
//        String username  = userName;
//        String passwd  = password;
//        //String provider  = "pop3";
//        String provider  = "imap";
//
//        // server setting
//        Properties props = new Properties();
//        props.put("mail.imap.host",hosts);
//        props.put("mail.imap.port",993);//port
//        // SSL setting
//        props.setProperty("mail.imap.socketFactory.class",
//                "javax.net.ssl.SSLSocketFactory");
//        props.setProperty("mail.imap.socketFactory.fallback", "false");
//        props.setProperty("mail.imap.socketFactory.port",
//                String.valueOf(993));
//        Session session = Session.getDefaultInstance(props);
//
//        try
//        {
//            //Connect to the message store
//            session.setDebug(true);
//            Store store = session.getStore(provider);
//            store.connect(host, username, password);
//
//            //open the inbox folder
//            Folder inbox = store.getFolder("INBOX");
//            inbox.open(Folder.READ_ONLY);
//
//            // creates a search criterion
//            SearchTerm term = new SearchTerm() {
//                @Override
//                public boolean match(Message message) {
//                    try {
//                        if (message.getSubject().contains("Javamail")) {
//                            return true;
//                        }
//                    } catch (MessagingException ex) {
//                        ex.printStackTrace();
//                    }
//                    return false;
//                }
//            };
//
//            int count = inbox.getMessageCount();
//            // get a list of javamail messages as an array of messages
//            Message[] messages = inbox.getMessages(count-15,count);
//            Message[] foundMessages = inbox.search(term);
//
//            TreeSet treeSet = new TreeSet();// ?
//
//            // performs search through the folder
////            Message[] foundMessages = Inbox.search(searchCondition);
////
//            for (int i = 15; i > 0; i--) {
//                Message message = foundMessages[i];
//                String subject = message.getSubject();
//                System.out.println("Found message #" + i + ": " + subject);
//            }
//
//            for(int i = 15; i > 0; i--)
//            {
//                //String subject = MimeUtility.decodeText(getSubject(messages[i]));
//                String from = MimeUtility.decodeText(String.valueOf(getFrom(messages[i])));
//                if ( from!=null)
//                {
//                    from = removeQuotes(from);
//                    treeSet.add(from);
//                }
//            }
//
//            Iterator it = treeSet.iterator();
//            while ( it.hasNext() )
//            {
//                System.out.println("from: " + it.next());
//            }
//
//            //close the inbox folder but do not
//            //remove the messages from the server
//            inbox.close(false);
//            store.close();
//        }
//        catch (NoSuchProviderException nspe)
//        {
//            System.err.println("invalid provider name");
//        }
//        catch (MessagingException me)
//        {
//            System.err.println("messaging exception");
//            me.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static String getFrom(Message javaMailMessage)
//            throws MessagingException
//    {
//        String from = "";
//        Address a[] = javaMailMessage.getFrom();
//        if ( a==null ) return null;
//        for ( int i=0; i<15; i++ )
//        {
//            Address address = a[i];
//            from = from + address.toString();
//        }
//
//        return from;
//    }
//
//    private static String removeQuotes(String stringToModify)
//    {
//        int indexOfFind = stringToModify.indexOf(stringToModify);
//        if ( indexOfFind < 0 ) return stringToModify;
//
//        StringBuffer oldStringBuffer = new StringBuffer(stringToModify);
//        StringBuffer newStringBuffer = new StringBuffer();
//        for ( int i=0, length=oldStringBuffer.length(); i<length; i++ )
//        {
//            char c = oldStringBuffer.charAt(i);
//            if ( c == '"' || c == '\'' )
//            {
//                // do nothing
//            }
//            else
//            {
//                newStringBuffer.append(c);
//            }
//
//        }
//        return new String(newStringBuffer);
//    }
//}