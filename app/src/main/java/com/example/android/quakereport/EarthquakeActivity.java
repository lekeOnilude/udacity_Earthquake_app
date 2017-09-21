
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;



public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {



    /* * Adapter for the list of earthquakes
     */
    private earthquakeAdapter mAdapter;

    private ListView earthquakeListView;

    private TextView emptyStateListView;

    private static final String requestUrl = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";

    private static final String LOG_TAG = EarthquakeActivity.class.getName();

    private static final int LOADER_ID = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "on Create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        earthquakeListView = (ListView) findViewById(R.id.list);

        emptyStateListView = (TextView) findViewById(R.id.empty_state);

        //
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        //check if the device is connected to the internet
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if(isConnected){
            // Start the Loader to fetch the earthquake data
            getLoaderManager().initLoader(LOADER_ID, null, this).forceLoad();
        }
        else {
            emptyStateListView.setText("No Internet Connection");
            earthquakeListView.setEmptyView(emptyStateListView);
        }




        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected earthquake.
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                Earthquake currentEarthquake = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });


    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int id, Bundle args) {
        //create a new Loader with the given URL
        Loader<List<Earthquake>> earthquake = new EarthquakeLoader(this, requestUrl);
        return earthquake;
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, final List<Earthquake> data) {
        ProgressBar progressBarView = (ProgressBar)findViewById(R.id.progress_bar);
        progressBarView.setVisibility(View.GONE);
        Log.i(LOG_TAG, "on load finish");

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (data != null ) {
            // Create a new adapter that takes an empty list of earthquakes as input
            mAdapter = new earthquakeAdapter(this, data);
            // Set the adapter on the {@link ListView}
            // so the list can be populated in the user interface
            earthquakeListView.setAdapter(mAdapter);
            //mAdapter.addAll(data);
            Log.i(LOG_TAG, "data added");

        }
        else {
            emptyStateListView.setText("No earthquake data to display");
            earthquakeListView.setEmptyView(emptyStateListView);
        }
        // Find a reference to the {@link ListView} in the layout

    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        //clear the data
        Log.i(LOG_TAG, "on load reset");
        mAdapter.clear();

    }


    /**
     * This class perform the Network coonection on the back thread
     * and then return the a list of Earthquake
     */
    public static class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {

        public String StringUrls;

        public EarthquakeLoader(Context context, String urls) {
            super(context);
            StringUrls = urls;
        }

        @Override
        protected void onStartLoading() {
            forceLoad();
        }

        @Override
        public List<Earthquake> loadInBackground() {
            //Log.i(LOG_TAG, "Load in background");
            // Don't perform the request if there are no URLs, or the first URL is null.
            if ( StringUrls == null) {
                System.out.print("Load in background");
                return null;
            }

            final List<Earthquake> result = QueryUtils.fetchEarthquakeData(StringUrls);
            return result;
        }
    }
}
