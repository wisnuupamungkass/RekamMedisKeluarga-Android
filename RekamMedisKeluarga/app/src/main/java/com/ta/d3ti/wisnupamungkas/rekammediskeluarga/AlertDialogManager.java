package com.ta.d3ti.wisnupamungkas.rekammediskeluarga;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
public class AlertDialogManager {
    @SuppressWarnings("deprecation")
    public void showAlertDialog(Context context, String title, String message, Boolean status){
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        //setting Doalog Title
        alertDialog.setTitle(title);
        //setting Dialog Message
        alertDialog.setMessage(message);
        //setting OK Button
        alertDialog.setButton("OK",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        //showing alert message
        alertDialog.show();
    }
}
