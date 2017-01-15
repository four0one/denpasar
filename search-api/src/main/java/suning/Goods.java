package suning;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by chenwei on 2017/1/15.
 */
public class Goods {

    private String name;

    private double price;

    private int goodNum;

    private int ordinaryNum;

    private int badNum;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getGoodNum() {
        return goodNum;
    }

    public void setGoodNum(int goodNum) {
        this.goodNum = goodNum;
    }

    public int getOrdinaryNum() {
        return ordinaryNum;
    }

    public void setOrdinaryNum(int ordinaryNum) {
        this.ordinaryNum = ordinaryNum;
    }

    public int getBadNum() {
        return badNum;
    }

    public void setBadNum(int badNum) {
        this.badNum = badNum;
    }

    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
