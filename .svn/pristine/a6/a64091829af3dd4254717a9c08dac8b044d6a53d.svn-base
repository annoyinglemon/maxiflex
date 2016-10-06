package ph.com.medilink.maxiflexmobileapp.Fragments.MedicalRecord;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ph.com.medilink.maxiflexmobileapp.PlainOldJavaObjects.AllergyClass;
import ph.com.medilink.maxiflexmobileapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_Allergy.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_Allergy#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Allergy extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView rvAllergy;
    private AllergyRecyclerViewAdapter mAdapter;
    private ArrayList<AllergyClass> mAllergyList;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Fragment_Allergy() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Allergy.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Allergy newInstance(String param1, String param2) {
        Fragment_Allergy fragment = new Fragment_Allergy();
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
            mAllergyList = (ArrayList<AllergyClass>) savedInstanceState.getSerializable("allergy_list");
            if (mAllergyList == null) {
                mAllergyList = new ArrayList<AllergyClass>();
            }
        } else {
            mAllergyList = new ArrayList<AllergyClass>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_allergy, container, false);

        rvAllergy = (RecyclerView) rootView.findViewById(R.id.rvAllergy);
        rvAllergy.setLayoutManager(new LinearLayoutManager(getContext()));
        rvAllergy.addOnItemTouchListener(new RecyclerTouchListener(getContext(), rvAllergy));

        if (mAllergyList.size() > 0) {
            mAdapter = new AllergyRecyclerViewAdapter(mAllergyList);
            rvAllergy.setAdapter(mAdapter);
            rvAllergy.setVisibility(View.VISIBLE);
        } else {
            mAdapter = new AllergyRecyclerViewAdapter(mAllergyList);
            rvAllergy.setAdapter(mAdapter);
            rvAllergy.setVisibility(View.GONE);
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mAllergyList.size() > 0) {
            outState.putSerializable("allergy_list", mAllergyList);
        }
        super.onSaveInstanceState(outState);
    }

    public void putInfo(AllergyClass allergyClass){
        mAdapter.addItem(allergyClass);
        rvAllergy.setVisibility(View.VISIBLE);
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
                        AllergyClass ac = mAdapter.getItem(itemSelected);

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
    public class AllergyRecyclerViewAdapter extends RecyclerView.Adapter<AllergyRecyclerViewAdapter.AllergyRecyclerViewHolder> {

        private ArrayList<AllergyClass> allergyClasses;


        public class AllergyRecyclerViewHolder extends RecyclerView.ViewHolder{
            TextView tvAllergy, tvDesensitized, tvSymptom, tvManagement;
            LinearLayout lldesens;


            AllergyRecyclerViewHolder(View itemView){
                super(itemView);
                lldesens = (LinearLayout) itemView.findViewById(R.id.lldesens);
                tvAllergy = (TextView) itemView.findViewById(R.id.tvAllergy);
                tvSymptom = (TextView) itemView.findViewById(R.id.tvSymptom);
                tvDesensitized = (TextView) itemView.findViewById(R.id.tvDesensitized);
                tvManagement= (TextView) itemView.findViewById(R.id.tvManagement);
            }
        }

        public AllergyRecyclerViewAdapter(ArrayList<AllergyClass> allergyClasses){
            this.allergyClasses = allergyClasses;
        }

        @Override
        public int getItemCount() {
            return allergyClasses.size();
        }

        public AllergyClass getItem(int position){
            return allergyClasses.get(position);
        }

        public void addItem(AllergyClass allergyClass){
            this.allergyClasses.add(allergyClass);
            notifyItemInserted(getPosition(allergyClass));
        }

        public int getPosition(AllergyClass allergy){
            for(int i = 0; i < allergyClasses.size(); i++){
                if(allergyClasses.get(i).equals(allergy)){
                    return i;
                }
            }
            return -1;
        }

        public void clearItems(){
            this.allergyClasses.clear();
            notifyDataSetChanged();
        }

        @Override
        public AllergyRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_allergy, parent, false);
            AllergyRecyclerViewHolder viewHolder = new AllergyRecyclerViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final AllergyRecyclerViewHolder holder, final int position) {
            holder.tvAllergy.setText(allergyClasses.get(position).getAllergyName());
            holder.tvSymptom.setText(allergyClasses.get(position).getSymptoms());
            if(allergyClasses.get(position).isDesensitized()){
                holder.tvDesensitized.setText(getContext().getResources().getString(R.string.desensitized));
                holder.lldesens.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.md_green_500));
            }else{
                holder.tvDesensitized.setText(getContext().getResources().getString(R.string.sensitive));
                holder.lldesens.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.md_deep_orange_500));
            }
            holder.tvManagement.setText(allergyClasses.get(position).getManagement());

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
