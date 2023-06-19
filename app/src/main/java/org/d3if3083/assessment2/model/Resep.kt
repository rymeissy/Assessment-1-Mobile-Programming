package org.d3if3083.assessment2.model

import android.os.Parcel
import android.os.Parcelable
import org.d3if3083.assessment2.db.ResepEntity
import java.io.Serializable

data class Resep(
    val id: Long = 0L,
    val namaResep: String,
    val descResep: String,
    val kategori: String,
    val gambarId: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(id)
        dest.writeString(namaResep)
        dest.writeString(descResep)
        dest.writeString(kategori)
        dest.writeString(gambarId)
    }

    fun toResepEntity(): ResepEntity {
        return ResepEntity(
            id = id,
            namaResep = namaResep,
            descResep = descResep,
            kategori = kategori,
            gambarId = gambarId
        )
    }

    companion object CREATOR : Parcelable.Creator<Resep> {
        override fun createFromParcel(parcel: Parcel): Resep {
            return Resep(parcel)
        }

        override fun newArray(size: Int): Array<Resep?> {
            return arrayOfNulls(size)
        }
    }
}
