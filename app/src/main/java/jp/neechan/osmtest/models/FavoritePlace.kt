package jp.neechan.osmtest.models

import java.io.Serializable

class FavoritePlace(val id:        Long,
                    val title:     String,
                    val latitude:  Double,
                    val longitude: Double) : Serializable