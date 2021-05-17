package com.example.learningwords;

import android.util.Log;
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
import java.util.Collections;
import java.util.List;


public class WordHomeAdapter extends RecyclerView.Adapter<WordHomeAdapter.WordHomeViewHolder> {

    List<Word> words = new ArrayList<>();
    Fragment f;

    User user;
    String level;
    int wordsAmount;

    FirebaseDatabase database;
    DatabaseReference dbWordsRef;
    DatabaseReference dbUsersRef;
    Repository repository;

    public WordHomeAdapter(Fragment f, User user , String level, int wordsAmount) {
        this.f = f;
        this.level = level;
        this.wordsAmount = wordsAmount;
        this.user = user;
        database = FirebaseDatabase.getInstance(FireBaseRef.ref);
        dbWordsRef = database.getReference(Constants.WORDS_KEY);
        dbUsersRef = database.getReference(Constants.USERS_KEY);
        repository = new Repository(f.getContext());
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

        //holder.updateButton.setEnabled(user.getTrainingPercent() >= 80);
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
                    Toast.makeText(f.getContext(), R.string.word_changed_text, Toast.LENGTH_SHORT).show();
                    Word wordToDelete = words.get(getAbsoluteAdapterPosition());
                    user.addLearntNumberByLevel(user.getLevel(), user.getWordsInProgress().get(getAbsoluteAdapterPosition()));
                    user.deleteWordInProgress(getAbsoluteAdapterPosition());
                    repository.deleteWord(wordToDelete);
                    updateWord(getAbsoluteAdapterPosition());
                    notifyItemChanged(getAbsoluteAdapterPosition());
                    Log.d(MainActivity.LOG_TAG, "Changed: " + wordToDelete.toString());
                }
            });
        }

        private void updateWord(int index){
            dbWordsRef.child(user.getLevel()).orderByChild("number")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            List<Integer> ignoreNumbers = new ArrayList<>(user.getLearntNumbersByLevel(level));
                            ignoreNumbers.addAll(user.getWordsInProgress());
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                Word word = snapshot.getValue(Word.class);
                                int number = ((Long) snapshot.child("number").getValue()).intValue();

                                if (!ignoreNumbers.contains(number)) {
                                    word.setType(Constants.WORD_TYPE_HOME);
                                    user.addWordInProgress(number);
                                    repository.insert(word);
                                    break;
                                }
                            }
                            dbUsersRef.child(user.getUserId()).setValue(user);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
    }
}
