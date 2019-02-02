package enemy;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import audio_png.GamePng;
import bullet.BossBullet;
import button.MyButton;
import core.Flyer;
import core.World;
import score.Score;

public class Boss extends Flyer {

	private static BufferedImage images[];
	private static BufferedImage bomImages[];

	private static BufferedImage ima_boss_1[];
	private static BufferedImage ima_boss_2[];
	private static BufferedImage ima_boss_3[];

	private int boss_level;

	public static final int ZERO = 0;
	public static final int ONE = 1;
	public static final int TWO = 2;
	public static final int THREE = 3;

	public int score;

	public final int createYspeed;// 构造后赋予的移动速度

	static {// 加载图片
		String path = "shoot/audio_png/pictures&/boss/";
		ima_boss_1 = new BufferedImage[4];
		ima_boss_2 = new BufferedImage[4];
		ima_boss_3 = new BufferedImage[5];
		bomImages = new BufferedImage[5];
		/** Boss1 图片 */
		for (int i = 0; i < ima_boss_1.length; i++) {
			ima_boss_1[i] = loadImage(path, "boss1_" + i);
		}
		/** Boss2 图片 */
		for (int i = 0; i < ima_boss_2.length; i++) {
			ima_boss_2[i] = loadImage(path, "boss2_" + i);

		}
		/** Boss3 图片 */
		for (int i = 0; i < ima_boss_3.length; i++) {
			ima_boss_3[i] = loadImage(path, "boss3_" + i);
		}
		/** 所有Boss 爆炸图片 */
		for (int i = 0; i < bomImages.length; i++) {
			bomImages[i] = loadImage(path, "boss_blast_" + i);
		}
	}

	/** Boss 构造,初始位置屏幕两旁 */
	public Boss(int level) {
		super(0, 0, 0, 0, 0);
		this.boss_level = level;
		// 根据,不同的BOSS 指定不同的图片\
		switch (level) {
		case 1:
			images = ima_boss_1;
			this.maxLife = 200;
			this.life = maxLife;
			break;
		case 2:
			images = ima_boss_2;
			this.maxLife = 270;
			this.life = maxLife;
			break;
		case 3:
			images = ima_boss_3;
			this.maxLife = 350;
			this.life = maxLife;
			break;
		}
		setWidth(images[0].getWidth());
		setHeight(images[0].getHeight());
		// 设置初始坐标
		this.y = 0;
		int xMove_xSite = ran.nextInt(2);
		// 随机的,移动速度
		xMove_xSite = 1;
		if (xMove_xSite == 0) {
			xSpeed = +3;
			this.x = -this.width;
		} else {
			xSpeed = -3;
			this.x = World.WIDTH;
		}
		this.ySpeed = 2;
		// 生成时候的y速度
		createYspeed = this.y;
		// 分数与boss等级有关
		this.score = 1000 * boss_level;
	}

	/** Boss移动,方式,比较缓慢,三角移动 */
	int stepTime = 0;

	public void step() {
		stepTime++;
		/** 三角移动算法 */
		if (y > World.HEIGHT / 6 + GamePng.hero_rect.getHeight()) {// 最下点
			xSpeed = 3;
			ySpeed = -2;
		}
		if (xSpeed > 0 && ySpeed != 0 && //
				y < GamePng.hero_rect.getHeight()) {// 最右点
			xSpeed = -4;
			ySpeed = 0;
		}
		if (ySpeed == 0 && x <= -this.width / 8) {// 最左点
			xSpeed = 3;
			ySpeed = 2;
		}
		if (stepTime % 60 == 0) {// 速率控制
			x += 2 * xSpeed;
			y += 2 * ySpeed;
		}
	}

	/** Boss机发射子弹 */
	public ArrayList<BossBullet> shoot() {
		reCentre();
		ArrayList<BossBullet> bs = new ArrayList<BossBullet>();
		int s = BossBullet.STRAI;
		int t = BossBullet.TRACE;
		switch (boss_level) {
		case 1:// boss 1 五散
			bs.add(new BossBullet(x, y + this.height / 3, s, -2, 2));
			bs.add(new BossBullet(x + this.width / 4, y + this.height / 2, s, -1, 2));
			bs.add(new BossBullet(x + this.width * 2 / 4, y + this.height, s, 0, 2));
			bs.add(new BossBullet(x + this.width * 3 / 4, y + this.height / 2, s, 1, 2));
			bs.add(new BossBullet(x + this.width, y + this.height / 3, s, 2, 2));
			break;
		case 2:// boss 2 三 追
			bs.add(new BossBullet(x + this.width / 4, y + this.height / 2, t, 3, 5));
			bs.add(new BossBullet(x + this.width * 2 / 4, y + this.height, t, 3, 5));
			bs.add(new BossBullet(x + this.width * 3 / 4, y + this.height / 2, t, 3, 5));
			break;
		case 3:// boss 3 五 三
			bs.add(new BossBullet(x, y + this.height / 3, s, -2, 2));
			bs.add(new BossBullet(x + this.width / 4, y + this.height / 2, s, -1, 2));
			bs.add(new BossBullet(x + this.width * 2 / 4, y + this.height, s, 0, 2));
			bs.add(new BossBullet(x + this.width * 3 / 4, y + this.height / 2, s, 1, 2));
			bs.add(new BossBullet(x + this.width, y + this.height / 3, s, 2, 2));

			bs.add(new BossBullet(x + this.width / 4, y + this.height / 2, t, 3, 5));
			bs.add(new BossBullet(x + this.width * 2 / 4, y + this.height, t, 3, 5));
			bs.add(new BossBullet(x + this.width * 3 / 4, y + this.height / 2, t, 3, 5));
			break;
		}
		return bs;
	}

	int time = 0;// 控制返回 图片,速率
	int index = 0;// 控制,存货,图片下标
	int deadIndex = 0;// 控制,死亡,图片下标

	/** 返回图片实现动画效果,与死亡动画 */
	public BufferedImage getImage() {
		time++;
		// 控制图片下标,速率
		if (isLife() && time % 15 == 0) {
			index++;
		} else if (isDead() && time % 8 == 0) {
			deadIndex++;
		}

		if (isLife()) {
			return images[index % images.length];
		} else if (isDead()) {
			if (deadIndex == bomImages.length - 1)
				// 下标到达极限,修改状态为可移除
				goRemove();
			if (boss_level == THREE) {
				Score.write(World.hero.score);
				// 最后一关boss死亡,游戏胜利
				World.gameState = World.GAME_VICTORY;
				// 放胜利背景音乐
				World.playBgm(World.bgm_victory, World.PLAY);
				// 使退出按钮可视化
				MyButton.addExit(World.getPanel());
			}
			World.levelTime = 0;// Boss死亡关卡时间归0
			return bomImages[deadIndex % bomImages.length];
		}
		return null;
	}

	/** 重载的血条绘制方式,Boss 的血量有 动态颜色 */
	public void paintRect(Graphics g) {
		// 血量百分比
		life_max = life * 1.0 / maxLife;
		// 血条坐标
		int rectX = this.x;
		int rectY = this.y + this.height;
		// 血条宽高
		int rectWidth = (int) (life_max * this.width);
		int rectHeight = 15;

		g.setColor(Color.MAGENTA);// boss血框颜色
		g.draw3DRect(this.x, this.y + this.height, //
				this.width, rectHeight, true);// 画出血框

		if (life_max >= 0)
			g.setColor(Color.RED);
		if (life_max > 0.33)
			g.setColor(Color.YELLOW);
		if (life_max > 0.66)
			g.setColor(Color.GREEN);
		g.fillRect(rectX, rectY + 1, rectWidth, rectHeight - 1);

	}

	/** 越界判断,Boss 永不越界 */
	public boolean outOfBounds() {
		return false;
	}

}
