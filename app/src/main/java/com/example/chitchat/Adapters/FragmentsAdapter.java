package com.example.chitchat.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.chitchat.Fragments.CallsFragment;
import com.example.chitchat.Fragments.Chatsfragment;
import com.example.chitchat.Fragments.Statusfragment;

public class FragmentsAdapter extends FragmentPagerAdapter {
    public FragmentsAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Chatsfragment();
            case 1:
                return new Statusfragment();
            case 2:
                return new CallsFragment();
            default:
                return new Chatsfragment();
        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title =null;
        if(position==0){
            title="CHATS";
        }
        if(position==1){
            title="STATUS";
        }
       if(position==2){
            title="CALLS";
        }
        return title;
    }
}