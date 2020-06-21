package `in`.missioned.missionedchat.ui.my_account

import `in`.missioned.missionedchat.R
import `in`.missioned.missionedchat.SignInActivity
import `in`.missioned.missionedchat.glide.GlideApp
import `in`.missioned.missionedchat.util.FirestoreUtil
import `in`.missioned.missionedchat.util.StorageUtil
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.ui.foundation.Clickable
import androidx.ui.foundation.Text
import com.bumptech.glide.Glide
import com.firebase.ui.auth.AuthUI
//import glide.GlideApp
import kotlinx.android.synthetic.main.fragment_my_account.*
import kotlinx.android.synthetic.main.fragment_my_account.view.*
import kotlinx.android.synthetic.main.fragment_my_account.view.name_input
import java.io.ByteArrayOutputStream

class MyAccountFragment: Fragment() {
    private lateinit var myAccountViewModel: MyAccountViewModel
    private val RC_SELECT_IMAGE = 2
    private lateinit var selectedImageBytes: ByteArray
    private var pictureJustChanged = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myAccountViewModel =
            ViewModelProviders.of(this).get(MyAccountViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_my_account, container, false)
//        val textView: TextView = root.findViewById(R.id.text_my_account)
//        myAccountViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        view.apply {
            imageView_profile_picture.setOnClickListener {
                val intent = Intent().apply {
                    type = "image/*"
                    action = Intent.ACTION_GET_CONTENT
                    putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
                }
                startActivityForResult(
                    Intent.createChooser(intent, "Select Image"),
                    RC_SELECT_IMAGE
                )
            }
            btn_save.setOnClickListener {
                if (::selectedImageBytes.isInitialized)
                    StorageUtil.uploadProfilePhoto(selectedImageBytes) {
                        imagePath ->  FirestoreUtil.updateCurrentUser(name_input.editText?.text.toString(),
                                                bio_input.editText?.text.toString(), imagePath )
                    }
                else
                    FirestoreUtil.updateCurrentUser(name_input.editText?.text.toString(),
                        bio_input.editText?.text.toString(), null )
                Toast.makeText(activity, "Saved", Toast.LENGTH_SHORT).show()
            }
            btn_sign_out.setOnClickListener {
                AuthUI.getInstance()
                    .signOut(this@MyAccountFragment.context!!)
                    .addOnCompleteListener {
                        val intent = Intent(activity, SignInActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                    }
            }
        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SELECT_IMAGE && resultCode == Activity.RESULT_OK &&
            data != null && data.data != null
        ) {
            val selectedImagePath = data.data
//            val selectedImageBmp = MediaStore.Images.Media.getBitmap(activity?.contentResolver, selectedImagePath)
            val selectedImageBmp = when {
                Build.VERSION.SDK_INT < 28 -> MediaStore.Images.Media.getBitmap(
                    activity?.contentResolver,
                    selectedImagePath
                )
                else -> {
                    val source = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        ImageDecoder.createSource(activity?.contentResolver!!, selectedImagePath!!)
                    } else {
                        TODO("VERSION.SDK_INT < P")
                    }
                    ImageDecoder.decodeBitmap(source)
                }
            }
            val outputStream = ByteArrayOutputStream()
            selectedImageBmp.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            selectedImageBytes = outputStream.toByteArray()
            GlideApp.with(this)
                .load(selectedImageBytes)
                .into(imageView_profile_picture)

            pictureJustChanged = true
        }
    }

    override fun onStart() {
        super.onStart()
        FirestoreUtil.getCurrentUser { user ->
            Log.d("user.name", user.name)
            Log.d("user.bio", user.bio)

            name_input.editText?.text = Editable.Factory.getInstance().newEditable(user.name)
            bio_input.editText?.text = Editable.Factory.getInstance().newEditable(user.bio)
            if (!pictureJustChanged && user.profilePicturePath != null)
                GlideApp.with(this)
                    .load(StorageUtil.pathToReference(user.profilePicturePath))
                    .placeholder(R.drawable.ic_account_circle_black_24dp)
                    .into(imageView_profile_picture)
        }
    }
}