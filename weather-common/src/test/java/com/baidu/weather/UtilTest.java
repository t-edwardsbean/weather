package com.baidu.weather;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by edwardsbean on 15-1-16.
 */
public class UtilTest {

    @Test
    public void testExtraProvince() throws Exception {
        List<String> pros = Arrays.asList("西藏自治区", "新疆维吾尔自治区", "宁夏回族自治区", "内蒙古自治区", "广西壮族自治区");
        Pattern p = Pattern.compile("(新疆|广西|西藏|宁夏|内蒙古)");
        for (String pro : pros) {
            Matcher m = p.matcher(pro);
            if (m.find()) {
                System.out.println(m.group());
            }
        }

    }

    @Test
    public void testExtraArea() throws Exception {
        List<String> pros = Arrays.asList("福州市", "卡吃了盟", "汀州", "铜仁地区");
        Pattern areaPattern = Pattern.compile("(.*)(市|盟|州|地区)");
        for (String pro : pros) {
            Matcher m = areaPattern.matcher(pro);
            if (m.find()) {
                System.out.println(m.group(1));
            }
        }
    }

    @Test
    public void testFilterArea() throws Exception {
        List<String> pros = Arrays.asList("通州区", "郴州市", "秦皇岛市", "扬州市", "亳州市", "忠县");
        Pattern areaPattern = Pattern.compile("(.*)(市|盟|州|地区)$");
        for (String pro : pros) {
            Matcher m = areaPattern.matcher(pro);
            if (m.find()) {
                System.out.println(m.group());
            }
        }
    }

    @Test
    public void testLogger() throws Exception {
        Logger log1 = LoggerFactory.getLogger("moduleA");
        Logger log2 = LoggerFactory.getLogger("moduleB");
        log1.info("haha1");
        log2.info("haha2");
    }

    @Test
    public void testParseInteger() throws Exception {
        String num = "01";
        System.out.println(Integer.parseInt(num));
    }

    @Test
    public void testParseTime() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        format.parse("7:33");
    }
}
