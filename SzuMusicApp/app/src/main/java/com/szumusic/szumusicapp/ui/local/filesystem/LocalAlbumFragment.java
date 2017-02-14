package com.szumusic.szumusicapp.ui.local.filesystem;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.szumusic.szumusicapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LocalAlbumFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LocalAlbumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocalAlbumFragment extends Fragment {

    public LocalAlbumFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_local_album, container, false);
    }
}
