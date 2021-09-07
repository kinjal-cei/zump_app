package com.catch32.zumpbeta.model;

/**
 * @author Ruchi Mehta
 * @version Jun 19, 2019
 */
public class DistReportData {

    private int dist_id;
    private String dist_nm;
    private int dist_agent_id;

    public int getDist_id() {
        return dist_id;
    }

    public void setDist_id(int dist_id) {
        this.dist_id = dist_id;
    }

    public String getDist_nm() {
        return dist_nm;
    }

    public void setDist_nm(String dist_nm) {
        this.dist_nm = dist_nm;
    }

    public int getDist_agent_id() {
        return dist_agent_id;
    }

    public void setDist_agent_id(int dist_agent_id) {
        this.dist_agent_id = dist_agent_id;
    }

    @Override
    public String toString() {
        return "DistReportData{" +
                "dist_id=" + dist_id +
                ", dist_nm='" + dist_nm + '\'' +
                ", dist_agenid=" + dist_agent_id +
                '}';
    }
}
