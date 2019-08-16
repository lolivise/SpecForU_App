package com.example.tomho.specforu.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.example.tomho.specforu.R;
import com.example.tomho.specforu.datastrucuture.SearchShop;


import java.util.ArrayList;
import java.util.List;

public class AutoCompleteSearchShopAdapter extends ArrayAdapter<SearchShop> {

    private List<SearchShop> searchShopListFull;

    // Constructor
    public AutoCompleteSearchShopAdapter(@NonNull Context context, @NonNull List<SearchShop> searchShopList) {
        super(context,0, searchShopList);
        searchShopListFull = new ArrayList<>(searchShopList);
    }

    // Set the selected Filter
    @NonNull
    @Override
    public Filter getFilter() {
        return searchShopFilter;
    }


    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_shop_item, parent, false);
        }

        TextView tv_shopName = convertView.findViewById(R.id.tv_shop_name);
        TextView tv_address = convertView.findViewById(R.id.tv_shop_address);

        SearchShop searchShop = getItem(position);

        if(searchShop != null){
            tv_shopName.setText(searchShop.getShopName());
            tv_address.setText(searchShop.getAddress());
        }

        return convertView;
    }

    private Filter searchShopFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<SearchShop> suggestion = new ArrayList<>();

            // if no text is typed
            if (constraint == null || constraint.length() == 0) {
                suggestion.addAll(searchShopListFull);
            } else {
                // if contain text
                String filterPattern = constraint.toString().toLowerCase().trim();

                // find the shop name which contains the pattern
                for (SearchShop item : searchShopListFull){
                    if (item.getShopName().toLowerCase().contains(filterPattern)){
                        suggestion.add(item);
                    }
                }
            }

            results.values = suggestion;
            results.count = suggestion.size();

            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List)(results.values));
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            // put the selected text on the text view
            return ((SearchShop) resultValue).getShopName();

        }
    };
}
