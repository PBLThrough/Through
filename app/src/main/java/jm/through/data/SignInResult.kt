package jm.through.data

class SignInResult {
    var token:String = ""
    var SocialEmailList = ArrayList<SocialEmailData>() //계정 정보
    var EmailList = ArrayList<EmailData>() //신뢰리스트


    inner class SocialEmailData {
        var email:String = ""
        var passwd:String = ""
    }

    inner class EmailData{
        var email:String = ""
    }
}
