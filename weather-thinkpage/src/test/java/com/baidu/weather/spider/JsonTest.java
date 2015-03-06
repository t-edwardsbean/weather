package com.baidu.weather.spider;

import com.alibaba.fastjson.JSON;
import com.baidu.weather.model.ThinkPageMsg;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;

/**
 * Created by edwardsbean on 15-3-3.
 */
public class JsonTest {
    @Test
    public void testJsonParse() throws Exception {
        String content = FileUtils.readFileToString(new File("/home/edwardsbean/json.txt"));
        ThinkPageMsg msg = JSON.parseObject(content, ThinkPageMsg.class);
        assert msg.getStatus().equals("OK");
    }
}
