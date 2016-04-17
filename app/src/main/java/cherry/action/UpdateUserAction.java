package cherry.action;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import cherry.action.util.ActionBase;
import cherry.action.util.CherryClient;
import cherry.action.util.Responser;

public class UpdateUserAction extends ActionBase {

	public UpdateUserAction(String userid, String username) {
		this.relativeUrl = "/response/change/username";
		this.params.put("userid", userid);
		this.params.put("username", username);
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
