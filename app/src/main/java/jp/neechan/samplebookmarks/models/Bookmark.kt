package jp.neechan.samplebookmarks.models

import java.io.Serializable

data class Bookmark(val id:        Long,
                    val title:     String,
                    val latitude:  Double,
                    val longitude: Double) : Serializable