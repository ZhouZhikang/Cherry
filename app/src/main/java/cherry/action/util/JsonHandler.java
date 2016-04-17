package cherry.action.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cherry.action.model.NewsSet;
import cherry.action.model.ViewNews;
import cherry.action.model.ViewTag;
import cherry.action.model.ViewUser;

public class JsonHandler {

	public static ViewUser getUser(String json) throws JSONException {
		JSONObject jo = new JSONObject(json);
		ViewUser user = new ViewUser();
		user.setUserid(jo.getString("userid"));
		user.setUsername(jo.getString("username"));
		return user;
	}

	public static List<ViewNews> getNewsList(String json) throws JSONException {
		List<ViewNews> newslist = new ArrayList<ViewNews>();
		System.out.println(json);
		JSONArray ja = new JSONArray(json);
		for (int i = 0; i < ja.length(); i++) {
			JSONObject element = ja.getJSONObject(i);
			ViewNews news = new ViewNews();
			news.setNewsid(element.getString("newsid"));
			news.setTitle(element.getString("newsTitle"));
			news.setSummary(element.getString("newsSummary"));
			news.setImgUrl(element.getString("imgUrl"));
			news.setPageUrl(element.getString("pageUrl"));
			news.setCommentUrl(element.getString("commentUrl"));
			news.setCreateTime(element.getString("createTime"));
			news.setNewsTags(getTagList(element.getString("tagsmark")));
			news.setCommentCount(element.getString("commentcount"));
			newslist.add(news);
		}
		return newslist;
	}

	public static NewsSet getNewsSet(String json) throws JSONException {
		NewsSet set = new NewsSet();
		JSONArray ja = new JSONArray(json);
		set.setMainList(getNewsList(ja.getString(0)));
		set.setRecommandList(getNewsList(ja.getString(1)));
		return set;
	}

	public static List<ViewTag> getTagList(String json) throws JSONException {
		List<ViewTag> taglist = new ArrayList<ViewTag>();
		JSONArray ja = new JSONArray(json);
		for (int i = 0; i < ja.length(); i++) {
			JSONObject element = ja.getJSONObject(i);
			ViewTag tag = new ViewTag();
			tag.setTagid(element.getString("tagid"));
			tag.setTagname(element.getString("tagName"));
			taglist.add(tag);
		}
		return taglist;
	}

}
