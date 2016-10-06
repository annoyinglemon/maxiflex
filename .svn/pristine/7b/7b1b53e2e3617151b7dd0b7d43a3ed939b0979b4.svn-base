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
 * {@link Fragment_PatientProfile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_PatientProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_PatientProfile extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView tvPatientName,  tvBirthdate, tvSex;

    private OnFragmentInteractionListener mListener;


    public Fragment_PatientProfile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Fragment_PatientProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_PatientProfile newInstance(String patientName, String birthDate, String sex) {
        Fragment_PatientProfile fragment = new Fragment_PatientProfile();
        Bundle args = new Bundle();
        args.putString("patient_name", patientName);
        args.putString("birth_date", birthDate);
        args.putString("sex", sex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_patient_profile, container, false);
        tvPatientName = (TextView) rootView.findViewById(R.id.tvPatientName);
        tvBirthdate = (TextView) rootView.findViewById(R.id.tvBirthdate);
        tvSex = (TextView) rootView.findViewById(R.id.tvSex);

        if(savedInstanceState!=null){
            tvPatientName.setText(savedInstanceState.getString("patient_name", ""));
            tvBirthdate.setText(savedInstanceState.getString("birth_date", ""));
            tvSex.setText(savedInstanceState.getString("sex", ""));
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("patient_name", tvPatientName.getText().toString());
        outState.putString("birth_date", tvBirthdate.getText().toString());
        outState.putString("sex", tvSex.getText().toString());
        super.onSaveInstanceState(outState);
    }


    public void putInfo(String patientName, String birhdate, String gender){
        tvPatientName.setText(patientName);
        tvBirthdate.setText(birhdate);
        tvSex.setText(gender);
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
