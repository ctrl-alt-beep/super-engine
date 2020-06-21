package `in`.missioned.missionedchat.item

import `in`.missioned.missionedchat.R
import `in`.missioned.missionedchat.glide.GlideApp
import `in`.missioned.missionedchat.model.User
import `in`.missioned.missionedchat.util.StorageUtil
import android.content.Context
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
//import glide.GlideApp
import kotlinx.android.synthetic.main.item_person.*
import kotlinx.android.synthetic.main.item_person.view.*
import kotlinx.android.synthetic.main.item_person.view.textView_name

class PersonItem(val person: User,
                 val userId: String,
                 private val context: Context)
    :Item(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.textView_name.text = person.name
        viewHolder.textView_name.text = person.bio
        if (person.profilePicturePath != null)
            GlideApp.with(context)
                .load(StorageUtil.pathToReference(person.profilePicturePath))
                .placeholder(R.drawable.ic_account_circle_black_24dp)
                .into(viewHolder.imageView_profile_picture)
    }

    override fun getLayout() = R.layout.item_person
    }