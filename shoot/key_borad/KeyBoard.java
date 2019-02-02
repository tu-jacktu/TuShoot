package key_borad;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

import audio_png.Music;
import button.MyButton;
import button.MyButtonMouse;
import core.World;
import friends.Hero;

//=技能的释放 
//=设置一个 boolean 开关 ,按下 为 true.
//=技能-释放完毕时候,赋予 false 值

/** 封装键盘操作事件 */
public class KeyBoard implements KeyListener {
	private static JPanel panel;

	public static void getKeyControl(JPanel panel) {
		KeyBoard.panel = panel;
		KeyListener key = new KeyBoard();
		panel.addKeyListener(key);
	}

	@Override
	public void keyTyped(KeyEvent e) {

		char key = e.getKeyChar();
		/** 按下空格，改变游戏状态 */
		// 按下 空格后 应當 使相關,按鈕可見,並且調整好位置
		if (World.gameState == World.RUNNING && key == ' ') {
			World.gameState = World.PAUSE;
			/** 重新布局 排行按钮位置 */
			MyButton.but_top.setBounds((World.WIDTH - 221) / 2, 500, MyButton.//
					ic_top.getIconWidth(), MyButton.ic_top.getIconHeight());

			MyButton.addBack(panel, World.SHOW_PAUSE);// 添加返回,按钮
			MyButton.addExit(panel);// 添加退出按钮
			MyButtonMouse.setStartButtonVisible(true);// 使相关按钮可见
			MyButton.but_start.setVisible(false);// 使开始按钮不可见

		} else if (World.gameState == World.PAUSE && World.buttonState == World.SHOW_PAUSE//
				&& key == ' ') {
			World.gameState = World.RUNNING;
			MyButton.but_back.setVisible(false);
			MyButton.but_exit.setVisible(false);
		}

		/** 只有当游戏处于运行 状态,技能才有效 */
		if (World.gameState == World.RUNNING) {
			if ((key == 'a' || key == 'A') && Hero.eleAmout > 0 && Hero.skillEle == false) {
				Hero.skillEle = true;
				Hero.eleAmout--;
				// System.out.println("释放!-超必杀.==万象天引");
				World.showEleTime = 1200;// 显示计时,初始化
				World.canShowEle = true;
				Music.playEffect("ele", "play");
			}
			if ((key == 's' || key == 'S') && Hero.petAmount > 0 && Hero.skillPet == false) {
				Hero.skillPet = true;
				Hero.petAmount--;
				// System.out.println("释放!-超必杀.==神力觉醒");
				World.showPetTime = 1200;// 显示计时,初始化
				World.canShowPet = true;
				Music.playEffect("fire", "play");
			}
		}
	}

	public static int stepDistance = 15;

	@Override
	public void keyPressed(KeyEvent e) {
		/** 键盘控制英雄 移动 */
		if (e.getKeyCode() == KeyEvent.VK_DOWN)
			World.hero.y += stepDistance;
		if (e.getKeyCode() == KeyEvent.VK_UP)
			World.hero.y -= stepDistance;
		if (e.getKeyCode() == KeyEvent.VK_RIGHT)
			World.hero.x += stepDistance;
		if (e.getKeyCode() == KeyEvent.VK_LEFT)
			World.hero.x -= stepDistance;
		Hero h = World.hero;
		if (h.x < 0)
			h.x = 0;
		if (h.x > World.WIDTH - h.width)
			h.x = World.WIDTH - h.width;
		if (h.y < 0)
			h.y = 0;
		if (h.y > World.HEIGHT - h.height)
			h.y = World.HEIGHT - h.height;
	}

	private static int canCheat;

	@Override
	public void keyReleased(KeyEvent e) {
		/** 开启作弊...为测试 或 菜鸟准备...(。・＿・。) */
		int code = e.getKeyChar();
		if (e.getKeyCode() == KeyEvent.VK_1 && canCheat < 3) {
			canCheat++;
		}
		if (canCheat > 1) {
			if ((code == 'p' || code=='P') && Hero.petAmount < 6){
				Hero.petAmount ++;//增加技能 神力
			}
			if ((code == 'e' || code=='E') && Hero.eleAmout < 6)
				Hero.eleAmout ++;//增加技能 雷电
			if ((code == 'l' || code=='L') && //
					World.hero.getLife() < World.hero.getMaxLife())
				World.hero.setLife(World.hero.getMaxLife());//一键满血
			if ((code == 'f' || code=='F') && //一键满火力值
					World.hero.getFire()<2500)
				World.hero.setFire(2500);
		}
	}
}
