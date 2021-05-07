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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learningwords.MainActivity;
import com.example.learningwords.R;
import com.example.learningwords.User;
import com.example.learningwords.Word;
import com.example.learningwords.WordHomeAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    Button buttonStart;
    WordHomeAdapter wordHomeAdapter;
    DatabaseReference dbUserRef;
    DatabaseReference dbWordsRef;
    FirebaseDatabase database;

    User user;
    String sLevel;
    int sAmount;

    public static final String WORDS_KEY = "words";
    public static final String USERS_KEY = "users";
    public static final String LEARNT_WORDS_KEY = "learntWords";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        database = FirebaseDatabase.getInstance("https://learningwordsdatabase-default-rtdb.europe-west1.firebasedatabase.app/");
        dbWordsRef = database.getReference(WORDS_KEY);
        dbUserRef = database.getReference(USERS_KEY);
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        user = new User(userId);

        dbUserRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);

                SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(getContext());
                int wordsAmount = Integer.parseInt(shared.getString("words_amount", "10"));
                String level = shared.getString("level", "A1").toUpperCase();

                RecyclerView recyclerView = root.findViewById(R.id.home_word_rv);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                wordHomeAdapter = new WordHomeAdapter(HomeFragment.this, user, level, wordsAmount);
                recyclerView.setAdapter(wordHomeAdapter);

                loadWords(user, level, wordsAmount);

                buttonStart = root.findViewById(R.id.button_start_learning);
                buttonStart.setEnabled(user.getTrainingPercent() >= 80);
                    buttonStart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            user.setTrainingPercent(0);
                            buttonStart.setEnabled(false);
                            dbUserRef.child(user.getUserId()).setValue(user);
                            wordHomeAdapter.notifyDataSetChanged();
                        }
                    });

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return root;
    }

    private void loadWords(User user, String level, int wordsAmount){

        int learntWordsByLevel =  user.getLearntWordsByLevel(level);
        int toLearn = learntWordsByLevel + wordsAmount - 1;

        List<Word> words = new ArrayList<>();
        dbWordsRef.child(level).orderByChild("number").startAt(learntWordsByLevel)
                .endAt(toLearn).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String original = String.valueOf(snapshot.child("original").getValue());
                    String translated = String.valueOf(snapshot.child("translated").getValue());
                    words.add(new Word(original, translated));
                }
                wordHomeAdapter.setWords(words);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}