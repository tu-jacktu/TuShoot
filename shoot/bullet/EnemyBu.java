package bullet;

import java.awt.image.BufferedImage;

/** 封装所有敌机子彈,属性,行为 */
// 中敌机偶尔发 直线子弹, 大敌机 发 追踪子弹
public class EnemyBu extends SuperBullet {
	// 敌机子弹只有两种,图片
	private static BufferedImage enemyNorBu;
	private static BufferedImage enemyTraBu;
	public static final int MID_STRA_BU = 1;
	public static final int BIG_TRA_BU = 0;
	static {
		enemyNorBu = loadImage//
		("shoot/audio_png/pictures&/enemy/", "enemyNorBu");
		enemyTraBu = loadImage//
		("shoot/audio_png/pictures&/enemy/", "enemyTraBu");
	}

	/** 构造,参数指定,子弹的选择 */
	public EnemyBu(int x, int y, int choose) {
		super(x, y, choose);
		switch (choose) {
		case 1:// 获得参数后,指定,速度,图片
			this.image = enemyNorBu;
			xSpeed = 0;
			ySpeed = 3;
			break;
		case 0:
			this.image = enemyTraBu;
			xSpeed = 2;
			ySpeed = 5;
			break;
		}
		this.width = image.getWidth();
		this.height = image.getHeight();
		this.xCentre = this.x + this.width / 2;
		this.yCentre = this.y + this.height / 2;
	}
	/**	返回一半的宽方便,创建子弹调整位置	*/
	public static int getNor_Width_2(){
		return enemyNorBu.getWidth()/2;
	}
	public static int getTrace_Width_2(){
		return enemyTraBu.getWidth()/2;
	}

}
