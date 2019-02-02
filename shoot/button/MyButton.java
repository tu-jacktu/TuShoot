package button;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import audio_png.GamePng;
import core.World;
import score.Score;

/*
 *  开始界面 按钮: 排行  关于  开始 帮助 
 *  暂停界面 按钮: 返回  退出  帮助
 *  
 *  排行 帮助 关于 界面均有退出 按钮
 */
/** 封装按钮相关的事件,和按钮图片 */
public class MyButton extends Component {
	/** 序列号 */
	private static final long serialVersionUID = 1L;
	/** 调用按钮事件,游戏,主类 需要的的属性 */
	// 按钮
	public static JButton but_about;
	public static JButton but_back;
	public static JButton but_exit;
	public static JButton but_help;
	public static JButton but_start;
	public static JButton but_top;
	// 按钮的图标
	public static Icon ic_about;
	public static Icon ic_back;
	public static Icon ic_exit;
	public static Icon ic_help;
	public static Icon ic_start;
	public static Icon ic_top;
	// 按钮的图片
	public static BufferedImage but_im_about;
	public static BufferedImage but_im_back;
	public static BufferedImage but_im_exit;
	public static BufferedImage but_im_help;
	public static BufferedImage but_im_start;
	public static BufferedImage but_im_top;

	/** 加载,按钮按钮图片 */
	static {
		try {
			// but_im_about = ImageIO.read(getFile("about"));
			// but_im_back = ImageIO.read(getFile("back"));
			// but_im_exit = ImageIO.read(getFile("exit"));
			// but_im_help = ImageIO.read(getFile("help"));
			// but_im_start = ImageIO.read(getFile("start"));
			// but_im_top = ImageIO.read(getFile("top"));
			but_im_about = loadImage("about");
			but_im_back = loadImage("back");
			but_im_exit = loadImage("exit");
			but_im_help = loadImage("help");
			but_im_start = loadImage("start");
			but_im_top = loadImage("top");
		} catch (Exception e) {
			e.printStackTrace();
		}
		/** 加载,按钮图标 */
		try {
			ic_about = new ImageIcon(but_im_about);
			ic_back = new ImageIcon(but_im_back);
			ic_exit = new ImageIcon(but_im_exit);
			ic_help = new ImageIcon(but_im_help);
			ic_start = new ImageIcon(but_im_start);
			ic_top = new ImageIcon(but_im_top);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 初始化 按钮 */
	static {
		but_about = new JButton(ic_about);
		but_back = new JButton(ic_back);
		but_exit = new JButton(ic_exit);
		but_help = new JButton(ic_help);
		but_start = new JButton(ic_start);
		but_top = new JButton(ic_top);
		// 使按钮透明化
		but_about.setContentAreaFilled(false);
		but_back.setContentAreaFilled(false);
		but_exit.setContentAreaFilled(false);
		but_help.setContentAreaFilled(false);
		but_start.setContentAreaFilled(false);
		but_top.setContentAreaFilled(false);
	}

	/** 需要主类调用的,绘制事件 */
	public static void paintButtonEvent(Graphics g) {
		// 暂停 ,显示 一系列 相关 按钮
		if (World.gameState == World.PAUSE && World.buttonState == World.SHOW_PAUSE) {
			g.drawImage(GamePng.pause, 0, 0, null);
			g.drawImage(but_im_about, but_about.getX(), but_about.getY(), null);
			g.drawImage(but_im_back, but_back.getX(), but_back.getY(), null);
			g.drawImage(but_im_help, but_help.getX(), but_help.getY(), null);
			g.drawImage(but_im_top, but_top.getX(), but_top.getY(), null);
			g.drawImage(but_im_exit, but_exit.getX(), but_exit.getY(), null);
		}
		// 暂停 胜利 失败都会显示退出按钮
		if (World.gameState == World.GAME_DEFEATED || World.gameState == World.GAME_VICTORY) {
			g.drawImage(but_im_exit, but_exit.getX(), but_exit.getY(), null);
		}
		// 界面状态是,开始 并且,游戏状态是开始.
		if (World.buttonState == World.WAIT && //
				World.gameState == World.START) {
			g.drawImage(GamePng.start, 0, 0, null);// 绘制背景
			g.drawImage(but_im_about, but_about.getX(), but_about.getY(), null);
			g.drawImage(but_im_help, but_help.getX(), but_help.getY(), null);
			g.drawImage(but_im_start, but_start.getX(), but_start.getY(), null);
			g.drawImage(but_im_top, but_top.getX(), but_top.getY(), null);
		}
		if (World.buttonState == World.TOP) {// 排行状态
			g.drawImage(GamePng.other_bg, 0, 0, null);// 绘制背景
			g.drawImage(GamePng.top, (World.WIDTH - GamePng.top.getWidth()) / 2//
					, (World.HEIGHT - GamePng.top.getHeight()) / 2, null);// 绘制排行图片
			g.drawImage(but_im_back, but_back.getX(), but_back.getY(), null);// 绘制按钮
			/** 排行榜 */
			ArrayList<Integer> list = Score.getScoreList();// 获取排行分数
			g.setFont(new Font("楷体", Font.PLAIN, 50));
			int limit = 0;//限制分数的个数
			if (list.size() < 5) {
				limit = list.size();
			} else {
				limit = 5;
			}
			for (int i = 0; i < limit; i++) {
				switch (i) {
				case 0:
					g.setColor(Color.ORANGE);
					break;
				case 1:
					g.setColor(Color.RED);
					break;
				case 2:
					g.setColor(Color.GREEN);
					break;
				default:
					g.setColor(Color.WHITE);
					break;
				}
				g.drawString(list.get(i) + "", 350, //
						85 + (i + 1) * 98);
			}
		}
		if (World.buttonState == World.HELP) {// 帮助状态
			g.drawImage(GamePng.other_bg, 0, 0, null);// 绘制背景
			g.drawImage(GamePng.help, 0, 0, null);// 绘制帮助
			g.drawImage(but_im_back, but_back.getX(), but_back.getY(), null);
		}
		if (World.buttonState == World.ABOUT) {// 关于状态
			g.drawImage(GamePng.other_bg, 0, 0, null);// 绘制背景
			g.drawImage(GamePng.about, 0, 0, null);// 绘制关于
			g.drawImage(but_im_back, but_back.getX(), but_back.getY(), null);
		}
	}

	/** 添加返回按钮,及其事件 */
	private static MyButtonMouse b;

	/** 添加,返回 按钮 */
	public static void addBack(JPanel panel, int new_buttonState) {
		World.buttonState = new_buttonState;// 修改 游戏等待状态,为指定状态
		if (b == null) {
			b = new MyButtonMouse(MyButtonMouse.BACK, panel);
			but_back.addMouseListener(b);// 返回按钮事件监听
			panel.add(but_back);// 返回按钮到面板.
		}

		but_back.setVisible(true);// 返回按钮可视化
		// 按钮 布局
		if (World.gameState == World.PAUSE) {
			but_back.setBounds((World.WIDTH - 141) / 2, 600, ic_back.getIconWidth(), ic_back.getIconHeight());
		} else {
			but_back.setBounds((World.WIDTH - 141) / 2, 700, ic_back.getIconWidth(), ic_back.getIconHeight());
		}
		if (World.buttonState == World.TOP) {
			but_back.setBounds((World.WIDTH - 141) / 2 + 130, 700, ic_back.getIconWidth(), ic_back.getIconHeight());
		}
	}

	/** 添加 退出 按钮,及其事件 */
	private static MyButtonMouse e;

	public static void addExit(JPanel panel) {
		if (e == null) {
			e = new MyButtonMouse(MyButtonMouse.EXIT, panel);
			but_exit.addMouseListener(e);// 退出按钮事件监听
			/** 退出按钮布局 */
			panel.add(but_exit);// 退出按钮到面板.
		}
		but_exit.setBounds(540, 220, ic_exit.getIconWidth(), ic_exit.getIconHeight());
		but_exit.setVisible(true);// 退出按钮可视化
	}

	/** 添加开始按钮,及其事件 */
	public static void addStartButton(JPanel panel) {
		panel.setLayout(null);// 清除面板默认布局
		/** 排行 */
		but_top.setBounds((World.WIDTH - 221) / 2, 350, ic_top.getIconWidth(), ic_top.getIconHeight());
		/** 帮助 */
		but_help.setBounds(World.WIDTH - 141 - 50, 650, ic_help.getIconWidth(), ic_help.getIconHeight());
		/** 开始 */
		but_start.setBounds((World.WIDTH - 190) / 2, 650, ic_start.getIconWidth(), ic_start.getIconHeight());
		/** 关于 */
		but_about.setBounds(50, 650, ic_about.getIconWidth(), ic_about.getIconHeight());

		/** 添加 鼠标单击 按钮 触发事件 */

		// 排行榜的单击事件
		but_top.addMouseListener(new MyButtonMouse(MyButtonMouse.TOP, panel));
		// 帮助的单击事件
		but_help.addMouseListener(new MyButtonMouse(MyButtonMouse.HELP, panel));
		// 开始的单击事件
		but_start.addMouseListener(new MyButtonMouse(MyButtonMouse.START, panel));
		// 关于的单击事件
		but_about.addMouseListener(new MyButtonMouse(MyButtonMouse.ABOUT, panel));

		/** 把几个按钮 添加到 面板 */
		panel.add(but_top);
		panel.add(but_help);
		panel.add(but_start);
		panel.add(but_about);

	}

	/** 私有的返回 图片 方法 */
	private static BufferedImage loadImage(final String path) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(MyButton.class.getResource("button-p/button_" + path + ".png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return image;
	}
}
