package com.example.messenger.components

import com.example.messenger.helpers.objects.ConversationListVO
import com.example.messenger.helpers.objects.ConversationVO
import com.example.messenger.helpers.objects.MessageVO
import com.example.messenger.models.Conversation
import com.example.messenger.services.ConversationServiceImpl
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class ConversationAssembler(val conversationService: ConversationServiceImpl, val messageAssembler: MessageAssembler) {



    fun toConversationVO(conversation: Conversation, userId: Long): ConversationVO {
        val conversationMessages: ArrayList<MessageVO> = ArrayList()
        val finalConversationMessages: ArrayList<MessageVO> = ArrayList()
        var count = 0;
        conversation.messages?.mapTo(conversationMessages){ messageAssembler.toMessageVO(it)}
        conversationMessages.forEach {
            if(it.unread == userId){
                count++;
            }
        }
        finalConversationMessages.add(conversationMessages[conversationMessages.size-1])
        return ConversationVO(conversation.id, conversationService
                .nameSecondParty(conversation, userId),
            conversationService.idSecondParty(conversation, userId),
            conversationService.urlSecondParty(conversation, userId),
            conversationService.secondPartyLastOnline(conversation, userId), finalConversationMessages, count)
    }

    fun toConversationListVO(conversations: ArrayList<Conversation>, userId: Long): ConversationListVO {
        val conversationVOList = conversations.map { toConversationVO(it, userId) }
        return  ConversationListVO(conversationVOList)
    }
}