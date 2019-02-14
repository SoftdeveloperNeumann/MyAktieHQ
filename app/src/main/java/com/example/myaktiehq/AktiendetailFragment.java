package com.example.myaktiehq;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AktiendetailFragment extends Fragment {

    public AktiendetailFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_aktiendetail,container,false);
        Intent empfangenerIntent = getActivity().getIntent();
        if(empfangenerIntent!= null && empfangenerIntent.hasExtra(Intent.EXTRA_TEXT))
            ((TextView)rootView.findViewById(R.id.aktiendetail_text))
                    .setText(empfangenerIntent.getStringExtra(Intent.EXTRA_TEXT));

        return rootView;
    }
}
