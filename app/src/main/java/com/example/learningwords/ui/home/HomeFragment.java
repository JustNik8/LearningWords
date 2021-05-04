package com.example.learningwords.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learningwords.MainActivity;
import com.example.learningwords.R;
import com.example.learningwords.Word;
import com.example.learningwords.WordHomeAdapter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    TextView tv;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.home_word_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        WordHomeAdapter wordHomeAdapter = new WordHomeAdapter(this);
        recyclerView.setAdapter(wordHomeAdapter);

        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(getContext());
        int wordsAmount = Integer.parseInt(shared.getString("words_amount", "10"));

        if (wordsAmount != homeViewModel.getWordsAmount()){
            List<Word> words = new ArrayList<>();
            Random random = new Random();
            for (int i = 0; i < wordsAmount; i++){
                int num = random.nextInt(20);
                words.add(new Word("Word" + num, "Слово" + num));
            }
            wordHomeAdapter.setWords(words);
            homeViewModel.setWordsAmount(wordsAmount);
        }


        /*
        tv = root.findViewById(R.id.acc);
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(getContext());
        if (signInAccount != null){
            tv.setText(signInAccount.getEmail());
        }*/

        return root;
    }

}