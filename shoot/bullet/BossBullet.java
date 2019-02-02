package bullet;

import java.awt.image.BufferedImage;

/** 封装BOSS 机子弹 ,行为 属性 */
// Tips : Boss 子弹 y 速度要 偏慢

public class BossBullet extends SuperBullet {
	private static BufferedImage imageTrace;
	private static BufferedImage imageStra;

	public static final int TRACE = 0;
	public static final int STRAI = 1;
	
	public int life;//子弹的生命值
	
	public int choose;

	static {
		String path = "shoot/audio_png/pictures&/boss/";
		imageStra = loadImage(path, "bossStrBu");
		imageTrace = loadImage(path, "bossTraceBullet");
	}

	/** Trace */
	public BossBullet(int x, int y, int choose,int xSpeed,int ySpeed) {
		super(x, y,choose);
		this.choose = choose;
		switch (choose) {
		case TRACE:
			this.image = imageTrace;
			break;
		case STRAI:
			this.image = imageStra;
			break;
		}
		this.width = image.getWidth();
		this.height = image.getHeight();
		this.xCentre = this.x + this.width / 2;
		this.yCentre = this.y + this.height / 2;
		//构造指定速度,实现散弹
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
		this.life=4;
		
	}
	
	/** 判断此子弹 是否追踪子弹 */
	public boolean isTrace(){
		return this.choose==BossBullet.TRACE;
	}
}
