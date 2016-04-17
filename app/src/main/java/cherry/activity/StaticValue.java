package cherry.activity;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cherry.action.model.ViewTag;
import cherry.action.model.ViewUser;

/**
 * Created by zhikangzhou on 15/7/13.
 */

public class StaticValue {
    public static Map<String,ViewTag> sTagGroup=new HashMap<String,ViewTag>();
    public static Map<String,ViewTag> sGetAllTag=new HashMap<String,ViewTag>();
    public static Map<String, ViewTag> updateMap = new HashMap<String, ViewTag>();
    public static boolean isAll=false;
    public static ViewUser sCurrentUser;
    public static String sFromWhere;
    public static List<ViewTag> mList;
    public static SharedPreferences sharedPreferences=null;
}
