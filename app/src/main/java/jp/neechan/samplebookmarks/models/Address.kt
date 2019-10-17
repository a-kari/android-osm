package jp.neechan.samplebookmarks.models

import com.google.gson.annotations.SerializedName

data class Address(@SerializedName("osm_id")       val id: Long,
                   @SerializedName("display_name") val address: String)