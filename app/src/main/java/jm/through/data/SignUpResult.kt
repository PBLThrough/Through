package jm.through.data

data class SignUpResult(

        var token: String,
        var id: String,
        var passwd: String,
        var name: String,
        var emailList: ArrayList<String>

)
