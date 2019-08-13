package com.example.pontoeletroniconew;

//package com.neugent.aethervoice.log;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.view.KeyEvent;
import android.view.View;

/**
 * @author Amando Jose Quinto II The class that shows the error dialog box.
 */
public class ErrorAlert implements OnKeyListener{

    private final Context mContext;

    public ErrorAlert(final Context context) {
        mContext = context;
    }

    public void showErrorDialog(final String title, final String message) {
        AlertDialog aDialog = new AlertDialog.Builder(mContext).setMessage(message).setTitle(title)
                .setNeutralButton("Fechar", new OnClickListener() {
                    public void onClick(final DialogInterface dialog,
                                        final int which) {
                        //Prevent to finish activity, if user clicks about.
                        if (!title.equalsIgnoreCase("Sobre") && !title.equalsIgnoreCase("Erro de Diretorio") && !title.equalsIgnoreCase("View")) {
                            ((Activity) mContext).finish();
                        }

                    }
                }).create();
        aDialog.setOnKeyListener(this);
        aDialog.show();
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            //disable the back button
        }
        return true;
    }


}