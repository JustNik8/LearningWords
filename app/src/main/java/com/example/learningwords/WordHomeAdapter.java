package com.example.learningwords;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learningwords.ui.home.HomeViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class WordHomeAdapter extends RecyclerView.Adapter<WordHomeAdapter.WordHomeViewHolder> {

    List<Word> words = new ArrayList<>();
    HomeViewModel homeViewModel;
    Fragment f;

    User user;
    String level;
    int wordsAmount;

    FirebaseDatabase database;
    DatabaseReference dbWordsRef;
    DatabaseReference dbUsersRef;

    public WordHomeAdapter(Fragment f, User user, String level, int wordsAmount) {
        homeViewModel = new ViewModelProvider(f).get(HomeViewModel.class);
        this.f = f;
        this.user = user;
        this.level = level;
        this.wordsAmount = wordsAmount;
        database = FirebaseDatabase.getInstance("https://learningwordsdatabase-default-rtdb.europe-west1.firebasedatabase.app/");
        dbWordsRef = database.getReference(MainActivity.WORDS_KEY);
        dbUsersRef = database.getReference(MainActivity.USERS_KEY);
    }

    public void setWords(List<Word> words) {
        this.words = words;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WordHomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_home_item, parent, false);
        return new WordHomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordHomeViewHolder holder, int position) {
        Word word = words.get(position);

        holder.original.setText(word.getOriginal());
        holder.translated.setText(word.getTranslated());

        holder.updateButton.setEnabled(user.getTrainingPercent() >= 80);
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    public class WordHomeViewHolder extends RecyclerView.ViewHolder {
        TextView original;
        TextView translated;
        ImageButton updateButton;
        public WordHomeViewHolder(@NonNull View itemView) {
            super(itemView);

            original = itemView.findViewById(R.id.home_original);
            translated = itemView.findViewById(R.id.home_translated);

            updateButton = itemView.findViewById(R.id.button_update);
            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(f.getContext(), "Updating", Toast.LENGTH_SHORT).show();
                    Word word = words.get(getAdapterPosition());
                    int numberOfWord = user.getLearntWordsByLevel(level) + wordsAmount;

                    dbWordsRef.child(level).orderByChild("number").startAt(numberOfWord)
                            .endAt(numberOfWord).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                String original = String.valueOf(snapshot.child("original").getValue());
                                String translated = String.valueOf(snapshot.child("translated").getValue());
                                word.setOriginal(original);
                                word.setTranslated(translated);
                                notifyDataSetChanged();

                                user.addLearntWordsByLevel(level, 1);
                                dbUsersRef.child(user.getUserId()).setValue(user);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });

                }
            });

        }
    }
}
