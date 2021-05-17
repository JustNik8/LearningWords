package com.example.learningwords.ui.training;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.preference.PreferenceManager;

import com.example.learningwords.Constants;
import com.example.learningwords.FireBaseRef;
import com.example.learningwords.MainActivity;
import com.example.learningwords.R;
import com.example.learningwords.User;
import com.example.learningwords.Word;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class TrainingFragment extends Fragment {

    Button answerA, answerB, answerC, answerD, answerE;
    Button buttonTryAgain, buttonGoHome;
    TextView tvTitle, tvPercent;
    TextView tvWord;
    ProgressBar progressBar;

    FirebaseDatabase database;
    DatabaseReference dbWordsRef;
    DatabaseReference dbUsersRef;
    String userId;
    User user;

    int wordsAmount;
    List<Word> correctAnswers;
    Map<Word, Integer> wordNumbers;
    String level;
    List<Button> answerButtons;
    private static final int SIZE = 5;
    int actions;
    Word word;

    TrainingViewModel trainingViewModel;
    SharedPreferences shared;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_training, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        progressBar.setMax(wordsAmount);
        tvTitle.setVisibility(View.GONE);
        tvPercent.setVisibility(View.GONE);
        buttonTryAgain.setVisibility(View.GONE);
        buttonGoHome.setVisibility(View.GONE);

        getUserAndStartTraining();



    }


    private void startTraining(List<Word> words){
        actions = 0;
        Collections.shuffle(words);

        word = words.get(actions);
        updateUI(words);

        correctAnswers = new ArrayList<>();

        for (Button button : answerButtons){
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String answer = button.getText().toString();
                    if (answer.equals(word.getTranslated())){
                        Toast.makeText(getContext(), getString(R.string.right), Toast.LENGTH_SHORT).show();
                        correctAnswers.add(word);
                    }
                    else {
                        Toast.makeText(getContext(), getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                    }
                    actions++;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        progressBar.setProgress(actions, true);
                    }
                    else{
                        progressBar.setProgress(actions);
                    }

                    //the User answered all the questions
                    if (progressBar.getProgress() >= wordsAmount){
                        tvWord.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        progressBar.setProgress(0);
                        for (Button btn : answerButtons){
                            btn.setVisibility(View.GONE);
                        }

                        int percent = (int)((correctAnswers.size()*1.0) / (wordsAmount*1.0) * 100);


                        tvTitle.setVisibility(View.VISIBLE);
                        tvPercent.setVisibility(View.VISIBLE);

                        user.setTrainingPercent(percent);
                        dbUsersRef.child(userId).setValue(user);

                        // If user passed the test
                        if (percent >= 80){
                            tvTitle.setText(R.string.test_passed);
                            //user.addLearntNumbersByLevel(user.getLevel(), user.getWordsInProgress());
                            for (Word correctWord : correctAnswers){
                                user.addLearntNumberByLevel(user.getLevel(), wordNumbers.get(correctWord));
                            }
                            user.clearWordsInProgress();
                            dbUsersRef.child(user.getUserId()).setValue(user);
                            buttonGoHome.setVisibility(View.VISIBLE);
                            SharedPreferences.Editor editor = shared.edit();
                            editor.putInt(Constants.CHANGED_KEY, 1);
                            editor.apply();
                            buttonGoHome.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    NavHostFragment.findNavController(TrainingFragment.this)
                                            .navigate(R.id.action_navigation_training_to_navigation_home);
                                }
                            });

                        }
                        // if user didn't pass the test
                        else {
                            tvTitle.setText(R.string.text_not_passed);
                            buttonTryAgain.setVisibility(View.VISIBLE);
                            buttonTryAgain.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getFragmentManager()
                                            .beginTransaction()
                                            .detach(TrainingFragment.this)
                                            .attach(TrainingFragment.this)
                                            .commit();
                                }
                            });
                        }
                        String message = getString(R.string.correct_answers_percent) + " " + percent + "%";
                        tvPercent.setText(message);
                    }
                    else{
                        updateUI(words);
                    }
                }
            });
        }


    }

    private void updateUI(List<Word> words){
        word = words.get(actions);
        List<String> wordsToAnswer = getWordsToAnswer(word, words);
        tvWord.setText(word.getOriginal());
        for (int i = 0; i < SIZE; i++){
            answerButtons.get(i).setText(wordsToAnswer.get(i));
        }
    }

    private List<String> getWordsToAnswer(Word word, List<Word> words){
        Random random = new Random();
        List<String> wordsToAnswer = new ArrayList<>(SIZE);
        wordsToAnswer.add(word.getTranslated());
        for (int i = 1; i < SIZE; i++){
            String wordToAdd = words.get(random.nextInt(words.size())).getTranslated();
            while (wordsToAnswer.contains(wordToAdd)){
                wordToAdd = words.get(random.nextInt(words.size())).getTranslated();
            }
            wordsToAnswer.add(wordToAdd);
        }
        Collections.shuffle(wordsToAnswer);
        return wordsToAnswer;
    }


    private void init(View view){
        tvWord = view.findViewById(R.id.word);
        answerA = view.findViewById(R.id.button_answer_a);
        answerB = view.findViewById(R.id.button_answer_b);
        answerC = view.findViewById(R.id.button_answer_c);
        answerD = view.findViewById(R.id.button_answer_d);
        answerE = view.findViewById(R.id.button_answer_e);
        answerButtons = new ArrayList<>();
        answerButtons.add(answerA);
        answerButtons.add(answerB);
        answerButtons.add(answerC);
        answerButtons.add(answerD);
        answerButtons.add(answerE);
        progressBar = view.findViewById(R.id.progress_bar);
        buttonTryAgain = view.findViewById(R.id.button_try_again);
        buttonGoHome = view.findViewById(R.id.button_go_home);
        tvTitle = view.findViewById(R.id.title_finish);
        tvPercent = view.findViewById(R.id.tv_percent);

        database = FirebaseDatabase.getInstance(FireBaseRef.ref);
        dbWordsRef = database.getReference(Constants.WORDS_KEY);
        dbUsersRef = database.getReference(Constants.USERS_KEY);
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        shared = PreferenceManager.getDefaultSharedPreferences(getContext());
        wordsAmount = Integer.parseInt(shared.getString("words_amount", "10"));
        level = shared.getString("level", "A1").toUpperCase();

        trainingViewModel = new ViewModelProvider(this).get(TrainingViewModel.class);

        wordNumbers = new HashMap<>();
    }

    private void getUserAndStartTraining(){
        dbUsersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                trainingViewModel.getWords().observe(getViewLifecycleOwner(), new Observer<List<Word>>() {
                    @Override
                    public void onChanged(List<Word> words) {
                        createMap(words, user.getWordsInProgress());
                        startTraining(words);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void createMap(List<Word> wordList, List<Integer> numbers){
        for (int i = 0; i < wordList.size(); i++){
            wordNumbers.put(wordList.get(i), numbers.get(i));
        }
    }


}