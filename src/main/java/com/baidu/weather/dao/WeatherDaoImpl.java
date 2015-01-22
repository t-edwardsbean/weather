package com.baidu.weather.dao;

import com.baidu.weather.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.Types;
import java.util.*;

/**
 * Created by linhao01_91 on 2015/1/12.
 */
@Repository("weatherDao")
public class WeatherDaoImpl implements WeatherDao {
    private static final Logger logger = LoggerFactory.getLogger(WeatherDaoImpl.class);

    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void insertWeather(Map<String, Object> weather) {
        String sql;
        int id = queryIdByCitycode("weather", weather.get("citycode").toString());
        if(id==-1){
            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
            simpleJdbcInsert.withTableName("weather");
            simpleJdbcInsert.execute(weather);
        }else{
            sql = processUpdateSql(weather,"weather",id);
            namedParameterJdbcTemplate.update(sql, weather);
        }
    }

    @Override
    public void insertAir(Map<String, Object> air) {
        String sql;
        int id = queryIdByCitycode("air",air.get("citycode").toString());
        if(id==-1){
            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
            simpleJdbcInsert.withTableName("air");
            simpleJdbcInsert.execute(air);
        }else{
            sql = processUpdateSql(air,"air",id);
            namedParameterJdbcTemplate.update(sql, air);
        }
    }

    @Override
    public void insertFuture(Future future) {
        Map<String, Object> fut = new HashMap<>();
        fut.put("citycode", future.getCityId());
        fut.put("cityname", future.getCityName());
        fut.put("week", future.getWeek());
        fut.put("date", future.getDate());
        fut.put("weather", future.getWeather());
        if(!future.getIconCodeDay().isEmpty()){
            fut.put("icon_code_day", future.getIconCodeDay());
        }
        fut.put("icon_code_night", future.getIconCodeNight());
        fut.put("temp_low", future.getTempLow());
        fut.put("temp_high", future.getTempHigh());
        if(!future.getWindDirectionDay().isEmpty()) {
            fut.put("wind_direction_day", future.getWindDirectionDay());
        }
        fut.put("wind_direction_night", future.getWindDirectionNight());
        if(!future.getWindSpeedDay().isEmpty()) {
            fut.put("wind_speed_day", future.getWindSpeedDay());
        }
        fut.put("wind_speed_night", future.getWindSpeedNight());
        if(!future.getWeatherDay().isEmpty()) {
            fut.put("weather_day", future.getWeatherDay());
        }
        fut.put("weather_night", future.getWeatherNight());
        fut.put("pubdate", future.getPubDate());
        fut.put("last_update", future.getLast_update());
        fut.put("source", future.getSource());
        String sql;
        int id = queryIdFromFutureByCitycodeAndDate(future.getCityId(),future.getDate());
        if(id==-1){
            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
            simpleJdbcInsert.withTableName("future");
            simpleJdbcInsert.execute(fut);
        }else{
            sql = processUpdateSql(fut,"future",id);
            namedParameterJdbcTemplate.update(sql, fut);
        }
    }

    private int queryIdFromFutureByCitycodeAndDate(String citycode, String date){
        String sql = "select id from future where citycode=? and date=?";
        int id = -1;
        try {
            id = jdbcTemplate.queryForObject(sql, new Object[]{citycode,date}, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            id = -1;
        } catch (DataAccessException e) {
            logger.warn("查询Future_id失败", e);
        }
        return id;
    }

    private void insertWarningRaw(Alarm alarm) {
        String sql = "delete from warning_raw where targetarea=:targetarea and warning_type_info_id=:warning_type_info_id";
        Map<String, Object> raw = new HashMap<>();
        raw.put("targetarea", alarm.getCitycode());
        raw.put("model", alarm.getModel());
        raw.put("typecode", alarm.getType_code());
        raw.put("typename", alarm.getAlarm_type());
        raw.put("grade", alarm.getLevel());
        raw.put("color", alarm.getAlarm_color());
        raw.put("warning_type_info_id", alarm.getCode());
        raw.put("msg", alarm.getContent());
        raw.put("title", alarm.getTitle());
        raw.put("pubdate", alarm.getPubdate());
        raw.put("last_update", alarm.getLast_update());
        raw.put("source", alarm.getSource());
        namedParameterJdbcTemplate.update(sql, raw);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.withTableName("warning_raw");
        simpleJdbcInsert.execute(raw);
    }

    @Override
    public void insertPublish(Alarm alarm) {
        insertWarningRaw(alarm);
        List<Alarm> citys = new ArrayList<>();
        if ("2".equals(alarm.getModel())) {
            citys.add(alarm);
        } else if ("1".equals(alarm.getModel()) || "0".equals(alarm.getModel())) {
            for (String citycode : alarm.getTargets()) {
                Alarm a = new Alarm();
                a.setCode(alarm.getCode());
                a.setContent(alarm.getContent());
                a.setTitle(alarm.getTitle());
                a.setModel(alarm.getModel());
                a.setPubdate(alarm.getPubdate());
                a.setLast_update(alarm.getLast_update());
                a.setSource(alarm.getSource());
                a.setCitycode(citycode);
                citys.add(a);
            }
        }
        Map<String, Object>[] maps = alarmToMap(citys);
        namedParameterJdbcTemplate.batchUpdate("delete from warning_publish where citycode=:citycode and warning_type_info_id=:warning_type_info_id", maps);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.withTableName("warning_publish");
        simpleJdbcInsert.executeBatch(maps);
    }

    private Map<String, Object>[] alarmToMap(List<Alarm> alarms) {
        int length = alarms.size();
        Map<String, Object>[] maps = new Map[length];
        int i = 0;
        for (Alarm alarm : alarms) {
            Map<String, Object> a = new HashMap<>();
            a.put("citycode", alarm.getCitycode());
            a.put("warning_type_info_id", alarm.getCode());
            a.put("msg", alarm.getContent());
            a.put("title", alarm.getTitle());
            a.put("model", alarm.getModel());
            a.put("pubdate", alarm.getPubdate());
            a.put("last_update", alarm.getLast_update());
            a.put("source", alarm.getSource());
            maps[i] = a;
            ++i;
        }
        return maps;
    }

    @Override
    public void insertSug(Map<String, Object> suggestion) {
        Map<String,Object> sug = new HashMap<>();
        sug.put("citycode",suggestion.get("citycode"));
        sug.put("cityname",suggestion.get("cityname"));
        sug.put("sug_type_info_id",suggestion.get("sug_type_info_id"));
        sug.put("pubdate",suggestion.get("pubdate"));
        sug.put("last_update",suggestion.get("last_update"));
        sug.put("source",suggestion.get("source"));
        int id = queryIdFromSug(suggestion.get("citycode").toString(), Integer.parseInt(suggestion.get("sug_type_info_id").toString()));
        String sql;
        if(id==-1){
            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
            simpleJdbcInsert.withTableName("sug");
            simpleJdbcInsert.execute(sug);
        }else{
            sql = processUpdateSql(sug,"sug",id);
            namedParameterJdbcTemplate.update(sql, sug);
        }
    }

    private int queryIdFromSug(String citycode, int infoId) {
        String sql = "select id from sug where citycode=? and sug_type_info_id=?";
        int key = -1;
        try {
            key = jdbcTemplate.queryForObject(sql, new Object[]{citycode, infoId}, new int[]{Types.VARCHAR, Types.INTEGER}, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            key = -1;
        } catch (DataAccessException e) {
            logger.warn("查询sug_type_info失败", e);
        }
        return key;
    }

    @Override
    public synchronized void insertSugType(SugType sugType) {
        Map<String, Object> type = new HashMap<>();
        type.put("type", sugType.getType());
        type.put("typename", sugType.getTypeName());
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.withTableName("sug_type");
        simpleJdbcInsert.execute(type);
    }

    @Override
    public synchronized int insertSugTypeInfo(SugTypeInfo sugTypeInfo) {
        Map<String, Object> info = new HashMap<>();
        info.put("type", sugTypeInfo.getType());
        info.put("info", sugTypeInfo.getInfo());
        info.put("suggestion", sugTypeInfo.getSuggestion());
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.withTableName("sug_type_info");
        simpleJdbcInsert.usingGeneratedKeyColumns("id");
        return simpleJdbcInsert.executeAndReturnKey(info).intValue();
    }

    private int queryIdByCitycode(String tableName,String citycode){
        String sql = "select id from "+tableName+" where citycode=?";
        int id = -1;
        try {
            id = jdbcTemplate.queryForObject(sql, new Object[]{citycode}, new int[]{Types.VARCHAR}, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            id = -1;
        } catch (DataAccessException e) {
            logger.warn("查询Weather_id失败", e);
        }
        return id;
    }

    private String processUpdateSql(Map<String, Object> map,String tableName,int id){
        StringBuilder sb = new StringBuilder();
        sb.append("update "+tableName+" set ");
        Set<String> keys = map.keySet();
        for(String key : keys){
            sb.append(key+"=:"+key+",");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append(" where id="+id);
        return sb.toString();
    }

    @Override
    public Map<City, String> getCityDict() {
        Map<City, String> result = new HashMap<>();
        Map<String, ?> paramMap = new HashMap<>();
        SqlRowSet set = namedParameterJdbcTemplate.queryForRowSet("select citycode,areacode,cityname from city", paramMap);
        while (set.next()) {
            City city = new City(set.getString("citycode"), set.getString("areacode"));
            result.put(city, set.getString("cityname"));
        }
        return result;
    }

    @Override
    public Map<String, List<String>> getProToAreaDict() {
        Map<String, List<String>> results = new HashMap<>();
        Map<String, ?> paramMap = new HashMap<>();
        SqlRowSet set = namedParameterJdbcTemplate.queryForRowSet("select areacode,provcode from area", paramMap);
        while (set.next()) {
            String areaCode = set.getString("areacode");
            String proCode = set.getString("provcode");
            List<String> areaCodes = results.get(proCode);
            if (areaCodes == null) {
                areaCodes = new ArrayList<>();
            }
            areaCodes.add(areaCode);
            results.put(proCode, areaCodes);
        }
        return results;
    }

    @Override
    public Map<String, String> getProvinceDict() {
        Map<String, String> result = new HashMap<>();
        Map<String, ?> paramMap = new HashMap<>();
        SqlRowSet set = namedParameterJdbcTemplate.queryForRowSet("select provname,provcode from province", paramMap);
        while (set.next()) {
            result.put(set.getString("provname"), set.getString("provcode"));
        }
        return result;
    }

    @Override
    public Map<String, String> getAreaDict() {
        Map<String, String> result = new HashMap<>();
        Map<String, ?> paramMap = new HashMap<>();
        SqlRowSet set = namedParameterJdbcTemplate.queryForRowSet("select areaname,areacode from area", paramMap);
        while (set.next()) {
            result.put(set.getString("areaname"), set.getString("areacode"));
        }
        return result;
    }

    @Override
    public Map<String, SugType> getSugTypeDict() {
        Map<String, SugType> result = new HashMap<>();
        Map<String, ?> paramMap = new HashMap<>();
        SqlRowSet set = namedParameterJdbcTemplate.queryForRowSet("select type,typename from sug_type", paramMap);
        while (set.next()) {
            String type = set.getString("type");
            String typeName = set.getString("typename");
            SugType sugType = new SugType(type, typeName);
            result.put(type, sugType);
        }
        return result;
    }

    @Override
    public Map<SugTypeInfo, SugTypeInfo> getSugTypeInfoDict() {
        Map<SugTypeInfo, SugTypeInfo> result = new HashMap<>();
        Map<String, ?> paramMap = new HashMap<>();
        SqlRowSet set = namedParameterJdbcTemplate.queryForRowSet("select id,type,suggestion,info from sug_type_info", paramMap);
        while (set.next()) {
            int id = set.getInt("id");
            String type = set.getString("type");
            String suggestion = set.getString("suggestion");
            String info = set.getString("info");
            SugTypeInfo sugTypeInfo = new SugTypeInfo(id, type, suggestion, info);
            result.put(sugTypeInfo, sugTypeInfo);
        }
        return result;
    }

    @Override
    public Map<String, List<String>> getAreaToCitycodeDict() {
        Map<String, List<String>> results = new HashMap<>();
        Map<String, ?> paramMap = new HashMap<>();
        SqlRowSet set = namedParameterJdbcTemplate.queryForRowSet("select areacode,citycode from city", paramMap);
        while (set.next()) {
            String areaCode = set.getString("areacode");
            String cityCode = set.getString("citycode");
            List<String> cityCodes = results.get(areaCode);
            if (cityCodes == null) {
                cityCodes = new ArrayList<>();
            }
            cityCodes.add(cityCode);
            results.put(areaCode, cityCodes);
        }
        return results;
    }
}
