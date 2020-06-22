package `in`.missioned.missionedchat.ui.recyclerView

import `in`.missioned.missionedchat.R
import `in`.missioned.missionedchat.glide.GlideApp
import `in`.missioned.missionedchat.model.ImageMessage
import `in`.missioned.missionedchat.util.StorageUtil
import android.content.Context
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.item_image_message.*

class ImageMessageItem(val message: ImageMessage,
val context: Context) : MessageItem(message) {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        super.bind(viewHolder, position)
        GlideApp.with(context)
            .load(StorageUtil.pathToReference(message.imagePath))
            .placeholder(R.drawable.ic_image_black_24dp)
            .into(viewHolder.imageView_message_image)
    }

    override fun getLayout(): Int = R.layout.item_image_message

    override fun isSameAs(other: com.xwray.groupie.Item<*>): Boolean {
        return !(other !is ImageMessageItem || this.message != other.message)
    }

    override fun equals(other: Any?): Boolean {
        return isSameAs(other as ImageMessageItem)
    }

    override fun hashCode(): Int {
        var result = message.hashCode()
        result = 31 * result + context.hashCode()
        return result
    }
}