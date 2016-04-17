package cherry.action;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import cherry.action.util.ActionBase;
import cherry.action.util.CherryClient;
import cherry.action.util.Responser;

public class UpdatePwdAction extends ActionBase {

	public UpdatePwdAction(String userid, String oldPwd, String newPwd) {
		this.relativeUrl = "/response/change/pwd";
		this.params.put("userid", userid);
		this.params.put("oldpwd", oldPwd);
		this.params.put("newpwd", newPwd);
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
