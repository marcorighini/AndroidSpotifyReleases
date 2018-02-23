package net.marcorighini.spotifyreleases.misc.model

import android.os.Parcel
import android.os.Parcelable


data class AlbumSimple(val id: String, val cover: String, val title: String, val artist: String) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(cover)
        parcel.writeString(title)
        parcel.writeString(artist)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AlbumSimple> {
        override fun createFromParcel(parcel: Parcel): AlbumSimple {
            return AlbumSimple(parcel)
        }

        override fun newArray(size: Int): Array<AlbumSimple?> {
            return arrayOfNulls(size)
        }
    }
}