package com.moutamid.educationappuser.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.moutamid.educationappuser.MainActivity;
import com.moutamid.educationappuser.R;
import com.moutamid.educationappuser.databinding.ActivityQuizBinding;
import com.moutamid.educationappuser.models.QuestionsList;
import com.moutamid.educationappuser.models.QuizModel;
import com.moutamid.educationappuser.utilis.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.NavigableMap;

public class QuizActivity extends AppCompatActivity {
    ActivityQuizBinding binding;
    int currentQuestionPosition = 0;
    String ID, SUBJECT, CLASS;
    String selectedOptionByUser = "";
    ArrayList<QuestionsList> questionsLists;
    ArrayList<QuizModel> quizList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        questionsLists = new ArrayList<>();
        quizList = new ArrayList<>();

        ID = getIntent().getStringExtra(Constants.ID);
        SUBJECT = getIntent().getStringExtra(Constants.Subject);
        CLASS = getIntent().getStringExtra(Constants.Class);

        binding.header.back.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        binding.header.tittle.setText("Quiz");

        binding.progress.setProgress(1);

        Constants.databaseReference().child(Constants.Quiz).child(ID).get()
                .addOnSuccessListener(dataSnapshot -> {
                    if (dataSnapshot.exists()) {
                        questionsLists.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            QuizModel model = snapshot.getValue(QuizModel.class);
                            QuestionsList questionsModel = new QuestionsList(model.getQuestion(), model.getAnswer1(), model.getAnswer2(), model.getAnswer3(), model.getAnswer4(), model.getCorrectAnswer());
                            questionsLists.add(questionsModel);
                            quizList.add(model);
                        }

                        Collections.shuffle(quizList);

                        QuizModel quiz = quizList.get(currentQuestionPosition);

                        if (quizList.size() > 60){
                            binding.progress.setMax(60);
                            binding.progressIndicator.setText("1/60");
                        } else {
                            binding.progress.setMax(quizList.size());
                            binding.progressIndicator.setText("1/" + quizList.size());
                        }
                        binding.quizName.setText(quiz.getQuizName());
                        binding.quizQuestion.setText(quiz.getQuestion());
                        binding.answer1.setText(quiz.getAnswer1());
                        binding.answer2.setText(quiz.getAnswer2());

                        if (quiz.getAnswer3().isEmpty()) {
                            binding.answer3.setVisibility(View.GONE);
                        } else {
                            binding.answer3.setText(quiz.getAnswer3());
                        }
                        if (quiz.getAnswer4().isEmpty()) {
                            binding.answer4.setVisibility(View.GONE);
                        } else {
                            binding.answer4.setText(quiz.getAnswer4());
                        }

                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });

        iniViews();

    }

    private void iniViews() {
        binding.answer1.setOnClickListener(v -> {
            if (selectedOptionByUser.isEmpty()) {
                selectedOptionByUser = binding.answer1.getText().toString();

                binding.answer1.setBackgroundColor(getColor(R.color.red));
                binding.answer1.setTextColor(Color.WHITE);

                // reveal answer
                revealAnswer();

                questionsLists.get(currentQuestionPosition).setUserSelectedOption(selectedOptionByUser);
            }
        });

        binding.answer2.setOnClickListener(v -> {
            if (selectedOptionByUser.isEmpty()) {
                selectedOptionByUser = binding.answer2.getText().toString();

                binding.answer2.setBackgroundColor(getColor(R.color.red));
                binding.answer2.setTextColor(Color.WHITE);

                // reveal answer
                revealAnswer();

                questionsLists.get(currentQuestionPosition).setUserSelectedOption(selectedOptionByUser);
            }
        });

        binding.answer3.setOnClickListener(v -> {
            if (selectedOptionByUser.isEmpty()) {
                selectedOptionByUser = binding.answer3.getText().toString();

                binding.answer3.setBackgroundColor(getColor(R.color.red));
                binding.answer3.setTextColor(Color.WHITE);

                // reveal answer
                revealAnswer();

                questionsLists.get(currentQuestionPosition).setUserSelectedOption(selectedOptionByUser);
            }
        });

        binding.answer4.setOnClickListener(v -> {
            if (selectedOptionByUser.isEmpty()) {
                selectedOptionByUser = binding.answer4.getText().toString();

                binding.answer4.setBackgroundColor(getColor(R.color.red));
                binding.answer4.setTextColor(Color.WHITE);

                // reveal answer
                revealAnswer();

                questionsLists.get(currentQuestionPosition).setUserSelectedOption(selectedOptionByUser);
            }
        });

        binding.submit.setOnClickListener(v -> {
            if (selectedOptionByUser.isEmpty()) {
                Toast.makeText(this, "Please select an option", Toast.LENGTH_SHORT).show();
            } else {
                changeNextQuestion();
            }
        });
    }

    private void changeNextQuestion() {
        currentQuestionPosition = currentQuestionPosition + 1;
        if (currentQuestionPosition < 60 && currentQuestionPosition < quizList.size()) {
            QuizModel quiz = quizList.get(currentQuestionPosition);
            selectedOptionByUser = "";

            binding.answer1.setBackgroundColor(getColor(R.color.blue));
            binding.answer1.setTextColor(Color.BLACK);
            binding.answer2.setBackgroundColor(getColor(R.color.blue));
            binding.answer2.setTextColor(Color.BLACK);
            binding.answer3.setBackgroundColor(getColor(R.color.blue));
            binding.answer3.setTextColor(Color.BLACK);
            binding.answer4.setBackgroundColor(getColor(R.color.blue));
            binding.answer4.setTextColor(Color.BLACK);


            if (quizList.size() > 60){
                binding.progressIndicator.setText((currentQuestionPosition + 1) + "/60");
            } else {
                binding.progressIndicator.setText( (currentQuestionPosition + 1) + "/" + quizList.size());
            }
            binding.progress.setProgressCompat(currentQuestionPosition + 1, true);
            binding.quizName.setText(quiz.getQuizName());
            binding.quizQuestion.setText(quiz.getQuestion());
            binding.answer1.setText(quiz.getAnswer1());
            binding.answer2.setText(quiz.getAnswer2());
            if (quiz.getAnswer3().isEmpty()) {
                binding.answer3.setVisibility(View.GONE);
            } else {
                binding.answer3.setVisibility(View.VISIBLE);
                binding.answer3.setText(quiz.getAnswer3());
            }
            if (quiz.getAnswer4().isEmpty()) {
                binding.answer4.setVisibility(View.GONE);
            } else {
                binding.answer4.setVisibility(View.VISIBLE);
                binding.answer4.setText(quiz.getAnswer4());
            }
        } else {
            checkingAnswer();
        }
    }

    private void checkingAnswer() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Checking Answers");
        progressDialog.show();

        int correctAnswers = 0;

        for (int i = 0; i < questionsLists.size() && i < 60; i++) {
            final String getUserSelectedOption = questionsLists.get(i).getUserSelectedOption();
            final String getAnswer = questionsLists.get(i).getAnswer();

            // compare user selected option with original answer
            if (getUserSelectedOption.equals(getAnswer)) {
                correctAnswers++;
            }
        }
        showVic(correctAnswers);
        progressDialog.dismiss();
    }

    public void showVic(int correctAnswers) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.score_dialog);

        TextView score = dialog.findViewById(R.id.score);
        if (questionsLists.size()>60){
            score.setText("You Score : " + correctAnswers + "/60");
        } else {
            score.setText("You Score : " + correctAnswers + "/" + questionsLists.size());
        }

        Button close = dialog.findViewById(R.id.close);
        close.setOnClickListener(v -> dialog.dismiss());

        Button save = dialog.findViewById(R.id.save);
        save.setOnClickListener(v -> {
            saveScore();
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.Dialog;
        dialog.getWindow().setGravity(Gravity.CENTER);
    }

    private void saveScore() {

    }

    private void revealAnswer() {
// get answer of current question
        final String getAnswer = questionsLists.get(currentQuestionPosition).getAnswer();

        // change background color and text color of option which match with answer
        if (binding.answer1.getText().toString().equals(getAnswer)) {
            binding.answer1.setBackgroundColor(getColor(R.color.green));
            binding.answer1.setTextColor(Color.WHITE);
        } else if (binding.answer2.getText().toString().equals(getAnswer)) {
            binding.answer2.setBackgroundColor(getColor(R.color.green));
            binding.answer2.setTextColor(Color.WHITE);
        } else if (binding.answer3.getText().toString().equals(getAnswer)) {
            binding.answer3.setBackgroundColor(getColor(R.color.green));
            binding.answer3.setTextColor(Color.WHITE);
        } else if (binding.answer4.getText().toString().equals(getAnswer)) {
            binding.answer4.setBackgroundColor(getColor(R.color.green));
            binding.answer4.setTextColor(Color.WHITE);
        }
    }
}