package friends;

import java.awt.image.BufferedImage;

import core.Flyer;
import core.World;

/** 封装大招 召唤 の 雷电 的 属性及 行为 */
// 雷电 只存在 两秒.
public class Skill_ele extends Flyer {
	private static BufferedImage images[];
	private final long produce;// 产生时间
	static {
		images = new BufferedImage[4];
		for (int i = 0; i < images.length; i++) {
			images[i] = loadImage//
			("shoot/audio_png/pictures&/hero/", "ele_" + i);
		}
	}

	public Skill_ele() {
		super(images[0].getWidth(), images[0].getHeight(), //
				ran.nextInt(World.WIDTH), ran.nextInt(World.HEIGHT), 1);
		produce = System.currentTimeMillis();
	}

	/** 检测 电 是否 死亡,雷电只存在 0.3秒 */
	public boolean checkHero_Ele_isDead() {
		long nowTime = System.currentTimeMillis();
		return (nowTime - produce) > 300;
	}

	public void step() {

	}

	int time = 0;
	int index = 0;

	public BufferedImage getImage() {
		time++;
		if (time % 12 == 0) {
			index++;
		}
		return images[index%images.length];
	}

	public boolean outOfBounds() {
		return false;
	}

}
