package com.moutamid.educationappuser.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.educationappuser.R;
import com.moutamid.educationappuser.models.ItemModel;
import com.moutamid.educationappuser.ui.MySubjectActivity;
import com.moutamid.educationappuser.ui.QuizActivity;
import com.moutamid.educationappuser.ui.TakeQuizActivity;
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
                showDialog(model);
            }
        });

    }

    private void showDialog(ItemModel model) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.choice_layout);

        Button all = dialog.findViewById(R.id.allQ);
        all.setOnClickListener(v -> {
            Intent i = new Intent(context, TakeQuizActivity.class);
            i.putExtra(Constants.ID, model.getSubjectModel().getID());
            i.putExtra(Constants.Class, model.getSubjectModel().getClassName());
            i.putExtra(Constants.Subject, model.getSubjectModel().getName());
            i.putExtra(Constants.Choice, 2);
            context.startActivity(i);
        });

        Button Q60 = dialog.findViewById(R.id.Q60);
        Q60.setOnClickListener(v -> {
            Intent i = new Intent(context, TakeQuizActivity.class);
            i.putExtra(Constants.ID, model.getSubjectModel().getID());
            i.putExtra(Constants.Class, model.getSubjectModel().getClassName());
            i.putExtra(Constants.Subject, model.getSubjectModel().getName());
            i.putExtra(Constants.Choice, 1);
            context.startActivity(i);
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.Dialog;
        dialog.getWindow().setGravity(Gravity.CENTER);

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
