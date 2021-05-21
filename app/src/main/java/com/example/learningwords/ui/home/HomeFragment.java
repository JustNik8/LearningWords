package com.example.learningwords.ui.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learningwords.Constants;
import com.example.learningwords.FireBaseRef;
import com.example.learningwords.MainActivity;
import com.example.learningwords.R;
import com.example.learningwords.Repository;
import com.example.learningwords.User;
import com.example.learningwords.Word;
import com.example.learningwords.WordHomeAdapter;
import com.example.learningwords.ui.training.TrainingFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.learningwords.Constants.USERS_KEY;
import static com.example.learningwords.Constants.WORDS_KEY;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private WordHomeAdapter wordHomeAdapter;
    private DatabaseReference dbUserRef;
    private DatabaseReference dbWordsRef;
    private FirebaseDatabase database;

    private User user;
    private String userId;
    private Repository repository;

    private  boolean changed;
    private int wordsAmount;
    private String level;
    private SharedPreferences shared;

    private TextView notSetInfo, notSetResources;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        database = FirebaseDatabase.getInstance(FireBaseRef.ref);
        dbWordsRef = database.getReference(WORDS_KEY);
        dbUserRef = database.getReference(USERS_KEY);
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        user = new User(userId);
        repository = new Repository(getContext());

        notSetInfo = root.findViewById(R.id.home_not_set_info);
        notSetResources = root.findViewById(R.id.home_not_set_resources);

        shared = PreferenceManager.getDefaultSharedPreferences(getContext());
        wordsAmount = Integer.parseInt(shared.getString("words_amount", "10"));
        level = shared.getString("level", "not_set").toUpperCase();
        changed = shared.getInt("changed", 0) == 1;
        Log.d(MainActivity.LOG_TAG, "LEVEL: " + level);
        Log.d(MainActivity.LOG_TAG, String.valueOf(shared.getInt("changed", 2)));

        dbUserRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);

                RecyclerView recyclerView = root.findViewById(R.id.home_word_rv);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                wordHomeAdapter = new WordHomeAdapter(HomeFragment.this, user, level, wordsAmount);
                recyclerView.setAdapter(wordHomeAdapter);

                if (level.equals("NOT_SET")){
                    recyclerView.setVisibility(View.GONE);
                    notSetInfo.setVisibility(View.VISIBLE);
                    notSetResources.setVisibility(View.VISIBLE);
                    return;
                }

                homeViewModel.getWords().observe(getViewLifecycleOwner(), new Observer<List<Word>>() {
                    @Override
                    public void onChanged(List<Word> words) {
                        if (words != null){
                            wordHomeAdapter.setWords(words);
                        }
                    }
                });

                if (changed){
                    repository.deleteAllWordsByType(Constants.WORD_TYPE_HOME);
                    user.clearWordsInProgress();
                    Log.d(MainActivity.LOG_TAG, "Inside changed = " + changed);
                    loadWordsFromFirebase(user, level, wordsAmount);
                    changed = false;
                    SharedPreferences.Editor editor = shared.edit();
                    editor.putInt("changed", 0);
                    editor.apply();
                }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return root;
    }

    private void loadWordsFromFirebase(User user, String level, int wordsAmount) {
        List<Word> words = new ArrayList<>();
        dbWordsRef.child(level).orderByChild("number")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int counter = 0;
                List<Integer> learntNumbers = user.getLearntNumbersByLevel(level);
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (counter >= wordsAmount){
                        break;
                    }
                    Word word = data.getValue(Word.class);
                    int number = ((Long) data.child("number").getValue()).intValue();
                    if (!learntNumbers.contains(number)) {
                        word.setType(Constants.WORD_TYPE_HOME);
                        user.addWordInProgress(number);
                        repository.insert(word);
                        counter++;
                    }
                }
                dbUserRef.child(user.getUserId()).setValue(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}