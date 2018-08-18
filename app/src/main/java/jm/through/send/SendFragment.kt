package jm.through.send

import android.Manifest
import android.app.Fragment
import android.os.Bundle
import android.util.Log
import android.widget.Button
import jm.through.R
import kotlinx.android.synthetic.main.fragment_send.*
import android.R.attr.data
import android.annotation.TargetApi
import android.provider.MediaStore.Images.Media.getBitmap
import android.graphics.Bitmap
import android.app.Activity.RESULT_OK
import android.os.Build
import android.provider.MediaStore
import android.R.attr.data
import android.annotation.SuppressLint
import android.content.*
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import jm.through.R.drawable.cursor
import android.os.Environment.getExternalStorageDirectory
import android.provider.ContactsContract
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.PermissionChecker.checkSelfPermission
import android.view.*
import android.widget.Toast
import com.pchmn.materialchips.ChipsInput
import com.pchmn.materialchips.model.ChipInterface
import jm.through.R.id.action_attach
import jm.through.R.id.action_send
import jm.through.chips.ContactChip
import java.net.URISyntaxException




class SendFragment : Fragment() {
     val REQ_PICK_CODE = 100
     var attach_url:String? = null
     var attach_list:ArrayList<String> = ArrayList()

    //Chips
     lateinit var mChipsInput:ChipsInput
     private lateinit var mContactList: ArrayList<ContactChip>


    @TargetApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    //menu 재정의
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.sendmenu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        Log.v("item_id", item.toString())
        when (item!!.itemId) {
            R.id.action_attach -> {
                Log.i("item id ", item.getItemId().toString() + "")
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "*/*"
                startActivityForResult(intent, REQ_PICK_CODE)
                return true
            }
            R.id.action_send -> {
                Log.i("item id ", item.getItemId().toString() + "")
                //Exception 나서 네트워크 연결 시 Thread사용
                if (goMail()) {
                    Toast.makeText(context, "success", Toast.LENGTH_SHORT).show()
                }else {
                    Toast.makeText(context, "fail", Toast.LENGTH_SHORT).show()
                }
                return true
            }
        }
        return false
    }




    //onCreateView
    @TargetApi(Build.VERSION_CODES.M)
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        var view: View = inflater!!.inflate(R.layout.fragment_send, container, false)
        mChipsInput = view.findViewById(R.id.chips_input) as ChipsInput
        mContactList = ArrayList()


        // chips listener
        mChipsInput.addChipsListener(object : ChipsInput.ChipsListener {
            override fun onChipAdded(chip: ChipInterface, newSize: Int) {
                Log.e(TAG, "chip added, $newSize")
            }

            override fun onChipRemoved(chip: ChipInterface, newSize: Int) {
                Log.e(TAG, "chip removed, $newSize")
            }

            override fun onTextChanged(text: CharSequence) {
                Log.e(TAG, "text changed: " + text.toString())
            }
        })


        return view
    }


    @TargetApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if(requestCode == REQ_PICK_CODE)
            {
                try {
                    val uri = data!!.data
                    attach_url = getPath(context,uri)
                    attach_list.add(attach_url!!)
                    Log.v("afterUri",attach_url)
                }catch (e: Exception){
                    Log.v("Fail", e.message)
                }

            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (checkSelfPermission(context,
                        android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED ) {
            requestPermissions(arrayOf(android.Manifest.permission.READ_CONTACTS),
                    1)
        } else {
            getContactList()
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun goMail () : Boolean{
        var flag = true

        Thread {
            run {
                try {
                    var recipient = edit_receiver.text.toString().trim()
                    var subject = edit_title.text.toString().trim()
                    var body = email_body.text.toString().trim()


                    //서버에서 받아와야하는 정보 id, pwd(임의로 넣음)
                    var sender: MailSender = MailSender("youremail",
                            "yourpassword")

                    //받는사람, 제목, 내용은 변수로 받고 보내는 이는 서버의 user정보
                    //Mail을 보내는 부분
                    Log.v("listlist", attach_list.toString())
                    sender.sendMail(subject,
                           "youremail", recipient, body, attach_list)
                } catch (e: Exception) {
                    Log.e("SendMail", e.message)
                    flag = false
                }
            }
        }.start()

        return flag
    }




    //TODO 공부해야 하는 부분
    @Throws(URISyntaxException::class)
    fun getPath(context: Context, uri: Uri): String? {
        var uri = uri
        val needToCheckUri = Build.VERSION.SDK_INT >= 19
        var selection: String? = null
        var selectionArgs: Array<String>? = null
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        // deal with different Uris.
        if (needToCheckUri && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                Log.v("hi1", "hi")
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
            } else if (isDownloadsDocument(uri)) {
                Log.v("hi2", "hi")
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                return split[1]

//                val id = DocumentsContract.getDocumentId(uri)
//                uri = ContentUris.withAppendedId(
//                        Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))
            } else if (isMediaDocument(uri)) {
                Log.v("hi3", "hi")
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]
                if ("image" == type) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                selection = "_id=?"
                selectionArgs = arrayOf(split[1])
            }
        }
        if ("content".equals(uri.scheme, ignoreCase = true)) {
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            var cursor: Cursor? = null
            try {
                cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null)
                val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index)
                }
            } catch (e: Exception) {
            }

        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            Log.v("hi5", "hi")
            return uri.path
        }
        return null
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }



    /**
     * Get the contacts of the user and add each contact in the mContactList
     * And finally pass the mContactList to the mChipsInput
     */
    @TargetApi(Build.VERSION_CODES.M)
    fun getContactList() {
        Log.v("hello", "hihi")
        val phones = context.contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)

        // loop over all contacts
        if (phones != null) {
            while (phones!!.moveToNext()) {
                // get contact info
                var phoneNumber: String? = null
                val id = phones!!.getString(phones!!.getColumnIndex(ContactsContract.Contacts._ID))
                val name = phones!!.getString(phones!!.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val avatarUriString = phones!!.getString(phones!!.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI))
                var avatarUri: Uri? = null
                if (avatarUriString != null)
                    avatarUri = Uri.parse(avatarUriString)

                // get phone number
                if (Integer.parseInt(phones!!.getString(phones!!.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    val pCur = context.contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", arrayOf<String>(id), null)

                    while (pCur != null && pCur!!.moveToNext()) {
                        phoneNumber = pCur!!.getString(pCur!!.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    }

                    pCur!!.close()

                }

                val contactChip = ContactChip(id, avatarUri, name, phoneNumber)
                // add contact to the list
               mContactList.add(contactChip)
            }
            phones!!.close()
        }

        // pass contact list to chips input
        mChipsInput.filterableList = mContactList
    }

// pass the ContactChip list



}

