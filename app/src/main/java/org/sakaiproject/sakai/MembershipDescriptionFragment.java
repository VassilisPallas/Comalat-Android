package org.sakaiproject.sakai;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.sakaiproject.api.memberships.SiteData;
import org.sakaiproject.customviews.rich_textview.RichTextView;
import org.sakaiproject.general.Actions;

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

        RichTextView shortDescr = (RichTextView) v.findViewById(R.id.membership_short_description);
        shortDescr.setContext(getContext());

        shortDescr.setSiteData(data.getId());

        if (shortDescription != null) {
            shortDescription = Actions.deleteHtmlTags(shortDescription);
            shortDescr.setText(shortDescription);
        } else {
            shortDescr.setText(getContext().getResources().getString(R.string.no_short_descr));
        }

        RichTextView descr = (RichTextView) v.findViewById(R.id.membership_description);
        descr.setContext(getContext());
        descr.setSiteData(data.getId());

        if (description != null) {
            description = Actions.deleteHtmlTags(description);
            descr.setText(description);
        } else {
            descr.setText(getContext().getResources().getString(R.string.no_descr));
        }

        return v;
    }

}
