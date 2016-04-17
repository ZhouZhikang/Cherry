package cherry.action;

import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cherry.action.model.ViewTag;
import cherry.action.util.ActionBase;
import cherry.action.util.CherryClient;
import cherry.action.util.Responser;

import com.loopj.android.http.AsyncHttpResponseHandler;

public class UpdateTagsAction extends ActionBase {

	public UpdateTagsAction(String userid, List<ViewTag> taglist) {
		this.relativeUrl = "/response/usertag/update";
		JSONArray ja = new JSONArray();
		for (ViewTag tag : taglist) {
			try {
				JSONObject jo = new JSONObject();
				jo.put("tagid", tag.getTagid());
				jo.put("tagName", tag.getTagname());
				ja.put(jo);
			} catch(JSONException e) {}
		}
		this.params.put("userid", userid);
		this.params.put("taglist", ja.toString());
	}

	@Override
	public void execute(final Responser responser) {
		// TODO Auto-generated method stub
		CherryClient.post(relativeUrl, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				responser.successfulResponse(new String(arg2));
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				responser.failedResponse(arg3.getMessage());
			}
		});
	}

}
