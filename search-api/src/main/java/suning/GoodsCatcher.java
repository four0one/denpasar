package suning;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by chenwei on 2017/1/13.
 */
public class GoodsCatcher {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String good_url = "http://product.suning.com/0000000000/#id.html";

    public void catcher() throws IOException {

        for (int i = 124500000; i < 124600000; i++) {
            logger.info("{}",i);
            String url = genertUrl(good_url, String.valueOf(i));
            Connection connect = Jsoup.connect(url);
            if (connect.response().statusCode() == 0) {
                logger.info("{} success", url);
            }
//            Document doc = connect.get();
        }

    }

    private String genertUrl(String url, String id) {
        String resultUrl = url.replaceAll("#id", id);
        return resultUrl;
    }

    public static void main(String[] args) {
        GoodsCatcher c = new GoodsCatcher();
        try {
            c.catcher();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
