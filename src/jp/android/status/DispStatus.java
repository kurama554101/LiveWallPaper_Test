package jp.android.status;

public class DispStatus{
	int width,height;
	
	/* 画面サイズの初期値は0 */
	public DispStatus() {
		width=0;
		height=0;
	}
	
	public void SetDispWidth(int w) {
		width = w;
	}
	
	public void SetDispHeight(int h) {
		height = h;
	}
	
	public int GetDispWidth() {
		return width;
	}
	
	public int GetDispHeight() {
		return height;
	}
}
