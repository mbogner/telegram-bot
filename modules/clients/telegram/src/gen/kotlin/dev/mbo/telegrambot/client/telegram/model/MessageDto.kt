/**
 *
 * Please note:
 * This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * Do not edit this file manually.
 *
 */

@file:Suppress(
    "ArrayInDataClass",
    "EnumEntryName",
    "RemoveRedundantQualifierName",
    "UnusedImport"
)

package dev.mbo.telegrambot.client.telegram.model

import dev.mbo.telegrambot.client.telegram.model.ChatDto
import dev.mbo.telegrambot.client.telegram.model.UserDto

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * 
 *
 * @param messageId 
 * @param from 
 * @param senderChat 
 * @param date 
 * @param chat 
 * @param forwardFrom 
 * @param forwardFromChat 
 * @param forwardFromMessageId 
 * @param forwardSignature 
 * @param forwardSenderName 
 * @param forwardDate 
 * @param isAutomaticForward 
 * @param replyToMessage 
 * @param viaBot 
 * @param editDate 
 * @param hasProtectedContent 
 * @param mediaGroupId 
 * @param authorSignature 
 * @param text 
 * @param leftChatMember 
 * @param newChatTitle 
 * @param deleteChatPhoto 
 * @param groupChatCreated 
 * @param supergroupChatCreated 
 * @param channelChatCreated 
 * @param migrateToChatId 
 * @param migrateFromChatId 
 * @param pinnedMessage 
 * @param connectedWebsite 
 */


data class MessageDto (

    @field:JsonProperty("message_id")
    val messageId: kotlin.Int? = null,

    @field:JsonProperty("from")
    val from: UserDto? = null,

    @field:JsonProperty("sender_chat")
    val senderChat: ChatDto? = null,

    @field:JsonProperty("date")
    val date: kotlin.Int? = null,

    @field:JsonProperty("chat")
    val chat: ChatDto? = null,

    @field:JsonProperty("forward_from")
    val forwardFrom: UserDto? = null,

    @field:JsonProperty("forward_from_chat")
    val forwardFromChat: ChatDto? = null,

    @field:JsonProperty("forward_from_message_id")
    val forwardFromMessageId: kotlin.Int? = null,

    @field:JsonProperty("forward_signature")
    val forwardSignature: kotlin.String? = null,

    @field:JsonProperty("forward_sender_name")
    val forwardSenderName: kotlin.String? = null,

    @field:JsonProperty("forward_date")
    val forwardDate: kotlin.Int? = null,

    @field:JsonProperty("is_automatic_forward")
    val isAutomaticForward: kotlin.Boolean? = null,

    @field:JsonProperty("reply_to_message")
    val replyToMessage: MessageDto? = null,

    @field:JsonProperty("via_bot")
    val viaBot: UserDto? = null,

    @field:JsonProperty("edit_date")
    val editDate: kotlin.Int? = null,

    @field:JsonProperty("has_protected_content")
    val hasProtectedContent: kotlin.Boolean? = null,

    @field:JsonProperty("media_group_id")
    val mediaGroupId: kotlin.String? = null,

    @field:JsonProperty("author_signature")
    val authorSignature: kotlin.String? = null,

    @field:JsonProperty("text")
    val text: kotlin.String? = null,

    @field:JsonProperty("left_chat_member")
    val leftChatMember: UserDto? = null,

    @field:JsonProperty("new_chat_title")
    val newChatTitle: kotlin.String? = null,

    @field:JsonProperty("delete_chat_photo")
    val deleteChatPhoto: kotlin.Boolean? = null,

    @field:JsonProperty("group_chat_created")
    val groupChatCreated: kotlin.Boolean? = null,

    @field:JsonProperty("supergroup_chat_created")
    val supergroupChatCreated: kotlin.Boolean? = null,

    @field:JsonProperty("channel_chat_created")
    val channelChatCreated: kotlin.Boolean? = null,

    @field:JsonProperty("migrate_to_chat_id")
    val migrateToChatId: kotlin.Int? = null,

    @field:JsonProperty("migrate_from_chat_id")
    val migrateFromChatId: kotlin.Int? = null,

    @field:JsonProperty("pinned_message")
    val pinnedMessage: MessageDto? = null,

    @field:JsonProperty("connected_website")
    val connectedWebsite: kotlin.String? = null

)

