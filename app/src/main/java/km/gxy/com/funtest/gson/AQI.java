package km.gxy.com.funtest.gson;

/**
 * "aqi":{
 *     "city":{
 *         "aqi":"44",
 *         "pm25":"13"
 *     }
 * }
 * @author xiayi.gu@2020/8/9
 */
public class AQI {
    public AQICity city;
    public class AQICity{
        public String aqi;
        public String pm25;
    }
}
