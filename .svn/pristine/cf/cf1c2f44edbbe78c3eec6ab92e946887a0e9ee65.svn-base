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

import ph.com.medilink.maxiflexmobileapp.PlainOldJavaObjects.MedicalHistoryClass;
import ph.com.medilink.maxiflexmobileapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_PastMedical.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_PastMedical#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_PastMedical extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView rvPastMedical;
    private MedicalHistoryRecyclerViewAdapter mAdapter;
    private ArrayList<MedicalHistoryClass> mMedicaList;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Fragment_PastMedical() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_PastMedical.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_PastMedical newInstance(String param1, String param2) {
        Fragment_PastMedical fragment = new Fragment_PastMedical();
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
            mMedicaList = (ArrayList<MedicalHistoryClass>) savedInstanceState.getSerializable("medical_list");
            if (mMedicaList == null) {
                mMedicaList = new ArrayList<MedicalHistoryClass>();
            }
        } else {
            mMedicaList = new ArrayList<MedicalHistoryClass>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_past_medical, container, false);

        rvPastMedical = (RecyclerView) rootView.findViewById(R.id.rvPastMedical);
        rvPastMedical.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPastMedical.addOnItemTouchListener(new RecyclerTouchListener(getContext(), rvPastMedical));

        if (mMedicaList.size() > 0) {
            mAdapter = new MedicalHistoryRecyclerViewAdapter(mMedicaList);
            rvPastMedical.setAdapter(mAdapter);
            rvPastMedical.setVisibility(View.VISIBLE);
        } else {
            mAdapter = new MedicalHistoryRecyclerViewAdapter(mMedicaList);
            rvPastMedical.setAdapter(mAdapter);
            rvPastMedical.setVisibility(View.GONE);
        }


        return rootView;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {

        if (mMedicaList.size() > 0) {
            outState.putSerializable("medical_list", mMedicaList);
        }
        super.onSaveInstanceState(outState);
    }

    public void putInfo(MedicalHistoryClass medicalHistoryClass){
        mAdapter.addItem(medicalHistoryClass);
        rvPastMedical.setVisibility(View.VISIBLE);
    }

    public void clearItems(){
        mAdapter.clearItems();
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
    public class MedicalHistoryRecyclerViewAdapter extends RecyclerView.Adapter<MedicalHistoryRecyclerViewAdapter.MedicalHistoryRecyclerViewHolder> {

        private ArrayList<MedicalHistoryClass> medicalHistoryClasses;


        public class MedicalHistoryRecyclerViewHolder extends RecyclerView.ViewHolder{
            TextView tvType, tvRecDate, tvReason, tvRemarks;

            MedicalHistoryRecyclerViewHolder(View itemView){
                super(itemView);
                tvType = (TextView) itemView.findViewById(R.id.tvType);
                tvRecDate = (TextView) itemView.findViewById(R.id.tvRecDate);
                tvReason = (TextView) itemView.findViewById(R.id.tvReason);
                tvRemarks = (TextView) itemView.findViewById(R.id.tvRemarks);

            }
        }

        public MedicalHistoryRecyclerViewAdapter(ArrayList<MedicalHistoryClass> medicalHistoryClasses){
            this.medicalHistoryClasses = medicalHistoryClasses;
        }

        @Override
        public int getItemCount() {
            return medicalHistoryClasses.size();
        }

        public MedicalHistoryClass getItem(int position){
            return medicalHistoryClasses.get(position);
        }

        public void addItem(MedicalHistoryClass medicalHistoryClass){
            this.medicalHistoryClasses.add(medicalHistoryClass);
            notifyItemInserted(getPosition(medicalHistoryClass));
        }

        public int getPosition(MedicalHistoryClass mh){
            for(int i = 0; i < medicalHistoryClasses.size(); i++){
                if(medicalHistoryClasses.get(i).equals(mh)){
                    return i;
                }
            }
            return -1;
        }

        public void clearItems(){
            this.medicalHistoryClasses.clear();
            notifyDataSetChanged();
        }

        @Override
        public MedicalHistoryRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_medical_history, parent, false);
            MedicalHistoryRecyclerViewHolder viewHolder = new MedicalHistoryRecyclerViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final MedicalHistoryRecyclerViewHolder holder, final int position) {
            holder.tvType.setText(medicalHistoryClasses.get(position).getType());
            holder.tvRecDate.setText(medicalHistoryClasses.get(position).getMonth() + " " + medicalHistoryClasses.get(position).getYear());
            holder.tvRemarks.setText(medicalHistoryClasses.get(position).getRemarks());
            holder.tvReason.setText(medicalHistoryClasses.get(position).getReason());

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
