import exceptions.ChatNotFoundException

class ChatService {
    private val chats = mutableListOf<Chat>()
    private var chatId = 0
    private var msgId = 0

    fun getUnreadChatsCount(userId: Int): Int {
        return chats.filter { (it.senderId == userId || it.receiverId == userId) && it.haveUnreadMsg }.size
    }

    fun getChats(id: Int): List<Chat> {
        return chats.filter { (it.senderId == id || it.receiverId == id) && !it.empty }
    }

    fun getMessagesFromChat(chatId: Int, lastMessageId: Int): List<Message> {
        val chat = chats.find { it.chatId == chatId } ?: throw ChatNotFoundException()
        val messageList = chat.messages.takeLastWhile { lastMessageId != it.msgId }
        messageList.forEach { it.isReaded = true }
        return messageList
    }

    fun sendMessage(senderId: Int, receiverId: Int, text: String) {
        msgId++
        val message = Message(msgId, senderId, receiverId, text)
        val chat =
            chats.find { it.senderId == senderId || it.senderId == receiverId && it.receiverId == receiverId || it.receiverId == senderId }
                ?: createChat(senderId, receiverId)
        chat.messages.add(message)
        if (chat.empty) {
            chat.empty = false
        }
        println("Сообщение добавлено!")
    }

    fun deleteLastMessage(chatId: Int) {
        val chat = chats.find { it.chatId == chatId } ?: throw ChatNotFoundException()

        if (chat.empty) {
            println("Чат пуст, удалять нечего")
            return
        }

        if (chat.messages.size == 1) {
            deleteChat(chatId)
        } else {
            chat.messages.removeLast()
            println("Сообщение удалено")
        }
    }

    fun createChat(senderId: Int, receiverId: Int): Chat {
        chatId++
        val chat = Chat(chatId, senderId, receiverId)
        chats += chat
        return chats.last()
    }

    fun deleteChat(chatId: Int) {
        val chat = chats.find { it.chatId == chatId } ?: throw ChatNotFoundException()
        chat.messages.clear()
        chat.empty = true
        println("Чат удален")
    }
}