package jm.through.read

import android.widget.ImageView
import java.util.*

data class ReadData (
        var mailTitle:String,
        var mailMemo:String,
        var mailDate: Date,
       // var mailSize: Int,
//        var mailContent: Object,
       // var mailImage:ImageView,
        var check:Boolean
)