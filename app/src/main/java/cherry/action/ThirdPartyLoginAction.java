package cherry.action;

import org.json.JSONException;
import org.apache.http.Header;

import cherry.action.util.ActionBase;
import cherry.action.util.CherryClient;
import cherry.action.util.JsonHandler;
import cherry.action.util.Responser;

import com.loopj.android.http.AsyncHttpResponseHandler;

public class ThirdPartyLoginAction extends ActionBase {
	
	public ThirdPartyLoginAction(String token, String username, String source) {
		StringBuffer sb_token = new StringBuffer(source).append("#").append(token);
		this.relativeUrl = "/response/thirdpartylogin";
		this.params.put("token", sb_token.toString());
		this.params.put("name", username);
		this.params.put("source", source);
	}

	@Override
	public void execute(final Responser responser) {
		// TODO Auto-generated method stub
		CherryClient.post(relativeUrl, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				try {
					responser.successfulResponse(JsonHandler.getUser(new String(arg2)));
				} catch(JSONException e) {
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
