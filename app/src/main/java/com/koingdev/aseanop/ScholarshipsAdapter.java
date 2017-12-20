package com.koingdev.aseanop;

import android.content.Context;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.koingdev.aseanop.Fragments.Scholarships;
import com.koingdev.aseanop.Models.FavoriteModel;
import com.koingdev.aseanop.Models.ScholarshipModel;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;


/**
 * Created by SSK on 30-Mar-17.
 */

public class ScholarshipsAdapter extends RecyclerView.Adapter {
    static OnItemClickListener listener;
    private ArrayList<ScholarshipModel> scholarships;
    private Context context;

    private final int VIEW_TYPE_CONTENT = 0;
    private final int VIEW_TYPE_FOOTER = 1;

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public ScholarshipsAdapter(ArrayList<ScholarshipModel> scholarships, Context context){
        this.scholarships = scholarships;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        RecyclerView.ViewHolder holder;

        if (viewType == VIEW_TYPE_CONTENT) {
            View item = inflater.inflate(R.layout.scholarships_row_layout, parent, false);
             holder = new ViewHolder(item);
        } else {
            View item = inflater.inflate(R.layout.footer_layout, parent, false);
            holder = new FooterViewHolder(item);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            ScholarshipModel data = scholarships.get(position);
            ImageView icon = ((ViewHolder) holder).image;
            TextView title = ((ViewHolder) holder).title;
            TextView date = ((ViewHolder) holder).date;
            Button btnSave = ((ViewHolder) holder).save;
            title.setText(data.title);
            String newDate = "Publish: " + data.date;
            date.setText(newDate);
            String url = data.image;
            if(url.contains("empty") || url.isEmpty()){
                icon.setImageResource(R.mipmap.not_available);
            }
            else {
                Picasso.with(context)
                        .load(url)
                        .fit()
                        .into(icon);
            }
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(InternetCheck.checkInternetConnection()) {
                        try {
                            DBHelper db = new DBHelper(context);
                            ScholarshipModel da = scholarships.get(position);
                            FavoriteModel fm = new FavoriteModel();
                            //Title is ID
                            if (db.searchData(da.title)) {
                                Toast.makeText(context, "ទិន្នន័យមានរួចហើយ!", Toast.LENGTH_LONG).show();
                            } else {
                                new Url2Html(da.title, context).execute(da.url);
                                String urlPath = "file://" + Environment.getExternalStorageDirectory().getAbsolutePath() + "/aseanop-saved-files/" + da.title + ".html";
                                fm.url = urlPath;
                                fm.id = fm.title = da.title;
                                fm.date = "Publish: " + da.date;
                                db.insertData(fm);
                                Toast.makeText(context, "ទិន្នន័យត្រូវបានរក្សាទុក...", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception ex) {
                            Toast.makeText(context, "ទិន្នន័យមិនត្រូវបានរក្សាទុក...", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(context, "សូមមេត្តាពិនិត្យមើល Internet Connection!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            //if no data on next page, hide the indicator
            //if has, show it at the bottom of recyclerview
            if (!Scholarships.hasNextPage) {
                ((FooterViewHolder) holder).indicator.hide();
            } else {
                ((FooterViewHolder) holder).indicator.show();
            }
        }
    }

    @Override
    public int getItemCount() {
        return scholarships.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position >= scholarships.size() ? VIEW_TYPE_FOOTER : VIEW_TYPE_CONTENT;
    }

    private static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView image;
        private TextView title;
        private TextView date;
        private Button save;
        public ViewHolder(final View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.imgIcon);
            title = (TextView) itemView.findViewById(R.id.txtTitle);
            date = (TextView) itemView.findViewById(R.id.txtDate);
            save = (Button) itemView.findViewById(R.id.favoriteBtn);
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

    private static class FooterViewHolder extends RecyclerView.ViewHolder {
        private AVLoadingIndicatorView indicator;

        public FooterViewHolder(View v) {
            super(v);
            indicator = (AVLoadingIndicatorView) v.findViewById(R.id.avi);
        }
    }

}
