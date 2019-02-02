package core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.imageio.ImageIO;

/** 飞行类,超级类 封装所有飞行物-所共同属性,行为 */
public abstract class Flyer {
	public static final int LIFE = 0;// 活着
	public static final int DEAD = 1;// 死亡
	public static final int SET_AWARD = 2;// 产生奖励
	public static final int REMOVE = 3;// 移除(删掉)
	protected int state = LIFE;// 当前状态默认活着

	public int x;
	public int y;
	public int width;
	public int height;

	public int xCentre;// 中心 x 坐标
	public int yCentre;// 中心 y 坐标

	protected int xSpeed;// x移动速度
	protected int ySpeed;// y移动速度

	protected int life;// 当前声明
	protected int maxLife;// 最大生命
	protected double life_max;// 生命比例

	public int score;// 单位所附带分数

	protected static Random ran = new Random();

	public abstract void step();// 抽象移动方法

	public abstract BufferedImage getImage();// 抽象返回图片方法

	public abstract boolean outOfBounds();// 越界检测

	/** 单位-构造方式,初始化- 坐标,宽 高 ,最大生命值,初始生命值 , */
	public Flyer(int width, int height, int x, int y, int maxLife) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		reCentre();
		this.maxLife = maxLife;// 设置最大生命值
		this.life = this.maxLife;// 构造时满血
	}

	/** 重新确定中心坐标 */
	public void reCentre() {
		this.xCentre = this.x + this.width / 2;
		this.yCentre = this.y + this.height / 2;
	}

	/** 载入图片的方法 */
	public static BufferedImage loadImage(String path, String fileName) {
		path = path.replaceAll("shoot/audio_png/pictures&/", "");
		try {
			BufferedImage image = ImageIO.read(audio_png.GamePng.class.getResource("pictures&/" + path + fileName + ".png"));
			return image;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/** 與 其他單位 的碰撞检测 */
	public boolean hit(Flyer other) {
		int x1 = this.x - other.width;
		int x2 = this.x + this.width;
		int y1 = this.y - other.height;
		int y2 = this.y + this.height;
		int x = other.x;
		int y = other.y;
		return x >= x1 && x <= x2 && y >= y1 && y <= y2;
	}

	/** 統一 畫出 血條 的方法 */
	public void paintRect(Graphics g) {
		// 血量百分比
		life_max = life * 1.0 / maxLife;
		// 血条坐标
		int rectX = this.x;
		int rectY = this.y + this.height;
		// 血条宽高
		int rectWidth = (int) (life_max * this.width);
		int rectHeight = 10;

		g.setColor(Color.yellow);
		g.draw3DRect(this.x, this.y + this.height, this.width, rectHeight, true);// 画出血框
		g.setColor(Color.RED);
		g.fillRect(rectX, rectY + 1, rectWidth, rectHeight - 1);

	}

	/** 画出飞行物图片 */
	public void paintObject(Graphics g) {
		try {
			g.drawImage(getImage(), x, y, null);
		} catch (Exception e) {
			System.out.println("空指针异常,找不到该图片.in flyer");
			e.printStackTrace();
		}
	}

	/** 修改状态为死亡 */
	public void goDead() {
		state = DEAD;
	}

	/** 修改状态为可删除 */
	public void goRemove() {
		state = REMOVE;
	}

	/** 判断单位,是否存活 */
	public boolean isLife() {
		return state == LIFE;
	}

	/** 判断单位是否,死亡 */
	public boolean isDead() {
		return state == DEAD;
	}

	/** 判断单位是否,可以产生奖励,状态 */
	public boolean isSet_Award() {
		return state == SET_AWARD;
	}

	/** 判断单位是否可移除 */
	public boolean isRemove() {
		return state == REMOVE;
	}

	/** 设置宽高 */
	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

}
