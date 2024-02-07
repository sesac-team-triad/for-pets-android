import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChatList(
    val roomKey: String,
    val friendName: String,
    val reqUid: String,
    val volUid: String,
    val token: String,
    val lastMessage: String,
    val lastMessageTime: String,
    val unreadMessageCount: String,
    val reqNickname: String,
    val volNickname: String,
    val transportReqKey: String
) {
    constructor() : this(
        "", "", "","",
         "", "", "", "","", "", ""
    )
}
