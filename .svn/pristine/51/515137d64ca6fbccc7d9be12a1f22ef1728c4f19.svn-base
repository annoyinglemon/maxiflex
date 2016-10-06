package ph.com.medilink.maxiflexmobileapp.Utilities;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import java.io.File;

import ph.com.medilink.maxiflexmobileapp.Globals.GlobalVariables;

/**
 * Created by kurt_capatan on 5/4/2016.
 *
 * This broadcast receiver is triggered upon successful install of an application.
 *
 ** more info here:
 * https://developer.android.com/reference/android/content/BroadcastReceiver.html
 *
 */
public class PackageInstalledReceiver extends BroadcastReceiver {
    private Context mContext;
    private File file;
    private String filetype;
    /**
     *
     * @param context the context
     * @param file
     * @param fileType
     */
    public PackageInstalledReceiver(Context context, File file, String fileType){
        this.mContext = context;
        this.file = file;
        this.filetype = fileType;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Installed Received", Toast.LENGTH_SHORT).show();
        Uri path = Uri.fromFile(file);
        Intent openPdfIntent = new Intent(Intent.ACTION_VIEW);
        openPdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        openPdfIntent.setDataAndType(path, filetype);
        try {
            final PackageManager packageManager = mContext.getPackageManager();
            ComponentName componentName = openPdfIntent.resolveActivity(packageManager);
            //if there is pdf reader installed, start it, then unregister
            if (componentName != null) {
                this.mContext.startActivity(openPdfIntent);
                this.mContext.unregisterReceiver(this);
                GlobalVariables.InstallReceiverRegistered = false;
            }
        }catch (ActivityNotFoundException e){}
    }

}