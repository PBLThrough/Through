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
import jm.through.AccountData
import jm.through.AccountData.accountList
import jm.through.R
import jm.through.UserData
import jm.through.activity.MailActivity
import jm.through.activity.SendActivity
import jm.through.activity.SettingActivity
import jm.through.activity.TrustActivity
import jm.through.data.DetailData

class DeleteDialogFragment : DialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var builder: Builder = AlertDialog.Builder(activity!!)
        var dialog: AlertDialog

        var deleteView = LayoutInflater.from(activity).inflate(R.layout.delete_dialog, null)
        var okBtn = deleteView.findViewById(R.id.delete_ok_btn) as Button
        var cancelBtn = deleteView.findViewById(R.id.delete_cancel_btn) as Button

        var position = 0

        if(arguments != null){
            position = arguments.getInt("position")
        }

        dialog = builder.create()
        dialog!!.setView(deleteView)


        //확인 버튼 클릭 시
        okBtn.setOnClickListener{
            //삭제 실행
            (activity as SettingActivity).removeItem(position)
            dialog.dismiss()
        }


        //취소 버튼 클릭 시
        cancelBtn.setOnClickListener{
            dialog.dismiss()
        }


        return dialog
    }
}