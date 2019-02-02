package bullet;

import java.awt.image.BufferedImage;

/** 封裝-英雄子彈,包括英雄的 追踪子弹 , 直射子弹 等 */
public class HeroBullet extends SuperBullet {
	private static BufferedImage norBullet;
	private static BufferedImage traceBullet;
	private static BufferedImage eleBullet;
	private static BufferedImage supEleBullet;
	private static BufferedImage supBullet;
	
	public int choose;

	public static final int NOR = 0;
	public static final int TRACE = 1;
	public static final int ELE = 2;
	public static final int SUP_ELE = 3;
	public static final int SUP = 4;

	static {
		String path = "shoot/audio_png/pictures&/hero/heroBu_";
		norBullet = loadImage(path, "nor");
		traceBullet = loadImage(path, "trace");
		eleBullet = loadImage(path, "ele");
		supEleBullet = loadImage(path, "superEle");
		supBullet = loadImage(path, "super");
	}

	/** 构造,参数指定,子弹的选择 */
	public HeroBullet(int x, int y, int choose) {
		super(x, y, choose);
		this.choose = choose;
		switch (this.choose) {
		case 1:// 获得参数后,指定图片,和 速度
			xSpeed = 6;
			ySpeed = -8;
			this.image = traceBullet;
			break;
		case 2:
			this.image = eleBullet;
			xSpeed = 0;
			ySpeed = -7;
			break;
		case 3:
			this.image = supEleBullet;
			xSpeed = 0;
			ySpeed = -9;
			break;
		case 4:
			this.image = supBullet;
			xSpeed = 0;
			ySpeed = -10;
			break;
		default:
			this.image = norBullet;
			xSpeed = 0;
			ySpeed = -7;
			break;
		}
		this.width = image.getWidth();
		this.height = image.getHeight();
		this.xCentre = this.x + this.width / 2;
		this.yCentre = this.y + this.height / 2;
	}
	
	/**	判定当前子弹是否,是追踪的子弹	*/
	public boolean isTrace(){
		return this.choose == HeroBullet.TRACE;
	}
	/**	返回超级子弹的 高方便 英雄射击的子弹创建	*/
	public static int getSupBulletHeight(){
		return supBullet.getHeight();
	}
}
