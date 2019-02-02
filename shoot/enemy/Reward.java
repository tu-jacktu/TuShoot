package enemy;

import friends.Hero;

import java.awt.image.BufferedImage;

import audio_png.Music;
import core.Awa;
import core.Flyer;
import core.World;

public class Reward extends Flyer implements Awa {
	private static BufferedImage life[];
	private static BufferedImage fire;
	private static BufferedImage shield;
	private static BufferedImage ele;
	private static BufferedImage pet;

	public static final int LIFE = 0;
	public static final int FIRE = 1;
	public static final int PROTECTED = 2;
	public static final int ELE = 3;
	public static final int PET = 4;

	public final int type;

	static {
		String path = "shoot/audio_png/pictures&/award/award_";
		life = new BufferedImage[2];
		for (int i = 0; i < life.length; i++) {
			life[i] = loadImage(path, "life_" + i);
		}
		fire = loadImage(path, "fire");
		shield = loadImage(path, "shield");
		ele = loadImage(path, "ele");
		pet = loadImage(path, "pet");
	}

	/** 奖励的构造 */
	public Reward(Flyer f, int awardType) {
		super(0, 0, f.x, f.y, 1);
		this.type = awardType;
		switch (type) {
		case Reward.LIFE:
			this.width = life[0].getWidth();
			this.height = life[0].getHeight();
			break;
		case Reward.FIRE:
			this.width = fire.getWidth();
			this.height = fire.getHeight();
			break;
		case Reward.PROTECTED:
			this.width = shield.getWidth();
			this.height = shield.getHeight();
			Hero.canHit = false;// 使英雄不能被碰撞
			World.hero_protected = 3 * 1000;// 护盾计时,初始化
			break;
		case Reward.ELE:
			this.width = ele.getWidth();
			this.height = ele.getHeight();
			break;
		case Reward.PET:
			this.width = pet.getWidth();
			this.height = pet.getHeight();
			break;
		}
		this.xSpeed = 1;
		this.ySpeed = 4;
	}

	/** 奖励的移动方式,向英雄靠拢 */
	public void stepToHero(Hero f) {
		reCentre();
		if (this.xCentre > f.xCentre)
			x -= xSpeed;
		if (this.xCentre < f.xCentre)
			x += xSpeed;
		y += ySpeed;
	}

	/** 给予奖励的方法 */
	public void payReward(Hero h) {
		switch (type) {
		case Reward.LIFE:// 给予 5%的最大生命值
			if (h.getLife() < h.getMaxLife() * 0.95) {
				h.setLife(h.getLife() + (int) (h.getMaxLife() * 0.05));
			}
			Music.playEffect("getLife", "play");
			break;
		case Reward.FIRE:
			// 英雄火力档次
			// int bestfire = 600;
			// int secodfire = 400;
			// int lastfire = 200;
			if (h.getFire() < 2500) {
				h.setFire(h.getFire() + 160);
			}
			Music.playEffect("getFire", "play");
			break;
		case Reward.PROTECTED:
			Hero.canHit = false;
			Music.playEffect("getShield", "play");
			break;
		case Reward.ELE:
			if (Hero.eleAmout <= 6) {
				Hero.eleAmout++;
			}
			Music.playEffect("getEle", "play");
			break;
		case Reward.PET:
			if (Hero.petAmount <= 6) {
				Hero.petAmount++;
			}
			Music.playEffect("getPet", "play");
			break;
		}
	}

	/** 返回图片实现动画 */
	int time = 0;
	int index = 0;

	public BufferedImage getImage() {
		time++;
		switch (type) {
		case Reward.FIRE:
			return fire;
		case Reward.PROTECTED:
			return shield;
		case Reward.ELE:
			return ele;
		case Reward.PET:
			return pet;
		}
		if (time % 1 == 0) {
			index++;
		}
		return life[index % life.length];
	}

	/** 奖励越界 检测 */
	public boolean outOfBounds() {
		return this.y > World.HEIGHT + this.height * 2;
	}

	/** 被强制的重写,无实际意义 */
	public void step() {
	}

}
