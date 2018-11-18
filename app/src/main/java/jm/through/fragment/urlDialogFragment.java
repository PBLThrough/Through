//package jm.through.read;
//
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.support.v4.app.DialogFragment;
//
//public class urlDialogFragment extends DialogFragment {
//    String dialogMessage = "not set";
//
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState){
//        Bundle bundle = getArguments();
//        if(bundle != null){
//            dialogMessage = bundle.getString(DIALOG_MESSAGE);
//        }
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
//
//        builder.setMessage(dialogMessage)
//                .setPositiveButton("네",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        ((FragmentAlertDialog)getActivity()).doPositiveClick();
//                    }
//                })
//                .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        ((FragmentAlertDialog)getActivity()).doNegativeClick();
//                    }
//                });
//
//        return builder.create();
//    }
//}
