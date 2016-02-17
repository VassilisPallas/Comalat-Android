package org.sakaiproject.api.pojos.membership;

import java.util.List;

/**
 * Created by vspallas on 16/02/16.
 */
public class PagePermissions {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        private List<String> access;
        private List<String> maintain;

        public List<String> getAccess() {
            return access;
        }

        public void setAccess(List<String> access) {
            this.access = access;
        }

        public List<String> getMaintain() {
            return maintain;
        }

        public void setMaintain(List<String> maintain) {
            this.maintain = maintain;
        }
    }
}
