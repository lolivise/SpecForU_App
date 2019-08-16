package com.example.tomho.specforu.mainpagefragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tomho.specforu.R;
import com.example.tomho.specforu.adapters.FriendsRecyclerViewAdapter;
import com.example.tomho.specforu.datastrucuture.Friends;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendFragment extends Fragment {

    private View v;
    private RecyclerView recyclerView;
    private List<Friends> friendsList;


    public FriendFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        friendsList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setupFriend();

        v = inflater.inflate(R.layout.fragment_friend,container,false);
        recyclerView = (RecyclerView) v.findViewById(R.id.friend_recyclerview);
        FriendsRecyclerViewAdapter friendsRecyclerViewAdapter = new FriendsRecyclerViewAdapter(getContext(),friendsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(friendsRecyclerViewAdapter);
        return v;
    }

    // Method for setting up friend list
    private void setupFriend(){
        friendsList.add(new Friends(R.drawable.photo1,"Rebaca","Hi", "12:00 AM",""));
        friendsList.add(new Friends(R.drawable.photo2,"Julie","Hi", "12:00 AM",""));
        friendsList.add(new Friends(R.drawable.photo3,"Joe","Hi", "12:00 AM",""));
    }

}
