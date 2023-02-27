package com.net.pvr.ui.payment.response

import android.os.Parcel
import android.os.Parcelable

class Promomap private constructor(`in`: Parcel) : Parcelable {
    var key: String?
    var value: String?

    init {
        key = `in`.readString()
        value = `in`.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(key)
        dest.writeString(value)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Promomap?> = object : Parcelable.Creator<Promomap?> {
            override fun createFromParcel(`in`: Parcel): Promomap? {
                return Promomap(`in`)
            }

            override fun newArray(size: Int): Array<Promomap?> {
                return arrayOfNulls(size)
            }
        }
    }
}