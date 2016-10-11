package ph.com.medilink.maxiflexmobileapp.Fragments.MedicalRecord;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import ph.com.medilink.maxiflexmobileapp.Globals.GlobalFunctions;
import ph.com.medilink.maxiflexmobileapp.Globals.GlobalVariables;
import ph.com.medilink.maxiflexmobileapp.PlainOldJavaObjects.UploadedFileClass;
import ph.com.medilink.maxiflexmobileapp.R;
import ph.com.medilink.maxiflexmobileapp.Utilities.PackageInstalledReceiver;
import ph.com.medilink.maxiflexmobileapp.WebServices.WebServiceClass;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_UploadFiles.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_UploadFiles#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_UploadFiles extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView rvUploadedFiles;
    private UploadedFilesRecyclerViewAdapter mAdapter;
    private ArrayList<UploadedFileClass> mUploadedList;

    private PackageInstalledReceiver mInstalledReceiver;
    private IntentFilter appInstalledFilter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Fragment_UploadFiles() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_UploadFiles.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_UploadFiles newInstance(String param1, String param2) {
        Fragment_UploadFiles fragment = new Fragment_UploadFiles();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        if (savedInstanceState != null) {
            mUploadedList = (ArrayList<UploadedFileClass>) savedInstanceState.getSerializable("uploaded_list");
            if (mUploadedList == null) {
                mUploadedList = new ArrayList<UploadedFileClass>();
            }
        } else {
            mUploadedList = new ArrayList<UploadedFileClass>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_upload_files, container, false);
        rvUploadedFiles = (RecyclerView) rootView.findViewById(R.id.rvUploadedFiles);
        rvUploadedFiles.setLayoutManager(new LinearLayoutManager(getContext()));
        rvUploadedFiles.addOnItemTouchListener(new RecyclerTouchListener(getContext(), rvUploadedFiles));
        if (mUploadedList.size() > 0) {
            mAdapter = new UploadedFilesRecyclerViewAdapter(mUploadedList);
            rvUploadedFiles.setAdapter(mAdapter);
            rvUploadedFiles.setVisibility(View.VISIBLE);
        } else {
            mAdapter = new UploadedFilesRecyclerViewAdapter(mUploadedList);
            rvUploadedFiles.setAdapter(mAdapter);
            rvUploadedFiles.setVisibility(View.GONE);
        }
        return rootView;
    }

    public void putInfo(UploadedFileClass upload) {
        mAdapter.addItem(upload);
        rvUploadedFiles.setVisibility(View.VISIBLE);
    }

    public void clearItems() {
        mAdapter.clearItems();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mUploadedList.size() > 0) {
            outState.putSerializable("uploaded_list", mUploadedList);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        if (GlobalVariables.InstallReceiverRegistered2) {
            getContext().registerReceiver(mInstalledReceiver, appInstalledFilter);
        }
        super.onStart();
    }

    @Override
    public void onDestroy() {
        if (GlobalVariables.InstallReceiverRegistered2) {
            getContext().unregisterReceiver(mInstalledReceiver);
        }
        super.onDestroy();
    }

    public void openFileAttempt(final File file) {
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_VIEW);
//        intent.setDataAndType(Uri.parse("file://" + "/sdcard/test.jpg"), "image/*");
//        startActivity(intent);
        Uri path = Uri.fromFile(file);
        Intent openImageIntent = new Intent(Intent.ACTION_VIEW);
        openImageIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        openImageIntent.setDataAndType(path, "image/*");
        try {
            final PackageManager packageManager = getContext().getPackageManager();
            //if there is no pdf reader apps installed on device, search the play store
            if (openImageIntent.resolveActivity(packageManager) == null) {
                Dialog.OnClickListener downloadImageViewerButton = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //register receiver that checks if pdf reader is installed
                        mInstalledReceiver = new PackageInstalledReceiver(getContext(), file, "image/*");
                        appInstalledFilter = new IntentFilter();
                        appInstalledFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
                        appInstalledFilter.addAction(Intent.ACTION_PACKAGE_INSTALL);
                        appInstalledFilter.addDataScheme("package");
                        getContext().registerReceiver(mInstalledReceiver, appInstalledFilter);
                        GlobalVariables.InstallReceiverRegistered = true;
                        //launch google play intent
                        Uri marketUri = Uri.parse("market://search?q=Image%20Viewer");
                        Intent marketIntent = new Intent(Intent.ACTION_VIEW).setData(marketUri);
                        //if google play store app is available, launch the app, else, launch the browser
                        if (marketIntent.resolveActivity(packageManager) == null) {
                            marketUri = Uri.parse("https://play.google.com/store/apps/search?q=Image%20Viewer");
                            marketIntent = new Intent(Intent.ACTION_VIEW).setData(marketUri);
                            // if no browsers installed, show snackbar of file path
                            if (marketIntent.resolveActivity(packageManager) == null) {
                                //unregisterReceiver()
                                getContext().unregisterReceiver(mInstalledReceiver);
                                GlobalVariables.InstallReceiverRegistered = false;
                                Snackbar snackbar = Snackbar.make(getView(), getResources().getString(R.string.fragment_download_saved) + file.getPath(), Snackbar.LENGTH_INDEFINITE);
                                snackbar.show();
                            } else {
                                getContext().startActivity(marketIntent);
                            }
                        } else {
                            getContext().startActivity(marketIntent);
                        }
                    }
                };
                new GlobalFunctions().showAlertMessage(getContext(), getResources().getString(R.string.fragment_download_view_unable), downloadImageViewerButton);
            } else {
                // open pdf
                getContext().startActivity(openImageIntent);
            }
        } catch (ActivityNotFoundException e) {
            Snackbar snackbar = Snackbar.make(getView(), getResources().getString(R.string.fragment_download_saved) + file.getPath(), Snackbar.LENGTH_INDEFINITE);
            snackbar.show();
        }
    }

    /**
     * Item CLick Listener for recyclerview
     */
    private class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetectorCompat gestureDetectorCompat;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView) {
            gestureDetectorCompat = new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener() {

                @Override
                public void onLongPress(MotionEvent e) {

                }

                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    int itemSelected = recyclerView.getChildAdapterPosition(child);
                    if (itemSelected > -1) {
                        if (!mAdapter.getItem(itemSelected).isSaved()) {
                            if (new GlobalFunctions().checkInternetState(getContext()))
                                new DownloadFile().execute(mAdapter.getItem(itemSelected));
                            else
                                new GlobalFunctions().showAlertMessage(getContext(), getResources().getString(R.string.enable_internet));
                        } else {
                            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                File[] files = new File(GlobalVariables.MAXIFLEX_FOLDER).listFiles();
                                for (File f : files) {
//                                    if (f.getName().contains(GlobalVariables.MEMBER_ID+"_"+mAdapter.getItem(itemSelected).getFileName().trim())) {
//                                        openFileAttempt(f);
//                                        break;
//                                    }
                                    if (f.getName().contains(mAdapter.getItem(itemSelected).getFileName().trim())) {
                                        openFileAttempt(f);
                                        break;
                                    }
                                }
                            } else {
                                ActivityCompat.requestPermissions((AppCompatActivity) getContext(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, GlobalVariables.REQUEST_EXTERNAL_READ_WRITE);
                            }
                        }

                    }
                    return false;
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            gestureDetectorCompat.onTouchEvent(e);
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }


    /**
     * ADAPTER
     */
    public class UploadedFilesRecyclerViewAdapter extends RecyclerView.Adapter<UploadedFilesRecyclerViewAdapter.UploadedFileRecyclerViewHolder> {

        private ArrayList<UploadedFileClass> uploadedFileClasses;


        public class UploadedFileRecyclerViewHolder extends RecyclerView.ViewHolder {
            TextView tvFileName, tvUploadedDate, tvsavedOrDownload;
            LinearLayout llsavedOrDownload;

            UploadedFileRecyclerViewHolder(View itemView) {
                super(itemView);
                tvFileName = (TextView) itemView.findViewById(R.id.tvFileName);
                tvUploadedDate = (TextView) itemView.findViewById(R.id.tvUploadedDate);
                tvsavedOrDownload = (TextView) itemView.findViewById(R.id.tvsavedOrDownload);
                llsavedOrDownload = (LinearLayout) itemView.findViewById(R.id.llsavedOrDownload);
            }
        }

        public UploadedFilesRecyclerViewAdapter(ArrayList<UploadedFileClass> uploadedFileClasses) {
            this.uploadedFileClasses = uploadedFileClasses;
        }

        @Override
        public int getItemCount() {
            return uploadedFileClasses.size();
        }

        public UploadedFileClass getItem(int position) {
            return uploadedFileClasses.get(position);
        }

        public void addItem(UploadedFileClass uploadedFileClass) {
            this.uploadedFileClasses.add(uploadedFileClass);
            notifyItemInserted(getPosition(uploadedFileClass));
        }

        public int getPosition(UploadedFileClass allergy) {
            for (int i = 0; i < uploadedFileClasses.size(); i++) {
                if (uploadedFileClasses.get(i).equals(allergy)) {
                    return i;
                }
            }
            return -1;
        }

        public void clearItems() {
            this.uploadedFileClasses.clear();
            notifyDataSetChanged();
        }

        @Override
        public UploadedFileRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_upload_file, parent, false);
            UploadedFileRecyclerViewHolder viewHolder = new UploadedFileRecyclerViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final UploadedFileRecyclerViewHolder holder, final int position) {
            holder.tvFileName.setText(uploadedFileClasses.get(position).getFileName());
            holder.tvUploadedDate.setText(uploadedFileClasses.get(position).getUploadDate());
            if (uploadedFileClasses.get(position).isSaved()) {
                holder.llsavedOrDownload.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.md_green_500));
                holder.tvsavedOrDownload.setText(getContext().getResources().getString(R.string.saved_tap_to_view));
            } else {
                holder.llsavedOrDownload.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.md_orange_500));
                holder.tvsavedOrDownload.setText(getContext().getResources().getString(R.string.not_saved_tap_to_dl));
            }
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }


    }

    /**
     * ASYNCTASKS
     */

    private class DownloadFile extends AsyncTask<UploadedFileClass, Void, String> {
        ProgressDialog progressDialog;
        UploadedFileClass uF;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new GlobalFunctions().showProgressDialog(getContext(), getContext().getResources().getString(R.string.download_file), new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    DownloadFile.this.cancel(true);
                }
            });
            GlobalFunctions.lockOrientation(getActivity());
        }

        @Override
        protected String doInBackground(UploadedFileClass... params) {
            String result = "Error";
            try {
                uF = params[0];
                result = new WebServiceClass().DownloadFile((AppCompatActivity) getContext(), params[0].getFileName());
//                result = new WebServiceClass().DownloadFile(params[0].getFileName());
                if (result.contains("Exception")) {
                    return result;
                }
            } catch (Exception ex) {
                result = "Exception: " + ex.getMessage();
            }
            return result;
        }

        @Override
        protected void onCancelled() {
            progressDialog.dismiss();
        }

        @Override
        protected void onPostExecute(final String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            GlobalFunctions.unlockOrientation(getActivity());
            if (s.contains("Exception")) {
                new GlobalFunctions().showAlertMessage(getContext(), getResources().getString(R.string.error_occurred));
//                Toast.makeText(getContext(), "Exception: " + s, Toast.LENGTH_LONG).show();
            } else if(s.contains("Error:")){
//                Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
                new GlobalFunctions().showAlertMessage(getContext(), getResources().getString(R.string.error_occurred));
            } else if (s.equalsIgnoreCase("Error"))
                Toast.makeText(getContext(), getContext().getResources().getString(R.string.fragment_download_error_file), Toast.LENGTH_LONG).show();
            else {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    uF.setIsSaved(true);
                    mAdapter.notifyItemChanged(mAdapter.getPosition(uF));
                    openFileAttempt(new File(s));
                } else {
                    ActivityCompat.requestPermissions((AppCompatActivity) getContext(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, GlobalVariables.REQUEST_EXTERNAL_READ_WRITE);
                }
            }
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
