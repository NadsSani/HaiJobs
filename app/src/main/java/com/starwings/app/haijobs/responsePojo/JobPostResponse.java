package com.starwings.app.haijobs.responsePojo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class JobPostResponse {

        @SerializedName("error")
        @Expose
        private Boolean error;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("jobPostID")
        @Expose
        private Integer jobPostID;

        public Boolean getError() {
            return error;
        }

        public void setError(Boolean error) {
            this.error = error;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Integer getJobPostID() {
            return jobPostID;
        }

        public void setJobPostID(Integer jobPostID) {
            this.jobPostID = jobPostID;
        }


}
