package com.szumusic.szumusicapp.ui.local.filesystem;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.szumusic.szumusicapp.R;


public class LocalDirectoryFragment extends Fragment {

    public LocalDirectoryFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_local_directory, container, false);
    }
}
