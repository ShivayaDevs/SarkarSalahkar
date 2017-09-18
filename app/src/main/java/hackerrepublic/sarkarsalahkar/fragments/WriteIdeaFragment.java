package hackerrepublic.sarkarsalahkar.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hackerrepublic.sarkarsalahkar.R;

/**
 * Created by yash on 18/9/17.
 */

public class WriteIdeaFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_write_idea, container, false);
        return view;
    }

}
