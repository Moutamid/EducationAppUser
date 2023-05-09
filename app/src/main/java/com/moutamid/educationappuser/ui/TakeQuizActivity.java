package com.moutamid.educationappuser.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.moutamid.educationappuser.MainActivity;
import com.moutamid.educationappuser.R;
import com.moutamid.educationappuser.adapters.QuizAdapter;
import com.moutamid.educationappuser.databinding.ActivityTakeQuizBinding;
import com.moutamid.educationappuser.listners.QuizListner;
import com.moutamid.educationappuser.models.QuestionsList;
import com.moutamid.educationappuser.models.QuizModel;
import com.moutamid.educationappuser.utilis.Constants;

import java.util.ArrayList;
import java.util.Collections;

public class TakeQuizActivity extends AppCompatActivity {
    ActivityTakeQuizBinding binding;
    ArrayList<QuestionsList> questionsLists;
    ArrayList<QuizModel> quizList;
    String ID, SUBJECT, CLASS;
    int choice = 1;
    int counter = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTakeQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ID = getIntent().getStringExtra(Constants.ID);
        SUBJECT = getIntent().getStringExtra(Constants.Subject);
        CLASS = getIntent().getStringExtra(Constants.Class);
        choice = getIntent().getIntExtra(Constants.Choice, 1);

        binding.header.back.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        quizList = new ArrayList<>();
        questionsLists = new ArrayList<>();

        binding.quizRC.setLayoutManager(new LinearLayoutManager(this));
        binding.quizRC.setHasFixedSize(false);

        binding.header.tittle.setText("Quiz");

        binding.progress.setProgress(0);

        Constants.databaseReference().child(Constants.Quiz).child(ID).get()
                .addOnSuccessListener(dataSnapshot -> {
                    if (dataSnapshot.exists()){
                        quizList.clear();
                        questionsLists.clear();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            QuizModel model = snapshot.getValue(QuizModel.class);
                            QuestionsList questionsModel = new QuestionsList(model.getQuestion(), model.getAnswer1(), model.getAnswer2(), model.getAnswer3(), model.getAnswer4(), model.getCorrectAnswer());
                            questionsLists.add(questionsModel);
                            quizList.add(model);
                        }
                        Collections.shuffle(quizList);

                        ArrayList<QuizModel> temp = new ArrayList<>(quizList);

                        if (choice == 1 && quizList.size() > 60) {
                            quizList.clear();
                            for (int j = 0; j < 60; j++) {
                                quizList.add(temp.get(j));
                            }
                        }

                        binding.progress.setMax(quizList.size());
                        binding.progressIndicator.setText("0/"+quizList.size());

                        QuizAdapter adapter = new QuizAdapter(TakeQuizActivity.this, quizList, listner);
                        binding.quizRC.setAdapter(adapter);


                    } else {
                        new AlertDialog.Builder(TakeQuizActivity.this)
                                .setIcon(R.drawable.round_warning_24)
                                .setTitle("Nothing Found")
                                .setMessage("No Quiz Found For This Subject")
                                .setPositiveButton("Ok", (dialog, which) -> {
                                    dialog.dismiss();
                                    startActivity(new Intent(TakeQuizActivity.this, MainActivity.class));
                                    finish();
                                })
                                .show();
                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });

        binding.check.setOnClickListener(v -> {

        });

    }

    QuizListner listner = new QuizListner() {
        @Override
        public void click(boolean check, QuizAdapter.QuizVH holder) {
            if (!check) {
                counter++;
                binding.progressIndicator.setText(counter+"/"+quizList.size());
                binding.progress.setProgress(counter, true);
                if (counter >= quizList.size()){
                    binding.check.setClickable(true);
                    binding.check.setCardBackgroundColor(getColor(R.color.green));
                    binding.allCheck.setImageResource(R.drawable.round_check_white);
                }
            }
        }
    } ;
}