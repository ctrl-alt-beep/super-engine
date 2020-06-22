package `in`.missioned.missionedchat.ui.recyclerView

import `in`.missioned.missionedchat.R
import `in`.missioned.missionedchat.model.TextMessage
import android.content.Context
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.item_text_message.*

class TextMessageItem(
    val message: TextMessage,
    val context: Context
) : MessageItem(message) {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.textView_message_text.text = message.text
        super.bind(viewHolder, position)
    }


    override fun getLayout(): Int = R.layout.item_text_message

    override fun isSameAs(other: com.xwray.groupie.Item<*>): Boolean {
        return !(other !is TextMessageItem || this.message != other.message)
    }

    override fun equals(other: Any?): Boolean {
        return isSameAs(other as TextMessageItem)
    }

    override fun hashCode(): Int {
        var result = message.hashCode()
        result = 31 * result + context.hashCode()
        return result
    }
}