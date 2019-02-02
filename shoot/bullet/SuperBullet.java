package bullet;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import core.Flyer;
import core.World;

/** 子弹类,超级类 封装所有子弹-所共同属性,行为 */
public class SuperBullet {

	public static final int LIFE = 0;// 活着
	public static final int REMOVE = 1;// 可移除子弹

	protected int state = LIFE;// 当前状态默认活着

	/** 基本属性 */
	public int x;
	public int y;
	protected int width;
	protected int height;

	public int xCentre;// 中心 x 坐标
	public int yCentre;// 中心 y 坐标

	protected int xSpeed;// x移动速度
	protected int ySpeed;// y移动速度

	public BufferedImage image;// 子弹图片

	/** 画出子彈图片 */
	public void paintObject(Graphics g) {
		g.drawImage(getImage(), x, y, null);
	}

	/** 返回子弹图片 */
	public BufferedImage getImage() {
		return image;
	};

	/** 子弹越界检测 */
	public boolean outOfBounds() {
		if (this.isRemove()) {
			// 死亡单位不考虑越界
			return false;
		}
		return x > World.WIDTH || x < -this.width || //
				y < -this.height || y > World.HEIGHT + this.height;
	};

	/** 单位-构造方式,初始化- 坐标,宽 高 ,最大生命值,初始生命值 , */
	public SuperBullet(int x, int y, int choose) {
		this.x = x;
		this.y = y;
		if (image != null) {
			this.width = image.getWidth();
			this.height = image.getHeight();
		}
		reCentre();
	}

	/** 载入子弹,图片的方法 */
	public static BufferedImage loadImage(String path, final String fileName) {
		path = path.replaceAll("shoot/audio_png/", "");
		try {
			BufferedImage image = ImageIO.read(audio_png.GamePng.class.getResource(path + fileName + ".png"));
			return image;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/** 與 其他單位 的碰撞检测 */
	public boolean myHit(Flyer other) {
		int H = Math.abs(x + width / 2 - other.x - other.width / 2);
		int V = Math.abs(y + height / 2 - other.y - other.height / 2);
		return H - width / 2 - other.width / 2 < 0 //
				&& V - height / 2 - other.height / 2 < 0;
	}

	/** 與 其他子弹 的碰撞检测 */
	public boolean myHit(SuperBullet other) {
		int H = Math.abs(x + width / 2 - other.x - other.width / 2);
		int V = Math.abs(y + height / 2 - other.y - other.height / 2);
		return H - width / 2 - other.width / 2 < 0 //
				&& V - height / 2 - other.height / 2 < 0;
	}

	/** 子弹机直走,或分散走 */
	public void step() {
		y += ySpeed;
		x += xSpeed;
	}

	/** 子弹追踪 */
	public void stepToFlyer(Flyer f) {
		this.xCentre = this.x + this.width / 2;
		this.yCentre = this.y + this.height / 2;
		if (this.xCentre > f.xCentre)
			x -= xSpeed;
		if (this.xCentre < f.xCentre)
			x += xSpeed;
		y += ySpeed;
	}

	/** 重写确定中心坐标 */
	public void reCentre() {
		this.xCentre = this.x + this.width / 2;
		this.yCentre = this.y + this.height / 2;
	}

	/** 修改子彈狀態,為可移除 */
	public void goRemove() {
		state = REMOVE;
	}

	/** 判断单位,是否存活 */
	public boolean isLife() {
		return state == LIFE;
	}

	/** 判断单位是否可移除 */
	public boolean isRemove() {
		return state == REMOVE;
	}

}
