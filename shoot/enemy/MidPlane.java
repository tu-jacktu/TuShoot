package enemy;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import bullet.EnemyBu;
import core.Awa;
import core.Flyer;
import core.World;

/** 封装中敌机的属性及行为 */
public class MidPlane extends Flyer implements Awa {
	private static BufferedImage images[];
	private static BufferedImage bomImages[];

	static {// 加载图片
		String path = "shoot/audio_png/pictures&/enemy/";
		images = new BufferedImage[2];
		bomImages = new BufferedImage[5];
		for (int i = 0; i < images.length; i++) {
			images[i] = loadImage(path, "midplane_" + i);
		}
		for (int i = 0; i < bomImages.length; i++) {
			bomImages[i] = loadImage(path, "mp_blast_" + i);
		}
	}

	/** 中敌机构造 */
	public MidPlane() {
		super(images[0].getWidth(), images[0].getHeight(), //
				ran.nextInt(World.WIDTH - images[0].getWidth()), //
				-images[0].getHeight(), 12);
		int xMove = ran.nextInt(2);
		if (xMove == 0) {// 随机,左 或 右移动
			xSpeed = -1;
		} else {
			xSpeed = -1;
		}
		ySpeed = 1;
		this.score = 50;
	}

	/** 中敌机移动,方式 */
	// 位于,9/10 屏高 内 时,遇到 一定位置会反弹
	public void step() {
		x += xSpeed;
		y += ySpeed;
		// 拐弯模式
		if (y < World.HEIGHT * 5 / 6) {
			if (x < -this.width / 10) {
				xSpeed = 1;
			}
			if (x > World.WIDTH - this.width) {
				xSpeed = -1;
			}
		}
	}

	/** 中敌机发射子弹 */
	public ArrayList<EnemyBu> shoot() {
		reCentre();
		ArrayList<EnemyBu> bs = //
				new ArrayList<EnemyBu>();
		bs.add(new EnemyBu(xCentre - EnemyBu.getNor_Width_2(), //
				this.y + this.height * 2 / 3, EnemyBu.MID_STRA_BU));
		return bs;
	}

	/** 返回图片实现动画效果,与死亡动画 */
	int time = 0;// 控制返回 图片,速率
	int index = 0;// 控制,存货,图片下标
	int deadIndex = 0;// 控制,死亡,图片下标

	public BufferedImage getImage() {
		time++;
		// 控制图片下标,速率
		if (isLife() && time % 5 == 0) {
			index++;
		} else if (isDead() && time % 5 == 0) {
			deadIndex++;
		}
		if (isLife()) {
			return images[index % 2];
		} else if (isDead()) {
			if (deadIndex == bomImages.length - 1)
				// 下标到达极限,修改状态为可移除
				goRemove();
			return bomImages[deadIndex % bomImages.length];
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
