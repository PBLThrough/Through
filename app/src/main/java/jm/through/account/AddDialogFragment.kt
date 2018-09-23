package jm.through.account

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AlertDialog.Builder
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import jm.through.AccountData.accountList
import jm.through.R
import jm.through.activity.MailActivity

class AddDialogFragment : DialogFragment() {
    lateinit var data:DetailData

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if(arguments!=null){
            data = arguments!!.getParcelable("newData")
        }

        var builder: Builder = AlertDialog.Builder(activity!!)
        var dialog: AlertDialog

        var dialogView = LayoutInflater.from(activity).inflate(R.layout.double_account_dialog, null)
        var cancelBtn = dialogView.findViewById(R.id.btn_cancle) as Button
        var okBtn = dialogView.findViewById(R.id.btn_ok) as Button

        dialog = builder.create()
        dialog!!.setView(dialogView)

        okBtn.setOnClickListener {
            dialog.dismiss()
            accountList.add(data)
            val intent = Intent(activity, MailActivity::class.java)
            startActivity(intent)
            activity!!.finish()
        }

        cancelBtn.setOnClickListener{
            dialog.dismiss()
        }



        return dialog
    }
}