package `in`.missioned.missionedchat.ui.people

import `in`.missioned.missionedchat.R
import `in`.missioned.missionedchat.util.FirestoreUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.fragment_people.*

class PeopleFragment : Fragment() {
    private lateinit var peopleViewModel: PeopleViewModel
    private lateinit var userListenerRegistration: ListenerRegistration

    private var shouldInitRecyclerView = true

    private lateinit var peopleSection: Section

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        userListenerRegistration =
            FirestoreUtil.addUsersListener(this.activity!!, this::updateRecyclerView)
        peopleViewModel =
            ViewModelProviders.of(this).get(PeopleViewModel::class.java)
        //        peopleViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        return inflater.inflate(R.layout.fragment_people, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        FirestoreUtil.removeListener(userListenerRegistration)
        shouldInitRecyclerView = true
    }

    private fun updateRecyclerView(items: List<Item>) {

        fun init() {
            recycler_view_people.apply {
                layoutManager = LinearLayoutManager(this@PeopleFragment.context)
                adapter = GroupAdapter<GroupieViewHolder>().apply {
                    peopleSection = Section(items)
                    add(peopleSection)
                }
            }
            shouldInitRecyclerView = false
        }

        fun updateItems() {}

        if (shouldInitRecyclerView) {
            init()
        } else {
            updateItems()
        }
    }
}
