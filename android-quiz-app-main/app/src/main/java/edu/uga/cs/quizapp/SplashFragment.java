package edu.uga.cs.quizapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SplashFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for the fragment
        View rootView = inflater.inflate(R.layout.fragment_splash, container, false);

        // Find the floating action button (FAB)
        FloatingActionButton fab = rootView.findViewById(R.id.fabHelp);

        // Set the click listener for the FAB
        fab.setOnClickListener(view -> {
            // Show the PopupFragment when FAB is clicked
            showPopupFragment();
        });

        return rootView;
    }

    // Method to show the PopupFragment as a dialog
    private void showPopupFragment() {
        PopupFragment popupFragment = new PopupFragment();
        popupFragment.show(getChildFragmentManager(), "PopupFragment");
    }
}