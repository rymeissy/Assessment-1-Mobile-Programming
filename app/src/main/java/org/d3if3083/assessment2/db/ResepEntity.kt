package org.d3if3083.assessment2.db

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipe")
data class ResepEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val namaResep: String,
    val descResep: String,
    val kategori: String,
    val gambar: Int = 0,
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(id)
        dest.writeString(namaResep)
        dest.writeString(descResep)
        dest.writeString(kategori)
        dest.writeInt(gambar)
    }

    companion object CREATOR : Parcelable.Creator<ResepEntity> {
        override fun createFromParcel(parcel: Parcel): ResepEntity {
            return ResepEntity(parcel)
        }

        override fun newArray(size: Int): Array<ResepEntity?> {
            return arrayOfNulls(size)
        }
    }
}
