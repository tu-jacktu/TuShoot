package button;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import core.World;
import friends.Hero;
import score.Score;

/**
 * 封装按钮的对应鼠标事件
 */
public class MyButtonMouse extends MouseAdapter {
	/** 调用按钮事件,游戏,主类 需要的的属性 */
	private World panel;
	private String button;// 当前的 按钮

	/** 用于辨别调用的 具体 按钮 */
	public static final String START = "start";// 开始按钮,的说明
	public static final String TOP = "top";
	public static final String ABOUT = "about";
	public static final String HELP = "help";
	public static final String BACK = "back";
	public static final String EXIT = "exit";

	/** 构造中,传入 按钮 说明(指定按钮),和主程序,用户修改游戏状态 */
	public MyButtonMouse(String buttonState, JPanel panel) {
		this.button = buttonState;
		if (panel instanceof World) {
			this.panel = (World) panel;
		} else {
			// System.out.println("类型不匹配.");
		}
	}

	/** 按钮单击事件 */
	public void mouseClicked(MouseEvent e) {
		switch (button) {
		case MyButtonMouse.START:// 当按钮是,开始
			// 清除上一个游戏的垃圾数据,防止堆栈溢出
			System.gc();
			setStartButtonVisible(false);// 相关按钮不可见
			// 播放BGM
			World.playBgm(World.bgm1, World.LOOP);
			/** 可以-播放失败背景音乐 */
			World.canPlayDefeated = true;
			/** 英雄相关属性,初始化 */
			World.canClear = true;
			World.hero = new Hero();
			World.hero.setLife(World.hero.getMaxLife());
			Hero.eleAmout = 1;
			Hero.petAmount = 1;
			Hero.helpAmout = 1;

			Hero.canHit = true;
			Hero.skillEle = false;
			Hero.skillPet = false;
			Hero.skillHelp = false;
			/** 关卡初始化 */
			World.gameLevel = 1;

			/** 游戏运行 */
			World.gameState = World.RUNNING;
			break;
		case MyButtonMouse.TOP:// 当按钮是,排行
			/** 读出排行榜信息,并设置 */
			Score.setScoreList();
			// 单击排行--开始按钮不可见--显示排行--添加返回按钮
			setStartButtonVisible(false);
			MyButton.addBack(panel, World.TOP);// 添加返回按钮
			break;
		case MyButtonMouse.ABOUT:// 当按钮是,关于
			// 单击关于--开始按钮不可见--显示关于--添加返回按钮
			setStartButtonVisible(false);
			MyButton.addBack(panel, World.ABOUT);// 添加返回按钮
			break;
		case MyButtonMouse.HELP:// 当按钮是,帮助
			// 单击帮助--帮助按钮不可见--显示帮助--添加返回按钮
			setStartButtonVisible(false);
			MyButton.addBack(panel, World.HELP);// 添加返回按钮
			break;
		case MyButtonMouse.BACK:// 当按钮是,返回
			// 单击返回 返回按钮不可见 显示开始状态
			MyButton.but_back.setVisible(false);// 隐藏返回按钮

			if (World.gameState == World.PAUSE && // 暂停时的返回
					World.buttonState == World.SHOW_PAUSE) {
				World.gameState = World.RUNNING;
			} else if (World.gameState == World.START) {// 开始 等待-的返回
				setStartButtonVisible(true);
				World.buttonState = World.WAIT;// 改界面状态为 ,等待
			} else {
				World.buttonState = World.SHOW_PAUSE;// 游戏中,进入 其他界面 ,返回的功能
				setStartButtonVisible(true);
				MyButton.but_start.setVisible(false);
				MyButton.but_back.setVisible(true);
			}
			// 暂停时,查看排行的返回按钮重新布局
			if (World.gameState == World.PAUSE && World.buttonState == World.SHOW_PAUSE) {
				MyButton.but_back.setBounds//
				((World.WIDTH - 141) / 2, 600, MyButton.ic_back.getIconWidth(), MyButton.ic_back.getIconHeight());
			}
			break;
		case MyButtonMouse.EXIT:// 当按钮是,退出
			// 排行分数植入
			if (World.gameState == World.PAUSE) {
				Score.write(World.hero.score);
			}
			// 单击退出,返回到开始界面
//			Music.playBgm("before_game", "play");
			World.playBgm(World.before, World.PLAY);
			MyButton.but_exit.setVisible(false);// 隐藏退出按钮

			World.gameState = World.START;// 退出了所有一切重新开始
			World.buttonState = World.WAIT;// 退出了所有一切重新开始

			/** 排行的重新布局 */
			MyButton.but_top.setBounds((World.WIDTH - 221) / 2, 350, MyButton.ic_top.getIconWidth(), //
					MyButton.ic_top.getIconHeight());
			setStartButtonVisible(true);
			break;
		}
	}

	/** 准备状态,开始按钮,设置 */
	public static void setStartButtonVisible(boolean flag) {
		MyButton.but_top.setVisible(flag);
		MyButton.but_about.setVisible(flag);
		MyButton.but_start.setVisible(flag);
		MyButton.but_help.setVisible(flag);
	}
}
