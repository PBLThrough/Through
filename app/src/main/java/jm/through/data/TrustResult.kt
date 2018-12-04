package jm.through.data

class TrustResult {
    var list = ArrayList<TrustEmailData>()

    inner class TrustEmailData{
        var email:String = ""
    }
}