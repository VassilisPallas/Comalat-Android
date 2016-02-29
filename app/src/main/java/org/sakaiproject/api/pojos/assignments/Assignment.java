package org.sakaiproject.api.pojos.assignments;

import com.google.gson.annotations.SerializedName;

import org.sakaiproject.api.pojos.Attachment;
import org.sakaiproject.api.pojos.Time;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vspallas on 22/02/16.
 */
public class Assignment implements Serializable {

    @SerializedName("assignment_collection")
    private List<AssignmentsCollection> assignmentsCollectionList = new ArrayList<>();

    public List<AssignmentsCollection> getAssignmentsCollectionList() {
        return assignmentsCollectionList;
    }

    public void setAssignmentsCollectionList(List<AssignmentsCollection> assignmentsCollectionList) {
        this.assignmentsCollectionList = assignmentsCollectionList;
    }

    public class AssignmentsCollection implements Serializable {
        private Object access;
        private String allPurposeItemText;
        private List<Attachment> attachments;
        private String authorLastModified;
        private List<String> authors;
        private String closeTimeString; /* Accept Until */
        private Object content;
        private String contentReference;
        private String context; /* siteId */
        private String creator;
        private String dropDeadTimeString;
        private String dueTimeString; /* Due Date */
        private String gradeScale;
        private String gradeScaleMaxPoints;
        private long gradebookItemId;
        private String gradebookItemName;
        private List<?> groups;
        private String id;
        private String instructions;
        private String modelAnswerText;
        private String openTimeString; /* Open Date */
        @SerializedName("position_order")
        private String positionOrder;
        private String privateNoteText;
        private String section; /* unknown */
        private String status;
        private String submissionType;
        private Time timeCreated;
        private Time timeLastModified;
        private String title;
        private boolean allowResubmission;
        private boolean draft;

        public Object getAccess() {
            return access;
        }

        public void setAccess(Object access) {
            this.access = access;
        }

        public String getAllPurposeItemText() {
            return allPurposeItemText;
        }

        public void setAllPurposeItemText(String allPurposeItemText) {
            this.allPurposeItemText = allPurposeItemText;
        }

        public List<Attachment> getAttachments() {
            return attachments;
        }

        public void setAttachments(List<Attachment> attachments) {
            this.attachments = attachments;
        }

        public String getAuthorLastModified() {
            return authorLastModified;
        }

        public void setAuthorLastModified(String authorLastModified) {
            this.authorLastModified = authorLastModified;
        }

        public List<String> getAuthors() {
            return authors;
        }

        public void setAuthors(List<String> authors) {
            this.authors = authors;
        }

        public String getCloseTimeString() {
            return closeTimeString;
        }

        public void setCloseTimeString(String closeTimeString) {
            this.closeTimeString = closeTimeString;
        }

        public Object getContent() {
            return content;
        }

        public void setContent(Object content) {
            this.content = content;
        }

        public String getContentReference() {
            return contentReference;
        }

        public void setContentReference(String contentReference) {
            this.contentReference = contentReference;
        }

        public String getContext() {
            return context;
        }

        public void setContext(String context) {
            this.context = context;
        }

        public String getCreator() {
            return creator;
        }

        public void setCreator(String creator) {
            this.creator = creator;
        }

        public String getDropDeadTimeString() {
            return dropDeadTimeString;
        }

        public void setDropDeadTimeString(String dropDeadTimeString) {
            this.dropDeadTimeString = dropDeadTimeString;
        }

        public String getDueTimeString() {
            return dueTimeString;
        }

        public void setDueTimeString(String dueTimeString) {
            this.dueTimeString = dueTimeString;
        }

        public String getGradeScale() {
            return gradeScale;
        }

        public void setGradeScale(String gradeScale) {
            this.gradeScale = gradeScale;
        }

        public String getGradeScaleMaxPoints() {
            return gradeScaleMaxPoints;
        }

        public void setGradeScaleMaxPoints(String gradeScaleMaxPoints) {
            this.gradeScaleMaxPoints = gradeScaleMaxPoints;
        }

        public long getGradebookItemId() {
            return gradebookItemId;
        }

        public void setGradebookItemId(long gradebookItemId) {
            this.gradebookItemId = gradebookItemId;
        }

        public String getGradebookItemName() {
            return gradebookItemName;
        }

        public void setGradebookItemName(String gradebookItemName) {
            this.gradebookItemName = gradebookItemName;
        }

        public List<?> getGroups() {
            return groups;
        }

        public void setGroups(List<?> groups) {
            this.groups = groups;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getInstructions() {
            return instructions;
        }

        public void setInstructions(String instructions) {
            this.instructions = instructions;
        }

        public String getModelAnswerText() {
            return modelAnswerText;
        }

        public void setModelAnswerText(String modelAnswerText) {
            this.modelAnswerText = modelAnswerText;
        }

        public String getOpenTimeString() {
            return openTimeString;
        }

        public void setOpenTimeString(String openTimeString) {
            this.openTimeString = openTimeString;
        }

        public String getPositionOrder() {
            return positionOrder;
        }

        public void setPositionOrder(String positionOrder) {
            this.positionOrder = positionOrder;
        }

        public String getPrivateNoteText() {
            return privateNoteText;
        }

        public void setPrivateNoteText(String privateNoteText) {
            this.privateNoteText = privateNoteText;
        }

        public String getSection() {
            return section;
        }

        public void setSection(String section) {
            this.section = section;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getSubmissionType() {
            return submissionType;
        }

        public void setSubmissionType(String submissionType) {
            this.submissionType = submissionType;
        }

        public Time getTimeCreated() {
            return timeCreated;
        }

        public void setTimeCreated(Time timeCreated) {
            this.timeCreated = timeCreated;
        }

        public Time getTimeLastModified() {
            return timeLastModified;
        }

        public void setTimeLastModified(Time timeLastModified) {
            this.timeLastModified = timeLastModified;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isAllowResubmission() {
            return allowResubmission;
        }

        public void setAllowResubmission(boolean allowResubmission) {
            this.allowResubmission = allowResubmission;
        }

        public boolean isDraft() {
            return draft;
        }

        public void setDraft(boolean draft) {
            this.draft = draft;
        }
    }
}
