package jp.android.chara;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Rect;
import android.graphics.drawable.NinePatchDrawable;

/*public class TalkBallon extends RectF*/
public class TalkBallon {
	/*
	 * Define Value
	 */
	public final static int COMMENT_LEFT_TOP_DIRECTION = 0;
	public final static int COMMENT_RIGHT_TOP_DIRECTION = 1;
	public final static int COMMENT_LEFT_BOTTOM_DIRECTION = 2;
	public final static int COMMENT_RIGHT_BOTTOM_DIRECTION = 3;
	public final static int COMMENT_DEFAULT_SHOWING_TIME = 300;
	
	/*
	 * Private Define Value
	 */
	
	/*
	 * Local Value
	 */
	private Bitmap[] image = null;//commentのイメージを格納する配列
	private String message = null;
	private NinePatchDrawable nDrawble = null;
	private Rect ballonRect = null;
	private int commentDirection;
	private float image_width;
	private float image_height;
	private int showingTime;//commentの表示時間
	private int showingCount;
	private boolean doTalk;
	
	/**
	 * Constructor
	 * @param res:resourceIDを取得
	 * @param image_id:resourceに格納されたcommentイメージファイルのIDを4つ列挙する（左上、右上、左下、右下の順番で格納）
	 */
	public TalkBallon(Resources res, int[] image_id) {
		init(res, image_id);
	}
	
	public TalkBallon(Resources res, int[] image_id, float center_x, float center_y, int nine_id) {
		init(res, image_id);
		SetCenterPointPosition(center_x, center_y);
	}
	
	/*public TalkBallon(Resources res, int[] image_id, RectF r) {
		init(res, image_id);
		//SetBallonPos(r);
	}*/
	
	/*
	 * Method
	 */
	private void init(Resources res, int[] image_id) {
		image = new Bitmap[4]; // commentは左右上下で同じ大きさのイメージを持つため。
		ballonRect = new Rect();
		
		SetCommentImage(res, image_id);
		commentDirection = COMMENT_LEFT_TOP_DIRECTION;
		showingTime = COMMENT_DEFAULT_SHOWING_TIME;
		showingCount = 0;
		doTalk = false;
	}
	
	/**
	 * commentのイメージ配列(private)にresourceにあるイメージファイルを格納する。
	 * @param res
	 * @param image_id
	 * @return
	 */
	private boolean SetCommentImage(Resources res, int[] image_id) {
		if(res==null) return false;
		image[COMMENT_LEFT_TOP_DIRECTION] = BitmapFactory.decodeResource(res, image_id[COMMENT_LEFT_TOP_DIRECTION]);
		image[COMMENT_RIGHT_TOP_DIRECTION] = BitmapFactory.decodeResource(res, image_id[COMMENT_RIGHT_TOP_DIRECTION]);
		image[COMMENT_LEFT_BOTTOM_DIRECTION] = BitmapFactory.decodeResource(res, image_id[COMMENT_LEFT_BOTTOM_DIRECTION]);
		image[COMMENT_RIGHT_BOTTOM_DIRECTION] = BitmapFactory.decodeResource(res, image_id[COMMENT_RIGHT_BOTTOM_DIRECTION]);
		
		/* Commentの矩形内容の初期化 */
		image_width = image[COMMENT_LEFT_TOP_DIRECTION].getWidth();
		image_height = image[COMMENT_LEFT_TOP_DIRECTION].getHeight();
		ballonRect.top = 0;
		ballonRect.left = 0;
		ballonRect.bottom = (int)image_height;
		ballonRect.right = (int)image_width;
		
		return true;
	}
	
	public void SetMessage(String message) {
		this.message = message;
	}
	
	/*public RectF GetBallonSize() {
		return this;
	}*/
	
	/**
	 * commentの矩形情報を取得する。
	 * @return rect
	 */
	public Rect GetBallonRect() {
		return ballonRect;
	}
	
	/**
	 * balloonの横の長さを取得
	 * @return
	 */
	public float GetWidth() {
		return ballonRect.width();
	}
	
	/**
	 * balloonの縦の長さを取得
	 * @return
	 */
	public float GetHeight() {
		return ballonRect.height();
	}
	
	/**
	 * 矩形型を代入する場合は事前に矩形の大きさが同一かを判定する
	 * @param r
	 * @return
	 */
	/*public Boolean SetBallonPos(RectF r) {
		if(r==null) return false;
		
		return true;
	}*/
	
	/**
	 * balloonの中心座標の位置を決定するメソッド。
	 * @param x
	 * :矩形の中心のx座標
	 * @param y
	 * :矩形の中心のy座標
	 */
	public void SetCenterPointPosition(float x, float y) {
		int height = ballonRect.height();
		int width = ballonRect.width();
		
		ballonRect.top = (int)(y - height/2);
		ballonRect.bottom = (int)(y + height/2);
		ballonRect.left = (int)(x - width/2);
		ballonRect.right = (int)(x + width/2);
	}
	
	/**
	 * balloonの右下の座標を決定するメソッド。
	 * @param x
	 * :矩形の右下の点のx座標
	 * @param y
	 * :矩形の右下の点のy座標
	 */
	public void SetRightBottomPointPosition(float x, float y) {
		int height = ballonRect.height();
		int width = ballonRect.width();
		
		ballonRect.bottom = (int)y;
		ballonRect.right = (int)x;
		ballonRect.left = (int)(x - width);
		ballonRect.top = (int)(y - height);
	}
	
	/**
	 * balloonの右上の座標を決定するメソッド。
	 * @param x
	 * :矩形の右上の点のx座標
	 * @param y
	 * :矩形の右上の点のy座標
	 */
	public void SetRightTopPointPosition(float x, float y) {
		int height = ballonRect.height();
		int width = ballonRect.width();
		
		ballonRect.right = (int)x;
		ballonRect.top = (int)y;
		ballonRect.left = (int)(x - width);
		ballonRect.bottom = (int)(y + height);
	}
	
	/**
	 * balloonの左下の座標を決定するメソッド。
	 * @param x
	 * :矩形の左下の点のx座標
	 * @param y
	 * :矩形の左下の点のy座標
	 */
	public void SetLeftBottomPointPosition(float x, float y) {
		int height = ballonRect.height();
		int width = ballonRect.width();
		
		ballonRect.left = (int)x;
		ballonRect.bottom = (int)y;
		ballonRect.right = (int)(x + width);
		ballonRect.top = (int)(y - height);
	}
	
	/**
	 * balloonの左上の座標を決定するメソッド。
	 * @param x
	 * :矩形の左上の点のx座標
	 * @param y
	 * :矩形の左上の点のy座標
	 */
	public void SetLeftTopPointPosition(float x, float y) {
		int height = ballonRect.height();
		int width = ballonRect.width();
		
		ballonRect.left = (int)x;
		ballonRect.top = (int)y;
		ballonRect.bottom = (int)(y + height);
		ballonRect.right = (int)(x + width);
	}
	
	/**
	 * commentの向きを設定する（キャラクターから見て、右上、右下、左上、左下が設定可能）
	 * @param direction_id
	 * directionについては、本クラスにstaticで宣言されている。
	 */
	public void SetCommentDirection(int direction_id) {
		commentDirection = direction_id;
	}
	
	/**
	 * commentの表示時間を決めるメソッド
	 * @param time
	 */
	public void setShowingTime(int time) {
		showingTime = time;
	}
	
	public int getShowingTime() {
		return showingTime;
	}
	
	public void start() {
		doTalk = true;
	}
	
	public void stop() {
		doTalk = false;
		showingCount = 0;
	}
	
	/**
	 * commentの表示が続いているか・否かを判定するメソッド。
	 * @return
	 */
	public boolean doTalk() {
		return this.doTalk;
	}
	
	public void update() {
		showingCount++;
		if(showingCount >= showingTime) {
			stop();
		}
	}
	
	public void Draw(Canvas c) {
		/* ninePatchDrawbleはfloatだと使えない。違う方法を検討する必要あり */
		/* 現状は無理やりRect型を使って、実装している */
		/*nDrawble = new NinePatchDrawable(image[commentDirection], image[commentDirection].getNinePatchChunk(), null, null);
		nDrawble.setBounds(ballonRect);*/
		
		//dummy paint
		Paint mPaint = new Paint();
		final int textSize = 20;
		mPaint.setStyle(Paint.Style.STROKE);  
        mPaint.setColor(Color.RED);  
        mPaint.setAntiAlias(true);  
        mPaint.setTextSize(textSize);
		float textWidth = mPaint.measureText(message);
		float textLeft = ballonRect.left + (ballonRect.width() - textWidth)/2;  
        float textTop  = ballonRect.top  + (ballonRect.height() - textSize)/2;
		
		//draw
		//nDrawble.draw(c);
		c.drawBitmap(image[commentDirection], ballonRect.left, ballonRect.top,null);
		
		//文字を描く
		c.drawText(message, textLeft, textTop, mPaint);
	}
}
