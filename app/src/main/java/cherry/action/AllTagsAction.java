package cherry.action;

import org.apache.http.Header;
import org.json.JSONException;

import com.loopj.android.http.AsyncHttpResponseHandler;

import cherry.action.util.ActionBase;
import cherry.action.util.CherryClient;
import cherry.action.util.JsonHandler;
import cherry.action.util.Responser;

public class AllTagsAction extends ActionBase {

	public AllTagsAction() {
		this.relativeUrl = "/response/gettags";
	}

	@Override
	public void execute(final Responser responser) {
		// TODO Auto-generated method stub
		CherryClient.get(relativeUrl, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				try {
					responser.successfulResponse(JsonHandler.getTagList(new String(arg2)));
				} catch (JSONException e) {
					responser.failedResponse(new String(arg2));
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				responser.failedResponse(arg3.getMessage());
			}
		});
	}
	
}
