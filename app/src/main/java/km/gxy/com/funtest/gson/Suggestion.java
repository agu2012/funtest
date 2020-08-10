package km.gxy.com.funtest.gson;

import com.google.gson.annotations.SerializedName;

/**
 * "suggestion":{
 *     "comf":{
 *         "txt:"白天天气较热，感觉不适。。。"
 *     }，
 *     "cw":{
 *         "txt:"白天天气较热，不宜洗车，推荐。。。"
 *     },
 *     "sport":{
 *         "txt:"白天天气较热，适宜室内运动。。。"
 *     }
 * }
 * @author xiayi.gu@2020/8/9
 */
public class Suggestion {
    @SerializedName("comf")
    public Comfort comfort;
    @SerializedName("cw")
    public CarWash carwash;

    public Sport sport;

    public class Comfort {
        @SerializedName("txt")
        public String info;
    }

    public class CarWash {
        @SerializedName("txt")
        public String info;
    }

    public class Sport {
        @SerializedName("txt")
        public String info;
    }
}
