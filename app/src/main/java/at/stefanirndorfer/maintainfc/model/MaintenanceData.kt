package at.stefanirndorfer.maintainfc.model

import android.os.Parcel
import android.os.Parcelable

class MaintenanceData(
    var timestamp: Long?,
    var nextTimestamp: Long?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readValue(Long::class.java.classLoader) as? Long
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(timestamp)
        parcel.writeValue(nextTimestamp)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MaintenanceData> {
        override fun createFromParcel(parcel: Parcel): MaintenanceData {
            return MaintenanceData(parcel)
        }

        override fun newArray(size: Int): Array<MaintenanceData?> {
            return arrayOfNulls(size)
        }
    }

}