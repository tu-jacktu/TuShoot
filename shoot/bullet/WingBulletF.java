package bullet;

import java.awt.image.BufferedImage;

/** 封装固定的僚机的子弹 */
public class WingBulletF extends SuperBullet {
	private static BufferedImage buStr;
	private static BufferedImage buStrA;

	private static BufferedImage buSup[];

	public static final int STR = 0;
	public static final int STR_A = 1;
	public static final int SUP = 2;

	static {
		buSup = new BufferedImage[2];
		String path = "shoot/audio_png/pictures&/pet/petBu_";
		buStr = loadImage(path, "str");
		buStrA = loadImage(path, "strA");
		for (int i = 0; i < buSup.length; i++) {
			buSup[i] = loadImage(path, "str_sup_" + i);
		}
	}

	public WingBulletF(int x, int y, int choose) {
		super(x, y, choose);
		switch (choose) {
		case 0:
			image = buStr;
			xSpeed = 0;
			ySpeed = -7;
			this.width = image.getWidth();
			this.height = image.getHeight();
			break;
		case 1:
			image = buStrA;
			xSpeed = 0;
			ySpeed = -10;
			this.width = image.getWidth();
			this.height = image.getHeight();
			break;
		case 2:
			image = buSup[0];
			xSpeed = 0;
			ySpeed = -32;
			this.width = image.getWidth();
			this.height = image.getHeight();
			break;
		}
	}

	int time = 0;
	int index = 0;

	/** 重写 返回图片的方法 */
	public BufferedImage getImage() {
		time++;
		if (time % 10 == 0) {
			index++;
		}
		if (this.image != buSup[0]) {
			return super.getImage();
		} else {
			return buSup[index % 2];
		}
	}

	/** 返回图片,获得宽高,以便于创建子弹 ,指定 x y */
	public static BufferedImage getBulletImage(int choose) {
		switch (choose) {
		case 0:
			return buStr;
		case 1:
			return buStrA;
		default:
			return buSup[0];
		}
	}
}
