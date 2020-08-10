package km.gxy.com.funtest.gson;

import com.google.gson.annotations.SerializedName;

/**
 * "now":{
 *     "tmp":"29",
 *     "cond":{
 *        "txt":"阵雨"
 *     }
 * }
 * @author xiayi.gu@2020/8/9
 */
public class Now {
    @SerializedName("tmp")
    public String temperature;
    @SerializedName("cond")
    public More more;

    public class More {
        @SerializedName("txt")
        public String info;
    }
}