package com.project.valdoc.utility;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.project.valdoc.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Avinash on 4/3/2016.
 */
public class Utilityies {
    private Utilityies() {

    }

    public static String parseDateToddMMyyyy(String time) {
        String str = "";
        Date date = null;
        if (null != time && time.length() > 0) {
            String inputPattern = "yyyy-MM-dd";
            String outputPattern = "dd-MM-yyyy";
            SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
            SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

            try {
                date = inputFormat.parse(time);
                str = outputFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return str;
    }

    public static void setCustomActionBar(Context ctx, ActionBar mActionBar, String userName){
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(ctx);

        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        TextView mUserName = (TextView) mCustomView.findViewById(R.id.user_name_tv);
        mUserName.setText(userName);

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

    }

    public static boolean checkInternetConnection(Context context) {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec = (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
        // Removed previous code. Because it is crashing. If device supports only wiFi, no 3G or 2G.
        //in that case it will give NPE
        // for method connec.getNetworkInfo(0).getState().
        final NetworkInfo activeNetwork = connec.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            // notify user you are online
            Toast.makeText(context, " Connected ", Toast.LENGTH_LONG).show();
            return true;
        } else {
            // notify user you are not online
            Toast.makeText(context, " Not Connected ", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public static void showAlert(Context context, String msg){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(msg);
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
/*
        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });*/
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
