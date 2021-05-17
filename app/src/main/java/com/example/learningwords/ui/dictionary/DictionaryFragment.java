package com.example.learningwords.ui.dictionary;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learningwords.MainActivity;
import com.example.learningwords.R;
import com.example.learningwords.Word;
import com.example.learningwords.WordAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class DictionaryFragment extends Fragment {

    private DictionaryViewModel dictionaryViewModel;
    private Word word;

    public static final String BUNDLE_ARG_WORD = "word";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dictionaryViewModel = new ViewModelProvider(this).get(DictionaryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dictionary, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.rv_words);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        WordAdapter wordAdapter = new WordAdapter(this);
        recyclerView.setAdapter(wordAdapter);

        dictionaryViewModel.getDictionary().observe( getViewLifecycleOwner(), new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                if (words != null) {
                    wordAdapter.setWords(words);
                    Log.d(MainActivity.LOG_TAG, words.toString());
                }
            }
        });

        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                NavHostFragment.findNavController(DictionaryFragment.this)
                        .navigate(R.id.action_navigation_dictionary_to_addWordFragment);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && fab.getVisibility() == View.VISIBLE){
                    fab.hide();
                }
                else if (dy < 0 && fab.getVisibility() != View.VISIBLE){
                    fab.show();
                }
            }
        });

        return root;
    }

}