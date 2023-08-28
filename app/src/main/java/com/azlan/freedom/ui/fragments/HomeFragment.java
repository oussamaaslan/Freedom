package com.azlan.freedom.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.azlan.freedom.R;
import com.azlan.freedom.models.Feed;
import com.azlan.freedom.ui.adapters.FeedAdapter;
import com.azlan.freedom.viewmodels.FeedViewModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import java.util.concurrent.atomic.AtomicReference;

public class HomeFragment extends Fragment {

    private FeedViewModel feedViewModel;
   private FeedAdapter feedadapter;
    public HomeFragment() {
        // Required empty public constructor
    }

  public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview= inflater.inflate(R.layout.fragment_home, container, false);
        // Inflate the layout for this fragment
        feedViewModel = new ViewModelProvider(this).get(FeedViewModel.class);
        RecyclerView recyclerView = rootview.findViewById(R.id.idrecyleVfeeds);
        // Your list of posts from API or other sources
        FirestoreRecyclerOptions<Feed> options;
        options=new FirestoreRecyclerOptions.Builder<Feed>()
                .setQuery(getfeeds(), Feed.class).build();
        feedadapter = new FeedAdapter(options);
        // below line is for setting linear layout manager to our recycler view.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(rootview.getContext(), RecyclerView.VERTICAL, false);
        // below line is to set layout manager to our recycler view.
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(feedadapter);
        return rootview;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private Query getfeeds(){
          return   feedViewModel.getFeedListLiveData();
    }


    @Override
    public void onStart() {
        super.onStart();
        feedadapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        feedadapter.startListening();
    }
}