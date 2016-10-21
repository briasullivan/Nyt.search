package com.codepath.nytsearch.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by briasullivan on 10/19/16.
 */
public class NewYorkTimesResponse {

    @SerializedName(value="response")
    private Response response;
    /**
     *
     * @return
     * The response
     */
    public Response getResponse() {
        return response;
    }

    /**
     *
     * @param response
     * The response
     */
    public void setResponse(Response response) {
        this.response = response;
    }

}
