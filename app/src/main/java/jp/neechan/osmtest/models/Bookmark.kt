package jp.neechan.osmtest.models

import java.io.Serializable

data class Bookmark(val id:        Long,
                    val title:     String,
                    val latitude:  Double,
                    val longitude: Double) : Serializable