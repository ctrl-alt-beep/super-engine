package `in`.missioned.missionedchat.model

import java.util.*

class ImageMessage (
    val imagePath: String,
    override val time: Date,
    override val senderId: String,
    override val type: String = MessageType.IMAGE
) : Message {
    constructor() : this("", Date(0), "")
}