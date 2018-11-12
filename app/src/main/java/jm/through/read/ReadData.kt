package jm.through.read

import java.util.*
import javax.mail.Message

data class ReadData (
        var mailTitle:String,
        var mailMemo:String,
        var mailDate: Date?,
        var mailContenttype: String,
       // var mailSize: Int,
        var mailContent: Object, //Message,
       // var mailImage:ImageView,
        var check:Boolean
)