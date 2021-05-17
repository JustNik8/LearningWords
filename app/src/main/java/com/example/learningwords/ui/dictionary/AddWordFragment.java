package com.example.learningwords.ui.dictionary;

import android.app.PendingIntent;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learningwords.Constants;
import com.example.learningwords.DBClient;
import com.example.learningwords.MainActivity;
import com.example.learningwords.R;
import com.example.learningwords.Repository;
import com.example.learningwords.Word;

import java.util.ArrayList;
import java.util.List;

public class AddWordFragment extends Fragment {

    Button addWordButton;
    EditText etOriginalWord;
    EditText etTranslatedWord;
    DictionaryViewModel dictionaryViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add_word, container, false);
        dictionaryViewModel = new ViewModelProvider(this).get(DictionaryViewModel.class);

        etOriginalWord = root.findViewById(R.id.add_original_word);
        etTranslatedWord = root.findViewById(R.id.add_translated_word);

        etTranslatedWord.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    if (checkWords()){
                        addWord();
                    }
                }
                return true;
            }
        });

        addWordButton = root.findViewById(R.id.add_word_button);
        addWordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkWords()){
                    addWord();
                }
            }
        });

        return root;
    }

    // main logic of adding word. Firstly, keyboard becomes hidden. Secondly, a new instance of Word is created,
    // this words is added to room database. Finally, AddWordFragment navigates to fragment dictionary.
    private void addWord(){
        View view = getActivity().getCurrentFocus();
        if (view != null){
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        String originalWord = etOriginalWord.getText().toString();
        String translatedWord = etTranslatedWord.getText().toString();
        Word word = new Word(originalWord, translatedWord, Constants.WORD_TYPE_DICTIONARY);

        dictionaryViewModel.insert(word);

        NavHostFragment.findNavController(AddWordFragment.this)
                .navigate(R.id.action_addWordFragment_to_navigation_dictionary);
    }

    // The method checks input words. If there are empty fields or original edit text has not english word,
    // then error sets to the edit text
    private boolean checkWords(){
        String originalWord = etOriginalWord.getText().toString();
        String translatedWord= etTranslatedWord.getText().toString();

        if (originalWord.isEmpty()){
            etOriginalWord.setError(getString(R.string.error_filled_in));
            etOriginalWord.requestFocus();
            return false;
        }

        originalWord = originalWord.toLowerCase();
        for (int i = 0; i < originalWord.length(); i++){
            //If char is english letter or number or space, it is ok
            if (originalWord.charAt(i) < ' ' || originalWord.charAt(i) > 'z'){
                etOriginalWord.setError(getString(R.string.error_english_word));
                etOriginalWord.requestFocus();
                return false;
            }
        }

        if (translatedWord.isEmpty()){
            etTranslatedWord.setError(getString(R.string.error_filled_in));
            etTranslatedWord.requestFocus();
            return false;
        }

        return true;
    }


}