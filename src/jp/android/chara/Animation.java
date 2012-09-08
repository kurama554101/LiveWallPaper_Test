package jp.android.chara;

import java.util.ArrayList;
import java.util.Arrays;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * アニメーションを作成するクラス
 * 各アニメーション画像の大きさは一律同じとする必要がある
 * @author sakamotoshouta
 *
 */
public class Animation {
	private ArrayList<Bitmap> imageList = null;//animationを作成するための画像リスト
	private Rect imageRect = null;//アニメーション画像の大きさを格納する矩形クラス
	private int intervalTime = 0;//アニメーション間隔
	private int imageIndex = 0;//現在の描画イメージ
	private boolean IsStartAnime;//アニメーションしているかどうかを判定する
	private int startIndex = 0;//アニメーション開始時のイメージリストのインデックス
	private int endIndex = 0;//アニメーション終了時のイメージリストのインデックス
	private int stopIndex = 0;//アニメーションが停止した際に表示するイメージリストのインデックス
	
	/* temprorary value */
	private int intervalCount = 0;
	private boolean IsFinishImageLoop = false;
	
	/**
	 * constructor
	 */
	public Animation() {
		imageList = new ArrayList<Bitmap>();
		IsStartAnime = false;
		imageRect = new Rect();
	}
	
	/**
	 * アニメーションのイメージリストを作成するメソッド
	 * アニメーション中は追加出来ない。
	 * @param imageList
	 * @param intervalTime
	 */
	public boolean setAnimation(Bitmap[] imageList, int intervalTime) {
		if(IsStartAnime) return false;
		
		//アニメーション画像の追加
		this.imageList.addAll(Arrays.asList(imageList));
		this.intervalTime = intervalTime;
		
		//画像の大きさを取得
		int w = this.imageList.get(0).getWidth();
		int h = this.imageList.get(0).getHeight();
		imageRect.set(0, 0, w, h);
		
		//アニメーションする画像範囲の初期化
		startIndex = 0;
		imageIndex = startIndex;
		endIndex = this.imageList.size()-1;
		
		return true;
	}
	
	/**
	 * アニメーションのイメージリストに画像を追加するメソッド。
	 * アニメーション中は追加出来ない。
	 * @param image
	 * @return
	 */
	public boolean addImage(Bitmap image) {
		if(IsStartAnime) return false;
		imageList.add(image);
		return true;
	}
	
	/**
	 * アニメーションのイメージリストに画像を追加するメソッド。
	 * アニメーション中は追加出来ない。
	 * @param image
	 * @param index
	 */
	public boolean addImage(Bitmap image, int index) {
		if(IsStartAnime) return false;
		imageList.add(index, image);
		return true;
	}
	
	/**
	 * アニメーション時の画像きりかえる間隔を指定する
	 * @param time
	 * update時にcountは進められる。例えば、time=5を指定すると、5回updateをすると画像が切り替わる
	 */
	public void setInterval(int time) {
		intervalCount = 0;//countをリセットする
		intervalTime = time;
	}
	
	/**
	 * アニメーションする画像範囲を指定するメソッド
	 * アニメーション中は使用できない
	 * アニメーション静止時の画像インデックスはstartIndexとendIndexの間にしている。
	 * @param startIndex
	 * @param endIndex
	 */
	public boolean setAnimationRange(int startIndex, int endIndex) {
		return setAnimationRange(startIndex, endIndex, (startIndex + endIndex)/2);
	}
	
	/**
	 * アニメーションする画像範囲を指定するメソッド
	 * アニメーション中は使用できない
	 * @param startIndex
	 * @param endIndex
	 * @param stopIndex
	 * @return
	 */
	public boolean setAnimationRange(int startIndex, int endIndex, int stopIndex) {
		if(startIndex < 0 || endIndex >= imageList.size()) {
			return false;
		} else if(IsStartAnime) {
			return false;
		}
		
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.stopIndex = stopIndex;
		return true;
	}
	
	/**
	 * 描画の画像位置を変更するメソッド。
	 * アニメーション中は使用できない。
	 * @param index
	 * @return
	 */
	public boolean setImageIndex(int index) {
		if(IsStartAnime) return false;
		imageIndex = index;
		return true;
	}
	
	/**
	 * アニメーションが開始しているか、否かを返すメソッド
	 * @return
	 * true:アニメーションが開始してる
	 * false:アニメーションは開始してない
	 */
	public boolean IsStartAnime() {
		return IsStartAnime;
	}
	
	/**
	 * アニメーションをスタートさせるメソッド
	 * @return
	 */
	public boolean startAnimation() {
		if(imageList.size()==0) return false;
		
		IsStartAnime = true;
		return true;
	}
	
	/**
	 * アニメーションをストップさせるメソッド。
	 */
	public void stopAnimation() {
		IsStartAnime = false;
		intervalCount = 0;
		//imageIndex = 0;//要修正
		imageIndex = stopIndex;
	}
	
	/**
	 * main threadで必ずアップデートをかけること
	 */
	public void update() {
		intervalCount++;
		if(intervalCount >= intervalTime) {
			imageIndex++;
			
			if(imageIndex > endIndex) {
				imageIndex = startIndex;
			}
		}
	}
	
	/**
	 * main threadで必ずアップデートをかけること
	 * アニメーション画像リストの中心にstopIndexがある場合に使うupdate関数
	 */
	public void update_custom1() {
		intervalCount++;
		if(intervalCount >= intervalTime) {
			if(IsFinishImageLoop) {
				imageIndex = startIndex;
				IsFinishImageLoop = false;
			} else {
				imageIndex++;
			}
			
			if(imageIndex > endIndex) {
				imageIndex = stopIndex;
				IsFinishImageLoop = true;
			}
		}
	}
	
	/**
	 * アニメーションイメージを描画するメソッド
	 * @param c
	 * :canvasクラス
	 * @param r
	 * :アニメーションイメージを描画する場所（大きさ）を指定する矩形
	 */
	public void draw(Canvas c, RectF r) {
		c.drawBitmap(imageList.get(imageIndex), imageRect, r, null);
	}
	
	/**
	 * test用のdrawメソッド
	 * @param c
	 */
	public void Testdraw(Canvas c, float left, float top) {
		c.drawBitmap(imageList.get(imageIndex), left, top, null);
	}
	
	/**
	 * animationクラスのリセット
	 */
	public void reset() {
		imageList.clear();
		imageRect.setEmpty();
		intervalTime = 0;
		IsStartAnime = false;
	}
}
