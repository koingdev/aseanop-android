package com.koingdev.aseanop.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.koingdev.aseanop.Constants;
import com.koingdev.aseanop.R;

/**
 * Created by SSK on 06-Jun-17.
 */

public class NoInternet extends Fragment {
    Button retry;
    public static NoInternet newInstance(){
        return new NoInternet();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.nointernet_layout,container, false);
        retry = (Button)rootView.findViewById(R.id.retryBtn);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (Scholarships.scholarshipType) {
                    case Constants.BACHELOR:
                        loadFragment(Constants.BACHELOR);
                        break;
                    case Constants.MASTER:
                        loadFragment(Constants.MASTER);
                        break;
                    case Constants.PHD:
                        loadFragment(Constants.PHD);
                        break;
                    case Constants.INTERN:
                        loadFragment(Constants.INTERN);
                        break;
                    case Constants.CONFERENCE:
                        loadFragment(Constants.CONFERENCE);
                        break;
                    case Constants.TRAINING:
                        loadFragment(Constants.TRAINING);
                        break;
                    case Constants.ONLINE_COURSE:
                        loadFragment(Constants.ONLINE_COURSE);
                        break;
                }

            }
        });
        return rootView;
    }

    public void loadFragment(String scholarshipType) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_main, Scholarships.newInstance(scholarshipType))
                .commit();
    }
}

