package ph.com.medilink.maxiflexmobileapp.Fragments.MedicalRecord;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ph.com.medilink.maxiflexmobileapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_Social.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_Social#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Social extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView tvEmployment, tvTobacco, tvAlcohol, tvSexual, tvTravel, tvOtherSubstances;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Fragment_Social() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Social.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Social newInstance(String param1, String param2) {
        Fragment_Social fragment = new Fragment_Social();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_social, container, false);
//        private TextView tvEmployment, tvTobacco, tvAlcohol, tvSexual, tvTravel, tvOtherSubstances;
//        private LinearLayout lvSocialProgress;
        tvEmployment = (TextView) rootView.findViewById(R.id.tvEmployment);
        tvTobacco = (TextView) rootView.findViewById(R.id.tvTobacco);
        tvAlcohol = (TextView) rootView.findViewById(R.id.tvAlcohol);
        tvSexual = (TextView) rootView.findViewById(R.id.tvSexual);
        tvTravel = (TextView) rootView.findViewById(R.id.tvTravel);
        tvOtherSubstances = (TextView) rootView.findViewById(R.id.tvOtherSubstances);

        if(savedInstanceState!=null){
            tvEmployment.setText(savedInstanceState.getString("employment", ""));
            tvTobacco.setText(savedInstanceState.getString("tobacco", ""));
            tvAlcohol.setText(savedInstanceState.getString("alcohol", ""));
            tvSexual.setText(savedInstanceState.getString("sexual", ""));
            tvTravel.setText(savedInstanceState.getString("travel", ""));
            tvOtherSubstances.setText(savedInstanceState.getString("otherSubstances", ""));
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("employment", tvEmployment.getText().toString());
        outState.putString("tobacco", tvTobacco.getText().toString());
        outState.putString("alcohol", tvAlcohol.getText().toString());
        outState.putString("sexual", tvSexual.getText().toString());
        outState.putString("travel", tvTravel.getText().toString());
        outState.putString("otherSubstances", tvOtherSubstances.getText().toString());
        super.onSaveInstanceState(outState);
    }

    public void putInfo(String employment, String tobacco, String alcohol, String sexual, String travel, String otherSubstances){
        tvEmployment.setText(employment);
        tvTobacco.setText(tobacco);
        tvAlcohol.setText(alcohol);
        tvSexual.setText(sexual);
        tvTravel.setText(travel);
        tvOtherSubstances.setText(otherSubstances);
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
