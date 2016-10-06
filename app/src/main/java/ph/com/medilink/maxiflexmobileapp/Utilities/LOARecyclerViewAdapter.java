package ph.com.medilink.maxiflexmobileapp.Utilities;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ph.com.medilink.maxiflexmobileapp.PlainOldJavaObjects.LOGClass;
import ph.com.medilink.maxiflexmobileapp.R;

/**
 * Created by kurt_capatan on 4/19/2016.
 *
 * Recyclerview adapter for LOA, Recyclerview is much more efficient than listview
 *
 * more info here:
 * http://www.androidhive.info/2016/01/android-working-with-recycler-view/
 */
public class LOARecyclerViewAdapter extends RecyclerView.Adapter<LOARecyclerViewAdapter.LOGRecyclerViewHolder> {

    private ArrayList<LOGClass> logClasses;
    private Context mContext;
    private DrawerLayout drawerLayout;


    public class LOGRecyclerViewHolder extends RecyclerView.ViewHolder{
        CardView cvLogItem;
        LinearLayout llsavedOrDownload;
        TextView tvsavedOrDownload,tvIssueDate, tvAvailmentDate,tvLOGNumber, tvClinicName , tvDentistName;

        LOGRecyclerViewHolder(View itemView){
            super(itemView);
            cvLogItem = (CardView) itemView.findViewById(R.id.cvLogItem);
            cvLogItem.setClickable(true);
            llsavedOrDownload = (LinearLayout) itemView.findViewById(R.id.llsavedOrDownload);
            tvsavedOrDownload = (TextView) itemView.findViewById(R.id.tvsavedOrDownload);
            tvIssueDate = (TextView) itemView.findViewById(R.id.tvIssueDate);
            tvAvailmentDate = (TextView) itemView.findViewById(R.id.tvAvailmentDate);
            tvLOGNumber = (TextView) itemView.findViewById(R.id.tvLOGNumber);
            tvClinicName = (TextView) itemView.findViewById(R.id.tvClinicName);
            tvDentistName = (TextView) itemView.findViewById(R.id.tvDentistName);
        }
    }

    public LOARecyclerViewAdapter(ArrayList<LOGClass> logClasses, Context context, DrawerLayout drawerLayout){
        this.mContext = context;
        this.logClasses = logClasses;
        this.drawerLayout = drawerLayout;
    }

    @Override
    public int getItemCount() {
        return logClasses.size();
    }

    public LOGClass getItem(int position){
        return logClasses.get(position);
    }

    public int getPosition(LOGClass lg){
        for(int i = 0; i < logClasses.size(); i++){
            if(logClasses.get(i).equals(lg)){
                return i;
            }
        }
        return -1;
    }

    @Override
    public LOGRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_log, parent, false);
        LOGRecyclerViewHolder viewHolder = new LOGRecyclerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final LOGRecyclerViewHolder holder, final int position) {
        if(logClasses.get(position).isSaved()) {
            holder.llsavedOrDownload.setBackgroundColor(ContextCompat.getColor(mContext, R.color.md_green_500));
            holder.tvsavedOrDownload.setText(mContext.getResources().getString(R.string.saved_tap_to_view));
        }else{
            holder.llsavedOrDownload.setBackgroundColor(ContextCompat.getColor(mContext, R.color.md_orange_400));
            holder.tvsavedOrDownload.setText(mContext.getResources().getString(R.string.not_saved_tap_to_dl));
        }
        holder.tvIssueDate.setText(logClasses.get(position).getIssueDate());
        holder.tvAvailmentDate.setText(logClasses.get(position).getAvailmentDate());
        holder.tvLOGNumber.setText(logClasses.get(position).getLOGNo());
        holder.tvClinicName.setText(logClasses.get(position).getClinicName());
        holder.tvDentistName.setText(logClasses.get(position).getDentistName());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


}
