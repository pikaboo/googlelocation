package com.lenabru.googlelocation.managers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;

import com.lenabru.googlelocation.base.BaseManager;

/**
 * Created by Lena Brusilovski on 29/12/2017.
 */

public class DialogManager extends BaseManager{

    public interface OnDialogButtonClick {
        void onButtonClicked();
    }

    public void showAlert(Context context, String message, final OnDialogButtonClick listener) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null) {
                            listener.onButtonClicked();
                        }
                    }
                })
                .show();
    }


}
