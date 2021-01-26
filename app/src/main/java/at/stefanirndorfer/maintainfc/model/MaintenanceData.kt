package at.stefanirndorfer.maintainfc.model

import android.os.Parcel
import android.os.Parcelable

class MaintenanceData(
        var employeeId: Int?,
        var timestamp: Long?,
        var nextTimestamp: Long?,
        var comment: String?) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Long::class.java.classLoader) as? Long,
            parcel.readValue(Long::class.java.classLoader) as? Long,
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(employeeId)
        parcel.writeValue(timestamp)
        parcel.writeValue(nextTimestamp)
        parcel.writeString(comment)
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