package jm.through

import jm.through.account.DetailData

object AccountData {
    var accountList = ArrayList<DetailData>() //이메일 전체 정보(id,pass,platform,count)
    var selectedData:DetailData?=null
}