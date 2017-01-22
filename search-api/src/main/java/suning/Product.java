package suning;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by chenwei on 2017/1/15.
 */
public class Product {

    private String id;

    private String name;

    private double price;

    private String url;

    private int totalReview;

    /**
     * "oneStarCount": 104,
     * "twoStarCount": 12,
     * "threeStarCount": 31,
     * "fourStarCount": 57,
     * "fiveStarCount": 5670,
     * "againCount": 131,
     * "bestCount": 0,
     * "picFlagCount": 1143,
     * "totalCount": 5874,
     * "qualityStar": 4.9,
     * "installCount": 0,
     * "smallVideoCount": 0
     *
     * @return
     */

    private int oneStarCount;
    private int twoStarCount;
    private int threeStarCount;
    private int fourStarCount;
    private int fiveStarCount;
    private int againCount;
    private int bestCount;
    private int picFlagCount;
    private int totalCount;
    private double qualityStar;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getOneStarCount() {
        return oneStarCount;
    }

    public void setOneStarCount(int oneStarCount) {
        this.oneStarCount = oneStarCount;
    }

    public int getTwoStarCount() {
        return twoStarCount;
    }

    public void setTwoStarCount(int twoStarCount) {
        this.twoStarCount = twoStarCount;
    }

    public int getThreeStarCount() {
        return threeStarCount;
    }

    public void setThreeStarCount(int threeStarCount) {
        this.threeStarCount = threeStarCount;
    }

    public int getFourStarCount() {
        return fourStarCount;
    }

    public void setFourStarCount(int fourStarCount) {
        this.fourStarCount = fourStarCount;
    }

    public int getFiveStarCount() {
        return fiveStarCount;
    }

    public void setFiveStarCount(int fiveStarCount) {
        this.fiveStarCount = fiveStarCount;
    }

    public int getAgainCount() {
        return againCount;
    }

    public void setAgainCount(int againCount) {
        this.againCount = againCount;
    }

    public int getBestCount() {
        return bestCount;
    }

    public void setBestCount(int bestCount) {
        this.bestCount = bestCount;
    }

    public int getPicFlagCount() {
        return picFlagCount;
    }

    public void setPicFlagCount(int picFlagCount) {
        this.picFlagCount = picFlagCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public double getQualityStar() {
        return qualityStar;
    }

    public void setQualityStar(double qualityStar) {
        this.qualityStar = qualityStar;
    }

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getTotalReview() {
        return totalReview;
    }

    public void setTotalReview(int totalReview) {
        this.totalReview = totalReview;
    }


    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
