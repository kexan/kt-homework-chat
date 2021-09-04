import exceptions.ChatNotFoundException
import org.junit.Assert.assertEquals
import org.junit.Test

class ChatServiceTest {

    @Test
    fun getUnreadChatsCount() {
        val service = ChatService()
        service.sendMessage(1, 2, "Тестовое сообщение 1")
        service.sendMessage(1, 2, "Тестовое сообщение 2")
        service.sendMessage(3, 2, "Тестовое сообщение 3")

        val result = service.getUnreadChatsCount(2)
        assertEquals(result, 2)
    }

    @Test
    fun getChats() {
        val service = ChatService()
        val chat1 = Chat(1, 1, 2)
        val chat2 = Chat(2, 2, 3)
        val expected = mutableListOf<Chat>()
        expected.add(chat1)
        expected.add(chat2)

        service.createChat(1, 2)
        service.createChat(2, 3)
        val result = service.getChats(2)

        assertEquals(result, expected)
    }

    @Test(expected = ChatNotFoundException::class)
    fun getMessagesFromChatThrowException() {
        val service = ChatService()
        service.sendMessage(1, 2, "Тестовое сообщение 1")
        service.sendMessage(2, 1, "Тестовое сообщение 2")
        service.getMessagesFromChat(2, 0)
    }

    @Test
    fun getMessagesFromChat() {
        val service = ChatService()
        val msg1 = Message(1, 1, 2, "Тестовое сообщение 1", isReaded = true)
        val msg2 = Message(2, 1, 2, "Тестовое сообщение 2", isReaded = true)
        val expected = mutableListOf<Message>()
        expected.add(msg1)
        expected.add(msg2)

        service.sendMessage(msg1.senderId, msg1.receiverId, msg1.text)
        service.sendMessage(msg2.senderId, msg2.receiverId, msg2.text)
        val result = service.getMessagesFromChat(1, 0)

        assertEquals(result, expected)
    }

    @Test
    fun sendMessage() {
        val service = ChatService()
        val msg1 = Message(1, 1, 2, "Тестовое сообщение 1", isReaded = true)
        val msg2 = Message(2, 1, 2, "Тестовое сообщение 2", isReaded = true)
        val expected = mutableListOf<Message>()
        expected.add(msg1)
        expected.add(msg2)

        service.sendMessage(msg1.senderId, msg1.receiverId, msg1.text)
        service.sendMessage(msg2.senderId, msg2.receiverId, msg2.text)
        val result = service.getMessagesFromChat(1, 0)

        assertEquals(result, expected)
    }

    @Test(expected = NoSuchElementException::class)
    fun deleteLastMessageShouldDeleteChat() {
        val service = ChatService()
        val expected = Chat(empty = true)

        service.sendMessage(1, 2, "Тестовое сообщение 1")
        service.sendMessage(1, 2, "Тестовое сообщение 2")
        service.deleteLastMessage(1)
        service.deleteLastMessage(1)
        val chatsFromService = service.getChats(1)
        val result = chatsFromService.last()

        assertEquals(result, expected)
    }

    @Test
    fun deleteLastMessage() {
        val service = ChatService()
        val msg1 = Message(1, 1, 2, "Тестовое сообщение 1")
        val msgList = mutableListOf<Message>()
        msgList.add(msg1)
        val expected = Chat(1, 1, 2, messages = msgList)

        service.sendMessage(1, 2, "Тестовое сообщение 1")
        service.sendMessage(1, 2, "Тестовое сообщение 2")
        service.deleteLastMessage(1)
        val chatsFromService = service.getChats(1)
        val result = chatsFromService.last()

        assertEquals(result, expected)
    }

    @Test
    fun createChat() {
        val service = ChatService()
        val expected = Chat(1, 1, 2)

        val result = service.createChat(1, 2)
        assertEquals(result, expected)
    }

    @Test
    fun deleteChat() {
        val service = ChatService()
        val expected = Chat(1, 1, 2, empty = true)

        val result = service.createChat(1, 2)
        service.sendMessage(1, 2, "Тестовое сообщение 1")
        service.deleteChat(1)

        assertEquals(result, expected)
    }
}