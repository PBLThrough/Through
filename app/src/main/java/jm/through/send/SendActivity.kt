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
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.Toast
import com.pchmn.materialchips.ChipsInput
import com.pchmn.materialchips.model.ChipInterface
import jm.through.chips.ContactChip
import jm.through.read.AttachAdapter
import jm.through.read.AttachData
import kotlinx.android.synthetic.main.app_bar_mail.*
import java.io.File
import java.net.URISyntaxException


class SendActivity : AppCompatActivity() {
    lateinit var rAdapter: AttachAdapter //recycler연결시킬 adapte

    val REQ_PICK_CODE = 100
    var attach_uri: String? = null
    var attach_list: ArrayList<AttachData> = ArrayList()

    //Chips
    lateinit var mChipsInput: ChipsInput
    private lateinit var mContactList: ArrayList<ContactChip>


    @TargetApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_send)

       // addChips()
        addToolbar()
        addRecycler()
    }


    fun addRecycler() {
        rAdapter = AttachAdapter(attach_list)
        attach_recycler.adapter = rAdapter
        attach_recycler.layoutManager = LinearLayoutManager(this)
    }

    fun addToolbar() {
        setSupportActionBar(toolbar)
        toolbar.title = "메일 쓰기"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) //뒤로가기 만들
    }

//    @TargetApi(Build.VERSION_CODES.M)
//    fun addChips()
//        //chips 연락처
//        mContactList = ArrayList()
//        mChipsInput = findViewById(R.id.chips_input)
//
//        mChipsInput.addChipsListener(object : ChipsInput.ChipsListener {
//            override fun onChipAdded(chip: ChipInterface, newSize: Int) {
//                Log.e(TAG, "chip added, $newSize")
//            }
//
//            override fun onChipRemoved(chip: ChipInterface, newSize: Int) {
//                Log.e(TAG, "chip removed, $newSize")
//            }
//
//            override fun onTextChanged(text: CharSequence) {
//                Log.e(TAG, "text changed: " + text.toString())
//            }
//        })
//
//        //permission Check (위치 애매)
//        if (checkSelfPermission(this,
//                        android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(arrayOf(android.Manifest.permission.READ_CONTACTS),
//                    1)
//        } else {
//            getContactList()
//        }
//    }


    //toolbar menu create
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.sendmenu, menu)
        return true
    }

    //toolbar item select
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {

            android.R.id.home -> finish() //뒤로 가기 누르면 꺼지게

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
                    Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show()
                }
                return true
            }
        }
        return false
    }


    //file manager select result
    @TargetApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == REQ_PICK_CODE) {
                try {
                    val uri = data!!.data
                    attach_uri = getPath(this, uri)
                    addAttach(attach_uri!!) //첨부파일 첨부
                    Log.v("afterUri", attach_uri)
                } catch (e: Exception) {
                    Log.v("Fail", e.message)
                }
                rAdapter.notifyDataSetChanged()

            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun addAttach(uri: String) {
        var file: File = File(uri)

        var fileUri = uri
        var fileSize = file.length()
        var fileName = file.name
        var fileType = fileName.split('.')[1]

        attach_list.add(AttachData(fileUri,fileType, fileName, fileSize))
        Log.v("fileInfo", attach_list.toString())
    }




    //sending email
    @TargetApi(Build.VERSION_CODES.M)
    fun goMail(): Boolean {
        var flag = true

        Thread {
            run {
                try {
                    var recipient = edit_receiver.text.toString().trim()
                    var subject = edit_title.text.toString().trim()
                    var body = email_body.text.toString().trim()


                    //서버에서 받아와야하는 정보 id, pwd(임의로 넣음)
                    var sender: MailSender = MailSender("dream7739@naver.com",
                            "ghdwjdals7739")

                    //받는사람, 제목, 내용은 변수로 받고 보내는 이는 서버의 user정보
                    //Mail을 보내는 부분
                    Log.v("listlist", attach_list.toString())
                    sender.sendMail(subject,
                            "dream7739@naver.com", recipient, body, attach_list)
                } catch (e: Exception) {
                    Log.e("SendMail", e.message)
                    flag = false
                }
            }
        }.start()

        return flag
    }


    //TODO 공부해야 하는 부분
    //get file path from file manager
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


    //get contactList
    @TargetApi(Build.VERSION_CODES.M)
    fun getContactList() {
        Log.v("hello", "hihi")
        val phones = this.contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)

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
                    val pCur = this.contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
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




}

