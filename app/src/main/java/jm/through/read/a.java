//package jm.through.read;
//import java.util.Vector;
//import javax.mail.event.MailEvent;
//
//final class a implements Runnable
//{
//    private a a = null;
//    private a b = null;
//    private Thread c = new Thread(this, "JavaMail-EventQueue");
//
//    public a()
//    {
//        this.c.setDaemon(true);
//        this.c.start();
//    }
//
//    private a a()
//    {
//        try
//        {
//            while (this.b == null) {
//                wait();
//            }
//            locala = this.b;
//        }
//        finally {}
//        a locala;
//        this.b = locala.b;
//        if (this.b == null) {
//            this.a = null;
//        }
//        for (;;)
//        {
//            locala.a = null;
//            locala.b = null;
//            return locala;
//            this.b.a = null;
//        }
//    }
//
//    /* Error */
//    public final void a(MailEvent paramMailEvent, Vector paramVector)
//    {
//        // Byte code:
//        //   0: aload_0
//        //   1: monitorenter
//        //   2: new 8 javax/mail/a$a
//        //   5: dup
//        //   6: aload_1
//        //   7: aload_2
//        //   8: invokespecial 47 javax/mail/a$a:<init> (Ljavax/mail/event/MailEvent;Ljava/util/Vector;)V
//        //   11: astore_1
//        //   12: aload_0
//        //   13: getfield 19 javax/mail/a:a Ljavax/mail/a$a;
//        //   16: ifnonnull +20 -> 36
//        //   19: aload_0
//        //   20: aload_1
//        //   21: putfield 19 javax/mail/a:a Ljavax/mail/a$a;
//        //   24: aload_0
//        //   25: aload_1
//        //   26: putfield 21 javax/mail/a:b Ljavax/mail/a$a;
//        //   29: aload_0
//        //   30: invokevirtual 50 java/lang/Object:notifyAll ()V
//        //   33: aload_0
//        //   34: monitorexit
//        //   35: return
//        //   36: aload_1
//        //   37: aload_0
//        //   38: getfield 19 javax/mail/a:a Ljavax/mail/a$a;
//        //   41: putfield 44 javax/mail/a$a:a Ljavax/mail/a$a;
//        //   44: aload_0
//        //   45: getfield 19 javax/mail/a:a Ljavax/mail/a$a;
//        //   48: aload_1
//        //   49: putfield 43 javax/mail/a$a:b Ljavax/mail/a$a;
//        //   52: aload_0
//        //   53: aload_1
//        //   54: putfield 19 javax/mail/a:a Ljavax/mail/a$a;
//        //   57: goto -28 -> 29
//        //   60: astore_1
//        //   61: aload_0
//        //   62: monitorexit
//        //   63: aload_1
//        //   64: athrow
//        // Local variable table:
//        //   start length slot name signature
//        //   0 65 0 this a
//        //   0 65 1 paramMailEvent MailEvent
//        //   0 65 2 paramVector Vector
//        // Exception table:
//        //   from to target type
//        //   2 29 60 finally
//        //   29 33 60 finally
//        //   36 57 60 finally
//    }
//
//    public final void run()
//    {
//        try
//        {
//            Object localObject = a();
//            if (localObject != null)
//            {
//                MailEvent localMailEvent = ((a)localObject).c;
//                localObject = ((a)localObject).d;
//                int i = 0;
//                for (;;)
//                {
//                    int j = ((Vector)localObject).size();
//                    if (i >= j) {
//                        break;
//                    }
//                    try
//                    {
//                        localMailEvent.dispatch(((Vector)localObject).elementAt(i));
//                        i += 1;
//                    }
//                    catch (Throwable localThrowable)
//                    {
//                        boolean bool;
//                        do
//                        {
//                            bool = localThrowable instanceof InterruptedException;
//                        } while (!bool);
//                    }
//                }
//            }
//            return;
//        }
//        catch (InterruptedException localInterruptedException) {}
//    }
//
//    static final class a
//    {
//        a a = null;
//        a b = null;
//        MailEvent c = null;
//        Vector d = null;
//
//        a(MailEvent paramMailEvent, Vector paramVector)
//        {
//            this.c = paramMailEvent;
//            this.d = paramVector;
//        }
//    }
//}