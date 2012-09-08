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
 * �L�����N�^�[�̃N���X�B
 * �\���E�g�p����L�����N�^�[���Ƃɂ��̃N���X���X�[�p�[�N���X�Ƃ��Ĉ���
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
	public final static int DIRECTION_COUNT = 4;//�����Ɋւ���ϐ��̐����i�[
	
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
	protected Random mRandom;//�����쐬�p
	
	//balloon
	private int[] balloon_ids;
	
	/* 
	 * Character Method
	 */
	/**
	 * constructer
	 * @param res:resource class
	 * @param chara_id:resource�Ɋi�[���ꂽ�L�����N�^�[�C���[�W��ID���w��
	 * @param ballon_ids:resource�Ɋi�[���ꂽcomment�C���[�W��ID��4�w��i����A�E��A�����A�E���̏���)
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
	 * �L�����N�^�[�̐����o���ptextView������������
	 * @param c
	 *  context�N���X
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
	
	/* X,Y�̓L�����N�^�[�̒��S���W */
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
			 //balloon����ʒ[�𒴂��Ă��Ȃ����̊m�F
			 //�f�t�H���g�̓L�����N�^�[�̍����balloon��z�u����
			boolean checkLeftSide = ((this.centerX()-this.width()/4) >= comment.GetWidth());
			boolean checkTopSide = ((this.centerY()-this.height()/4) >= comment.GetHeight());
			
			if(checkLeftSide && checkTopSide) {
				//��ʒ[�𒴂��Ă��Ȃ�
				comment.SetCommentDirection(TalkBallon.COMMENT_RIGHT_BOTTOM_DIRECTION);
				comment.SetRightBottomPointPosition(this.centerX()-this.width()/4, this.centerY()-this.height()/4);
			} else if(checkLeftSide) {
				//��[�𒴂����ꍇ�Fballoon�̓L�����N�^�[�̍����ɔz�u����B
				Log.d("check_side","checkLeftSide = true");
				comment.SetCommentDirection(TalkBallon.COMMENT_RIGHT_TOP_DIRECTION);
				comment.SetRightTopPointPosition(this.centerX()-this.width()/4, this.centerY()+this.height()/4);
			} else if(checkTopSide) {
				//���[�𒴂����ꍇ�Fballoon�̓L�����N�^�[�̉E��ɔz�u����B
				Log.d("check_side","checkTopSide = true");
				comment.SetCommentDirection(TalkBallon.COMMENT_LEFT_BOTTOM_DIRECTION);
				comment.SetLeftBottomPointPosition(this.centerX()+this.width()/4, this.centerY()-this.height()/4);
			} else {
				//���[����я�[�𒴂����ꍇ�Fballoon�̓L�����N�^�[�̉E���ɔz�u����B
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
	 * �A�j���[�V�������X�g���쐬���郁�\�b�h
	 * ���傫���摜������s�`�b�v���쐬����
	 * ���A�j���[�V�������̓��X�g���쐬�o���Ȃ��B
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
	 * �A�j���[�V�������X�g���쐬���郁�\�b�h
	 * ���傫���摜������s�`�b�v���쐬����
	 * ���A�j���[�V�������̓��X�g���쐬�o���Ȃ��B
	 * ���摜���ɂ���S�Ẵ`�b�v��ǂݍ���
	 * @param width
	 * @param height
	 * @return
	 */
	protected boolean setAnimation(int width, int height) {
		return setAnimation(width, height, Integer.MAX_VALUE);
	}
	
	/**
	 * character���A�N�V���������邽�߂̃��\�b�h�B
	 * �O�����瓮����~�߂�ۂ��{���\�b�h���g�p���邱�ƁB
	 * @param action_id:�ǂ̂悤�ȓ���������邩��id�Ŏw��icharacter class��action�̎�ނ��L�ڂ���Ă���)
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
	 * �L�����N�^�[�̕��s���\�b�h
	 * �����s����΁A������ړ�����
	 */
	abstract protected void Walk();
	
	/**
	 * �L�����N�^�[�̕�����ݒ肷�郁�\�b�h�i���s���͎g���Ȃ��j
	 * @return
	 */
	abstract protected boolean setDirection(int direct);
	
	/**
	 * �L�����N�^�[�̕�����ύX���郁�\�b�h
	 */
	abstract protected void ChangeDirection(int direct);
	
	/**
	 * comment�̕\�����b�Z�[�W���Z�b�g����B
	 * @param message
	 */
	public void SetTalkMessage(String message) {
		comment.SetMessage(message);
	}
	
	/* touch event���������Ƃ��ɔ��肷��֐� */
	public Boolean TouchCheck(float x, float y) {
		return this.contains(x, y);
	}
	
	/** 
	 * �L�����N�^�[�����̕��̂�ǂƂ̂����蔻����s���ۂɎg���֐�
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
