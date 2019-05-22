package application;

public class PathInfo {
	//Path to wcsp executable
	public String wcsp;
	
	//Path to top-k-solutions main.py
	public String topK;
	
	public PathInfo(String wcsp, String topK) {
		this.wcsp = wcsp;
		this.topK = topK;
	}
}
