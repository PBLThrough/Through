package jm.through.read

import android.widget.ImageView
import java.util.*

data class ReadData (
        var mailTitle:String,
        var mailMemo:String,
        var mailDate: Date,
       // var mailImage:ImageView,
        var check:Boolean
)