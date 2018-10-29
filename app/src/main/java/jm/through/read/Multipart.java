//package jm.through.read;
//import java.io.OutputStream;
//import java.util.Vector;
//import javax.mail.BodyPart;
//import javax.mail.MessagingException;
//import javax.mail.MultipartDataSource;
//import javax.mail.Part;
//
//public abstract class Multipart
//{
//    protected String contentType = "multipart/mixed";
//    protected Part parent;
//    protected Vector parts = new Vector();
//
//    public void addBodyPart(BodyPart paramBodyPart)
//    {
//        try
//        {
//            if (this.parts == null) {
//                this.parts = new Vector();
//            }
//            this.parts.addElement(paramBodyPart);
//            paramBodyPart.a(this);
//            return;
//        }
//        finally {}
//    }
//
//    public void addBodyPart(BodyPart paramBodyPart, int paramInt)
//    {
//        try
//        {
//            if (this.parts == null) {
//                this.parts = new Vector();
//            }
//            this.parts.insertElementAt(paramBodyPart, paramInt);
//            paramBodyPart.a(this);
//            return;
//        }
//        finally {}
//    }
//
//    public BodyPart getBodyPart(int paramInt)
//    {
//        try
//        {
//            if (this.parts == null) {
//                throw new IndexOutOfBoundsException("No such BodyPart");
//            }
//        }
//        finally {}
//        BodyPart localBodyPart = (BodyPart)this.parts.elementAt(paramInt);
//        return localBodyPart;
//    }
//
//    public String getContentType()
//    {
//        try
//        {
//            String str = this.contentType;
//            return str;
//        }
//        finally
//        {
//            localObject = finally;
//            throw ((Throwable)localObject);
//        }
//    }
//
//
//    public Part getParent()
//    {
//        try
//        {
//            Part localPart = this.parent;
//            return localPart;
//        }
//        finally
//        {
//            localObject = finally;
//            throw ((Throwable)localObject);
//        }
//    }
//
//    public void removeBodyPart(int paramInt)
//    {
//        try
//        {
//            if (this.parts == null) {
//                throw new IndexOutOfBoundsException("No such BodyPart");
//            }
//        }
//        finally {}
//        BodyPart localBodyPart = (BodyPart)this.parts.elementAt(paramInt);
//        this.parts.removeElementAt(paramInt);
//        localBodyPart.a(null);
//    }
//
//    public boolean removeBodyPart(BodyPart paramBodyPart)
//    {
//        try
//        {
//            if (this.parts == null) {
//                throw new MessagingException("No such body part");
//            }
//        }
//        finally {}
//        boolean bool = this.parts.removeElement(paramBodyPart);
//        paramBodyPart.a(null);
//        return bool;
//    }
//
//    protected void setMultipartDataSource(MultipartDataSource paramMultipartDataSource)
//    {
//        try
//        {
//            this.contentType = paramMultipartDataSource.getContentType();
//            int j = paramMultipartDataSource.getCount();
//            int i = 0;
//            while (i < j)
//            {
//                addBodyPart(paramMultipartDataSource.getBodyPart(i));
//                i += 1;
//            }
//            return;
//        }
//        finally {}
//    }
//
//    public void setParent(Part paramPart)
//    {
//        try
//        {
//            this.parent = paramPart;
//            return;
//        }
//        finally
//        {
//            paramPart = finally;
//            throw paramPart;
//        }
//    }
//
//    public abstract void writeTo(OutputStream paramOutputStream);
//}
