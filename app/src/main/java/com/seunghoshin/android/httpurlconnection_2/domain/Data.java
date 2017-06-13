package com.seunghoshin.android.httpurlconnection_2.domain;

/**
 * Created by SeungHoShin on 2017. 6. 14..
 */

public class Data {
    private SearchPublicToiletPOIService SearchPublicToiletPOIService;

    public SearchPublicToiletPOIService getSearchPublicToiletPOIService() {
        return SearchPublicToiletPOIService;
    }

    public void setSearchPublicToiletPOIService(SearchPublicToiletPOIService SearchPublicToiletPOIService) {
        this.SearchPublicToiletPOIService = SearchPublicToiletPOIService;
    }





    @Override
    public String toString() {
        return "ClassPojo [SearchPublicToiletPOIService = " + SearchPublicToiletPOIService + "]";
    }
}