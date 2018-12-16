package jm.through.fragment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AlertDialog.Builder
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import jm.through.AccountData.accountList
import jm.through.R
import jm.through.UserData
import jm.through.activity.MailActivity
import jm.through.activity.SendActivity
import jm.through.activity.TrustActivity
import jm.through.data.DetailData

class TrustDialogFragment : DialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var builder: Builder = AlertDialog.Builder(activity!!)
        var dialog: AlertDialog

        var trustView = LayoutInflater.from(activity).inflate(R.layout.trust_dialog, null)
        var okBtn = trustView.findViewById(R.id.btn_ok) as Button
        var cancelBtn = trustView.findViewById(R.id.btn_cancel) as Button
        var trustText = trustView.findViewById(R.id.trust_text) as TextView


        dialog = builder.create()
        dialog!!.setView(trustView)

        var anonymousList = ArrayList<String>()

        if(arguments!= null){
            anonymousList = arguments.getStringArrayList("annymous")
        }
        var trustString = ""


        for (i in anonymousList){
            var str = i

            trustString += (str + "\n")
        }

        trustText.text = trustString


        //확인 버튼 클릭 시
        okBtn.setOnClickListener{
            //계속 진행 -> 메일 보내라
            (activity as SendActivity).goMail()
            dialog.dismiss()
        }


        //취소 버튼 클릭 시
        cancelBtn.setOnClickListener{
            dialog.dismiss()
        }



        return dialog
    }
}