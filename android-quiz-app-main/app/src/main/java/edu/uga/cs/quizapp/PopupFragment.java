package edu.uga.cs.quizapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

public class PopupFragment extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout
        View rootView = inflater.inflate(R.layout.fragment_popup, container, false);

        // Set help text from strings.xml
        TextView popupText = rootView.findViewById(R.id.textViewPopup);
        popupText.setText(getString(R.string.help_popup_text));

        // Set up Close button
        Button btnClose = rootView.findViewById(R.id.btnClosePopup);
        btnClose.setOnClickListener(view -> dismiss());

        return rootView;
    }
}