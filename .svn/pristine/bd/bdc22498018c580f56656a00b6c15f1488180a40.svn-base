package ph.com.medilink.maxiflexmobileapp.Fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ph.com.medilink.maxiflexmobileapp.Activities.MainActivity;
import ph.com.medilink.maxiflexmobileapp.Globals.GlobalFunctions;
import ph.com.medilink.maxiflexmobileapp.Globals.GlobalVariables;
import ph.com.medilink.maxiflexmobileapp.R;
import ph.com.medilink.maxiflexmobileapp.WebServices.WebServiceClass;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_eCard.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_eCard#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_eCard extends Fragment implements Animation.AnimationListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private LinearLayout lvDownloadProgress;
    private Animation toMiddle;
    private Animation fromMiddle;
    private RelativeLayout cardFront;
    private RelativeLayout cardBack;
    private ImageView ivEcard;
    private ImageView ivQRCode;

    private View rootView;

    private boolean isFrontOfCardShowing = true;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Fragment_eCard() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_eCard.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_eCard newInstance(String param1, String param2) {
        Fragment_eCard fragment = new Fragment_eCard();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_ecard, container, false);
        lvDownloadProgress = (LinearLayout) rootView.findViewById(R.id.lvDownloadProgress);

        cardFront = (RelativeLayout) rootView.findViewById(R.id.cardFront);
        cardBack = (RelativeLayout) rootView.findViewById(R.id.cardBack);

        TextView tvEcard_cardNumber = (TextView) rootView.findViewById(R.id.tvEcard_cardNumber);
        TextView tvEcard_cardNumber4 = (TextView) rootView.findViewById(R.id.tvEcard_cardNumber4);
        TextView tvValidFrom = (TextView) rootView.findViewById(R.id.tvValidFrom);
        TextView tvValidFromMonthYear = (TextView) rootView.findViewById(R.id.tvValidFromMonthYear);
        TextView tvEcard_Valid = (TextView) rootView.findViewById(R.id.tvEcard_Valid);
        TextView tvValidThru = (TextView) rootView.findViewById(R.id.tvValidThru);
        TextView tvValidThruMonthYear = (TextView) rootView.findViewById(R.id.tvValidThruMonthYear);
        TextView tvEcard_expiry = (TextView) rootView.findViewById(R.id.tvEcard_expiry);
        TextView tvEcard_memberName = (TextView) rootView.findViewById(R.id.tvEcard_memberName);
//        TextView tvEcard_memberId = (TextView) rootView.findViewById(R.id.tvEcard_memberId);
        TextView tvEcard_corpName = (TextView) rootView.findViewById(R.id.tvEcard_corpName);


        Typeface ottawa = Typeface.createFromAsset(getContext().getAssets(),"fonts/Ottawa.ttf");
        tvEcard_cardNumber.setTypeface(ottawa);
        tvEcard_cardNumber4.setTypeface(ottawa);
        tvValidFrom.setTypeface(ottawa);
        tvValidFromMonthYear.setTypeface(ottawa);
        tvEcard_Valid.setTypeface(ottawa);
        tvValidThru.setTypeface(ottawa);
        tvValidThruMonthYear.setTypeface(ottawa);
        tvEcard_expiry.setTypeface(ottawa);
        tvEcard_memberName.setTypeface(ottawa);
        tvEcard_corpName.setTypeface(ottawa);

        ivQRCode = (ImageView) rootView.findViewById(R.id.ivQRCode);
        ivEcard = (ImageView) rootView.findViewById(R.id.ivEcard);

        if (GlobalVariables.CARD_NO != null && GlobalVariables.MEMBER_NAME != null && GlobalVariables.MEMBER_ID != null && GlobalVariables.CORP_NAME != null && GlobalVariables.EXPIRY_DATE != null) {
            String cardNumber = GlobalVariables.CARD_NO;
            cardNumber = new StringBuilder(cardNumber).insert(4, " ").toString();
            cardNumber = new StringBuilder(cardNumber).insert(9, " ").toString();
            cardNumber = new StringBuilder(cardNumber).insert(14, " ").toString();
            tvEcard_cardNumber.setText(cardNumber);
            tvEcard_cardNumber4.setText(cardNumber.substring(0, 4));
            tvEcard_memberName.setText(GlobalVariables.MEMBER_NAME.toUpperCase());
//            tvEcard_memberId.setText(GlobalVariables.MEMBER_ID);
            tvEcard_corpName.setText(GlobalVariables.CORP_NAME);
            try {
                Date exp_date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(GlobalVariables.EXPIRY_DATE_MMddYYYY);
//                tvEcard_expiry.setText(new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH).format(exp_date));
                tvEcard_expiry.setText(new SimpleDateFormat("MM/yy", Locale.ENGLISH).format(exp_date));
            } catch (ParseException e) {
                tvEcard_expiry.setText(GlobalVariables.EXPIRY_DATE);
            }
        }

        toMiddle = AnimationUtils.loadAnimation(getContext(), R.anim.to_middle);
        toMiddle.setAnimationListener(this);
        fromMiddle = AnimationUtils.loadAnimation(getContext(), R.anim.from_middle);
        fromMiddle.setAnimationListener(this);
//
        ivEcard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ivEcard.clearAnimation();
                ivEcard.setAnimation(toMiddle);
                ivEcard.startAnimation(toMiddle);
            }
        });

        if (savedInstanceState == null) {
            ivEcard.setClickable(false);
            cardFront.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    cardFront.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    new GenerateCardDesign().execute(GlobalVariables.PLAN_CODE);
                }
            });
        } else {
            lvDownloadProgress.setVisibility(View.GONE);
            ivEcard.setClickable(true);
            ivEcard.setImageBitmap(GlobalVariables.bmFront);
        }

        setHasOptionsMenu(true);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_ecard, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_refresh:
                if (new GlobalFunctions().checkInternetState(getContext())) {
                    new GenerateCardDesign().execute(GlobalVariables.PLAN_CODE);
//                    new GenerateQRCode().execute(GlobalVariables.MEMBER_ID + "|" + GlobalVariables.MAIN_MEMBER_ID + "|" + GlobalVariables.CARD_NO + "|" + GlobalVariables.EXPIRY_DATE_MMddYYYY);
                }else {
                    new GlobalFunctions().showAlertMessage(getContext(), getResources().getString(R.string.enable_internet));
                }
                return true;
            case R.id.action_signOut:
                ((MainActivity) (Activity) getContext()).SignOut();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (animation == toMiddle) {
            if (isFrontOfCardShowing) {
                ivEcard.setImageBitmap(GlobalVariables.bmBack);
            } else {
                ivEcard.setImageBitmap(GlobalVariables.bmFront);
            }
            ivEcard.clearAnimation();
            ivEcard.setAnimation(fromMiddle);
            ivEcard.startAnimation(fromMiddle);
        } else {
            isFrontOfCardShowing = !isFrontOfCardShowing;
        }
        ivEcard.setClickable(true);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onAnimationStart(Animation animation) {
        // TODO Auto-generated method stub
        ivEcard.setClickable(false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * Used to convert any visible/invisible to bitmap.
     *
     * @param view view to be converted, view's visibility must be != GONE
     * @return bitmap of view
     */
    private Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
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




    /**
     * ASYNCTASKS
     */


    private class GenerateCardDesign extends AsyncTask<String, byte[], String> {

        int count = 1;
        String front = "_F.jpg";
        String back = "_B.jpg";

        @Override
        protected void onPreExecute() {
            lvDownloadProgress.setVisibility(View.VISIBLE);
            GlobalFunctions.lockOrientation(getActivity());
        }

        @Override
        protected String doInBackground(String... params) {
            //possible results
            //successful - front and back are downloaded
            //failed - either of them has an error on download,
            String result = "successful";
            byte[] cardDesignFront = new WebServiceClass().DownloadCardDesign(params[0].concat(front));
            if(GlobalVariables.ERROR_MESSAGE.contains("Error:")||GlobalVariables.ERROR_MESSAGE.contains("Exception:")||cardDesignFront==null){
                result = "failed";
            } else {
                publishProgress(cardDesignFront);
                byte[] cardDesignBack = new WebServiceClass().DownloadCardDesign(params[0].concat(back));
                if(GlobalVariables.ERROR_MESSAGE.contains("Error:")||GlobalVariables.ERROR_MESSAGE.contains("Exception:")||cardDesignBack==null){
                    result = "failed";
                }else{
                    publishProgress(cardDesignBack);
                }
            }
            return result;
        }

        @Override
        protected void onProgressUpdate(byte[]... values) {
            if(count==1){
                count++;
                Bitmap frontCard = BitmapFactory.decodeByteArray(values[0], 0, values[0].length);
                cardFront.setBackground(new BitmapDrawable(getResources(), frontCard));
            } else{
                Bitmap backCard = BitmapFactory.decodeByteArray(values[0], 0, values[0].length);
                cardBack.setBackground(new BitmapDrawable(getResources(), backCard));
            }
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
//            lvDownloadProgress.setVisibility(View.GONE);
//            GlobalFunctions.unlockOrientation(getActivity());
            if (result.equalsIgnoreCase("failed")) {
                new GlobalFunctions().showAlertMessage(getContext(), GlobalVariables.ERROR_MESSAGE);
                cardFront.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.ecard_front));
                cardBack.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.ecard_back));
            }else {
                GlobalVariables.bmFront = getBitmapFromView(cardFront);
                ivEcard.setImageBitmap(GlobalVariables.bmFront);
            }
//            GlobalVariables.bmBack = getBitmapFromView(cardBack);
//            ivEcard.setClickable(true);
            new GenerateQRCode().execute("Member ID: " + GlobalVariables.MEMBER_ID + ";"
                    + "Main Member ID: " + GlobalVariables.MAIN_MEMBER_ID + ";"
                    + "Card Number: " + GlobalVariables.CARD_NO + ";"
                    + "Expiry Date: " + GlobalVariables.EXPIRY_DATE_MMddYYYY);
            super.onPostExecute(result);
        }
    }

    private class GenerateQRCode extends AsyncTask<String, Void, byte[]> {

        @Override
        protected void onPreExecute() {
            lvDownloadProgress.setVisibility(View.VISIBLE);
            GlobalFunctions.lockOrientation(getActivity());
        }

        @Override
        protected byte[] doInBackground(String... params) {
            return new WebServiceClass().GenerateQR_eCard(params[0]);
        }

        @Override
        protected void onPostExecute(byte[] bitmapArray) {
            lvDownloadProgress.setVisibility(View.GONE);
            GlobalFunctions.unlockOrientation(getActivity());
            if (bitmapArray == null)
                new GlobalFunctions().showAlertMessage(getContext(), GlobalVariables.ERROR_MESSAGE);
            else {
                ivQRCode.setImageBitmap(BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length));
            }
            GlobalVariables.bmBack = getBitmapFromView(cardBack);
            ivEcard.setClickable(true);
            super.onPostExecute(bitmapArray);
        }
    }
}
