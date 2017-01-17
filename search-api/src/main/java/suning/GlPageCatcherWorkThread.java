package suning;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by chenwei on 2017/1/16.
 */
public class GlPageCatcherWorkThread implements Runnable {

    private List<String> activeUrl;

    @Override
    public void run() {
        if (activeUrl == null || activeUrl.isEmpty()) {
            return;
        }
        List<String> goodsUrls = new ArrayList<>();
        for (String url : activeUrl) {
            for (int i = 0; i < 10; i++) {
                String pageUrl = page(url, i);
                if (!CatcherUtil.activeUrl(pageUrl)) {
                    break;
                }
                Connection connect = Jsoup.connect(url);
                try {
                    Connection.Response response = connect.execute();
                    if (response.statusCode() == 200) {
                        Document pageDoc = connect.get();
                        Elements pointElements = pageDoc.select("p.sell-point");
                        if (!pointElements.isEmpty()) {
                            Iterator<Element> iterator = pointElements.iterator();
                            while (iterator.hasNext()) {
                                Element nextP = iterator.next();
                                String href = nextP.getElementsByTag("a").attr("href");
                                if (!Strings.isNullOrEmpty(href)) {
                                    goodsUrls.add(href);
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    break;
                }

                if (goodsUrls.size() >= 1000) {
                    productCarcher(goodsUrls);
                }
            }
            productCarcher(goodsUrls);
        }
    }

    private void productCarcher(List<String> goodsUrls) {
        if (goodsUrls != null && !goodsUrls.isEmpty()) {
            //交给抓取商品详情的线程处理
            ProductCatcherThread productCatcherThread = new ProductCatcherThread();
            productCatcherThread.setUrls(Lists.newArrayList(goodsUrls));
            WorkThreadPool.addWork(productCatcherThread);
            //清理
            goodsUrls.clear();
        }
    }

    private String page(String url, int cp) {
        return url + "&cp=" + cp;
    }

    public List<String> getActiveUrl() {
        return activeUrl;
    }

    public void setActiveUrl(List<String> activeUrl) {
        this.activeUrl = Lists.newArrayList(activeUrl);
    }
}
