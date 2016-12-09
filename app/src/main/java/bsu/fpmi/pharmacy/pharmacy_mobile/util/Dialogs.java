package bsu.fpmi.pharmacy.pharmacy_mobile.util;

import android.app.AlertDialog;
import android.content.Context;

/**
 * Created by annashunko
 */

public class Dialogs {
    public static void showErrorDialog(Context context, String dialogTitle, String errorMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(errorMessage)
                .setTitle(dialogTitle)
                .setPositiveButton(android.R.string.ok, null);
        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
