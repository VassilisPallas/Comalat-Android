package org.sakaiproject.helpers;

import android.app.Activity;
import android.graphics.drawable.Drawable;
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

    private View.OnClickListener clickListener;

    private Drawable editDrawable;

    private LinearLayout booksLayout, tvShowsLayout, moviesLayout, quotesLayout;
    private TextView booksTextView, tvShowsTextView, moviesTextView, quotesTextView;
    private ImageView editPersonalInformationImageView;
    private EditText booksEditText, tvShowsEditText, moviesEditText, quotesEditText;

    public PersonalInfoHelper(Activity activity, Drawable editDrawable, View.OnClickListener clickListener) {
        this.clickListener = clickListener;
        this.editDrawable = editDrawable;

        initialize(activity);
        fillValues();
        checkVisibilities(activity);
    }

    @Override
    public void initialize(Activity activity) {
        booksLayout = (LinearLayout) activity.findViewById(R.id.books_layout);
        tvShowsLayout = (LinearLayout) activity.findViewById(R.id.tv_shows_layout);
        moviesLayout = (LinearLayout) activity.findViewById(R.id.movies_layout);
        quotesLayout = (LinearLayout) activity.findViewById(R.id.quotes_layout);

        booksTextView = (TextView) activity.findViewById(R.id.books_value);
        tvShowsTextView = (TextView) activity.findViewById(R.id.tv_shows_value);
        moviesTextView = (TextView) activity.findViewById(R.id.movies_value);
        quotesTextView = (TextView) activity.findViewById(R.id.quotes_value);

        editPersonalInformationImageView = (ImageView) activity.findViewById(R.id.edit_personal_info);
        editPersonalInformationImageView.setImageDrawable(editDrawable);

        booksEditText = (EditText) activity.findViewById(R.id.books_change);
        tvShowsEditText = (EditText) activity.findViewById(R.id.tv_shows_change);
        moviesEditText = (EditText) activity.findViewById(R.id.movies_change);
        quotesEditText = (EditText) activity.findViewById(R.id.quotes_change);

        activity.findViewById(R.id.edit_personal_button).setOnClickListener(clickListener);
    }

    @Override
    public void fillValues() {
        if (!Profile.getFavouriteBooks().equals("")) {
            booksTextView.setText(Profile.getFavouriteBooks());
        } else {
            booksLayout.setVisibility(View.GONE);
        }

        if (!Profile.getFavouriteTvShows().equals("")) {
            tvShowsTextView.setText(Profile.getFavouriteTvShows());
        } else {
            tvShowsLayout.setVisibility(View.GONE);
        }

        if (!Profile.getFavouriteMovies().equals("")) {
            moviesTextView.setText(Profile.getFavouriteMovies());
        } else {
            moviesLayout.setVisibility(View.GONE);
        }

        if (!Profile.getFavouriteQuotes().equals("")) {
            quotesTextView.setText(Profile.getFavouriteQuotes());
        } else {
            quotesLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void checkVisibilities(Activity activity) {
        if (booksLayout.getVisibility() == View.GONE && tvShowsLayout.getVisibility() == View.GONE && moviesLayout.getVisibility() == View.GONE
                && quotesLayout.getVisibility() == View.GONE) {
            activity.findViewById(R.id.personal_information_buttons).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void enableEdit() {
        booksLayout.setVisibility(View.VISIBLE);
        tvShowsLayout.setVisibility(View.VISIBLE);
        moviesLayout.setVisibility(View.VISIBLE);
        quotesLayout.setVisibility(View.VISIBLE);

        booksTextView.setVisibility(View.GONE);
        booksEditText.setVisibility(View.VISIBLE);
        booksEditText.setText(Profile.getFavouriteBooks());

        tvShowsTextView.setVisibility(View.GONE);
        tvShowsEditText.setVisibility(View.VISIBLE);
        tvShowsEditText.setText(Profile.getFavouriteTvShows());

        moviesTextView.setVisibility(View.GONE);
        moviesEditText.setVisibility(View.VISIBLE);
        moviesEditText.setText(Profile.getFavouriteMovies());

        quotesTextView.setVisibility(View.GONE);
        quotesEditText.setVisibility(View.VISIBLE);
        quotesEditText.setText(Profile.getFavouriteQuotes());
    }

    @Override
    public void saveEdit() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void cancelEdit() {
        throw new UnsupportedOperationException();
    }
}
