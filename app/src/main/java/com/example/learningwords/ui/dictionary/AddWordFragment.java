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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

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

    public static final String bundleArg1Key = "arg1";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add_word, container, false);
        dictionaryViewModel = new ViewModelProvider(this).get(DictionaryViewModel.class);

        etOriginalWord = root.findViewById(R.id.add_original_word);
        etTranslatedWord = root.findViewById(R.id.add_translated_word);

        addWordButton = root.findViewById(R.id.add_word_button);
        addWordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkWords()){
                    View view = getActivity().getCurrentFocus();
                    if (view != null){
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    String originalWord = etOriginalWord.getText().toString();
                    String translatedWord = etTranslatedWord.getText().toString();
                    Word word = new Word(originalWord, translatedWord);

                    dictionaryViewModel.insert(word);

                    NavHostFragment.findNavController(AddWordFragment.this)
                            .navigate(R.id.action_addWordFragment_to_navigation_dictionary);
                }
            }
        });

        return root;
    }


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
            if ((originalWord.charAt(i) < 'a' || originalWord.charAt(i) > 'z') &&
                    (originalWord.charAt(i) < '1' || originalWord.charAt(i) > '9') &&
                    originalWord.charAt(i) != ' '){
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