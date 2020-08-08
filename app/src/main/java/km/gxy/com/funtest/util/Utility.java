package km.gxy.com.funtest.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import km.gxy.com.funtest.db.City;
import km.gxy.com.funtest.db.Country;
import km.gxy.com.funtest.db.Province;

/**
 * @author xiayi.gu@2020/8/8
 */
public class Utility {

    /**
     * 【网络】解析Response
     * <p>
     * 省
     *
     * @param response
     */
    public static boolean handleProvinceResponse(String response) {
        try {
            JSONArray allProvinces = new JSONArray(response);
            for (int i = 0; i < allProvinces.length(); i++) {
                JSONObject o = allProvinces.getJSONObject(i);
                Province province = new Province();
                province.setProvinceName(o.getString("name"));
                province.setProvinceCode(o.getInt("id"));
                province.save();//存储到数据库
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 【网络】解析Response
     * <p>
     * 市
     *
     * @param response
     */
    public static boolean handleCityResponse(String response, int provinceId) {
        try {
            JSONArray allCities = new JSONArray(response);
            for (int i = 0; i < allCities.length(); i++) {
                JSONObject o = allCities.getJSONObject(i);
                City city = new City();
                city.setCityName(o.getString("name"));
                city.setCityCode(o.getInt("id"));
                city.setProvinceId(provinceId);
                city.save();//存储到数据库
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 【网络】解析Response
     * <p>
     * 县
     *
     * @param response
     */
    public static boolean handleCountryResponse(String response, int cityId) {
        try {
            JSONArray allCountres = new JSONArray(response);
            for (int i = 0; i < allCountres.length(); i++) {
                JSONObject o = allCountres.getJSONObject(i);
                Country country = new Country();
                country.setCountyName(o.getString("name"));
                country.setWeatherId(o.getString("weather_id"));
                country.setCityId(cityId);
                country.save();//存储到数据库
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
}
