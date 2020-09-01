package com.inspect.ocr.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextThemeWrapper;
import com.inspect.ocr.R;

public class DialogManager {
    /**
     * Show Category Dialog
     *
     */
    public static void showRadioDialog(Context context, String title, CharSequence[] items, int initValue , String[] btnText,
                                       DialogInterface.OnClickListener listener) {
        if (listener == null)
            listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface,
                                    int paramInt) {
                    paramDialogInterface.dismiss();
                }
            };
        ContextThemeWrapper cw = new ContextThemeWrapper( context, R.style.CustomAlertDialogStyle);
        final AlertDialog.Builder builder = new AlertDialog.Builder(cw);
        builder.setTitle(title);

        final DialogInterface.OnClickListener finalListener = listener;
        builder.setSingleChoiceItems(items, initValue,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        finalListener.onClick(dialog, item);
                        dialog.dismiss();
                    }
                });

        if (btnText != null) {
            for (int i=0; i<btnText.length; i++) {
                builder.setNegativeButton(btnText[i], listener);
            }
        }
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);
    }
}
