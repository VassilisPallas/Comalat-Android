package org.sakaiproject.api.pojos.membership;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by vspallas on 15/02/16.
 */
public class Membership {
    @SerializedName("membership_collection")
    private List<MembershipCollection> membershipCollectionList;

    public List<MembershipCollection> getMembershipCollectionList() {
        return membershipCollectionList;
    }

    public void setMembershipCollectionList(List<MembershipCollection> membershipCollectionList) {
        this.membershipCollectionList = membershipCollectionList;
    }

    public class MembershipCollection {
        private String id;
        private long lastLoginTime;
        private String memberRole;
        private boolean active, provided;
        private String siteType;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public long getLastLoginTime() {
            return lastLoginTime;
        }

        public void setLastLoginTime(long lastLoginTime) {
            this.lastLoginTime = lastLoginTime;
        }

        public String getMemberRole() {
            return memberRole;
        }

        public void setMemberRole(String memberRole) {
            this.memberRole = memberRole;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public boolean isProvided() {
            return provided;
        }

        public void setProvided(boolean provided) {
            this.provided = provided;
        }

        public String getSiteType() {
            return siteType;
        }

        public void setSiteType(String siteType) {
            this.siteType = siteType;
        }
    }
}
