package km.gxy.com.funtest.gson;

import com.google.gson.annotations.SerializedName;

/**
 * {
 *     "date": "2020-07-25",
 *     "cond":{
 *         "txt_d":"阵雨"
 *     },
 *     "tmp":{
 *         "max":"34",
 *         "min":"27"
 *     }
 * }
 * @author xiayi.gu@2020/8/9
 */
public class Forecast {
    public String date;

    @SerializedName("tmp")
    public Temperature temperature;

    public More more;

    public class Temperature {
        public String max;
        public String min;
    }

    public class More {
        @SerializedName("txt_d")
        public String info;
    }
}
