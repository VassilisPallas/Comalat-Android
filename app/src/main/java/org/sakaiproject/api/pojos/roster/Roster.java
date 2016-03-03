package org.sakaiproject.api.pojos.roster;

import java.util.List;
import java.util.Map;

/**
 * Created by vspallas on 02/03/16.
 */
public class Roster {
    private List<Member> members;
    private int membersTotal;
    private Map<String, Integer> roleCounts;

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    public int getMembersTotal() {
        return membersTotal;
    }

    public void setMembersTotal(int membersTotal) {
        this.membersTotal = membersTotal;
    }

    public Map<String, Integer> getRoleCounts() {
        return roleCounts;
    }

    public void setRoleCounts(Map<String, Integer> roleCounts) {
        this.roleCounts = roleCounts;
    }
}
