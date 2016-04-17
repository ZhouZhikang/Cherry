package cherry.action;

import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;

import cherry.action.model.ViewTag;
import cherry.action.util.ActionBase;
import cherry.action.util.CherryClient;
import cherry.action.util.JsonHandler;
import cherry.action.util.Responser;

import com.loopj.android.http.AsyncHttpResponseHandler;

public class GetNewsAction extends ActionBase {

	public GetNewsAction(List<ViewTag> taglist) {
		this.relativeUrl = "/response/getnewsbytags";
		JSONArray ja = new JSONArray();
		for (ViewTag tag : taglist) {
			ja.put(tag.getTagid());
		}
		this.params.put("taglist", ja.toString());
	}

	@Override
	public void execute(final Responser responser) {
		// TODO Auto-generated method stub
		CherryClient.post(relativeUrl, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				try {
					responser.successfulResponse(JsonHandler.getNewsSet(new String(arg2)));
				} catch (JSONException e) {
					responser.failedResponse(new String(arg2));
				}
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
