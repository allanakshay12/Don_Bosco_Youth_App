package com.allanakshay.donboscoyouth;

/**
 * Created by Allan Akshay on 05-06-2017.
 */

public class FinanceProduct {
    private int sl_no;
    private String name;
    private String ph_no;
    private String occupation;
    private String type;
    private String desc;

    public FinanceProduct(int sl_no1, String name1, String ph_no1, String occupation1, String type1, String desc1) {
        this.sl_no = sl_no1;
        this.name = name1;
        this.ph_no = ph_no1;
        this.occupation = occupation1;
        this.type = type1;
        this.desc=desc1;
    }

    public int getId() {
        return sl_no;
    }


    public String getName() {
        return name;
    }

    public String getPrice() {return ph_no; }

    public String getDescription() {
        return occupation;
    }

    public String getType() { return  type;}

    public String getDesc() {return desc;}
}
