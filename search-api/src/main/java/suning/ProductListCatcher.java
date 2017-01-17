package suning;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenwei on 2017/1/16.
 */
public class ProductListCatcher implements Catcher {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String product_list_url = "http://list.suning.com/emall/showProductList.do?ci=#id&pg=03";
    

    @Override
    public void catcher() throws IOException {
        List<String> activeUrl = new ArrayList<>();
        for (int i = 20006; i < 20007; i++) {
            String url = CatcherUtil.genertUrl(product_list_url, String.valueOf(i));
            logger.debug(url);
            Connection connect = Jsoup.connect(url);
            try {
                Connection.Response response = connect.execute();
                int statusCode = response.statusCode();
                if (statusCode == 200) {
                    activeUrl.add(url);
                }
                if (activeUrl.size() >= 100) {
                    pageCatcher(activeUrl);
                }
            } catch (Exception e) {
                continue;
            }
        }
        pageCatcher(activeUrl);
    }

    private void pageCatcher(List<String> activeUrl) {
        if (activeUrl != null && !activeUrl.isEmpty()) {
            GlPageCatcherWorkThread glPageCatcherWorkThread = new GlPageCatcherWorkThread();
            glPageCatcherWorkThread.setActiveUrl(activeUrl);
            WorkThreadPool.addWork(glPageCatcherWorkThread);
            activeUrl.clear();
        }
    }

    public static void main(String[] args) {
        ProductListCatcher pc = new ProductListCatcher();
        try {
            pc.catcher();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
