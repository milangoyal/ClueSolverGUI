package application;

/**
 * Holds absolute path information for WCSP Lyft executable and 
 * top-k-solutions python script
 * @author Milan
 *
 */
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
