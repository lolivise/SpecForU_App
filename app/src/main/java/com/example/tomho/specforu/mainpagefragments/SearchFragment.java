package com.example.tomho.specforu.mainpagefragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;

import com.example.tomho.specforu.R;
import com.example.tomho.specforu.adapters.AutoCompleteSearchShopAdapter;
import com.example.tomho.specforu.adapters.ShopRecyclerViewAdapter;
import com.example.tomho.specforu.datastrucuture.SearchShop;
import com.example.tomho.specforu.datastrucuture.Shop;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    // For Search Item
    private List<SearchShop> searchShopList;

    // For Showing RecyclerView
    private View v;
    private RecyclerView recyclerView;
    private List<Shop> shopList;


    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchShopList = new ArrayList<>();
        shopList = new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fillSearchShopList();
        setupShop();

        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_search, container, false);

        // AutoCompleteTextView
        AutoCompleteTextView home_actv = v.findViewById(R.id.home_actv);
        AutoCompleteSearchShopAdapter autoCompleteSearchShopAdapter = new AutoCompleteSearchShopAdapter(getContext(), searchShopList);
        home_actv.setAdapter(autoCompleteSearchShopAdapter);

        // show RecycleView
        recyclerView = (RecyclerView) v.findViewById(R.id.shop_recyclerview);
        ShopRecyclerViewAdapter shopRecyclerViewAdapter = new ShopRecyclerViewAdapter(getContext(),shopList);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        recyclerView.setAdapter(shopRecyclerViewAdapter);

        return v;
    }

    private void fillSearchShopList(){
        searchShopList.add(new SearchShop("little cross","12 street, suburb, WA","11wokfoijwfbhdyhsedolemffk"));
        searchShopList.add(new SearchShop("McDonal","232 road, Swan View, WA","djkonoanjgbdkpkoifuielkfmk"));
    }

    // Method for setting up shop list
    private void setupShop(){
        shopList.add(new Shop("Rebaca",R.drawable.photo1,"100", "5"));
        shopList.add(new Shop("Julie",R.drawable.photo2,"10", "4"));
        shopList.add(new Shop("Joe",R.drawable.photo3,"50", "1"));
    }

}
