package cherry.action.model;

public class ViewTag {

	private String tagid;
	private String tagname;

	public ViewTag() {
		
	}
	
	public ViewTag(String tagid, String tagname) {
		this.tagid = tagid;
		this.tagname = tagname;
	}
	
	public String getTagid() {
		return tagid;
	}

	public void setTagid(String tagid) {
		this.tagid = tagid;
	}

	public String getTagname() {
		return tagname;
	}

	public void setTagname(String tagname) {
		this.tagname = tagname;
	}

}
