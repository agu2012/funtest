package km.gxy.com.funtest.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * @author xiayi.gu@2020/8/8
 */
public class HttpUtil {
    public static void sendOkHttpRequest(String url, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(callback);
    }
}
