package com.example.learningwords;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordViewHolder> {

    List<Word> words = new ArrayList<>();


    @NonNull
    @Override
    public WordAdapter.WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_item, parent, false);
        return new WordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordAdapter.WordViewHolder holder, int position) {
        Word word = words.get(position);
        holder.originalWord.setText(word.getOriginal());
        holder.translatedWord.setText(word.getTranslated());

        boolean isExpanded = words.get(position).isExpanded();
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
    }

    public void addWord(Word word){
        this.words.add(word);
        notifyDataSetChanged();
    }

    public void setWords(List<Word> words){
        this.words = words;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    public class WordViewHolder extends RecyclerView.ViewHolder {

        TextView originalWord;
        TextView translatedWord;
        ConstraintLayout expandableLayout;
        ConstraintLayout mainLayout;

        public WordViewHolder(@NonNull View itemView) {
            super(itemView);

            originalWord = itemView.findViewById(R.id.original_word);
            translatedWord = itemView.findViewById(R.id.translated_word);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);
            mainLayout = itemView.findViewById(R.id.main);

            mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Word word = words.get(getAdapterPosition());
                    word.setExpanded(!word.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });



        }
    }
}
