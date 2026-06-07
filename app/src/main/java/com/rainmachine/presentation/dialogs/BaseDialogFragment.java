package com.rainmachine.presentation.dialogs;

import androidx.fragment.app.DialogFragment;

import org.parceler.Parcels;

public class BaseDialogFragment extends DialogFragment {

    public <T> T getParcelable(String extra) {
        return Parcels.unwrap(getArguments().getParcelable(extra));
    }

    public void setParcelable(String extra, Object obj) {
        getArguments().putParcelable(extra, Parcels.wrap(obj));
    }
}
