data class Chat(
    val chatId: Int = 0,
    val senderId: Int = 0,
    val receiverId: Int = 0,
    val messages: MutableList<Message> = mutableListOf(),
    val haveUnreadMsg: Boolean = true,
    var empty: Boolean = false
)

data class Message(
    val msgId: Int = 0,
    val senderId: Int = 0,
    val receiverId: Int = 0,
    val text: String,
    var isReaded: Boolean = false
)