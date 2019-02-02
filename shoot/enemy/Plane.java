package enemy;

import java.awt.image.BufferedImage;

import core.Awa;
import core.Flyer;
import core.World;

/** 封装小敌机的属性,行为 */
public class Plane extends Flyer  implements Awa{

	private static BufferedImage images[];
	private static BufferedImage bomImages[];

	static {// 加载图片
		String path = "shoot/audio_png/pictures&/enemy/";
		images = new BufferedImage[2];
		bomImages = new BufferedImage[7];
		for (int i = 0; i < images.length; i++) {
			images[i] = loadImage(path, "plane_" + i);
		}
		for (int i = 0; i < bomImages.length; i++) {
			bomImages[i] = loadImage(path, "p_blast_" + i);
		}
	}

	/** 小敌机构造 */
	public Plane() {
		super(images[0].getWidth(), images[0].getHeight(), //
				ran.nextInt(World.WIDTH - images[0].getWidth()), //
				-images[0].getHeight(), 3);
		xSpeed = 0;
		ySpeed = 4;
		this.score = 15;
	}

	/** 小敌机移动,方式 */
	public void step() {
		x += xSpeed;
		y += ySpeed;
	}

	/** 返回图片实现动画效果,与死亡动画 */
	int time = 0;// 控制返回 图片,速率
	int index = 0;// 控制,存货,图片下标
	int deadIndex = 0;// 控制,死亡,图片下标

	public BufferedImage getImage() {
		time++;
		// 控制图片下标,速率
		if (isLife() && time % 15 == 0) {
			index++;
		} else if (isDead() && time % 10 == 0) {
			deadIndex++;
		}

		if (isLife()) {
			return images[index % 2];
		} else if (isDead()) {
			if (deadIndex == bomImages.length - 1)
				// 下标到达极限,修改状态为可移除
				goRemove();
			return bomImages[deadIndex%bomImages.length];
		}
		return null;
	}

	/** 越界判断,超边界 追加 宽高 才算越界 */
	public boolean outOfBounds() {
		if (this.isDead()) {
			//死亡单位不考虑越界
			return false;
		}
		int w_h = this.width + this.height;
		return y < -w_h || y > World.HEIGHT + w_h || //
				x < -w_h || x > World.WIDTH + w_h;
	}
}
