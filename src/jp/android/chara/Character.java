package jp.android.chara;

import java.util.ArrayList;
import java.util.Random;

import android.R;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.widget.TextView;

/**
 * キャラクターのクラス。
 * 表示・使用するキャラクターごとにこのクラスをスーパークラスとして扱う
 * @author sakamotoshouta
 *
 */
public abstract class Character extends RectF {
	
	/*
	 * Character Define Value
	 */
	
	//character direction
	public final static int DIRECTION_LEFT = 0;
	public final static int DIRECTION_RIGHT = 1;
	public final static int DIRECTION_BACK = 2;
	public final static int DIRECTION_FRONT = 3;
	public final static int DIRECTION_COUNT = 4;//方向に関する変数の数を格納
	
	//character action id
	public final static int ACTION_WALK_START = 10;
	public final static int ACTION_WALK_STOP = 11;
	public final static int ACTION_TALK_START = 12;
	public final static int ACTION_TALK_STOP = 13;
	public final static int ACTION_DIRECTION_CHANGE = 14;
	public final static int ACTION_ALL_STOP = 20;
	
	//other value
	public final static int DEFAULT_ANIMATION_INTERVAL = 5;
	public final static int DEFAULT_WALK_SPEED = 5;
	public final static int DEFAULT_WALK_INTERVAL = 10;
	
	/*
	 * Character Private Define Value
	 */
	
	
	/*
	 * Character Value
	 */
	private Bitmap image;
	private ArrayList<Bitmap> imageList;
	protected TalkBallon comment;
	protected Animation mAnimation;
	public boolean IsTalkAction;
	public boolean IsWalkAction;
	protected int animationInterval;
	protected int walkSpeed;
	protected int walkInterval;
	protected int direction;
	protected Random mRandom;//乱数作成用
	
	//balloon
	private int[] balloon_ids;
	
	/* 
	 * Character Method
	 */
	/**
	 * constructer
	 * @param res:resource class
	 * @param chara_id:resourceに格納されたキャラクターイメージのIDを指定
	 * @param ballon_ids:resourceに格納されたcommentイメージのIDを4つ指定（左上、右上、左下、右下の順番)
	 */
	public Character(Resources res, int chara_id, int[] ballon_ids) {
		image = BitmapFactory.decodeResource(res, chara_id);
		SetBallonImage(res, ballon_ids);
		IsTalkAction = false;
		this.left = 0;
		this.top = 0;
		this.right = left + image.getWidth();
		this.bottom = top + image.getHeight();
		mAnimation = new Animation();
		imageList = new ArrayList<Bitmap>();
		animationInterval = DEFAULT_ANIMATION_INTERVAL;
		walkSpeed = DEFAULT_WALK_SPEED;
		direction = DIRECTION_FRONT;
		mRandom = new Random();
	}
	
	/**
	 * constructer
	 * @param c
	 * @param chara_id
	 * @param ballon_ids
	 */
	/*public Character(Context c, int chara_id, int[] balloon_ids) {
		image = BitmapFactory.decodeResource(c.getResources(), chara_id);
		IsTalkAction = false;
		this.left = 0;
		this.top = 0;
		this.right = left + image.getWidth();
		this.bottom = top + image.getHeight();
		//setBalloon(c, balloon_ids);
	}*/
	
	/**
	 * キャラクターの吹き出し用textViewを初期化する
	 * @param c
	 *  contextクラス
	 */
	/*private void setBalloon(Context c, int[] balloon_ids) {
		this.balloon = new TextView(c);
		balloon.setBackgroundResource(balloon_ids[0]);
	}*/
	
	public void SetX(float center_x) {
		float width = this.width();
		this.left = center_x - width/2;
		this.right = center_x + width/2;
	}
	
	public void SetY(float center_y) {
		float height = this.height();
		this.top = center_y - height/2;
		this.bottom = center_y + height/2;
	}
	
	/* X,Yはキャラクターの中心座標 */
	public void SetXY(float center_x, float center_y) {
		SetX(center_x);
		SetY(center_y);
	}
	
	private void SetBallonImage(Resources res, int[] ballon_ids) {
		comment = new TalkBallon(res, ballon_ids);
	}
	
	abstract void update();
	/*public void update() {
		if(IsTalkAction) {
			 //balloonが画面端を超えていないかの確認
			 //デフォルトはキャラクターの左上にballoonを配置する
			boolean checkLeftSide = ((this.centerX()-this.width()/4) >= comment.GetWidth());
			boolean checkTopSide = ((this.centerY()-this.height()/4) >= comment.GetHeight());
			
			if(checkLeftSide && checkTopSide) {
				//画面端を超えていない
				comment.SetCommentDirection(TalkBallon.COMMENT_RIGHT_BOTTOM_DIRECTION);
				comment.SetRightBottomPointPosition(this.centerX()-this.width()/4, this.centerY()-this.height()/4);
			} else if(checkLeftSide) {
				//上端を超えた場合：balloonはキャラクターの左下に配置する。
				Log.d("check_side","checkLeftSide = true");
				comment.SetCommentDirection(TalkBallon.COMMENT_RIGHT_TOP_DIRECTION);
				comment.SetRightTopPointPosition(this.centerX()-this.width()/4, this.centerY()+this.height()/4);
			} else if(checkTopSide) {
				//左端を超えた場合：balloonはキャラクターの右上に配置する。
				Log.d("check_side","checkTopSide = true");
				comment.SetCommentDirection(TalkBallon.COMMENT_LEFT_BOTTOM_DIRECTION);
				comment.SetLeftBottomPointPosition(this.centerX()+this.width()/4, this.centerY()-this.height()/4);
			} else {
				//左端および上端を超えた場合：balloonはキャラクターの右下に配置する。
				Log.d("check_side","checkTopSide||checkLeftSide = false");
				comment.SetCommentDirection(TalkBallon.COMMENT_LEFT_TOP_DIRECTION);
				comment.SetLeftTopPointPosition(this.centerX()+this.width()/4, this.centerY()+this.height()/4);
			}
		} 
		
		if(IsWalkAction) {
			mAnimation.update();
		}
	}*/
	
	/**
	 * アニメーションリストを作成するメソッド
	 * ※大きい画像から歩行チップを作成する
	 * ※アニメーション中はリストを作成出来ない。
	 * @param width
	 * @param height
	 * @param count
	 * @return
	 */
	protected boolean setAnimation(int width, int height, int max_count) {
		int index = 0;
		int x = 0;
		int y = 0;
		
		if(mAnimation.IsStartAnime()) {
			return false;
		} else {
			while(index < max_count) {
				imageList.add(Bitmap.createBitmap(image, x, y, width, height));
				x = x + width;
				
				if(x >= image.getWidth()) {
					y = y + height;
					x = 0;
					
					if (y >= image.getHeight()) break;
				}
				index++;
			}
		}
		
		Bitmap[] list = imageList.toArray(new Bitmap[imageList.size()]);
		mAnimation.setAnimation(list, DEFAULT_ANIMATION_INTERVAL);
		return true;
	}
	
	/**
	 * アニメーションリストを作成するメソッド
	 * ※大きい画像から歩行チップを作成する
	 * ※アニメーション中はリストを作成出来ない。
	 * ※画像内にある全てのチップを読み込む
	 * @param width
	 * @param height
	 * @return
	 */
	protected boolean setAnimation(int width, int height) {
		return setAnimation(width, height, Integer.MAX_VALUE);
	}
	
	/**
	 * characterをアクションさせるためのメソッド。
	 * 外部から動作を止める際も本メソッドを使用すること。
	 * @param action_id:どのような動作をさせるかをidで指定（character classにactionの種類が記載されている)
	 */
	public void Action(int action_id) {
		Log.d("Character action", "start");
		
		switch(action_id) {
		case ACTION_WALK_START:
			IsWalkAction = true;
			mAnimation.startAnimation();
			break;
		case ACTION_WALK_STOP:
			IsWalkAction = false;
			mAnimation.stopAnimation();
			break;
		case ACTION_TALK_START:
			IsTalkAction = true;
			comment.start();
			break;
		case ACTION_TALK_STOP:
			IsTalkAction = false;
			comment.stop();
			break;
		}
	}
	
	/**
	 * キャラクターの歩行メソッド
	 * 一回実行すれば、一歩分移動する
	 */
	abstract protected void Walk();
	
	/**
	 * キャラクターの方向を設定するメソッド（歩行中は使えない）
	 * @return
	 */
	abstract protected boolean setDirection(int direct);
	
	/**
	 * キャラクターの方向を変更するメソッド
	 */
	abstract protected void ChangeDirection(int direct);
	
	/**
	 * commentの表示メッセージをセットする。
	 * @param message
	 */
	public void SetTalkMessage(String message) {
		comment.SetMessage(message);
	}
	
	/* touch eventがあったときに判定する関数 */
	public Boolean TouchCheck(float x, float y) {
		return this.contains(x, y);
	}
	
	/** 
	 * キャラクターが他の物体や壁とのあたり判定を行う際に使う関数
	 * @return
	 */
	public Boolean IsHitObjectAndEdge() {
		//TBD
		return true;
	}
	
	public void Draw(Canvas c, Paint p) {
		//c.drawBitmap(image, this.left, this.top, p);
		mAnimation.Testdraw(c, this.left, this.top);
		
		if(IsTalkAction) {
			comment.Draw(c);
		}
	}
}
