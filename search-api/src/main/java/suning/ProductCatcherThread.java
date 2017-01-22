package suning;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bali.ApiConnector;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by chenwei on 2017/1/16.
 */
public class ProductCatcherThread implements Runnable {

    private LinkedList<String> urls;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String review_url_1 = "http://review.suning" +
            ".com/ajax/review_satisfy/general-000000000#id-0000000000-----.htm";

    //http://review.suning.com/ajax/review_satisfy/package-000000000945006504-0000000000-----satisfy
    // .htm?callback=satisfy
    private final String review_url_2 = "http://review.suning" +
            ".com/ajax/review_satisfy/package-000000000#id-0000000000-----.htm";

    //189466551
    private final String price_inf_url = "http://ds.suning.cn/ds/generalForTile/000000000#id-9100-1-0000000000-1--" +
            ".json";

    @Override
    public void run() {
        if (urls == null || urls.isEmpty()) {
            return;
        }
        Product tmpProduct = null;
        String reviewUrl, priceUrl,url;
        for (int i=0;i<urls.size();i++) {
            tmpProduct = new Product();
            url = urls.get(i);
            logger.info("{}", url);
            try {
                Connection connect = Jsoup.connect(url);
                Connection.Response response = connect.execute();
                int statusCode = response.statusCode();

                if (statusCode == 200) {
                    Document doc = connect.get();
                    Elements hi_title = doc.select("h1#itemDisplayName");
                    Element titleEle = hi_title.get(0);
                    String name = titleEle.text();
                    tmpProduct.setName(name);
                    tmpProduct.setUrl(url);
                    //获取评论数 好评 差评 中评
                    String productId = CatcherUtil.getProductId(url);
                    if (!Strings.isNullOrEmpty(productId)) {
                        tmpProduct.setId(productId);
                        priceUrl = CatcherUtil.genertUrl(price_inf_url, productId);
                        priceForProduct(tmpProduct, priceUrl);

                        reviewUrl = CatcherUtil.genertUrl(review_url_1, productId);
                        int generalRevSuccess = reviewForProductGeneral(tmpProduct, reviewUrl);
                        if (generalRevSuccess == 1) {
                            reviewUrl = CatcherUtil.genertUrl(review_url_2, productId);
                            generalRevSuccess = reviewForProductPackage(tmpProduct, reviewUrl);
                            if (generalRevSuccess == -1) {
//                                urls.add(url);
                                continue;
                            }
                        } else if (generalRevSuccess == -1) {
//                            urls.add(url);
                            continue;
                        }
                        //得到正常的产品信息
                        ApiConnector.postJson("http://192.168.54.131:9200/suning/product/" + tmpProduct.getId(),
                                tmpProduct.toString());

                    }
                }
            } catch (IOException e) {
//                logger.error("{}", e);
            }
        }
    }
/*
    public Product getProduct(String url) {
        Product tmpProduct = new Product();
        String reviewUrl, priceUrl;
        logger.debug("{}", url);
        try {
            Connection connect = Jsoup.connect(url);
            Connection.Response response = connect.execute();
            int statusCode = response.statusCode();
            if (statusCode == 200) {
                Document doc = connect.get();
                Elements hi_title = doc.select("h1#itemDisplayName");
                Element titleEle = hi_title.get(0);
                String name = titleEle.text();
                tmpProduct.setName(name);
                tmpProduct.setUrl(url);
                //获取评论数 好评 差评 中评
                String productId = CatcherUtil.getProductId(url);
                if (!Strings.isNullOrEmpty(productId)) {
                    priceUrl = CatcherUtil.genertUrl(price_inf_url, productId);
                    priceForProduct(tmpProduct, priceUrl);

                    reviewUrl = CatcherUtil.genertUrl(review_url_1, productId);
                    boolean generalRevSuccess = reviewForProduct(tmpProduct, reviewUrl);
                    if (!generalRevSuccess) {
                        reviewUrl = CatcherUtil.genertUrl(review_url_2, productId);
                        reviewForProduct(tmpProduct, reviewUrl);
                    }

                } else {
                    logger.error("product id is null!{}", url);
                }
            }
        } catch (IOException e) {
            logger.error("{}",e);
        }
        return tmpProduct;
    }*/

    private void priceForProduct(Product tmpProduct, String priceUrl) {
        String resultJson = ApiConnector.get(priceUrl, null);
        JSONObject jsonObject = JSONObject.parseObject(resultJson);
        JSONArray rsArray = jsonObject.getJSONArray("rs");
        JSONObject rsObject = (JSONObject) rsArray.get(0);
        String priceStr = rsObject.getString("price");
        tmpProduct.setPrice(Strings.isNullOrEmpty(priceStr) ? 0 : Double.parseDouble(priceStr));
    }

    /**
     * @param tmpProduct
     * @param reviewUrl
     * @return 0 成功 -1 异常
     */
    private int reviewForProductPackage(Product tmpProduct, String reviewUrl) {
        try {
            String resultJsonP = ApiConnector.get(reviewUrl, null);
            String resultJson = resultJsonP.substring(1, resultJsonP.length() - 1);
            JSONObject jsonObject = JSONObject.parseObject(resultJson);
            JSONObject reviewCounts = (JSONObject) jsonObject.getJSONArray("reviewCounts").get(0);
            tmpProduct.setTotalReview(reviewCounts.getInteger("totalCount"));
            tmpProduct.setAgainCount(reviewCounts.getInteger("againCount"));
            tmpProduct.setBestCount(reviewCounts.getInteger("bestCount"));
            tmpProduct.setFiveStarCount(reviewCounts.getInteger("fiveStarCount"));
            tmpProduct.setFourStarCount(reviewCounts.getInteger("fourStarCount"));
            tmpProduct.setThreeStarCount(reviewCounts.getInteger("threeStarCount"));
            tmpProduct.setTwoStarCount(reviewCounts.getInteger("twoStarCount"));
            tmpProduct.setOneStarCount(reviewCounts.getInteger("oneStarCount"));
            tmpProduct.setPicFlagCount(reviewCounts.getInteger("picFlagCount"));
            tmpProduct.setQualityStar(reviewCounts.getDouble("qualityStar"));
            return 0;
        } catch (Exception e) {
//            logger.error("{}", e);
            return -1;
        }
    }

    /**
     * @param tmpProduct
     * @param reviewUrl
     * @return 1 换一个地址再试试 0 成功-1 异常
     */
    private int reviewForProductGeneral(Product tmpProduct, String reviewUrl) {
        try {
            String resultJsonP = ApiConnector.get(reviewUrl, null);
            String resultJson = resultJsonP.substring(1, resultJsonP.length() - 1);
            JSONObject jsonObject = JSONObject.parseObject(resultJson);
            if (jsonObject.getInteger("returnCode") == 1) {
                JSONObject reviewCounts = (JSONObject) jsonObject.getJSONArray("reviewCounts").get(0);
                Integer totalCount = reviewCounts.getInteger("totalCount");
                if (totalCount == 0) {
                    return 1;
                }
                tmpProduct.setTotalReview(reviewCounts.getInteger("totalCount"));
                tmpProduct.setAgainCount(reviewCounts.getInteger("againCount"));
                tmpProduct.setBestCount(reviewCounts.getInteger("bestCount"));
                tmpProduct.setFiveStarCount(reviewCounts.getInteger("fiveStarCount"));
                tmpProduct.setFourStarCount(reviewCounts.getInteger("fourStarCount"));
                tmpProduct.setThreeStarCount(reviewCounts.getInteger("threeStarCount"));
                tmpProduct.setTwoStarCount(reviewCounts.getInteger("twoStarCount"));
                tmpProduct.setOneStarCount(reviewCounts.getInteger("oneStarCount"));
                tmpProduct.setPicFlagCount(reviewCounts.getInteger("picFlagCount"));
                tmpProduct.setQualityStar(reviewCounts.getDouble("qualityStar"));
                return 0;
            }
            return 1;
        } catch (Exception e) {
//            logger.error("{}", e);
            return -1;
        }
    }

    /**
     * <i>¥</i>3999.<span>00</span>
     *
     * @param text
     * @return
     */

    private double price(String text) {
        String regex = "<i>¥</i>(\\d+\\.)<span>(\\d+)</span>";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        if (matcher.matches()) {
            String ps = matcher.group(1) + matcher.group(2);
            return Double.parseDouble(ps);
        }
        return 0.0;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = Lists.newLinkedList(urls);
    }

    public static void main(String[] args) {
        /*ProductCatcherThread t = new ProductCatcherThread();
        Product product = t.getProduct("http://product.suning.com/0000000000/945006504.html");
        System.out.println(product);*/
    }
}
