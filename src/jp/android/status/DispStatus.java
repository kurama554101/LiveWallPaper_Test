package jp.android.status;

public class DispStatus{
	int width,height;
	
	/* ��ʃT�C�Y�̏����l��0 */
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
