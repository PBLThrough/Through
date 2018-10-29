package jm.through.read;

import javax.mail.Multipart;
import javax.mail.Part;

public abstract class BodyPart implements Part {
    protected Multipart parent;

    final void a(Multipart paramMultipart){
        this.parent = paramMultipart;
    }
    public Multipart getParent(){
        return this.parent;
    }
}
