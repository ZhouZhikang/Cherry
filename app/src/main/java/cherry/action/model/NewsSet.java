package cherry.action.model;

import java.util.List;

public class NewsSet {

	private List<ViewNews> mainList;
	private List<ViewNews> recommandList;

	public List<ViewNews> getMainList() {
		return mainList;
	}

	public void setMainList(List<ViewNews> mainList) {
		this.mainList = mainList;
	}

	public List<ViewNews> getRecommandList() {
		return recommandList;
	}

	public void setRecommandList(List<ViewNews> recommandList) {
		this.recommandList = recommandList;
	}

}
