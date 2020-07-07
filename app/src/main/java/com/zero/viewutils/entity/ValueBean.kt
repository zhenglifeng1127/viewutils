package com.zero.viewutils.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ValueBean(
    var id: Int = 0,
    var name: String? = null,
    var value: String? = null
) : Parcelable