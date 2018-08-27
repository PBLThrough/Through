package jm.through.read

import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import jm.through.R
import jm.through.R.id.*
import jm.through.attachment.RattachData
import jm.through.read.FolderFetchImap.readList

//import jm.through.read.MailReader.readList
import kotlinx.android.synthetic.main.check_board_item.view.*
import java.io.File

/**
 * ReadFragment <- ReadAdapter, ReadViewholder
 * */

class ReadFragment : Fragment(), View.OnClickListener  {
    lateinit var rAdapter: ReadAdapter //recycler연결시킬 adapte
    lateinit var checkRecycler: RecyclerView
    lateinit var readProgress: ProgressBar
    @RequiresApi(Build.VERSION_CODES.M)

    var rattach_list: ArrayList<RattachData> = ArrayList()
    // Activity에 fragment 가 호출될 때
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        var readTask = ReadTask()
        readTask.execute()
    }

    // 초기화해야하는 리스트를 초기화함
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    // 레이아웃을 inflate 하는 곳.
    // view 객체를 얻을 수 있고 TextView, Button 등 초기화 가능
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val checkView: View = inflater!!.inflate(R.layout.fragment_check, container, false)
        checkRecycler = checkView.findViewById(R.id.recycler) as RecyclerView
        readProgress = checkView.findViewById(read_progress) as ProgressBar

        return checkView
    }


    //여기는 셀 하나를 클릭했을 때의 이벤트를 처리하는 곳이기 때문에 onClickListener를 사용할 필요가 읎요오!!!!!!!!!!!
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(v: View?) {
        val idx: Int = checkRecycler.getChildAdapterPosition(v)

        var bundle = Bundle();
            bundle.putInt("position",idx);

        var message = MessageFragment() as Fragment//메일 보내는 fragment
        var fm = fragmentManager //fragment교체에 필요한 fragmentManager

        message.arguments = bundle;

        fm.beginTransaction().replace(R.id.fragment_container, message).commit()
    }


    inner class ReadTask : AsyncTask<Void, Void, Void>() {
        override fun onProgressUpdate(vararg values: Void?) {
            super.onProgressUpdate(*values)
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            readProgress.visibility = View.INVISIBLE
            try {
                rAdapter = ReadAdapter(readList)
                rAdapter.setOnItemClickListener(this@ReadFragment)
                checkRecycler.adapter = rAdapter
                checkRecycler.layoutManager = LinearLayoutManager(activity)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.v("fail", "")
            }
        }

        override fun doInBackground(vararg params: Void?): Void? {
            var reader = FolderFetchImap()
            //var reader = MailReader()

            // 업데이트 할 땐 아이디와 비밀번호 바꾸기 ^^!!
            reader.readImapMail("cisspmit@naver.com","@!gorden")
            //reader.readMail("cisspmit@naver.com", "@!gorden")
            Log.v("list",readList.toString())

            return null
        }

        fun receiveAttach(uri: String) {
            var file: File = File(uri)
            var totalFileSize:Long = 0

            var fileUri = uri
            var fileSize = file.length()
            var fileName = file.name
            var fileType = fileName.split('.')[1]

            for(attachData in rattach_list) {
                totalFileSize += attachData.receive_fileSize //기존 파일 크기
            }
            totalFileSize += fileSize //현재 select해서 가져온 파일 크기

            //gmail 20MB 이상일 시, naver 10MB 이상일 시(상황에 따라 처리 But 속도가 느릴 수 있음.)
            if (totalFileSize  > 10485760) {
                System.out.println("파일첨부 10MB 넘어감")
                //Toast.makeText(this, "파일첨부는 10MB를 넘을 수 없습니다", Toast.LENGTH_SHORT).show()
            } else {
                rattach_list.add(RattachData(fileUri, fileType, fileName, fileSize))
                Log.v("fileInfo", rattach_list.toString())
                Log.v("totalFileSize", totalFileSize.toString())
            }
        }
        override fun onCancelled(result: Void?) {
            super.onCancelled(result)
        }

        override fun onCancelled() {
            super.onCancelled()
        }

        override fun onPreExecute() {
            super.onPreExecute()
        }
    }
}
