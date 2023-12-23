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


import com.fasterxml.jackson.annotation.JsonProperty

/**
 * 
 *
 * @param id 
 * @param isBot 
 * @param firstName 
 * @param lastName 
 * @param username 
 * @param languageCode 
 * @param canJoinGroups 
 * @param canReadAllGroupMessages 
 * @param supportsInlineQueries 
 */


data class UserDto (

    @field:JsonProperty("id")
    val id: kotlin.Long? = null,

    @field:JsonProperty("is_bot")
    val isBot: kotlin.Boolean? = null,

    @field:JsonProperty("first_name")
    val firstName: kotlin.String? = null,

    @field:JsonProperty("last_name")
    val lastName: kotlin.String? = null,

    @field:JsonProperty("username")
    val username: kotlin.String? = null,

    @field:JsonProperty("language_code")
    val languageCode: kotlin.String? = null,

    @field:JsonProperty("can_join_groups")
    val canJoinGroups: kotlin.Boolean? = null,

    @field:JsonProperty("can_read_all_group_messages")
    val canReadAllGroupMessages: kotlin.Boolean? = null,

    @field:JsonProperty("supports_inline_queries")
    val supportsInlineQueries: kotlin.Boolean? = null

)

