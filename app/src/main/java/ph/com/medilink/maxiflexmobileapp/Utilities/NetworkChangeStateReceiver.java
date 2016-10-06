package ph.com.medilink.maxiflexmobileapp.Utilities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;

import ph.com.medilink.maxiflexmobileapp.Activities.LoginActivity;
import ph.com.medilink.maxiflexmobileapp.Globals.GlobalFunctions;
import ph.com.medilink.maxiflexmobileapp.Globals.GlobalVariables;
import ph.com.medilink.maxiflexmobileapp.R;

/**
 * Created by kurt_capatan on 6/27/2016.
 *
 * This broadcast receiver is triggered upon change of internet connectivity state.
 * (ie. if device is disconnected from wifi or user turns off his/her mobile data )
 * more info here:
 * https://developer.android.com/reference/android/content/BroadcastReceiver.html
 */
public class NetworkChangeStateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        GlobalFunctions globe = new GlobalFunctions();
        boolean connected = globe.checkInternetState(context);
        if(!connected && GlobalVariables.NetworkChangeReceiverRegistered){
            DialogInterface.OnClickListener postitiveListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    GlobalVariables.NetworkChangeReceiverRegistered = false;
                    dialog.dismiss();
                }
            };
            DialogInterface.OnClickListener neutralListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                    Intent mainIntent = new Intent(context, LoginActivity.class);
                    Intent settings = new Intent(Settings.ACTION_SETTINGS);
                    context.startActivity(settings);
//                    ((Activity) context).finish();
                    dialog.dismiss();
                }
            };
            DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent mainIntent = new Intent(context, LoginActivity.class);
                    context.startActivity(mainIntent);
                    ((Activity) context).finish();
                    dialog.dismiss();
                }
            };
            globe.showAlertMessage(context, context.getResources().getString(R.string.disconnected), context.getResources().getString(R.string.proceed_offline), postitiveListener, context.getResources().getString(R.string.settings), neutralListener, context.getResources().getString(R.string.quit), negativeListener);
        } else if(connected) {
            GlobalVariables.NetworkChangeReceiverRegistered = true;
        }
    }
}
