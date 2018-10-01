package jm.through.read

import java.util.*

data class ReadData (
        var mailTitle:String,
        var mailMemo:String,
        var mailDate: Date?,
        var mailContenttype: String,
       // var mailSize: Int,
        var mailContent: Object,
       // var mailImage:ImageView,
        var check:Boolean
)