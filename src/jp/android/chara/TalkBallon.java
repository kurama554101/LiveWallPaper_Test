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
	private Bitmap[] image = null;//comment�̃C���[�W���i�[����z��
	private String message = null;
	private NinePatchDrawable nDrawble = null;
	private Rect ballonRect = null;
	private int commentDirection;
	private float image_width;
	private float image_height;
	private int showingTime;//comment�̕\������
	private int showingCount;
	private boolean doTalk;
	
	/**
	 * Constructor
	 * @param res:resourceID���擾
	 * @param image_id:resource�Ɋi�[���ꂽcomment�C���[�W�t�@�C����ID��4�񋓂���i����A�E��A�����A�E���̏��ԂŊi�[�j
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
		image = new Bitmap[4]; // comment�͍��E�㉺�œ����傫���̃C���[�W�������߁B
		ballonRect = new Rect();
		
		SetCommentImage(res, image_id);
		commentDirection = COMMENT_LEFT_TOP_DIRECTION;
		showingTime = COMMENT_DEFAULT_SHOWING_TIME;
		showingCount = 0;
		doTalk = false;
	}
	
	/**
	 * comment�̃C���[�W�z��(private)��resource�ɂ���C���[�W�t�@�C�����i�[����B
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
		
		/* Comment�̋�`���e�̏����� */
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
	 * comment�̋�`�����擾����B
	 * @return rect
	 */
	public Rect GetBallonRect() {
		return ballonRect;
	}
	
	/**
	 * balloon�̉��̒������擾
	 * @return
	 */
	public float GetWidth() {
		return ballonRect.width();
	}
	
	/**
	 * balloon�̏c�̒������擾
	 * @return
	 */
	public float GetHeight() {
		return ballonRect.height();
	}
	
	/**
	 * ��`�^��������ꍇ�͎��O�ɋ�`�̑傫�������ꂩ�𔻒肷��
	 * @param r
	 * @return
	 */
	/*public Boolean SetBallonPos(RectF r) {
		if(r==null) return false;
		
		return true;
	}*/
	
	/**
	 * balloon�̒��S���W�̈ʒu�����肷�郁�\�b�h�B
	 * @param x
	 * :��`�̒��S��x���W
	 * @param y
	 * :��`�̒��S��y���W
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
	 * balloon�̉E���̍��W�����肷�郁�\�b�h�B
	 * @param x
	 * :��`�̉E���̓_��x���W
	 * @param y
	 * :��`�̉E���̓_��y���W
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
	 * balloon�̉E��̍��W�����肷�郁�\�b�h�B
	 * @param x
	 * :��`�̉E��̓_��x���W
	 * @param y
	 * :��`�̉E��̓_��y���W
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
	 * balloon�̍����̍��W�����肷�郁�\�b�h�B
	 * @param x
	 * :��`�̍����̓_��x���W
	 * @param y
	 * :��`�̍����̓_��y���W
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
	 * balloon�̍���̍��W�����肷�郁�\�b�h�B
	 * @param x
	 * :��`�̍���̓_��x���W
	 * @param y
	 * :��`�̍���̓_��y���W
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
	 * comment�̌�����ݒ肷��i�L�����N�^�[���猩�āA�E��A�E���A����A�������ݒ�\�j
	 * @param direction_id
	 * direction�ɂ��ẮA�{�N���X��static�Ő錾����Ă���B
	 */
	public void SetCommentDirection(int direction_id) {
		commentDirection = direction_id;
	}
	
	/**
	 * comment�̕\�����Ԃ����߂郁�\�b�h
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
	 * comment�̕\���������Ă��邩�E�ۂ��𔻒肷�郁�\�b�h�B
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
		/* ninePatchDrawble��float���Ǝg���Ȃ��B�Ⴄ���@����������K�v���� */
		/* ����͖������Rect�^���g���āA�������Ă��� */
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
		
		//������`��
		c.drawText(message, textLeft, textTop, mPaint);
	}
}
