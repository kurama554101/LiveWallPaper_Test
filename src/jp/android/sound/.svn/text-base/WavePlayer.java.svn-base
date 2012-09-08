package jp.android.sound;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class WavePlayer {
	/* Define Value */
	private int MAX_SOUND_STREAMES = 10;
	private float LEFT_VOLUME = 100;
	private float RIGHT_VOLUME = 100;
	private int STREAM_PRIORITY = 1;
	private int LOOP_MODE = 0; /* 0 = no loop, -1 = loop for forever */
	private float RATE = 1; /* 1.0 = normal playback, range 0.5 to 2.0 */
	
	private int[] soundResources = null; //サウンドDB読み込み用1
	private int[] soundIds = null; //サウンドDB読み込み用2
	Context context = null;
	
	/* Local Value */
	SoundPool sp = null;
	
	public WavePlayer() {
		
	}
	
	public WavePlayer(Context context, int[] soundDBIds) {
		ReloadAll(context, soundDBIds);
	}
	
	public void ReloadAll(Context context, int[] soundDBIds) {
		if(sp!=null) sp.release();
		sp = new SoundPool(MAX_SOUND_STREAMES, AudioManager.STREAM_MUSIC, 0);
		
		soundResources = soundDBIds;
		soundIds = new int[soundResources.length];
		
		/* Context からサウンドを登録分だけ読み込む */
		for(int i=0; i<soundResources.length; i++) {
			soundIds[i] = sp.load(context, soundResources[i], 1);
		}
	}
	
	/* 正常に再生 = true, 再生失敗 = false */
	public boolean Play(int soundId) {
		int playResult = 0;
		if(sp==null) {
			return false;
		} else {
			/* 再生失敗時はsp.playは0を返す */
			sp.stop(soundId);
			playResult = sp.play(soundId, LEFT_VOLUME, RIGHT_VOLUME, STREAM_PRIORITY, LOOP_MODE, RATE);
		}
		
		if(playResult==0) {
			return false;
		} else {
			return true;
		}
	}
	
	/* resourceファイルのIDを直接使う場合はこちらの関数を使う */
	public boolean PlayRes(int res_soundId) {
		int soundId = -1;
		
		/* サウンドDBに登録されているresourceIdと一致する値を検索する */
		for(int i=0; i<soundResources.length; i++) {
			if(res_soundId == soundResources[i]) soundId = i+1;
		}
		
		/* サウンドDBから値が見つからなければ、falseを返す */
		if(soundId == -1) {
			return false;
		} else {
			return Play(soundId);
		}
	}
	
	public void Stop(int soundId) {
		if(sp==null) return;
		sp.stop(soundId);
	}
	
	public void Destroy() {
		if(sp != null){
			sp.release();
		}
	}
	
	/* 
	 * Setter and Getter 
	 * */
	public void SetMaxStreams(int maxStreams) {
		this.MAX_SOUND_STREAMES = maxStreams;
	}
	
	public void SetVolume(float left, float right) {
		LEFT_VOLUME = left;
		RIGHT_VOLUME = right;
	}
	
	public void SetLopeCount(int count) {
		LOOP_MODE = count;
	}
	
	public void SetRate(float rate) {
		RATE = rate;
	}
	
	public int[] GetSoundIds() {
		return soundIds;
	}
	
	public int GetMaxStreams() {
		return MAX_SOUND_STREAMES;
	}
}
