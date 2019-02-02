package enemy;

import java.awt.image.BufferedImage;

import core.Awa;
import core.Flyer;
import core.World;

/** 封装冲击的,电球障碍 */
public class Rusher extends Flyer  implements Awa{

	private static BufferedImage images[];
	private static BufferedImage blastImages[];

	// 决定电球直冲位置
	private int key;

	static {
		images = new BufferedImage[3];
		blastImages = new BufferedImage[5];
		for (int i = 0; i < images.length; i++) {
			images[i] = loadImage//
			("shoot/audio_png/pictures&/block/block_", i + "");
		}
		for (int i = 0; i < blastImages.length; i++) {
			blastImages[i] = loadImage//
			("shoot/audio_png/pictures&/block/block_blast_", i + "");
		}
	}
	
	public Rusher() {
		super(images[0].getWidth(), images[0].getHeight()//
				, 0, -images[0].getHeight(), 20);
		key = ran.nextInt(5) + 1;
		this.ySpeed = 2;
		int create = ran.nextInt(2);
		switch (create) {
		case 0:
			x = -this.width;
			this.xSpeed = 5;
			break;
		case 1:
			x = World.WIDTH;
			this.xSpeed = -5;
			break;
		}
		this.score = 100;
	}

	int rusherTime = 0;

	/** 电球 障碍 移动方式 */
	public void step() {
		rusherTime++;
		if (xSpeed != 0 && y > (World.HEIGHT / 10 / this.key)) {
			xSpeed = 0;
			ySpeed = 18;
		}
		if (rusherTime % 15 == 0) {
			x += xSpeed;
			y += ySpeed;
		}
	}

	int time = 0;
	int index = 0;
	int deadIndex = 0;

	/** 图片切换速率 */
	public BufferedImage getImage() {
		/**	速率控制 	*/
		time++;
		if (isLife() && time % 5 == 0) {
			index++;
		} else if (isDead() && time % 10 == 0) {
			deadIndex++;
		}
		
		
		if (isLife()) {
			return images[index % images.length];
		} else if (isDead()) {
			this.ySpeed = 3;
			if (deadIndex == blastImages.length) {
				goRemove();
			}
			return blastImages[deadIndex % blastImages.length];
		}
		return null;
	}

	public boolean outOfBounds() {
		if (this.isDead()) {
			//死亡单位不考虑越界
			return false;
		}
		return y > World.HEIGHT + this.height;
	}

}
