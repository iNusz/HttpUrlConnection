package com.seunghoshin.android.httpurlconnection_2.domain;

/**
 * Created by SeungHoShin on 2017. 6. 14..
 */

public class RESULT {
    private String MESSAGE;

    private String CODE;

    public String getMESSAGE() {
        return MESSAGE;
    }

    public void setMESSAGE(String MESSAGE) {
        this.MESSAGE = MESSAGE;
    }

    public String getCODE() {
        return CODE;
    }

    public void setCODE(String CODE) {
        this.CODE = CODE;
    }

    @Override
    public String toString() {
        return "ClassPojo [MESSAGE = " + MESSAGE + ", CODE = " + CODE + "]";
    }
}
