package com.example.learningwords.ui.dictionary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.learningwords.R;

public class DictionaryFragment extends Fragment {

    private DictionaryViewModel dictionaryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dictionaryViewModel =
                new ViewModelProvider(this).get(DictionaryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dictionary, container, false);
        final TextView textView = root.findViewById(R.id.text_dictionary);
        dictionaryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}