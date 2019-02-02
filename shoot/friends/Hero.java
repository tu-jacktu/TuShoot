package friends;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import audio_png.GamePng;
import bullet.HeroBullet;
import core.Flyer;
import core.World;

/** 封装英雄机的类 */
public class Hero extends Flyer {
	private static BufferedImage images[];
	private int fire = 0;

	/** 为英雄添加护盾,检测.为false 则碰撞无效！ */
	public static boolean canHit = true;

	/** 为英雄添加技能开关 */
	public static boolean skillHelp;
	public static boolean skillEle;
	public static boolean skillPet;
	/** 为英雄添加技能-计数 */
	public static int helpAmout = 1;
	public static int eleAmout = 1;
	public static int petAmount = 1;

	/** 方便创建子弹所创建的常量 */
	public final int x_4;

	public final int sideL;
	public final int sideML;
	public final int sideM;
	public final int sideMR;
	public final int sideR;

	/** 加載英雄的圖片 */
	static {
		images = new BufferedImage[5];
		for (int i = 0; i < images.length; i++) {
			images[i] = loadImage //
			("shoot/audio_png/pictures&/hero/", "hero_" + i);
		}
	}

	/** 英雄构造 */
	public Hero() {
		super(images[0].getWidth(), images[0].getHeight(), //
				World.WIDTH / 2 - images[0].getWidth() / 2, 500, 180);
		// 给 常量赋值
		this.x_4 = this.width / 4;
		this.sideL = 0;
		this.sideML = x_4;
		this.sideM = this.width / 2;
		this.sideMR = x_4 * 3;
		this.sideR = this.width;
	}

	/** 英雄机随着鼠标移动 x,y :鼠标的x和y坐标 */
	public void moveTo(int a, int b) {
		x = a - width / 2;// a-83
		y = b - height / 2; // b-70
	}

	/** 英雄返回图片，实现动画 */
	int imageIndex = 0;
	int imageTime = 0;

	/** 返回英雄的图片,实现动画效果 */
	public BufferedImage getImage() {
		imageTime++;// 动画速率控制
		if (imageTime % 8 == 0)
			imageIndex++;
		if (isLife()) {
			return images[imageIndex % 5];
		} else {
			return null;
		}
	}

	int shootTime = 0;
	public static int bestfire = 600;
	public static int secondfire = 400;
	public static int lastfire = 200;
	/** 根据英雄位置创建 子弹，实现 射击 */

	public ArrayList<HeroBullet> shoot() {
		shootTime++;
		ArrayList<HeroBullet> bs = new ArrayList<HeroBullet>();
		if (fire > bestfire && shootTime % 180 == 0) {// 五倍,最边上 supEle 靠里 两旁 Tra
														// 中间 sup
			bs.add(new HeroBullet(this.x + this.sideL, this.y, HeroBullet.SUP_ELE));
			bs.add(new HeroBullet(this.x + this.sideML, this.y - this.height / 2, HeroBullet.TRACE));
			bs.add(new HeroBullet(this.x + this.sideM - 12, this.y - HeroBullet.getSupBulletHeight(), HeroBullet.SUP));
			bs.add(new HeroBullet(this.x + this.sideMR, this.y - this.height / 2, HeroBullet.TRACE));
			bs.add(new HeroBullet(this.x + this.sideR, this.y, HeroBullet.SUP_ELE));
			fire -= 5;
			return bs;
		} else if (fire <= bestfire && fire > secondfire && shootTime % 300 == 0) {// 三倍,最边上
																					// supEle
																					// 中间
																					// tra
			bs.add(new HeroBullet(this.x + this.sideL, this.y, HeroBullet.SUP_ELE));
			bs.add(new HeroBullet(this.x + this.sideM, this.y, HeroBullet.TRACE));
			bs.add(new HeroBullet(this.x + this.sideR, this.y, HeroBullet.SUP_ELE));
			fire -= 3;
		} else if (fire <= secondfire && fire > lastfire && shootTime % 450 == 0) {// 三倍,最边上
																					// ele
																					// 中间
																					// nor
			bs.add(new HeroBullet(this.x + this.sideL, this.y, HeroBullet.ELE));
			bs.add(new HeroBullet(this.x + this.sideM, this.y, HeroBullet.NOR));
			bs.add(new HeroBullet(this.x + this.sideR, this.y, HeroBullet.ELE));
			fire -= 3;
		} else if (fire <= lastfire && shootTime % 600 == 0) { // ---------------双倍
																// 两旁靠里 nor
			bs.add(new HeroBullet(this.x + this.sideML, this.y, HeroBullet.NOR));
			bs.add(new HeroBullet(this.x + this.sideMR, this.y, HeroBullet.NOR));
		}
		if (fire < 0) {
			fire = 0;// 火力小于零,归零
		}
		return bs;
	}

	/** 英雄 血條 绘制 的方法 */

	public void paintRect(Graphics g) {
		// 血量百分比
		life_max = life * 1.0 / maxLife;
		/** 血量阶梯颜色设置 */
		if (life_max <= 1)
			g.setColor(Color.GREEN);// 绿
		if (life_max < 0.8)
			g.setColor(Color.PINK);// 粉
		if (life_max < 0.6)
			g.setColor(Color.YELLOW);// 黄
		if (life_max < 0.4)
			g.setColor(Color.MAGENTA);// 品红
		if (life_max < 0.2)
			g.setColor(Color.RED);// 红
		// 500宽是满血
		g.fill3DRect(58, 8, (int) (376 * life_max), 16, true);
		g.drawImage(GamePng.hero_rect, 0, 0, null);// 血条框
	}

	/** 英雄越界 永远为 false */
	public boolean outOfBounds() {
		return false;
	}

	/** 向外界 提供 火力值 */
	public int getFire() {
		return this.fire;
	}

	/** 向外界 提供 设置-火力值 */
	/** 与上面联用达到 增加火力的效果 */
	public void setFire(int fire) {
		this.fire = fire;
	}

	/** 向外界 提供 生命值 */
	public int getLife() {
		return this.life;
	}

	/** 向外界 提供 设置-生命值 */
	/** 与上面联用达到 增加生命的效果 */
	public void setLife(int life) {
		this.life = life;
	}

	/** 向外界提供最大生命值,方便获取奖励 */
	public int getMaxLife() {
		return maxLife;
	}

	/** 被強制重寫的,無意義 */
	public void step() {
	}

}
