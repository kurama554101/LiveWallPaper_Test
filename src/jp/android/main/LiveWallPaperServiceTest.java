package jp.android.main;

import jp.android.chara.Character;
import jp.android.chara.Saber1;
import jp.android.sound.WavePlayer;
import jp.android.status.DispStatus;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.WindowManager;
import android.widget.TextView;

public class LiveWallPaperServiceTest extends WallpaperService {
	
	private final Handler mHandler = new Handler();
	private NFcReceiver mNFcReceiver;
	public DispStatus mDispStatus = new DispStatus();
	//Character chara1 = null;
	Saber1 chara1 = null;
	double currentFps = 0;
	
	private static final int FPS = 20;//1�b�Ԃɉ���`�悷�邩�̉�

	@Override
	public void onCreate() {
		super.onCreate();
		android.os.Debug.waitForDebugger(); //service���f�o�b�O���邽�߂̂��܂��Ȃ�
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public Engine onCreateEngine() {
		return new LiveWallPaperEngine();
	}
	
	class LiveWallPaperEngine extends Engine {
		/* Local Variable */
		boolean mVisible = false;
		WavePlayer wplayer = null;
		
		/* Resource File */
		Context context = getApplicationContext();
		Resources res = getResources();
		//int chara1_id = R.drawable.minami160;
		int chara1_id = R.drawable.saber_f;
		int wav_id[] = {R.raw.minami_syoukan_koe1};
		int[] ballon_ids = {
				R.drawable.comment_left,
				R.drawable.comment_right,
				R.drawable.comment_left_bottom,
				R.drawable.comment_right_bottom
		};
		
		/* Draw Thread(main loop) */
		private final Runnable mDrawFrame = new Runnable() {
			public void run() {
				long currentTime = System.currentTimeMillis();
				
				drawFrame();
				while((System.currentTimeMillis()-currentTime) <= 1000/FPS) {
					//�ݒ肳�ꂽFPS�𒴂���ꍇ�͂����Ń��[�v������B
				}
				
				mHandler.post(mDrawFrame);
				currentFps = (double)(1000 / (System.currentTimeMillis()-currentTime));
			}
		};
		
		/* Constructer */
		LiveWallPaperEngine() {
			//chara1 = new Character(context, chara1_id, ballon_ids);
			chara1 = new Saber1(res, chara1_id, ballon_ids);
			wplayer = new WavePlayer(context, wav_id);
			chara1.Action(Character.ACTION_WALK_START);
			
			//init BroadCastReceiver
			mNFcReceiver = new NFcReceiver();
			IntentFilter filter = new IntentFilter();
			filter.addAction("com.access_company.android.nfcommunicator.action.RECEIVE_MESSAGE");
			registerReceiver(mNFcReceiver, filter);
		}
		
		@Override
		public void onCreate(SurfaceHolder surfaceHolder) {
			super.onCreate(surfaceHolder);
			setTouchEventsEnabled(true);
            
			/* draw thread start */
			mHandler.post(mDrawFrame);
		}
		
		@Override
        public void onDestroy() {
            super.onDestroy();
            mHandler.removeCallbacks(mDrawFrame);
        }

		@Override
        public void onVisibilityChanged(boolean visible) {
            mVisible = visible;
            if (visible) {
                drawFrame();
            } else {
                /* TBD */
            }
        }
		
		@Override
	    public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	        super.onSurfaceChanged(holder, format, width, height);
	        mDispStatus.SetDispHeight(height);
	        mDispStatus.SetDispWidth(width);
	        /* TBD */
	    }
		
		@Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
        }
		
		@Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            mVisible = false;
        }
		
		@Override
        public void onOffsetsChanged(float xOffset, float yOffset,
                float xStep, float yStep, int xPixels, int yPixels) {
            //mOffset = xOffset;
            drawFrame();
        }
		
		@Override
		public void onTouchEvent(MotionEvent event) {
			switch(event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if(chara1.TouchCheck(event.getX(), event.getY())) {
					wplayer.PlayRes(R.raw.minami_syoukan_koe1);
					chara1.IsTalkAction = false;
				}
				chara1.SetXY(event.getX(), event.getY());
				update();
				
			case MotionEvent.ACTION_MOVE:
				;
			case MotionEvent.ACTION_UP:
				;
			}
		}
		
		/**
		 * LiveWallPaper�S�̂�`�悷�邽�߂̃��\�b�h
		 */
		public void drawFrame() {
			final SurfaceHolder holder = getSurfaceHolder();
			Canvas c = null;
			
			//update
			update();
			
			//draw
			try {
				c = holder.lockCanvas();
				drawClean(c);
				chara1.Draw(c, null);
				drawFPS(c);
				
			} finally {
				if(c!=null) {
					holder.unlockCanvasAndPost(c);
				}
			}
		}
		
		/**
		 * �`�悷��O�̊e�I�u�W�F�N�g�i�L�����N�^�[��炻�̑����́j�̍X�V�����{���郁�\�b�h
		 */
		private void update() {
			RectF r = new RectF(chara1);
			chara1.update();
			
			//�ǂ𓞒B���Ă���ꍇ�͈ʒu����O�ɖ߂�
			if(IsHitEdge()) {
				chara1.set(r);
			}
		}
		
		/**
		 * �������ŁB�L�����N�^�[���ǂɒB���Ă��Ȃ������m�F����֐�
		 */
		private boolean IsHitEdge() {
			boolean ret =
					(chara1.top < 0) || (chara1.bottom > mDispStatus.GetDispHeight()) ||
					(chara1.left < 0) || (chara1.right > mDispStatus.GetDispWidth()); 
			return ret;
		}
		
		/**
		 * debug�p��FPS�`�惁�\�b�h
		 * @param c
		 *  canvas
		 */
		private void drawFPS(Canvas c) {
			Paint p = new Paint();
			final int textSize = 40;
			p.setStyle(Paint.Style.STROKE);  
	        p.setColor(Color.WHITE);  
	        p.setAntiAlias(true);  
	        p.setTextSize(textSize);
	        
	        c.drawText(String.valueOf(currentFps), 100, 100, p);
		}
		
		public void drawClean(Canvas c) {
			c.drawColor(Color.BLACK);
		}
	}
	
	public class NFcReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d("NFcReceive", "NFcMessageReceive!");
			if(chara1 != null) {
				chara1.Action(Character.ACTION_TALK_START);
			}
		}
	}

}
