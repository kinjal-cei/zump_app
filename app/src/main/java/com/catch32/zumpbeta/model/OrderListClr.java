package com.catch32.zumpbeta.model;

/**
 * @author Ruchi Mehta
 * @version Jul 18, 2019
 */
public class OrderListClr {
    private Object etd;
    private String mcno;
    private String flag_color;

    public Object getEtd() {
        return etd;
    }

    public void setEtd(Object etd) {
        this.etd = etd;
    }

    public String getMcno() {
        return mcno;
    }

    public void setMcno(String mcno) {
        this.mcno = mcno;
    }

    public String getFlagColor() {
        return flag_color;
    }

    public void setFlagColor(String flagColor) {
        this.flag_color = flagColor;
    }


    @Override
    public String toString() {
        return "OrderListClr{" +
                "etd=" + etd +
                ", mcno='" + mcno + '\'' +
                ", flag_color='" + flag_color + '\'' +
                '}';
    }
}
