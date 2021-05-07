package com.example.learningwords.ui.training;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.learningwords.MainActivity;
import com.example.learningwords.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TrainingFragment extends Fragment {

    Button answerA, answerB, answerC, answerD, answerE;
    DatabaseReference dbWordsRef;
    FirebaseDatabase database;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_training, container, false);
        View separator = root.findViewById(R.id.separator);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO){
            separator.setBackgroundColor(getResources().getColor(R.color.black));
        }
        else{
            separator.setBackgroundColor(getResources().getColor(R.color.white));
        }

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);



    }

    private void init(View view){
        answerA = view.findViewById(R.id.button_answer_a);
        answerB = view.findViewById(R.id.button_answer_b);
        answerC = view.findViewById(R.id.button_answer_c);
        answerD = view.findViewById(R.id.button_answer_d);
        answerE = view.findViewById(R.id.button_answer_e);
        database = FirebaseDatabase.getInstance("https://learningwordsdatabase-default-rtdb.europe-west1.firebasedatabase.app/");
        dbWordsRef = database.getReference(MainActivity.WORDS_KEY);
    }
}