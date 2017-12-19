package com.example.julian.movielistbeta;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Stellt den Ausgangspunkt des Programms dar.
 * Filme werden ihrem Status entsprechend in verschiedene Tabs unterteilt
 */
public class MainActivity extends AppCompatActivity {
    /**
     * Log Tag für das Debuggen
     */
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private int actualPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), MovieInfoActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onPause(){
        super.onPause();
        actualPage = mViewPager.getCurrentItem();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(actualPage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private ListView list;
        /**
         * Die {@link MovieListDataSource} mit der die Datenbankzugriffe erfolen
         */
        private MovieListDataSource dataSource;
        /**
         * {@link MyAdapter} der die View mit den jeweiligen Filmen aufbaut
         */
        private MyAdapter adapter;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            list = (ListView) rootView.findViewById(R.id.movie_list_view);

            dataSource = new MovieListDataSource(rootView.getContext());
            dataSource.open();

            Context context = rootView.getContext();
            int itemLayout = R.layout.list_image_item;
            Cursor cursor = dataSource.getAllMoviesCursor(getArguments().getInt(ARG_SECTION_NUMBER));
            String[] from = new String[]{
                    MovieListDbHelper.COLUMN_POSTER,
                    MovieListDbHelper.COLUMN_TITLE,
                    MovieListDbHelper.COLUMN_GENRE,
                    MovieListDbHelper.COLUMN_RUNTIME,

            };
            int[] to = new int[]{
                    R.id.poster,
                    R.id.movie_title,
                    R.id.movie_genre,
                    R.id.movie_runtime
            };

            adapter = new MyAdapter(context, itemLayout, cursor, from, to ,0);
            list.setAdapter(adapter);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Cursor clickCursor = (Cursor) adapter.getItem(i);
                    clickCursor.moveToPosition(i);
                    String id = clickCursor.getString(clickCursor.getColumnIndexOrThrow(MovieListDbHelper.COLUMN_ID));

                    Intent intent = new Intent(getContext(), MovieDetailsActivity.class);
                    intent.putExtra("ID",id);
                    startActivity(intent);
                }
            });

            dataSource.close();

            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Interesting";
                case 1:
                    return "Currently watching";
                case 2:
                    return "Watched";
            }
            return null;
        }
    }
}
