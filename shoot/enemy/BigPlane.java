package enemy;

import java.util.List;
import java.util.ArrayList;
import java.awt.image.BufferedImage;

import bullet.EnemyBu;
import core.Awa;
import core.Flyer;
import core.World;

/** 封装大型敌机 */
public class BigPlane extends Flyer implements Awa {
	private static BufferedImage images[];
	private static BufferedImage bomImages[];

	static {// 加载图片
		String path = "shoot/audio_png/pictures&/enemy/";
		images = new BufferedImage[2];
		bomImages = new BufferedImage[5];
		for (int i = 0; i < images.length; i++) {
			images[i] = loadImage(path, "bigplane_" + i);
		}
		for (int i = 0; i < bomImages.length; i++) {
			bomImages[i] = loadImage(path, "bp_blast_" + i);
		}
	}

	/** 大敌机构造,初始位置屏幕两旁 */
	public BigPlane() {
		super(images[0].getWidth(), images[0].getHeight(), 0, 0, 55);
		this.y = this.height / 12;
		int xMove_xSite = ran.nextInt(2);
		// 随机的,移动速度
		if (xMove_xSite == 0) {
			this.x = -this.width;
			this.xSpeed = +6;
		} else {
			this.x = World.WIDTH;
			this.xSpeed = -6;
		}
		this.ySpeed = 1;
		this.score = 150;
	}

	/** 大敌机移动,方式,比较缓慢,左右下移 */
	int stepTime = 0;

	public void step() {
		stepTime++;
		this.x += this.xSpeed;
		if (stepTime % 15 == 0) {// 速率控制
			this.y += this.ySpeed;
		}
		// 左右移动
		if (this.x <= 0) {
			this.xSpeed = 4;
		}
		if (this.x >= World.WIDTH - this.width - this.width / 16) {
			this.xSpeed = -2;
		}
	}

	/** 大敌机发射子弹 ,双发 追踪 */
	public List<EnemyBu> shoot() {
		reCentre();
		List<EnemyBu> bs = //
				new ArrayList<EnemyBu>();
		bs.add(new EnemyBu(this.x, this.y + this.height * 2 / 3, EnemyBu.BIG_TRA_BU));
		bs.add(new EnemyBu(this.x + this.width - EnemyBu.getTrace_Width_2() * 2//
				, this.y + this.height * 2 / 3, EnemyBu.BIG_TRA_BU));
		return bs;
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

	/** 越界判断,超边界 追加 两倍,宽高 才算越界 */
	public boolean outOfBounds() {
		if (this.isDead()) {
			//死亡单位不考虑越界
			return false;
		}
		int two_w_h = this.width * 2 + this.height * 2;
		return y < -two_w_h || y > World.HEIGHT + two_w_h || //
				x < -two_w_h || x > World.WIDTH + two_w_h;
	}
}
