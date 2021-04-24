package com.example.learningwords.ui.dictionary;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

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

import com.example.learningwords.R;
import com.example.learningwords.Word;

public class ChangeWordFragment extends Fragment {

    private Button changeWordButton;
    private EditText etOriginalWord;
    private EditText etTranslatedWord;

    private Word wordToChange;

    DictionaryViewModel dictionaryViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            wordToChange = (Word) getArguments().getSerializable(DictionaryFragment.BUNDLE_ARG_WORD);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dictionaryViewModel = new ViewModelProvider(this).get(DictionaryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_change_word, container, false);

        etOriginalWord = root.findViewById(R.id.change_original_word);
        etTranslatedWord = root.findViewById(R.id.change_translated_word);

        etOriginalWord.setText(wordToChange.getOriginal());
        etTranslatedWord.setText(wordToChange.getTranslated());
        etTranslatedWord.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    if (checkWords()){
                        changeWord();
                        Toast.makeText(getContext(), getString(R.string.word_changed_text), Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            }
        });

        changeWordButton = root.findViewById(R.id.change_word_button);
        changeWordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkWords()){
                    changeWord();
                    Toast.makeText(getContext(), getString(R.string.word_changed_text), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }

    public void changeWord(){
        View view = getActivity().getCurrentFocus();
        if (view != null){
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        String originalWord = etOriginalWord.getText().toString();
        String translatedWord = etTranslatedWord.getText().toString();
        wordToChange.setOriginal(originalWord);
        wordToChange.setTranslated(translatedWord);

        dictionaryViewModel.update(wordToChange);

        NavHostFragment.findNavController(ChangeWordFragment.this)
                .navigate(R.id.action_changeWordFragment_to_navigation_dictionary);
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