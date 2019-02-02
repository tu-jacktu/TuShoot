package bullet;

import java.awt.image.BufferedImage;

/** 封装环绕僚机子弹 */
public class WingBulletR extends SuperBullet {
	private static BufferedImage buTrace;
	private static BufferedImage buTraceA;
	
	public static final int TRACE = 0;
	public static final int TRACE_A = 1;
	static {
		String path = "shoot/audio_png/pictures&/pet/petBu_";
		buTrace = loadImage(path, "trace");
		buTraceA = loadImage(path, "traceA");
	}

	public WingBulletR(int x, int y, int choose) {
		super(x, y, choose);
		switch (choose) {
		case 0:
			image = buTrace;
			xSpeed = 4;
			ySpeed = -7;
			break;
		case 1:
			image = buTraceA;
			xSpeed = 7;
			ySpeed = -12;
			break;
		}
	}
}
