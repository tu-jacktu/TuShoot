package enemy;

import java.awt.image.BufferedImage;

import core.Awa;
import core.Flyer;
import core.World;

/** 封装,激光线 障碍 */
public class BlockRay extends Flyer implements Awa{

	private static BufferedImage image;
	private static BufferedImage[] blastImage;

	static {
		blastImage = new BufferedImage[5];
		image = loadImage//
		("shoot/audio_png/pictures&/block/", "block_tra");
		for (int i = 0; i < blastImage.length; i++) {
			blastImage[i]=loadImage//
					("shoot/audio_png/pictures&/block/","block_tra_blast_"+i);
		}
	}

	public BlockRay() {
		super(image.getWidth(), image.getHeight(), 0, //
				-image.getHeight(), 25);
		// 随机初始位置,左或右靠边
		int key = ran.nextInt(2);
		switch (key) {
		case 0:
			x = 0;
			break;
		case 1:
			x = World.WIDTH - this.width;
			break;
		}
		this.xSpeed = 0;
		this.ySpeed = 2;
		this.score = 100;
	}

	int rayTime = 0;

	public void step() {
		rayTime++;
		if (rayTime % 15 == 0) {
			x += xSpeed;
			y += ySpeed;
		}
	}
	int time =0;
	int deadindex =0;
	/**	返回图片实现动画效果	*/
	public BufferedImage getImage() {
		time++;
		if (isDead() && time%5==0) {
			deadindex++;
		}
		
		
		if (isLife()) {
			return image;
		}else if(isDead()){
			if (deadindex==blastImage.length) {
				goRemove();
			}
			return blastImage[deadindex%blastImage.length];
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
