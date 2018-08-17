package jm.through.read;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;
import java.io.*;

/**
 * imap 실험중
 * */
public class AddressExtractor
{

    public static void main(String[] args)
    {
        Properties props = new Properties();

        String host      = args[0];
        String username  = args[1];
        String password  = args[2];
        //String provider  = "pop3";
        String provider  = "imap";

        try
        {
            //Connect to the server
            Session session = Session.getDefaultInstance(props, null);
            Store store     = session.getStore(provider);
            store.connect(host, username, password);

            //open the inbox folder
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            // get a list of javamail messages as an array of messages
            Message[] messages = inbox.getMessages();

            TreeSet treeSet = new TreeSet();

            for(int i = 0; i < messages.length; i++)
            {
                String from = getFrom(messages[i]);
                if ( from!=null)
                {
                    from = removeQuotes(from);
                    treeSet.add(from);
                }
            }

            Iterator it = treeSet.iterator();
            while ( it.hasNext() )
            {
                System.out.println("from: " + it.next());
            }

            //close the inbox folder but do not
            //remove the messages from the server
            inbox.close(false);
            store.close();
        }
        catch (NoSuchProviderException nspe)
        {
            System.err.println("invalid provider name");
        }
        catch (MessagingException me)
        {
            System.err.println("messaging exception");
            me.printStackTrace();
        }
    }

    private static String getFrom(Message javaMailMessage)
            throws MessagingException
    {
        String from = "";
        Address a[] = javaMailMessage.getFrom();
        if ( a==null ) return null;
        for ( int i=0; i<15; i++ )
        {
            Address address = a[i];
            from = from + address.toString();
        }

        return from;
    }

    private static String removeQuotes(String stringToModify)
    {
        int indexOfFind = stringToModify.indexOf(stringToModify);
        if ( indexOfFind < 0 ) return stringToModify;

        StringBuffer oldStringBuffer = new StringBuffer(stringToModify);
        StringBuffer newStringBuffer = new StringBuffer();
        for ( int i=0, length=oldStringBuffer.length(); i<length; i++ )
        {
            char c = oldStringBuffer.charAt(i);
            if ( c == '"' || c == '\'' )
            {
                // do nothing
            }
            else
            {
                newStringBuffer.append(c);
            }

        }
        return new String(newStringBuffer);
    }

}