package cherry.action;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpResponseHandler;

import cherry.action.util.ActionBase;
import cherry.action.util.CherryClient;
import cherry.action.util.Responser;

public class AddCollectionAction extends ActionBase {

	public AddCollectionAction(String userid, String newsid) {
		this.relativeUrl = "/response/collection/add";
		this.params.put("userid", userid);
		this.params.put("newsid", newsid);
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
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				responser.failedResponse(arg3.getMessage());
			}
		});
	}

}
