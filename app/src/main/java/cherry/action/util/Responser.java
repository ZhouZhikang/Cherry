package cherry.action.util;

public interface Responser {

	void successfulResponse(Object param);
	void failedResponse(String error);
	
}
