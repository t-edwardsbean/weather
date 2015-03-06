package com.baidu.weather.spider;

import com.baidu.weather.model.Today;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by edwardsbean on 15-1-11.
 */
public class HttpPipeline implements Pipeline {
    private Logger logger = LoggerFactory.getLogger(getClass());
    PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
    CloseableHttpClient httpClient;
    private String host;


    public HttpPipeline(String host, int thread) {
        this.host = host;
        // Increase max total connection to 200
        cm.setMaxTotal(thread);
        // Increase default max connection per route to 20
        cm.setDefaultMaxPerRoute(20);
        this.httpClient = HttpClients.custom()
                .setConnectionManager(cm)
                .build();
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        if (!resultItems.getAll().isEmpty()) {
            HttpRequestBase request = buildRequest(resultItems);
            try {
                CloseableHttpResponse response = httpClient.execute(request, new BasicHttpContext());

                try {
                    if (response.getStatusLine().getStatusCode() != 200) {
                        String content = EntityUtils.toString(response.getEntity());
                        logger.warn("HttpPipleline Request Fail {}", content);
                    }
                } finally {
                    response.close();
                }
            } catch (IOException e) {
                logger.error("HttpPipleline Request Error {}", e);
            }
        }
    }

    public HttpRequestBase buildRequest(ResultItems resultItems) {
        List<NameValuePair> formParams = new ArrayList<>();
        String path = null;
        for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
            //当天天气+日出日落
            path = entry.getKey();
            if ("/weather".equals(entry.getKey())) {
                Today today = (Today) entry.getValue();
//                formParams.add(new BasicNameValuePair("aqi", today.getAqi()));
//                formParams.add(new BasicNameValuePair("city", today.getCity()));
//                formParams.add(new BasicNameValuePair("cityId", today.getCityId()));
//                formParams.add(new BasicNameValuePair("sd", today.getSd()));
//                formParams.add(new BasicNameValuePair("sunrise", today.getSunrise()));
//                formParams.add(new BasicNameValuePair("sunset", today.getSunset()));
//                formParams.add(new BasicNameValuePair("temp", today.getTemp()));
//                formParams.add(new BasicNameValuePair("last_update", today.getLast_update()));
//                formParams.add(new BasicNameValuePair("wd", today.getWd()));
//                formParams.add(new BasicNameValuePair("weather", today.getWeather()));
//                formParams.add(new BasicNameValuePair("ws", today.getWs()));
//                formParams.add(new BasicNameValuePair("source", today.getSource()));

            } else {
                Map<String, String> flags = (Map<String, String>) entry.getValue();
                for (Map.Entry<String, String> flag : flags.entrySet()) {
                    formParams.add(new BasicNameValuePair(flag.getKey(), flag.getValue()));
                }
            }
        }
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParams, Consts.UTF_8);
        HttpPost httpPost = new HttpPost(host + path);
        httpPost.setEntity(entity);
        return httpPost;
    }

}
