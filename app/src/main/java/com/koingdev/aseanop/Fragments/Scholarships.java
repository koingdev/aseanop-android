package com.koingdev.aseanop.Fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.koingdev.aseanop.Constants;
import com.koingdev.aseanop.EndlessRecyclerViewScrollListener;
import com.koingdev.aseanop.Models.ScholarshipModel;
import com.koingdev.aseanop.ScholarshipsAdapter;
import com.koingdev.aseanop.R;
import com.koingdev.aseanop.WebViewActivity;
import com.wang.avi.AVLoadingIndicatorView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import static com.koingdev.aseanop.Constants.webViewURL;

/**
 * Created by SSK on 30-May-17.
 */

public class Scholarships extends Fragment {

    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private ScholarshipsAdapter adapter;
    private AVLoadingIndicatorView indicator;

    private ArrayList<ScholarshipModel> scholarships;
    private static final String ARG_SCHOLARSHIP_TYPE = "scholarship_type";
    public static String scholarshipType;
    private SpannableString screenTitle;
    private String scholarshipUrl;

    public static boolean hasNextPage;
    private ScholarshipsAsyncTask asyncTask;

    public static Scholarships newInstance(String scholarshipType) {
        Scholarships fragment = new Scholarships();
        Bundle args = new Bundle();
        args.putString(ARG_SCHOLARSHIP_TYPE, scholarshipType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            scholarshipType = getArguments().getString(ARG_SCHOLARSHIP_TYPE);
            switch (scholarshipType) {
                case Constants.BACHELOR:
                    scholarshipUrl = Constants.BACHELOR_URL;
                    screenTitle = new SpannableString(Constants.BACHELOR);
                    break;
                case Constants.MASTER:
                    scholarshipUrl = Constants.MASTER_URL;
                    screenTitle = new SpannableString(Constants.MASTER);
                    break;
                case Constants.PHD:
                    scholarshipUrl = Constants.PHD_URL;
                    screenTitle = new SpannableString(Constants.PHD);
                    break;
                case Constants.INTERN:
                    scholarshipUrl = Constants.INTERN_URL;
                    screenTitle = new SpannableString(Constants.INTERN);
                    break;
                case Constants.TRAINING:
                    scholarshipUrl = Constants.TRAINING_URL;
                    screenTitle = new SpannableString(Constants.TRAINING);
                    break;
                case Constants.ONLINE_COURSE:
                    scholarshipUrl = Constants.ONLINE_COURSE_URL;
                    screenTitle = new SpannableString(Constants.ONLINE_COURSE);
                    break;
                case Constants.CONFERENCE:
                    scholarshipUrl = Constants.CONFERENCE_URL;
                    screenTitle = new SpannableString(Constants.CONFERENCE);
                    break;
            }
        }
        hasNextPage = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (asyncTask != null) {
            asyncTask.cancel(true);
            Log.d("cancel-async", "Cancelling Async");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        scholarships = new ArrayList<>();
        getActivity().setTitle(screenTitle);
        View rootView = inflater.inflate(R.layout.scholarships_fragment,container, false);

        indicator = (AVLoadingIndicatorView) rootView.findViewById(R.id.avi);
        indicator.show();

        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView = (RecyclerView) rootView.findViewById(R.id.bachelor_rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                asyncTask = new ScholarshipsAsyncTask(page);
                asyncTask.execute();
            }
        });

        asyncTask = new ScholarshipsAsyncTask();
        asyncTask.execute();
        return rootView;
    }
    //AsyncTask
    public class ScholarshipsAsyncTask extends AsyncTask<ArrayList<ScholarshipModel>, Void, ArrayList<ScholarshipModel>> {
        private int page;
        public ScholarshipsAsyncTask(int page) {
            this.page = page;
        }
        public ScholarshipsAsyncTask() {
            page = 1;
        }
        @Override
        protected ArrayList<ScholarshipModel> doInBackground(ArrayList<ScholarshipModel>... params) {
            Document document = null;
            try {
                document = Jsoup.connect(scholarshipUrl + page).get();
            }
            catch (IOException ex){
                if (page > 1) {
                    hasNextPage = false;
                }
                return new ArrayList<ScholarshipModel>();
            }
            if(document != null) {
                Elements es = document.select("article");
                for (Element b : es) {
                    ScholarshipModel s = new ScholarshipModel();
                    s.title = b.select("h2.post-title.entry-title").text().replaceAll("[^a-zA-Z\\d\\s()/,]+", "");
                    s.url = b.select("a[href]").attr("href");
                    s.image = b.select("img[src]").attr("src");
                    s.date = b.select("p.post-date").text();
                    scholarships.add(s);
                }
            }
            return scholarships;
        }
        @Override
        protected void onPostExecute(final ArrayList<ScholarshipModel> scholarships) {
            if (scholarships.size() > 0) {
                if (page == 1) {
                    adapter = new ScholarshipsAdapter(scholarships, getActivity());
                    adapter.setOnItemClickListener(new ScholarshipsAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            ScholarshipModel scholarship = scholarships.get(position);
                            if (scholarship != null) {
                                webViewURL = scholarship.url;
                                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                                getActivity().startActivity(intent);
                            }
                        }
                    });
                    recyclerView.setAdapter(adapter);
                } else {
                    //refresh recyclerview with new data
                    adapter.notifyDataSetChanged();
                }
            }
            /**
             * If no internet connection on first page
             * Replace current fragment with NoInternet
             */
            if (scholarships.size() <= 0 && page == 1){

                if (getActivity() != null) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                                    .replace(R.id.content_main, NoInternet.newInstance())
                                    .commit();
                }
            }
            //refresh recyclerview after hide footer indicator
            if (!hasNextPage) {
                adapter.notifyDataSetChanged();
            }

            indicator.hide();
        }
    }
}

