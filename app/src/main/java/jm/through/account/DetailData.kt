package jm.through.account

import android.os.Parcel
import android.os.Parcelable



//번들에 담기 위해서 Parcelable(소포꾸러미)에 담음,
data class DetailData(
        var platform:String,
        var id:String,
        var pass:String,
        var count:Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt())

    //parcel안에 내용물 넣어주는 작업, 이때 read해주는 작업과 순서가 동일하게 넣어주어야 한다.
    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(platform)
        dest?.writeString(id)
        dest?.writeString(pass)
        dest?.writeInt(count)
    }

    //보통 구현을 안하고 0을 리
    override fun describeContents(): Int {
        return 0
    }

    //CREATOR
    //한 객체의 표현방식을 저장 또는 전송에 적합한 다른 데이터 형식으로 변환할  Parcelable클래스로 변환해주는 역할
    companion object CREATOR : Parcelable.Creator<DetailData> {
        override fun createFromParcel(parcel: Parcel): DetailData {
            return DetailData(parcel)
        }

        override fun newArray(size: Int): Array<DetailData?> {
            return arrayOfNulls(size)
        }
    }

}


