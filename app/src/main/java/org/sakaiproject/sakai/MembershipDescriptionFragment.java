package org.sakaiproject.sakai;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.sakaiproject.api.site.SiteData;
import org.sakaiproject.general.Actions;
import org.sakaiproject.general.Image;

import java.io.File;
import java.util.regex.Matcher;

/**
 * Created by vasilis on 1/26/16.
 */
public class MembershipDescriptionFragment extends DialogFragment {

    private String ownerShortName;
    private String email;
    private String shortDescription;
    private String description;
    private SiteData data;

    public MembershipDescriptionFragment() {
    }

    /**
     * get the owner data
     *
     * @param ownerShortName   the owner full name
     * @param email            the owner email
     * @param shortDescription the short description from the site
     * @param description      the description from the site
     * @return the fragment with the data
     */
    public MembershipDescriptionFragment getData(String ownerShortName, String email, String shortDescription, String description, SiteData data) {
        MembershipDescriptionFragment membershipDescriptionFragment = new MembershipDescriptionFragment();
        Bundle b = new Bundle();
        b.putString("ownerShortName", ownerShortName);
        b.putString("email", email);
        b.putString("shortDescription", shortDescription);
        b.putString("description", description);
        b.putSerializable("data", data);
        membershipDescriptionFragment.setArguments(b);
        return membershipDescriptionFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_membership_description, container, false);

        ownerShortName = getArguments().getString("ownerShortName");
        email = getArguments().getString("email");
        shortDescription = getArguments().getString("shortDescription");
        description = getArguments().getString("description");
        data = (SiteData) getArguments().getSerializable("data");

        TextView ownerName = (TextView) v.findViewById(R.id.owner_data);
        ownerName.setText(ownerShortName);
        if (email != null && !email.equals("")) {
            ownerName.append(" (");
            ownerName.append(email);
            ownerName.append(")");
        }

        org.sakaiproject.customviews.TextViewWithImages shortDescr = (org.sakaiproject.customviews.TextViewWithImages) v.findViewById(R.id.membership_short_description);

        shortDescr.setSiteData(data.getId());

        shortDescription = Actions.deleteHtmlTags(shortDescription);

        if (shortDescription != null && !shortDescription.equals("")) {
            shortDescr.setText(shortDescription);
        } else {
            shortDescr.setText(getContext().getResources().getString(R.string.no_short_descr));
        }

        org.sakaiproject.customviews.TextViewWithImages descr = (org.sakaiproject.customviews.TextViewWithImages) v.findViewById(R.id.membership_description);
        descr.setSiteData(data.getId());
        description = Actions.deleteHtmlTags(description);

        if (description != null && !description.equals("")) {
            descr.setText(description);
        } else {
            descr.setText(getContext().getResources().getString(R.string.no_descr));
        }

        return v;
    }
}
