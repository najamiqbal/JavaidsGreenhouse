package com.fyp.javaidsgreenhouse.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp.javaidsgreenhouse.R;
import com.fyp.javaidsgreenhouse.models.feedbackModel;

import java.util.List;

public class FeedbackListAdapter extends RecyclerView.Adapter<FeedbackListAdapter.ViewHolder> {
    Context context;
    List<feedbackModel> modelList;
    public FeedbackListAdapter(Context context, List<feedbackModel> itemList) {
        this.context = context;
        this.modelList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_list_viewholder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final feedbackModel modelClass=modelList.get(position);
        holder.tv_feedback.setText(modelClass.getFeedback());
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_feedback;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_feedback=itemView.findViewById(R.id.tv_feedback);
        }
    }
}
