package com.moutamid.educationappuser.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.google.firebase.database.DataSnapshot;
import com.moutamid.educationappuser.MainActivity;
import com.moutamid.educationappuser.R;
import com.moutamid.educationappuser.adapters.QuizAdapter;
import com.moutamid.educationappuser.databinding.ActivityTakeQuizBinding;
import com.moutamid.educationappuser.listners.QuizListner;
import com.moutamid.educationappuser.models.QuestionsList;
import com.moutamid.educationappuser.models.QuizModel;
import com.moutamid.educationappuser.models.SaveScoreModel;
import com.moutamid.educationappuser.models.SelectedAnswersModel;
import com.moutamid.educationappuser.utilis.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

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
            ArrayList<SelectedAnswersModel> selectedAnswers = Stash.getArrayList(Constants.SELECTED, SelectedAnswersModel.class);
            int correctAnswers = 0;
            for (int i = 0; i < quizList.size(); i++) {
                for (int j = 0; j < selectedAnswers.size(); j++) {
                    if (i == selectedAnswers.get(j).getPosition()) {
                        if (quizList.get(i).getCorrectAnswer().trim().equals(selectedAnswers.get(j).getAnswer())){
                            correctAnswers++;
                        }
                    }
                }
            }
            showVic(correctAnswers, quizList.size());
        });

    }

    public void showVic(int correctAnswers, int size) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.score_dialog);

        TextView score = dialog.findViewById(R.id.score);
        if (questionsLists.size() > 60) {
            score.setText("You Score : " + correctAnswers + "/60");
        } else {
            score.setText("You Score : " + correctAnswers + "/" + questionsLists.size());
        }

        Button close = dialog.findViewById(R.id.close);
        close.setOnClickListener(v -> dialog.dismiss());

        Button save = dialog.findViewById(R.id.save);
        save.setOnClickListener(v -> {
            saveScore(correctAnswers, size);
            dialog.dismiss();
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.Dialog;
        dialog.getWindow().setGravity(Gravity.CENTER);
    }

    private void saveScore(int correctAnswers, int size) {
        SaveScoreModel model = new SaveScoreModel(CLASS, SUBJECT, correctAnswers, size, new Date().getTime());
        Constants.databaseReference().child(Constants.SCORE).child(Constants.auth().getCurrentUser().getUid())
                .push()
                .setValue(model)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Score Saved", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "Something went wrong when saving the score", Toast.LENGTH_SHORT).show();
                });
    }

    QuizListner listner = new QuizListner() {
        @Override
        public void click(boolean check, QuizAdapter.QuizVH holder) {
            if (!check) {
                counter++;
                binding.progressIndicator.setText(counter+"/"+quizList.size());
                binding.progress.setProgress(counter, true);
                if (counter >= quizList.size()) {
                    binding.check.setClickable(true);
                    binding.check.setCardBackgroundColor(getColor(R.color.green));
                    binding.allCheck.setImageResource(R.drawable.round_check_white);
                }
            }
        }
    } ;
}