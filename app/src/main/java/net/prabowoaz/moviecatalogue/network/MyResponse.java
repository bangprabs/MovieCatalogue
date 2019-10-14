package net.prabowoaz.moviecatalogue.network;

import com.google.gson.annotations.SerializedName;

public class MyResponse<A> {
    @SerializedName("results")
    A results;
    @SerializedName("success")
    boolean success;

    public A getResults() {
        return results;
    }

    public void setResults(A results) {
        this.results = results;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
