package jm.through

import jm.through.data.SignInResult

object UserData {
    var token:String = ""
    var trustList = ArrayList<SignInResult.EmailData>()
}