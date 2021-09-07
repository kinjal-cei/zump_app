package com.catch32.zumpbeta.model;

/**
 * @author Ruchi Mehta
 * @version Jul 11, 2019
 */
public class Year {
    String YEAR;

    public String getYEAR() {

        return YEAR;
    }

    public void setYEAR(String YEAR) {
        this.YEAR = YEAR;
    }

    @Override
    public String toString() {
        return "Year{" +
                "YEAR='" + YEAR + '\'' +
                '}';
    }
}
