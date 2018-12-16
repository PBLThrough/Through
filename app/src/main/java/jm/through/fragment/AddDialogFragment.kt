package jm.through.fragment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AlertDialog.Builder
import android.view.LayoutInflater
import android.widget.Button
import jm.through.AccountData.accountList
import jm.through.R
import jm.through.activity.MailActivity
import jm.through.data.DetailData

class AddDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var builder: Builder = AlertDialog.Builder(activity!!)
        var dialog: AlertDialog

        var dialogView = LayoutInflater.from(activity).inflate(R.layout.double_account_dialog, null)
        var cancelBtn = dialogView.findViewById(R.id.cancel_btn) as Button

        dialog = builder.create()
        dialog!!.setView(dialogView)


        cancelBtn.setOnClickListener{
            dialog.dismiss()
        }



        return dialog
    }
}