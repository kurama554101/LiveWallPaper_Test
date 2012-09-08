package jp.android.chara;

import android.content.res.Resources;
import android.util.Log;

public class Saber1 extends Character {
	
	/*
	 * private value
	 */
	private int imageWidth = 24;
	private int imageHeight = 32;
	private int walkcount = 0;//���s�Ԋu�̃J�E���g�p�ϐ�

	/*
	 * define value
	 */
	public final static int ANIMATION_DEFAULT_INDEX = 7;
	public final static int ANIMATION_BACK_START_INDEX = 0;
	public final static int ANIMATION_BACK_END_INDEX = 2;
	public final static int ANIMATION_RIGHT_START_INDEX = 3;
	public final static int ANIMATION_RIGHT_END_INDEX = 5;
	public final static int ANIMATION_FRONT_START_INDEX = 6;
	public final static int ANIMATION_FRONT_END_INDEX = 8;
	public final static int ANIMATION_LEFT_START_INDEX = 9;
	public final static int ANIMATION_LEFT_END_INDEX = 11;
	
	public Saber1(Resources res, int chara_id, int[] ballon_ids) {
		super(res, chara_id, ballon_ids);
		this.top = 0;
		this.left = 0;
		this.bottom = imageHeight;
		this.right = imageWidth;
		setAnimation();
		setDirection(Character.DIRECTION_FRONT);
	}
	
	private boolean setAnimation() {
		return setAnimation(imageWidth, imageHeight);
	}
	
	@Override
	protected void Walk() {
		switch(direction) {
		case Character.DIRECTION_BACK:
			this.top = this.top - walkSpeed;
			this.bottom = this.bottom - walkSpeed;
			break;
		case Character.DIRECTION_FRONT:
			this.top = this.top + walkSpeed;
			this.bottom = this.bottom + walkSpeed;
			break;
		case Character.DIRECTION_LEFT:
			this.left = this.left - walkSpeed;
			this.right = this.right - walkSpeed;
			break;
		case Character.DIRECTION_RIGHT:
			this.left = this.left + walkSpeed;
			this.right = this.right + walkSpeed;
			break;
		}
		//mAnimation.startAnimation();
	}
	
	protected void RandomWalk() {
		int value = mRandom.nextInt(Character.DIRECTION_COUNT+1);
		
		walkcount++;
		if(walkcount < Character.DEFAULT_WALK_INTERVAL) {
			return;
		} else {
			walkcount = 0;//interval�𒴂�����A�J�E���g���O�ɂ���
		}
		
		if(value == Character.DIRECTION_COUNT) {
			return;//�����Ɋւ���萔�ȊO�̒l�̏ꍇ�͕��s���Ȃ��悤�ɂ���
		} else if(value != direction) {
			ChangeDirection(value);
		}
		
		Walk();
	}
	
	protected void StopWalk() {
		walkcount = 0;
		mAnimation.stopAnimation();
	}
	
	@Override
	protected boolean setDirection(int direct) {
		if(mAnimation.IsStartAnime()) {
			return false;
		}
		
		this.direction = direct;//������������
		
		switch(direct) {
		case Character.DIRECTION_BACK:
			mAnimation.setAnimationRange(ANIMATION_BACK_START_INDEX, ANIMATION_BACK_END_INDEX);
			mAnimation.setImageIndex((ANIMATION_BACK_END_INDEX+ANIMATION_BACK_START_INDEX)/2);
			break;
		case Character.DIRECTION_FRONT:
			mAnimation.setAnimationRange(ANIMATION_FRONT_START_INDEX, ANIMATION_FRONT_END_INDEX);
			mAnimation.setImageIndex((ANIMATION_FRONT_END_INDEX+ANIMATION_FRONT_START_INDEX)/2);
			break;
		case Character.DIRECTION_LEFT:
			mAnimation.setAnimationRange(ANIMATION_LEFT_START_INDEX, ANIMATION_LEFT_END_INDEX);
			mAnimation.setImageIndex((ANIMATION_LEFT_END_INDEX+ANIMATION_LEFT_START_INDEX)/2);
			break;
		case Character.DIRECTION_RIGHT:
			mAnimation.setAnimationRange(ANIMATION_RIGHT_START_INDEX, ANIMATION_RIGHT_END_INDEX);
			mAnimation.setImageIndex((ANIMATION_RIGHT_END_INDEX+ANIMATION_RIGHT_START_INDEX)/2);
			break;
		}
		
		return true;
	}
	
	@Override
	protected void ChangeDirection(int direct) {
		mAnimation.stopAnimation();
		setDirection(direct);
		mAnimation.startAnimation();
	}
	
	@Override
	public void update() {
		if(IsTalkAction) {
			comment.update();
			if(!comment.doTalk()) {
				IsTalkAction = false;
			}
			
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
			//mAnimation.update();
			mAnimation.update_custom1();
			RandomWalk();
		}
	}
}
