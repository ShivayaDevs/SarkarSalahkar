package hackerrepublic.sarkarsalahkar.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hackerrepublic.sarkarsalahkar.R;

/**
 * Inflates the user's profile.
 *
 * @author vermayash8
 */
public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required.
    }

    /**
     * Called when first created.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle
            savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        return view;
    }
}
