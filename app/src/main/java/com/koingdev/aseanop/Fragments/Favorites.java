package com.koingdev.aseanop.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.koingdev.aseanop.Constants;
import com.koingdev.aseanop.DBHelper;
import com.koingdev.aseanop.Models.FavoriteModel;
import com.koingdev.aseanop.FavoritesAdapter;
import com.koingdev.aseanop.R;
import com.koingdev.aseanop.WebViewActivity;
import java.util.ArrayList;

import static com.koingdev.aseanop.Constants.webViewURL;

/**
 * Created by SSK on 07-Jun-17.
 */

public class Favorites extends Fragment {
    private ArrayList<FavoriteModel> favorites;
    public static Favorites newInstance(){
        return new Favorites();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.favorites_fragment,container, false);
        SpannableString title = new SpannableString(Constants.FAVORITE);
        getActivity().setTitle(title);

        favorites = new ArrayList<>();
        DBHelper db = new DBHelper(getActivity());
        favorites = db.getAllData();

        FavoritesAdapter adapter = new FavoritesAdapter(favorites, getActivity());
        adapter.setOnItemClickListener(new FavoritesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
            FavoriteModel favorite = favorites.get(position);
                if(favorite != null) {
                    webViewURL = favorite.url;
                    Intent intent = new Intent(getActivity(), WebViewActivity.class);
                    startActivity(intent);
                }
            }
        });
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.myfavor_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        return rootView;
    }
}
