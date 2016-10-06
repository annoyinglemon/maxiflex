package ph.com.medilink.maxiflexmobileapp.Fragments.MedicalRecord;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ph.com.medilink.maxiflexmobileapp.PlainOldJavaObjects.VitalSignClass;
import ph.com.medilink.maxiflexmobileapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_VitalSign.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_VitalSign#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_VitalSign extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

//    private DrawerLayout dl_vitalSign;
//    private TextView tvHeight, tvWeight, tvbmi, tvBloodPressure, tvTemperature
//                        ,tvPulseRate, tvRespiratory, tvSP, tvPain, tvRecordDate, tvEmptyList;
    private RecyclerView rvVitalSigns;
    private VitalSignRecyclerViewAdapter mAdapter;
    private ArrayList<VitalSignClass> mVitalSignList;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Fragment_VitalSign() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_VitalSign.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_VitalSign newInstance(String param1, String param2) {
        Fragment_VitalSign fragment = new Fragment_VitalSign();
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
            mVitalSignList = (ArrayList<VitalSignClass>) savedInstanceState.getSerializable("vital_sign_list");
            if (mVitalSignList == null) {
                mVitalSignList = new ArrayList<VitalSignClass>();
            }
        } else {
            mVitalSignList = new ArrayList<VitalSignClass>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_vital_sign, container, false);

        rvVitalSigns = (RecyclerView) rootView.findViewById(R.id.rvVitalSigns);
        rvVitalSigns.setLayoutManager(new LinearLayoutManager(getContext()));
        rvVitalSigns.addOnItemTouchListener(new RecyclerTouchListener(getContext(), rvVitalSigns));

        if (mVitalSignList.size() > 0) {
            mAdapter = new VitalSignRecyclerViewAdapter(mVitalSignList);
            rvVitalSigns.setAdapter(mAdapter);
            rvVitalSigns.setVisibility(View.VISIBLE);
        } else {
            mAdapter = new VitalSignRecyclerViewAdapter(mVitalSignList);
            rvVitalSigns.setAdapter(mAdapter);
            rvVitalSigns.setVisibility(View.GONE);
        }

//        if(savedInstanceState!=null){
//            tvHeight.setText(savedInstanceState.getString("height", ""));
//            tvWeight.setText(savedInstanceState.getString("weight", ""));
//            tvbmi.setText(savedInstanceState.getString("bmi", ""));
//            tvBloodPressure.setText(savedInstanceState.getString("blood_pressure", ""));
//            tvTemperature.setText(savedInstanceState.getString("temperature", ""));
//            tvPulseRate.setText(savedInstanceState.getString("pulse_rate", ""));
//            tvRespiratory.setText(savedInstanceState.getString("respiratory", ""));
//            tvSP.setText(savedInstanceState.getString("sp", ""));
//            tvPain.setText(savedInstanceState.getString("pain_index", ""));
//            tvRecordDate.setText(savedInstanceState.getString("record_date", ""));
//        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        if (mVitalSignList.size() > 0) {
            outState.putSerializable("vital_sign_list", mVitalSignList);
        }

        super.onSaveInstanceState(outState);
    }

    public void putInfo(VitalSignClass vitalSignClass){
        mAdapter.addItem(vitalSignClass);
        rvVitalSigns.setVisibility(View.VISIBLE);
    }

    public void clearItems(){
        mAdapter.clearItems();
    };

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
                    if(itemSelected > -1){

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
     *
     */
    public class VitalSignRecyclerViewAdapter extends RecyclerView.Adapter<VitalSignRecyclerViewAdapter.VitalSignRecyclerViewHolder> {

        private ArrayList<VitalSignClass> vitalSignClasses;


        public class VitalSignRecyclerViewHolder extends RecyclerView.ViewHolder{
            TextView tvPainIndex, tvRecDate, tvHeight, tvWeight, tvBMI, tvBloodPressure, tvTemperature, tvPulse, tvRespiratory, tvSP ;

            VitalSignRecyclerViewHolder(View itemView){
                super(itemView);
                tvPainIndex = (TextView) itemView.findViewById(R.id.tvPainIndex);
                tvRecDate = (TextView) itemView.findViewById(R.id.tvRecDate);
                tvHeight = (TextView) itemView.findViewById(R.id.tvHeight);
                tvWeight = (TextView) itemView.findViewById(R.id.tvWeight);
                tvBMI = (TextView) itemView.findViewById(R.id.tvBMI);
                tvBloodPressure = (TextView) itemView.findViewById(R.id.tvBloodPressure);
                tvTemperature = (TextView) itemView.findViewById(R.id.tvTemperature);
                tvPulse = (TextView) itemView.findViewById(R.id.tvPulse);
                tvRespiratory = (TextView) itemView.findViewById(R.id.tvRespiratory);
                tvSP = (TextView) itemView.findViewById(R.id.tvSP);
            }
        }

        public VitalSignRecyclerViewAdapter(ArrayList<VitalSignClass> vitalSignClasses){
            this.vitalSignClasses = vitalSignClasses;
        }

        public void addItem(VitalSignClass vitalSignClass){
            this.vitalSignClasses.add(vitalSignClass);
            notifyItemInserted(getPosition(vitalSignClass));
        }

        @Override
        public int getItemCount() {
            return vitalSignClasses.size();
        }

        public VitalSignClass getItem(int position){
            return vitalSignClasses.get(position);
        }

        public int getPosition(VitalSignClass vs){
            for(int i = 0; i < vitalSignClasses.size(); i++){
                if(vitalSignClasses.get(i).equals(vs)){
                    return i;
                }
            }
            return -1;
        }

        public void clearItems(){
            this.vitalSignClasses.clear();
            notifyDataSetChanged();
        }

        @Override
        public VitalSignRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vital_sign, parent, false);
            VitalSignRecyclerViewHolder viewHolder = new VitalSignRecyclerViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final VitalSignRecyclerViewHolder holder, final int position) {
            holder.tvPainIndex.setText(vitalSignClasses.get(position).getPainIndex());
            holder.tvRecDate.setText(vitalSignClasses.get(position).getRecDate());
            holder.tvHeight.setText(String.format("%.2f", vitalSignClasses.get(position).getHeight()));
            holder.tvWeight.setText(String.format("%.2f", vitalSignClasses.get(position).getWeight()));
            holder.tvBMI.setText(String.format("%.2f", vitalSignClasses.get(position).getBMI()));
            holder.tvBloodPressure.setText(vitalSignClasses.get(position).getBloodPressure());
            holder.tvTemperature.setText(String.format("%.2f", vitalSignClasses.get(position).getTemperature()));
            holder.tvPulse.setText(String.format("%.2f", vitalSignClasses.get(position).getPulseRate()));
            holder.tvRespiratory.setText(String.format("%.2f", vitalSignClasses.get(position).getRespiratoryRate()));
            holder.tvSP.setText(String.format("%.2f", vitalSignClasses.get(position).getSP()));

        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
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
