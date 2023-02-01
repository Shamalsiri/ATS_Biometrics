/*
 * Engineer  : Shamal Siriwardana
 * Date      : 02-01-2023
 */

package com.siriwardana.ats_biometrics;

import android.content.Context;
import android.widget.Toast;

public class Message {
    //Toast messages long
    public static void message(Context context, String msg){
        Toast.makeText(context,msg, Toast.LENGTH_LONG).show();
    }
}
