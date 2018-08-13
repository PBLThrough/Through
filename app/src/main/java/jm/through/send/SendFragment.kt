package jm.through.send

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import jm.through.R
import kotlinx.android.synthetic.main.fragment_send.*

class SendFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        var view: View = inflater!!.inflate(R.layout.fragment_send, container, false)
        var send_btn = view.findViewById(R.id.send) as Button
        var attach_btn= view.findViewById(R.id.attach) as Button

        attach_btn.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK)
            startActivity(intent)
        }

        send_btn.setOnClickListener {

            //Exception 나서 네트워크 연결 시 Thread사용
            Thread {
                run {
                    try {
                        var recipient = edit_receiver.text.toString().trim()
                        var subject = edit_title.text.toString().trim()
                        var body = email_body.text.toString().trim()


                        //서버에서 받아와야하는 정보 id, pwd(임의로 넣음)
                        var sender: MailSender = MailSender("djgmd1021@gmail.com",
                                "@!gorden10")

                        //받는사람, 제목, 내용은 변수로 받고 보내는 이는 서버의 user정보
                        //Mail을 보내는 부분
                        sender.sendMail(subject, body,
                                "sender@gmail.com", recipient)
                    } catch (e: Exception) {
                        Log.e("SendMail", e.message)
                    }
                }
            }.start()
        }

        return view

    }
}
