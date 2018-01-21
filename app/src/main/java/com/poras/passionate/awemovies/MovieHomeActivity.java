package com.poras.passionate.awemovies;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.poras.passionate.awemovies.utils.MovieUtil;
import com.poras.passionate.awemovies.utils.NetworkUtil;

public class MovieHomeActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_home);
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.pager);
        if (viewPager != null) {
            PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
            viewPager.setAdapter(adapter);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        }

        tabLayout = findViewById(R.id.tabMovies);
        tabLayout.setupWithViewPager(viewPager);


        if (!NetworkUtil.isNetworkAvailable(this)) {
            viewPager.setCurrentItem(3);
        }
    }

    private static class PagerAdapter extends FragmentStatePagerAdapter {

        private PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return MovieFragment.newInstance(MovieUtil.POPULAR_KEY);
                case 1:
                    return MovieFragment.newInstance(MovieUtil.TOP_RATED_KEY);
                default:
                    return MovieFragment.newInstance(MovieUtil.FAVORITE_KEY);
            }
        }


        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Popular";
                case 1:
                    return "Top Rated";
                default:
                    return "Favorite";
            }
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

    }
}
