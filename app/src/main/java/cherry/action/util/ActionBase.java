package cherry.action.util;

import com.loopj.android.http.RequestParams;

public abstract class ActionBase {
	
	protected String relativeUrl;
	protected RequestParams params = new RequestParams();
	
	public abstract void execute(Responser responser);
	
	public ActionBase getPage(int page) {
		this.params.add("page", String.valueOf(page - 1));
		return this;
	}
	
}
