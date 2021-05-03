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

import java.util.ArrayList;
import java.util.List;

public class WordHomeAdapter extends RecyclerView.Adapter<WordHomeAdapter.WordHomeViewHolder> {

    List<Word> words = new ArrayList<>();
    HomeViewModel homeViewModel;
    Fragment f;

    public WordHomeAdapter(Fragment f) {
        homeViewModel = new ViewModelProvider(f).get(HomeViewModel.class);
        this.f = f;
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
                }
            });

        }
    }
}
