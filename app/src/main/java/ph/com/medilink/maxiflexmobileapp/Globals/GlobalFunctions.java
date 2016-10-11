package ph.com.medilink.maxiflexmobileapp.Globals;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.widget.TextView;

import java.io.File;

import ph.com.medilink.maxiflexmobileapp.R;

/**
 * Created by kurt_capatan on 4/12/2016.
 */
public class GlobalFunctions {

    /**
     * Check if device is connected to WiFi or mobile data
     * @param context context for ConnectivityManager
     * @return true if connected, false otherwise
     */
    public boolean checkInternetState(Context context) {
        boolean connected = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if(activeNetwork!=null){
            //connected to internet, check if wifi or data
            if(activeNetwork.getType()==ConnectivityManager.TYPE_WIFI||activeNetwork.getType()==ConnectivityManager.TYPE_MOBILE){
                connected = true;
            }
        }
        return connected;
    }

    /**
     * Checks if device's location provider is enbaled
     * @param context
     * @return
     * @throws Settings.SettingNotFoundException
     */
    public static boolean isLocationEnabled(Context context) throws Settings.SettingNotFoundException{
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        }else{
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    /**
     *  Runs at MainActivity's onCreate to get the default directory for saving LOA pdf files. (Letter of Authorization)
     *  The filepath will be saved on GlobalVariables.MAXIFLEX_FOLDER
     * @param context the app Context of this app
     */
    public void getAppFileDirectory(Context context){
        File folder;
        //Check if Directory is Existing
        if (Environment.MEDIA_MOUNTED.equalsIgnoreCase(Environment.getExternalStorageState())) {
            folder = new File(Environment.getExternalStorageDirectory(), "/MaxiFlex/");
            folder.mkdirs();
        } else {
            folder = new File(context.getFilesDir(), "/MaxiFlex/");
            folder.mkdirs();
        }
        GlobalVariables.MAXIFLEX_FOLDER =  folder.getPath();
    }


    /**
     * Locks the screen orientation, I usually lock the activity's screen on onPreExecute of an asynctask
     * @param activity activity's orientation screen to be locked
     */
    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public static void lockOrientation(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        int rotation = display.getRotation();
        int height;
        int width;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2) {
            height = display.getHeight();
            width = display.getWidth();
        } else {
            Point size = new Point();
            display.getSize(size);
            height = size.y;
            width = size.x;
        }
        switch (rotation) {
            case Surface.ROTATION_90:
                if (width > height)
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                else
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
                break;
            case Surface.ROTATION_180:
                if (height > width)
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
                else
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                break;
            case Surface.ROTATION_270:
                if (width > height)
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                else
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
            default :
                if (height > width)
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                else
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    /**
     * Unlocks the activity's screen orientation. I usually unlock the activity's screen on onPostExecute of an asynctask
     * @param activity
     */
    public static void unlockOrientation(Activity activity){
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    /**
     * shows a simple alert message with "OK" button
     * @param context context of the app
     * @param message message to be shown on alert dialog
     */
    public void showAlertMessage(Context context, String message){
        AlertDialog.Builder builder= new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    /**
     * Shows an alert message with "OK" button and accepts Positive button click listener.
     * @param context context of the app
     * @param message message to be shown on alert dialog
     * @param positiveButtonListener custom on click listener
     */
    public void showAlertMessage(Context context, String message, DialogInterface.OnClickListener positiveButtonListener){
        AlertDialog.Builder builder= new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(context.getResources().getString(R.string.ok), positiveButtonListener);
        builder.show();
    }

    /**
     * Shows an alert message
     * @param context context of the app
     * @param message message to be shown on alert dialog
     * @param positiveButton text to be show on positive button
     * @param positiveButtonListener custom on positive button click listener
     * @param negativeButton text to be show on negative button
     * @param negativeButtonListener custom on negative button click listener
     */
    public void showAlertMessage(Context context, String message, String positiveButton, DialogInterface.OnClickListener positiveButtonListener, String negativeButton,DialogInterface.OnClickListener negativeButtonListener){
        AlertDialog.Builder builder= new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(positiveButton, positiveButtonListener);
        builder.setNegativeButton(negativeButton, negativeButtonListener);
        builder.show();
    }

    /**
     * Shows an alert message
     * @param context context of the app
     * @param message message to be shown on alert dialog
     * @param positiveButton text to be show on positive button
     * @param positiveButtonListener custom on positive button click listener
     * @param neutralButton text to be show on neutral button
     * @param neutralButtonListener custom on neutral button click listener
     * @param negativeButton text to be show on negative button
     * @param negativeButtonListener custom on negative button click listener
     */
    public void showAlertMessage(Context context, String message, String positiveButton, DialogInterface.OnClickListener positiveButtonListener, String neutralButton, DialogInterface.OnClickListener neutralButtonListener, String negativeButton,DialogInterface.OnClickListener negativeButtonListener){
        AlertDialog.Builder builder= new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(positiveButton, positiveButtonListener);
        builder.setNeutralButton(neutralButton, neutralButtonListener);
        builder.setNegativeButton(negativeButton, negativeButtonListener);
        builder.show();
    }

    /**
     * Shows a progress dialog with message with "Cancel" as negative button
     * @param context context of the app
     * @param message message to be shown on alert dialog
     * @param dismissListener custom negative button click listener
     * @return progress dialog that is shown to the user
     */
    public ProgressDialog showProgressDialog(Context context, String message, DialogInterface.OnDismissListener dismissListener){
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        progressDialog.setOnDismissListener(dismissListener);
        progressDialog.show();
        return progressDialog;
    }


    /**
     * Shows a progress dialog with message
     * @param context context of the app
     * @param message message to be shown on alert dialog
     * @return progress dialog that is shown to the user
     */
    public ProgressDialog showProgressDialog(Context context, String message){
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        return progressDialog;
    }


//    /**
//     * shows a simple alert message containing the error message
//     * @param context context of the app
//     * @param errorMessage error message to be shown on alert dialog
//     */
//    public void showErrorMessage(Context context, String errorMessage){
//        View dialog_error_message = LayoutInflater.from(context).inflate(R.layout.dialog_error_message, null);
//        TextView tvErrorMessage = (TextView) dialog_error_message.findViewById(R.id.tvErrorMessage);
//        tvErrorMessage.setText(errorMessage);
//        AlertDialog.Builder builder= new AlertDialog.Builder(context);
//        builder.setMessage(context.getString(R.string.error_occurred_dialog));
//        builder.setView(dialog_error_message);
//        builder.setCancelable(false);
//        builder.setPositiveButton(context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        builder.show();
//    }

    /**
     * add separator lines on items of recycler view
     */
    public class DividerItemDecoration extends RecyclerView.ItemDecoration {

        private final int[] ATTRS = new int[]{
                android.R.attr.listDivider
        };

        public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

        public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

        private Drawable mDivider;

        private int mOrientation;

        public DividerItemDecoration(Context context) {
            final TypedArray a = context.obtainStyledAttributes(ATTRS);
            mDivider = a.getDrawable(0);
            a.recycle();
            setOrientation(VERTICAL_LIST);
        }

        public void setOrientation(int orientation) {
            if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
                throw new IllegalArgumentException("invalid orientation");
            }
            mOrientation = orientation;
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            if (mOrientation == VERTICAL_LIST) {
                drawVertical(c, parent);
            } else {
                drawHorizontal(c, parent);
            }
        }

        public void drawVertical(Canvas c, RecyclerView parent) {
            final int left = parent.getPaddingLeft();
            final int right = parent.getWidth() - parent.getPaddingRight();

            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                        .getLayoutParams();
                final int top = child.getBottom() + params.bottomMargin;
                final int bottom = top + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }

        public void drawHorizontal(Canvas c, RecyclerView parent) {
            final int top = parent.getPaddingTop();
            final int bottom = parent.getHeight() - parent.getPaddingBottom();

            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                        .getLayoutParams();
                final int left = child.getRight() + params.rightMargin;
                final int right = left + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            if (mOrientation == VERTICAL_LIST) {
                outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
            } else {
                outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
            }
        }
    }

}
