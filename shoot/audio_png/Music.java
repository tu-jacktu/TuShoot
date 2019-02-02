package audio_png;

import java.applet.Applet;
import java.applet.AudioClip;

/**
 * 封装背景音乐及音效. 播放会随着当前线程 结束 而 结束!
 */
public class Music {
	protected String a;

//	private static AudioClip bgm;// 背景音乐,只有一份.
	/** 由于音乐太大,容易临时加载容易造成卡顿,所以放入主类加载! */
//	// 播放背景音乐静态的方法
//	private static void playBgm(String bgmName, String PlayStyle) {
//		try {
//			if (bgm == null) {// bgm为空 新建一个 bgm
//				bgm = Applet.newAudioClip(Music.class.getResource("Bgm&Effec/" + bgmName + ".wav").toURI().toURL());
//			} else {
//				bgm.stop();// bgm不为空 停止原bgm,new 指定bgm 执行新操作!
//				bgm = Applet.newAudioClip(Music.class.getResource("Bgm&Effec/" + bgmName + ".wav").toURI().toURL());
//			}
//
//			if (PlayStyle.equals("play"))
//				bgm.play();
//			if (PlayStyle.equals("loop"))
//				bgm.loop();
//			if (PlayStyle.equals("stop"))
//				bgm.stop();
//		} catch (Exception e) {
//			System.out.println("播放音乐遇到麻烦...");
//		}
//
//	}

	// 播放音效的静态方法
	public static void playEffect(final String effectName, final String playStyle) {
		new Thread(new Runnable() {
			public void run() {
				try {// 直接新建一个音效 和线程 然后播放!
					AudioClip effect = Applet.newAudioClip//
					(Music.class.getResource("Bgm&Effec/" + effectName + ".wav").toURI().toURL());
					if (playStyle.equals("play")) {
						effect.play();
					} else {
						effect.loop();
					}
				} catch (Exception e) {
					System.out.println("播放音乐遇到麻烦...");
				}
			}
		}).start();
	}
}
