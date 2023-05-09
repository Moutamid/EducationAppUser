package com.moutamid.educationappuser.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.moutamid.educationappuser.R;
import com.moutamid.educationappuser.listners.QuizListner;
import com.moutamid.educationappuser.models.QuizModel;
import com.moutamid.educationappuser.models.SelectedAnswersModel;

import java.util.ArrayList;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.QuizVH> {
    Context context;
    ArrayList<QuizModel> quizList;
    ArrayList<SelectedAnswersModel> selectedAnswers;
    QuizListner quizListner;
    String[] selected_answers = new String[]{};
    int[] positions;

    private static final int QUIZ_MCQs = 0;
    private static final int QUIZ_NORMAL = 1;

    public QuizAdapter(Context context, ArrayList<QuizModel> quizList, QuizListner quizListner) {
        this.context = context;
        this.quizList = quizList;
        this.quizListner = quizListner;
        this.selectedAnswers = new ArrayList<>();
    }

    @NonNull
    @Override
    public QuizVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == QUIZ_NORMAL){
            view = LayoutInflater.from(context).inflate(R.layout.question_layout, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.question_mcqs_layout, parent, false);
        }
        return new QuizVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizVH holder, int position) {
        QuizModel quizModel = quizList.get(holder.getAdapterPosition());
        String ques = "Q" + (holder.getAdapterPosition() + 1) + " " + quizModel.getQuestion();
        holder.quizQuestion.setText(ques);

        holder.answer1.setText(quizModel.getAnswer1());
        holder.answer2.setText(quizModel.getAnswer2());

        if (quizModel.getAnswer3().isEmpty()) {
            holder.quizAns3.setVisibility(View.GONE);
        } else {
            holder.answer3.setText(quizModel.getAnswer3());
        }

        if (quizModel.getAnswer4().isEmpty()) {
            holder.quizAns4.setVisibility(View.GONE);
        } else {
            holder.answer4.setText(quizModel.getAnswer4());
        }

        clickListeners(holder, holder.getAdapterPosition());

    }

    private void clickListeners(QuizVH holder, int pos) {
        holder.quizAns1.setOnClickListener(v -> {
            if (quizList.get(pos).isType()) {
                if (!holder.multiCheck){
                    holder.multiCheck = true;
                    holder.option1Image.setImageResource(R.drawable.round_check_white);
                    holder.option1.setCardBackgroundColor(context.getColor(R.color.green));
                } else {
                    holder.multiCheck = false;
                    holder.option1Image.setImageResource(R.drawable.round_check_24);
                    holder.option1.setCardBackgroundColor(context.getColor(R.color.blue));
                }
            } else {
                holder.option1Text.setTextColor(context.getColor(R.color.white));
                holder.option1.setCardBackgroundColor(context.getColor(R.color.green));

                holder.option2Text.setTextColor(context.getColor(R.color.dark));
                holder.option2.setCardBackgroundColor(context.getColor(R.color.blue));
                holder.option3Text.setTextColor(context.getColor(R.color.dark));
                holder.option3.setCardBackgroundColor(context.getColor(R.color.blue));
                holder.option4Text.setTextColor(context.getColor(R.color.dark));
                holder.option4.setCardBackgroundColor(context.getColor(R.color.blue));
            }
            quizListner.click(holder.check, holder);
            holder.check = true;
        });

        holder.quizAns2.setOnClickListener(v -> {
            if (quizList.get(pos).isType()){
                if (!holder.multiCheck){
                    holder.multiCheck = true;
                    holder.option2Image.setImageResource(R.drawable.round_check_white);
                    holder.option2.setCardBackgroundColor(context.getColor(R.color.green));
                } else {
                    holder.multiCheck = false;
                    holder.option2Image.setImageResource(R.drawable.round_check_24);
                    holder.option2.setCardBackgroundColor(context.getColor(R.color.blue));
                }
            } else {
                holder.option2Text.setTextColor(context.getColor(R.color.white));
                holder.option2.setCardBackgroundColor(context.getColor(R.color.green));

                holder.option1Text.setTextColor(context.getColor(R.color.dark));
                holder.option1.setCardBackgroundColor(context.getColor(R.color.blue));
                holder.option3Text.setTextColor(context.getColor(R.color.dark));
                holder.option3.setCardBackgroundColor(context.getColor(R.color.blue));
                holder.option4Text.setTextColor(context.getColor(R.color.dark));
                holder.option4.setCardBackgroundColor(context.getColor(R.color.blue));
            }
            quizListner.click(holder.check, holder);
            holder.check = true;
        });

        holder.quizAns3.setOnClickListener(v -> {
            if (quizList.get(pos).isType()){
                if (!holder.multiCheck){
                    holder.multiCheck = true;
                    holder.option3Image.setImageResource(R.drawable.round_check_white);
                    holder.option3.setCardBackgroundColor(context.getColor(R.color.green));
                } else {
                    holder.multiCheck = false;
                    holder.option3Image.setImageResource(R.drawable.round_check_24);
                    holder.option3.setCardBackgroundColor(context.getColor(R.color.blue));
                }
            } else {
                holder.option3Text.setTextColor(context.getColor(R.color.white));
                holder.option3.setCardBackgroundColor(context.getColor(R.color.green));

                holder.option1Text.setTextColor(context.getColor(R.color.dark));
                holder.option1.setCardBackgroundColor(context.getColor(R.color.blue));
                holder.option2Text.setTextColor(context.getColor(R.color.dark));
                holder.option2.setCardBackgroundColor(context.getColor(R.color.blue));
                holder.option4Text.setTextColor(context.getColor(R.color.dark));
                holder.option4.setCardBackgroundColor(context.getColor(R.color.blue));
            }
            quizListner.click(holder.check, holder);
            holder.check = true;
        });

        holder.quizAns4.setOnClickListener(v -> {
            if (quizList.get(pos).isType()){
                if (!holder.multiCheck){
                    holder.multiCheck = true;
                    holder.option4Image.setImageResource(R.drawable.round_check_white);
                    holder.option4.setCardBackgroundColor(context.getColor(R.color.green));
                } else {
                    holder.multiCheck = false;
                    holder.option4Image.setImageResource(R.drawable.round_check_24);
                    holder.option4.setCardBackgroundColor(context.getColor(R.color.blue));
                }

            } else {
                holder.option4Text.setTextColor(context.getColor(R.color.white));
                holder.option4.setCardBackgroundColor(context.getColor(R.color.green));

                holder.option1Text.setTextColor(context.getColor(R.color.dark));
                holder.option1.setCardBackgroundColor(context.getColor(R.color.blue));
                holder.option2Text.setTextColor(context.getColor(R.color.dark));
                holder.option2.setCardBackgroundColor(context.getColor(R.color.blue));
                holder.option3Text.setTextColor(context.getColor(R.color.dark));
                holder.option3.setCardBackgroundColor(context.getColor(R.color.blue));
            }
            quizListner.click(holder.check, holder);
            holder.check = true;
        });
    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return quizList.get(position).isType() ? QUIZ_MCQs : QUIZ_NORMAL;
    }

    public class QuizVH extends RecyclerView.ViewHolder {
        TextView quizQuestion, answer1, answer2, answer3, answer4;
        TextView option1Text, option2Text, option3Text, option4Text;
        MaterialCardView option1, option2, option3, option4;
        ImageView option1Image, option2Image, option3Image, option4Image;
        MaterialCardView quizAns1, quizAns2, quizAns3, quizAns4;
        public boolean check, multiCheck;
        public QuizVH(@NonNull View itemView) {
            super(itemView);

            quizQuestion = itemView.findViewById(R.id.quizQuestion);
            answer1 = itemView.findViewById(R.id.answer1);
            answer2 = itemView.findViewById(R.id.answer2);
            answer3 = itemView.findViewById(R.id.answer3);
            answer4 = itemView.findViewById(R.id.answer4);

            option1Text = itemView.findViewById(R.id.option1Text);
            option2Text = itemView.findViewById(R.id.option2Text);
            option3Text = itemView.findViewById(R.id.option3Text);
            option4Text = itemView.findViewById(R.id.option4Text);

            option1 = itemView.findViewById(R.id.option1);
            option2 = itemView.findViewById(R.id.option2);
            option3 = itemView.findViewById(R.id.option3);
            option4 = itemView.findViewById(R.id.option4);

            option1Image = itemView.findViewById(R.id.option1Image);
            option2Image = itemView.findViewById(R.id.option2Image);
            option3Image = itemView.findViewById(R.id.option3Image);
            option4Image = itemView.findViewById(R.id.option4Image);

            quizAns1 = itemView.findViewById(R.id.quizAns1);
            quizAns2 = itemView.findViewById(R.id.quizAns2);
            quizAns3 = itemView.findViewById(R.id.quizAns3);
            quizAns4 = itemView.findViewById(R.id.quizAns4);

            check = multiCheck = false;
        }
    }

}
