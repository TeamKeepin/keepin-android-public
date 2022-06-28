package com.keepin.android.data.entity.response

import android.os.Parcelable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResponseDetailPresent(
    val data: Data,
    val message: String,
    val status: Int
) : Parcelable

@Parcelize
data class Data(
    val _id: String,
    val category: List<String>,
    val date: String,
    val friends: List<Friend>,
    val photo: List<String>,
    val record: String,
    val taken: Boolean,
    val title: String
) : Parcelable {

    fun getFriendsText(format: String, color: Int): SpannableStringBuilder {
        return if (friends.size == 1) {
            getFormattedText(format, "", color)
        } else {
            getFormattedText(format, " 외 " + (friends.size - 1) + "명", color)
        }
    }

    private fun getFormattedText(
        format: String,
        numberOfFriends: String = "",
        color: Int
    ): SpannableStringBuilder {
        val friendsStr = String.format(format, friends[0].name, numberOfFriends, getIsTaken())
        return SpannableStringBuilder(friendsStr).apply {
            setSpan(
                ForegroundColorSpan(color),
                friendsStr.length - 5,
                friendsStr.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    private fun getIsTaken(): String {
        return when (taken) {
            true -> "받은 선물"
            false -> "준 선물"
        }
    }
}
