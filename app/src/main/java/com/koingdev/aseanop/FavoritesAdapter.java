package com.koingdev.aseanop;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.koingdev.aseanop.Fragments.Favorites;
import com.koingdev.aseanop.Models.FavoriteModel;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by SSK on 07-Jun-17.
 */

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {
    ArrayList<FavoriteModel> data;
    static OnItemClickListener listener;
    Context context;

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public FavoritesAdapter(ArrayList<FavoriteModel> data, Context context){
        this.data = data;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View item = inflater.inflate(R.layout.favorites_row_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(item);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        FavoriteModel d = data.get(position);
        TextView t1 = holder.t1;
        TextView t2 = holder.t2;
        t1.setText(d.title);
        t2.setText(d.date);
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                alertBuilder.setTitle("តើអ្នកពិតជាចង់លុបទិន្នន័យមួយនេះ?");
                alertBuilder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP && !event.isCanceled()){
                            dialog.cancel();
                            return true;
                        }
                        return false;
                    }
                });
                alertBuilder
                        .setCancelable(false)
                        .setPositiveButton("យល់ព្រម", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                try{
                                    new DBHelper(context).deleteData(data.get(position));
                                    File file = new File(data.get(position).url.substring(7));
                                    if (file.exists()){
                                        if(file.delete()){
                                            Toast.makeText(context, "ទិន្នន័យត្រូវបានលុប...", Toast.LENGTH_SHORT).show();
                                            FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                                            fragmentManager.beginTransaction().replace(R.id.content_main, Favorites.newInstance()).commit();
                                        }
                                        else{
                                            Toast.makeText(context, "ទិន្នន័យមិនត្រូវបានលុប...", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else{
                                        Toast.makeText(context, "ទិន្នន័យមិនត្រូវបានលុប...", Toast.LENGTH_SHORT).show();
                                    }
                                }catch (Exception ex) {
                                    Toast.makeText(context, "ទិន្នន័យមិនត្រូវបានលុប...", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("ទេ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = alertBuilder.create();
                alert.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView t1, t2;
        Button del;
        ViewHolder(final View itemView) {
            super(itemView);
            t1 = (TextView) itemView.findViewById(R.id.txtFavorTitle);
            t2 = (TextView) itemView.findViewById(R.id.txtFavorDate);
            del = (Button) itemView.findViewById(R.id.delBtn);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(itemView, position);
                        }
                    }
                }
            });
        }
    }
}