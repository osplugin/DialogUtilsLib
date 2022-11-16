package com.osard.dialogfragmentutilslib;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.osard.dialogfragmentutilslib.databinding.DialogUtilsLibTipBinding;

public class DialogFragmentLibCommon extends DialogFragment implements View.OnClickListener {

    private DialogUtilsLibTipBinding binding;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_utils_lib_tip, container, false);
        binding.setClick(this);
        return binding.getRoot();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void showNow(@NonNull FragmentManager manager, @Nullable String tag) {
        super.showNow(manager, tag);

    }
}
