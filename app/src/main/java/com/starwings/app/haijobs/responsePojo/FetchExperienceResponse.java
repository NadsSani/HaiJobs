package com.starwings.app.haijobs.responsePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.starwings.app.haijobs.data.Experience;

import java.util.List;

public class FetchExperienceResponse {





        @SerializedName("experience")
        @Expose
        private List<Experience> experience = null;
        @SerializedName("error")
        @Expose
        private Boolean error;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("count")
        @Expose
        private Integer count;

        public List<Experience> getExperience() {
            return experience;
        }

        public void setExperience(List<Experience> experience) {
            this.experience = experience;
        }

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

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }


}
