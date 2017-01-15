package suning;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by chenwei on 2017/1/13.
 */
public class GoodsCatcher {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String good_url = "http://product.suning.com/0000000000/#id.html";

    private final String good_review_url = "http://review.suning.com/cmmdty_review/general-000000000#id-0000000000-1-total.htm";

    private ExecutorService workThreadPool = Executors.newFixedThreadPool(3);

    public void catcher() throws IOException {

        for (int i = 100000000; i < 200000000; i = i + 100000000) {
            WorkThread workThread = new WorkThread();
            workThread.setB(i);
            workThread.setS(i + 100000000);
            workThreadPool.submit(workThread);
        }
    }

    private String genertUrl(String url, String id) {
        String resultUrl = url.replaceAll("#id", id);
        return resultUrl;
    }


    /**
     * <i>¥</i>3999.<span>00</span>
     * @param text
     * @return
     */
    private double price(String text){
        String regex = "<i>¥</i>(\\d+\\.)<span>(\\d+)</span>";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        if(matcher.matches()){
            String ps = matcher.group(1) + matcher.group(2);
            return Double.parseDouble(ps);
        }
        return 0.0;
    }

    class WorkThread implements Runnable {

        private int b, s;

        public int getB() {
            return b;
        }

        public void setB(int b) {
            this.b = b;
        }

        public int getS() {
            return s;
        }

        public void setS(int s) {
            this.s = s;
        }

        @Override
        public void run() {
            Goods tmpGood = null;
            for (int i = b; i < s; i++) {
                try {
                    String url = genertUrl(good_url, String.valueOf(i));
                    Connection connect = Jsoup.connect(url);
                    Connection.Response response = connect.execute();
                    String statusMessage = response.statusMessage();
                    int statusCode = response.statusCode();
//                    logger.debug("url:{},code:{}", url, statusCode);
                    if (statusCode == 200) {
                        Document doc = connect.get();
                        Elements hi_title = doc.select("h1#itemDisplayName");
                        Element titleEle = hi_title.get(0);
                        String name = titleEle.text();
//                        logger.debug("{}", url);

                        Element pricepromo = doc.select("dl.price-promo").get(0);
                        Elements mainprice = pricepromo.select("span.mainprice");
                        logger.debug("{}",mainprice.text());
                        if (mainprice.size() > 0) {
                            tmpGood = new Goods();
                            tmpGood.setName(name);
                            tmpGood.setPrice(price(mainprice.text()));
                        }
                        logger.debug("{}",tmpGood);
                    }
                } catch (Exception e) {
//                    logger.error("{}", e);
                    continue;
                }

            }

        }
    }

    public static void main(String[] args) {
        GoodsCatcher c = new GoodsCatcher();
        try {
            c.catcher();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        double price = c.price("<i>¥</i>3999.<span>00</span>");
//        System.out.println(price);
    }
}
