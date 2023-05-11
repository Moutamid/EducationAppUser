package com.moutamid.educationappuser.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.educationappuser.R;
import com.moutamid.educationappuser.models.SaveScoreModel;
import com.moutamid.educationappuser.ui.ScoreActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreVH> {
    Context context;
    ArrayList<SaveScoreModel> list;

    public ScoreAdapter(Context context, ArrayList<SaveScoreModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ScoreVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ScoreVH(LayoutInflater.from(context).inflate(R.layout.score_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreVH holder, int position) {
        SaveScoreModel model = list.get(holder.getAbsoluteAdapterPosition());

        holder.className.setText(model.getClassName());
        holder.subject.setText(model.getSubjectName());

        String score = model.getScore() + "/" + model.getSize();
        holder.score.setText(score);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa", Locale.getDefault());

        String date = dateFormat.format(model.getTimestamp());
        String time = timeFormat.format(model.getTimestamp());

        holder.date.setText(date);
        holder.time.setText(time);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ScoreVH extends RecyclerView.ViewHolder{
        TextView className, subject, score, date, time;
        public ScoreVH(@NonNull View itemView) {
            super(itemView);

            className = itemView.findViewById(R.id.className);
            subject = itemView.findViewById(R.id.subjectName);
            score = itemView.findViewById(R.id.totalScore);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);

        }
    }

}
