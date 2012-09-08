package jp.android.chara;

import java.util.ArrayList;
import java.util.Arrays;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * �A�j���[�V�������쐬����N���X
 * �e�A�j���[�V�����摜�̑傫���͈ꗥ�����Ƃ���K�v������
 * @author sakamotoshouta
 *
 */
public class Animation {
	private ArrayList<Bitmap> imageList = null;//animation���쐬���邽�߂̉摜���X�g
	private Rect imageRect = null;//�A�j���[�V�����摜�̑傫�����i�[�����`�N���X
	private int intervalTime = 0;//�A�j���[�V�����Ԋu
	private int imageIndex = 0;//���݂̕`��C���[�W
	private boolean IsStartAnime;//�A�j���[�V�������Ă��邩�ǂ����𔻒肷��
	private int startIndex = 0;//�A�j���[�V�����J�n���̃C���[�W���X�g�̃C���f�b�N�X
	private int endIndex = 0;//�A�j���[�V�����I�����̃C���[�W���X�g�̃C���f�b�N�X
	private int stopIndex = 0;//�A�j���[�V��������~�����ۂɕ\������C���[�W���X�g�̃C���f�b�N�X
	
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
	 * �A�j���[�V�����̃C���[�W���X�g���쐬���郁�\�b�h
	 * �A�j���[�V�������͒ǉ��o���Ȃ��B
	 * @param imageList
	 * @param intervalTime
	 */
	public boolean setAnimation(Bitmap[] imageList, int intervalTime) {
		if(IsStartAnime) return false;
		
		//�A�j���[�V�����摜�̒ǉ�
		this.imageList.addAll(Arrays.asList(imageList));
		this.intervalTime = intervalTime;
		
		//�摜�̑傫�����擾
		int w = this.imageList.get(0).getWidth();
		int h = this.imageList.get(0).getHeight();
		imageRect.set(0, 0, w, h);
		
		//�A�j���[�V��������摜�͈͂̏�����
		startIndex = 0;
		imageIndex = startIndex;
		endIndex = this.imageList.size()-1;
		
		return true;
	}
	
	/**
	 * �A�j���[�V�����̃C���[�W���X�g�ɉ摜��ǉ����郁�\�b�h�B
	 * �A�j���[�V�������͒ǉ��o���Ȃ��B
	 * @param image
	 * @return
	 */
	public boolean addImage(Bitmap image) {
		if(IsStartAnime) return false;
		imageList.add(image);
		return true;
	}
	
	/**
	 * �A�j���[�V�����̃C���[�W���X�g�ɉ摜��ǉ����郁�\�b�h�B
	 * �A�j���[�V�������͒ǉ��o���Ȃ��B
	 * @param image
	 * @param index
	 */
	public boolean addImage(Bitmap image, int index) {
		if(IsStartAnime) return false;
		imageList.add(index, image);
		return true;
	}
	
	/**
	 * �A�j���[�V�������̉摜���肩����Ԋu���w�肷��
	 * @param time
	 * update����count�͐i�߂���B�Ⴆ�΁Atime=5���w�肷��ƁA5��update������Ɖ摜���؂�ւ��
	 */
	public void setInterval(int time) {
		intervalCount = 0;//count�����Z�b�g����
		intervalTime = time;
	}
	
	/**
	 * �A�j���[�V��������摜�͈͂��w�肷�郁�\�b�h
	 * �A�j���[�V�������͎g�p�ł��Ȃ�
	 * �A�j���[�V�����Î~���̉摜�C���f�b�N�X��startIndex��endIndex�̊Ԃɂ��Ă���B
	 * @param startIndex
	 * @param endIndex
	 */
	public boolean setAnimationRange(int startIndex, int endIndex) {
		return setAnimationRange(startIndex, endIndex, (startIndex + endIndex)/2);
	}
	
	/**
	 * �A�j���[�V��������摜�͈͂��w�肷�郁�\�b�h
	 * �A�j���[�V�������͎g�p�ł��Ȃ�
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
	 * �`��̉摜�ʒu��ύX���郁�\�b�h�B
	 * �A�j���[�V�������͎g�p�ł��Ȃ��B
	 * @param index
	 * @return
	 */
	public boolean setImageIndex(int index) {
		if(IsStartAnime) return false;
		imageIndex = index;
		return true;
	}
	
	/**
	 * �A�j���[�V�������J�n���Ă��邩�A�ۂ���Ԃ����\�b�h
	 * @return
	 * true:�A�j���[�V�������J�n���Ă�
	 * false:�A�j���[�V�����͊J�n���ĂȂ�
	 */
	public boolean IsStartAnime() {
		return IsStartAnime;
	}
	
	/**
	 * �A�j���[�V�������X�^�[�g�����郁�\�b�h
	 * @return
	 */
	public boolean startAnimation() {
		if(imageList.size()==0) return false;
		
		IsStartAnime = true;
		return true;
	}
	
	/**
	 * �A�j���[�V�������X�g�b�v�����郁�\�b�h�B
	 */
	public void stopAnimation() {
		IsStartAnime = false;
		intervalCount = 0;
		//imageIndex = 0;//�v�C��
		imageIndex = stopIndex;
	}
	
	/**
	 * main thread�ŕK���A�b�v�f�[�g�������邱��
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
	 * main thread�ŕK���A�b�v�f�[�g�������邱��
	 * �A�j���[�V�����摜���X�g�̒��S��stopIndex������ꍇ�Ɏg��update�֐�
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
	 * �A�j���[�V�����C���[�W��`�悷�郁�\�b�h
	 * @param c
	 * :canvas�N���X
	 * @param r
	 * :�A�j���[�V�����C���[�W��`�悷��ꏊ�i�傫���j���w�肷���`
	 */
	public void draw(Canvas c, RectF r) {
		c.drawBitmap(imageList.get(imageIndex), imageRect, r, null);
	}
	
	/**
	 * test�p��draw���\�b�h
	 * @param c
	 */
	public void Testdraw(Canvas c, float left, float top) {
		c.drawBitmap(imageList.get(imageIndex), left, top, null);
	}
	
	/**
	 * animation�N���X�̃��Z�b�g
	 */
	public void reset() {
		imageList.clear();
		imageRect.setEmpty();
		intervalTime = 0;
		IsStartAnime = false;
	}
}
