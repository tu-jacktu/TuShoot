package core;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import core.Flyer;
import core.World;

/** 天空 */
public class Sky extends Flyer {
	public int y1;// 第二张图片的的y坐标
	private static BufferedImage[] images;

	/** 加载背景-图片 */
	static {
		images = new BufferedImage[4];
		for (int i = 0; i < images.length; i++) {
			images[i] = loadImage//
			("shoot/audio_png/pictures&/sky/", "bg_" + i);
		}
	}

	/** sky构造方法 */
	public Sky() {
		super(images[0].getWidth(), images[0].getHeight(), 0, 0, 1);
		y1 = -this.height;
		xSpeed = 0;
		ySpeed = 1;
	}

	/** 天空移动 */
	int index = 0;

	public void step() {
		index++;
		if (index % 8 == 0) {
			y1 += ySpeed;
			y += ySpeed;
		}
		if (y >= height) {
			y = -height;
		}
		if (y1 >= height) {
			y1 = -height;
		}
	}

	/** 根据当前游戏关卡,返回天空图片 */
	public BufferedImage getImage() {
		// System.out.println("天空返回图");
		switch (World.gameLevel) {
		case 1:
			return images[1];
		case 2:
			return images[2];
		case 3:
			return images[3];
		default:
			// 游戏开始状态,返回此图片
			return images[0];
		}
	}

	@Override
	public void paintObject(Graphics g) {
		g.drawImage(getImage(), 0, y, null);
		g.drawImage(getImage(), x, y1, null);
	}

	@Override
	public boolean outOfBounds() {
		return false;
	}
}
