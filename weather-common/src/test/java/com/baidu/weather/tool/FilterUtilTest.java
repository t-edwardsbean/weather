package com.baidu.weather.tool;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class FilterUtilTest {

    @Test
    public void testPutIsDate() throws Exception {
        Map<String, String> m = new HashMap<>();
        FilterUtil.putIsDate(m, "time", "15:30", "a");
    }
}