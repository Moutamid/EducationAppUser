package com.moutamid.educationappuser.listners;

import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.moutamid.educationappuser.adapters.QuizAdapter;

public interface QuizListner {
    void click(boolean check, QuizAdapter.QuizVH holder);
}
