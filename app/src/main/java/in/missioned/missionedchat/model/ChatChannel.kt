package `in`.missioned.missionedchat.model

data class ChatChannel(val userIds: MutableList<String>) {
    constructor() : this(mutableListOf())
}