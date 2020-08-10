package km.gxy.com.funtest.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * "daily_forecast":[{
 *  *     "date": "2020-07-25",
 *  *     "cond":{
 *  *         "txt_d":"阵雨"
 *  *     },
 *  *     "tmp":{
 *  *         "max":"34",
 *  *         "min":"27"
 *  *     }
 *  * },
 *  * {
 *  *  *     "date": "2020-07-26",
 *  *  *     "cond":{
 *  *  *         "txt_d":"多云"
 *  *  *     },
 *  *  *     "tmp":{
 *  *  *         "max":"34",
 *  *  *         "min":"27"
 *  *  *     }
 *  *  * }]
 * @author xiayi.gu@2020/8/9
 */
public class Weather {
    public String status;
    public Basic basic;
    public AQI aqi;
    public Now now;
    public Suggestion suggestion;

    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;
}
