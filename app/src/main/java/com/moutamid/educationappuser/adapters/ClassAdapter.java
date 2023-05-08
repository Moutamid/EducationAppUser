package com.moutamid.educationappuser.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.educationappuser.R;
import com.moutamid.educationappuser.models.ItemModel;
import com.moutamid.educationappuser.ui.MySubjectActivity;
import com.moutamid.educationappuser.ui.QuizActivity;
import com.moutamid.educationappuser.utilis.Constants;

import java.util.ArrayList;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.MyVh> {
    Context context;
    ArrayList<ItemModel> list;

    public ClassAdapter(Context context, ArrayList<ItemModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyVh(LayoutInflater.from(context).inflate(R.layout.class_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyVh holder, int position) {
        ItemModel model = list.get(holder.getAdapterPosition());

        holder.icon.setImageResource(model.getIcon());

        if (model.getClassModel() != null) {
            holder.name.setText(model.getClassModel().getName());
        } else {
            holder.name.setText(model.getSubjectModel().getName());
        }

        holder.itemView.setOnClickListener(v -> {
            if (model.getClassModel() != null) {
                Intent i = new Intent(context, MySubjectActivity.class);
                i.putExtra(Constants.ID, model.getClassModel().getID());
                context.startActivity(i);
            } else {
                Intent i = new Intent(context, QuizActivity.class);
                i.putExtra(Constants.ID, model.getSubjectModel().getID());
                i.putExtra(Constants.Class, model.getSubjectModel().getClassName());
                i.putExtra(Constants.Subject, model.getSubjectModel().getName());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyVh extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView name;

        public MyVh(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            name = itemView.findViewById(R.id.name);
        }
    }
}
