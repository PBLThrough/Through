package jm.through

object AccountData {
    var accountList = ArrayList<DetailData>() //이메일 전체 정보(id,pass,platform,count)
    var selectedMail: String? = null //선택한 이메일
    var selectedPass:String?=null
}