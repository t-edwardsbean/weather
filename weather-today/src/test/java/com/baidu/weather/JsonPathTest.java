package com.baidu.weather;

import org.junit.Test;
import us.codecraft.webmagic.selector.JsonPathSelector;

/**
 * Created by edwardsbean on 15-3-6.
 */
public class JsonPathTest {
    @Test
    public void testSelect() throws Exception {
        String data = new JsonPathSelector("$.time").select("{\"nameen\":\"beijing\",\"cityname\":\"北京\",\"city\":\"101010100\",\"temp\":\"-5\",\"tempf\":\"23\",\"WD\":\"西南风\",\"wde\":\"SW \",\"WS\":\"1级\",\"wse\":\"<12km/h\",\"SD\":\"38%\",\"time\":\"05:30\",\"weather\":\"暂无实况\",\"weathere\":\"no live\",\"weathercode\":\"暂无实况\",\"weathercodee\":\"no live\",\"qy\":\"1023\",\"njd\":\"暂无实况\",\"sd\":\"38%\",\"aqi\":\"\",\"}");
        System.out.println(data);

    }
}
