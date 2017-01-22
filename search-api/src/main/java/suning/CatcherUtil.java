package suning;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by chenwei on 2017/1/16.
 */
public class CatcherUtil {

    private static Pattern pattern = Pattern.compile("http://product.suning.com/\\d+/(\\d+).*.html");

    public static String genertUrl(String url, String id) {
        String resultUrl = url.replaceAll("#id", id);
        return resultUrl;
    }

    public static String getProductId(String url) {
        Matcher matcher = pattern.matcher(url);
        boolean matches = matcher.matches();
        String id = null;
        if (matches) {
            id = matcher.group(1);
        }
        return id;
    }

    public static boolean activeUrl(String url) {
        Connection connect = Jsoup.connect(url);
        try {
            Connection.Response response = connect.execute();
            if (response.statusCode() == 200) {
                return true;
            }
        } catch (IOException e) {
            return false;
        }
        return false;
    }
}
