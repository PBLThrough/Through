//package jm.through.read
//
//import javax.mail.BodyPart
//import javax.mail.Message
//import javax.mail.Multipart
//
//public class MessageContext
//{
//    private Part a;
//
//    public MessageContext(Part paramPart)
//    {
//        this.a = paramPart;
//    }
//
//    public Message getMessage()
//    {
//        try
//        {
//            for (Object localObject = this.a; localObject != null; localObject = ((Multipart)localObject).getParent())
//            {
//                if ((localObject instanceof Message)) {
//                    return (Message)localObject;
//                }
//                localObject = ((BodyPart)localObject).getParent();
//                if (localObject == null) {
//                    return null;
//                }
//            }
//            return null;
//        }
//        catch (MessagingException localMessagingException) {}
//        return null;
//    }
//
//    public Part getPart()
//    {
//        return this.a;
//    }
//
//    public Session getSession()
//    {
//        Message localMessage = getMessage();
//        if (localMessage != null) {
//            return localMessage.session;
//        }
//        return null;
//    }