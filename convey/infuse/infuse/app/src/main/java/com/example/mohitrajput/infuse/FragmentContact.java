package com.example.mohitrajput.infuse;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FragmentContact extends Fragment {

    View v;
    private RecyclerView myrecycleview;
    private List<contact> lstContact;

    public FragmentContact(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.contacct_fragment, container, false);
        myrecycleview = (RecyclerView) v.findViewById(R.id.contact_recycleview);
        RecycleViewAdapter recycleViewAdapter = new RecycleViewAdapter(getContext(),lstContact);
        myrecycleview.setLayoutManager(new LinearLayoutManager(getActivity()));
        myrecycleview.setAdapter(recycleViewAdapter);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lstContact = new ArrayList<>();
        lstContact.add(new contact("mohit","(111) 21542398",R.drawable.ic_group));
        lstContact.add(new contact("mohit","(111) 21542398",R.drawable.ic_group));
        lstContact.add(new contact("mohit","(111) 21542398",R.drawable.ic_group));
        lstContact.add(new contact("mohit","(111) 21542398",R.drawable.ic_group));
        lstContact.add(new contact("mohit","(111) 21542398",R.drawable.ic_group));
        lstContact.add(new contact("mohit","(111) 21542398",R.drawable.ic_group));
        lstContact.add(new contact("mohit","(111) 21542398",R.drawable.ic_group));
    }
}
