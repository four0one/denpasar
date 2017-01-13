package suning;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;

/**
 * Created by chenwei on 2017/1/13.
 */
public class GoodsTypeCatcher {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String Suning_url = "http://www.suning.com/";

    public void catcher() throws IOException {
        Document doc = Jsoup.connect(Suning_url).get();
        Elements sortList = doc.select("ul.sort-list");
        if (!sortList.isEmpty()) {
            Element element = sortList.get(0);
            Elements lis = element.children();
            Iterator<Element> liIterator = lis.iterator();
            while (liIterator.hasNext()) {
                Element li = liIterator.next();
                Elements as = li.getElementsByTag("a");
                Iterator<Element> aIterator = as.iterator();
                while (aIterator.hasNext()) {
                    Element a = aIterator.next();
                    logger.info("{}|{}", a.text(), a.attr("href"));
                }
            }
        }
    }


    public static void main(String[] args) {
        GoodsTypeCatcher c = new GoodsTypeCatcher();
        try {
            c.catcher();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
