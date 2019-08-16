package com.example.tomho.specforu.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tomho.specforu.R;
import com.example.tomho.specforu.ShowShopActivity;
import com.example.tomho.specforu.datastrucuture.Shop;

import java.util.List;


public class ShopRecyclerViewAdapter extends RecyclerView.Adapter<ShopRecyclerViewAdapter.ShopViewHolder> {

    private Context context;
    private List<Shop> shopList;
    private View view;

    public ShopRecyclerViewAdapter(Context context, List<Shop> shopList) {
        this.context = context;
        this.shopList = shopList;
    }

    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view = LayoutInflater.from(context).inflate(R.layout.cardview_item_shop,parent,false);

        return new ShopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder holder, final int position) {
        holder.tv_shopName.setText(shopList.get(position).getShopName());
        holder.img_shop.setImageResource(shopList.get(position).getPhoto());
        holder.tv_favoriteNum.setText(shopList.get(position).getFavoriteNum());
        holder.tv_specialNum.setText(shopList.get(position).getSpecialNum());

        holder.item_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShowShopActivity.class);
                intent.putExtra("Title", shopList.get(position).getShopName());
                intent.putExtra("photo",shopList.get(position).getPhoto());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return shopList.size();
    }

    public static class ShopViewHolder extends RecyclerView.ViewHolder{

        private CardView item_shop;
        private TextView tv_shopName;
        private ImageView img_shop;
        private TextView tv_favoriteNum;
        private TextView tv_specialNum;

        public ShopViewHolder(@NonNull View itemView) {
            super(itemView);

            item_shop = (CardView) itemView.findViewById(R.id.item_shop);
            tv_shopName = (TextView) itemView.findViewById(R.id.tv_shop_name);
            img_shop = (ImageView) itemView.findViewById(R.id.img_shop);
            tv_favoriteNum = (TextView) itemView.findViewById(R.id.tv_favorite_num);
            tv_specialNum = (TextView) itemView.findViewById(R.id.tv_special_num);
        }
    }
}
