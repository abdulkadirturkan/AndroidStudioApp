package com.example.project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class OgrenciFragment extends Fragment implements View.OnClickListener {
    private Button oldStudent,activeSemStu,newSemStu;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ogrenci,container,false);
        oldStudent = view.findViewById(R.id.oldStudent);
        activeSemStu = view.findViewById(R.id.activeSemStu);
        newSemStu = view.findViewById(R.id.newSemStu);
        oldStudent.setOnClickListener(this);
        activeSemStu.setOnClickListener(this);
        newSemStu.setOnClickListener(this);
      /*  oldStudent.setOnClickListener(this);
        activeStudent.setOnClickListener(this);
        nextStudent.setOnClickListener(this); */
      /*  oldStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //oldStudentFragment oldStudentFragment = new oldStudentFragment();
                //FragmentManager manager = getChildFragmentManager();
               // FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
               // FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                //fragmentTransaction.replace(R.id.fragment_container,oldStudentFragment);
                //fragmentTransaction.addToBackStack(null);
                //fragmentTransaction.commit();
            }
        });

       */
 /*       nextStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextStudentFragment nextStudentFragment = new nextStudentFragment();
                //FragmentManager manager = getChildFragmentManager();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container,nextStudentFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        */
        return view;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.oldStudent:
                oldStudentFragment oldStudentFragment = new oldStudentFragment();
                //FragmentManager manager = getChildFragmentManager();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container,oldStudentFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
        }
        switch (v.getId()){
            case R.id.activeSemStu:
                currentStudentFragment currentStudentFragment = new currentStudentFragment();
                //FragmentManager manager = getChildFragmentManager();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container,currentStudentFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
        }
        switch (v.getId()){
            case R.id.newSemStu:
                nextStudentFragment nextStudentFragment = new nextStudentFragment();
                //FragmentManager manager = getChildFragmentManager();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container,nextStudentFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
        }
    }
}
