package org.sakaiproject.helpers.user_info_helpers;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.sakaiproject.api.user.profile.Profile;
import org.sakaiproject.sakai.R;

/**
 * Created by vasilis on 1/13/16.
 */
public class PersonalInfoHelper implements IUserAbout {

    private LinearLayout booksLayout, tvShowsLayout, moviesLayout, quotesLayout;
    private TextView booksTextView, tvShowsTextView, moviesTextView, quotesTextView;

    /**
     * PersonalInfoHelper constructor
     *
     * @param v activity
     */
    public PersonalInfoHelper(AppCompatActivity v) {
        initialize(v);
        fillValues();
        checkVisibilities(v);
    }

    @Override
    public void initialize(AppCompatActivity v) {
        booksLayout = (LinearLayout) v.findViewById(R.id.books_layout);
        tvShowsLayout = (LinearLayout) v.findViewById(R.id.tv_shows_layout);
        moviesLayout = (LinearLayout) v.findViewById(R.id.movies_layout);
        quotesLayout = (LinearLayout) v.findViewById(R.id.quotes_layout);

        booksTextView = (TextView) v.findViewById(R.id.books_value);
        tvShowsTextView = (TextView) v.findViewById(R.id.tv_shows_value);
        moviesTextView = (TextView) v.findViewById(R.id.movies_value);
        quotesTextView = (TextView) v.findViewById(R.id.quotes_value);
    }

    @Override
    public void fillValues() {
        if (Profile.getFavouriteBooks() != null) {
            booksTextView.setText(Profile.getFavouriteBooks());
        } else {
            booksLayout.setVisibility(View.GONE);
        }

        if (Profile.getFavouriteTvShows() != null) {
            tvShowsTextView.setText(Profile.getFavouriteTvShows());
        } else {
            tvShowsLayout.setVisibility(View.GONE);
        }

        if (Profile.getFavouriteMovies() != null) {
            moviesTextView.setText(Profile.getFavouriteMovies());
        } else {
            moviesLayout.setVisibility(View.GONE);
        }

        if (Profile.getFavouriteQuotes() != null) {
            quotesTextView.setText(Profile.getFavouriteQuotes());
        } else {
            quotesLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void checkVisibilities(AppCompatActivity v) {
        if (booksLayout.getVisibility() == View.GONE && tvShowsLayout.getVisibility() == View.GONE && moviesLayout.getVisibility() == View.GONE
                && quotesLayout.getVisibility() == View.GONE) {
            v.findViewById(R.id.no_personal_information).setVisibility(View.VISIBLE);
        }
    }
}
