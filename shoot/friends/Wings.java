package friends;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import bullet.WingBulletF;
import bullet.WingBulletR;
import core.Flyer;

/** 封装僚机,提供两种移动方式 */
public class Wings extends Flyer {
	private static BufferedImage imagesF[];
	private static BufferedImage imagesR[];
	private final int choose;

	static {
		imagesF = new BufferedImage[2];
		imagesR = new BufferedImage[2];
		String path = "shoot/audio_png/pictures&/pet/";
		for (int i = 0; i < 2; i++) {
			imagesF[i] = loadImage//
			(path, "petF_" + i);
			imagesR[i] = loadImage//
			(path, "petR_" + i);
		}
	}

	/** 构造 ,choose 分辨宠物类型 */
	public Wings(int choose) {
		super(imagesF[0].getWidth(), imagesF[0].getHeight(), //
				450, 540, 1);
		this.choose = choose;
		if (choose == 0) {// 环绕的,宠物
			this.width = imagesR[0].getWidth();
			this.height = imagesR[0].getHeight();
			this.x = 141;
			this.y = 550;
			this.xSpeed = 1;
			this.ySpeed = 1;
		}
	}

	/** Round 宠物的射击 */
	int shoot_TraceTime = 0;

	public ArrayList<WingBulletR> shoot_Trace() {
		shoot_TraceTime++;
		ArrayList<WingBulletR> bs = new ArrayList<>();
		if (Hero.skillPet && shoot_TraceTime % 40 == 0) {
			bs.add(new WingBulletR(this.x + this.width / 3, this.y + this.height / 3//
					, WingBulletR.TRACE_A));
		} else if (shoot_TraceTime % 350 == 0) {
			bs.add(new WingBulletR(this.x + this.width / 3, this.y + this.height / 3//
					, WingBulletR.TRACE));// 发射普通子弹
		}
		return bs;
	}

	/** Fix 宠物的射击 */
	int shootTime = 0;
	int time = 1000 * 6;// 6秒
	public ArrayList<WingBulletF> shoot_Stra() {
		shootTime++;
		if (time < 0) {// 技能持续6s
			Hero.skillPet = false;
			time = 1000 * 6;// 重置计时器为 6秒
		}
		
		ArrayList<WingBulletF> bs = new ArrayList<>();
		if (Hero.skillPet && shootTime % 35 == 0) {// 发射超级子弹每,30 ms 走一次
			time -= 35;// 技能 计时
			bs.add(new WingBulletF(this.x + this.width / 8, this.y +25 - WingBulletF.//
					getBulletImage(WingBulletF.SUP).getHeight(), WingBulletF.SUP));
		} else if (shootTime % 350 == 0) {// 发射普通子弹
			bs.add(new WingBulletF(this.x + 3 + this.width / 3, this.y, WingBulletF.STR));
		}
		return bs;
	}

	/** 环绕的宠物,移动方式 */
	int timeR = 0;

	public void stepRound(Hero hero) {
		timeR++;
		xCentre = this.x + this.width / 2;
		yCentre = this.y + this.height / 2;

		// 扯回来的算法
		int H = xCentre - hero.xCentre;
		int V = yCentre - hero.yCentre;
		if (H * H + V * V > 150 * 200) {
			x = x - (xCentre - hero.xCentre) / 100;
			y = y - (yCentre - hero.yCentre) / 100;
		}

		// 围绕的算法
		if (timeR % 3 == 0) {
			if (yCentre > hero.yCentre) {
				x -= xSpeed;
			} else {
				x += xSpeed;
			}
			if (xCentre > hero.xCentre) {
				y += ySpeed;
			} else {
				y -= ySpeed;
			}
		}
	}

	/** 固定的宠物移动方式 */
	int timeT = 0;

	public void stepTrace(Hero hero) {
		timeT++;
		int tX = hero.xCentre - 83 + 180;// 目标X坐标
		int tY = hero.yCentre - 70 + 30;// 目标Y坐标
		// 向目标 位置 靠拢的算法
		if (this.x < tX) {
			this.x++;
		} else if (this.x > tX) {
			this.x--;
		}
		if (this.y < tY) {
			this.y++;
		} else if (this.y > tY) {
			this.y--;
		}
	}

	/** 返回图片,实现动画 */
	int index = 0;

	public BufferedImage getImage() {
		if (choose == 1) {
			return imagesF[index++ % 2];
		} else {
			return imagesR[index++ % 2];

		}
	}

	/** 宠物机 永不越界 */
	public boolean outOfBounds() {
		return false;
	}

	/** 被强制的 重写 */
	public void step() {
	}

}
