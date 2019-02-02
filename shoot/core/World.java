package core;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;

import audio_png.GamePng;
import audio_png.Music;
import bullet.BossBullet;
import bullet.EnemyBu;
import bullet.HeroBullet;
import bullet.SuperBullet;
import bullet.WingBulletF;
import bullet.WingBulletR;
import button.MyButton;
import enemy.BigPlane;
import enemy.BlockRay;
import enemy.Boss;
import enemy.MidPlane;
import enemy.Plane;
import enemy.Reward;
import enemy.Rusher;
import friends.Help;
import friends.Hero;
import friends.Skill_ele;
import friends.Wings;
import key_borad.KeyBoard;
import score.Score;

/** 游戏的主体类,调动所有的类进行游戏 */
public class World extends JPanel {
	/** 自定义的序列号 */
	private static final long serialVersionUID = 1L;

	/** 调用按钮事件,游戏,主类 需要的的属性 */
	private static World panel;

	// 游戏界面状态,方便绘图调用
	public static int buttonState;
	/** 用于显示当前游戏 的按 钮状态,的常亮 */
	public static final int WAIT = 0;
	public static final int TOP = 1;
	public static final int HELP = 2;
	public static final int ABOUT = 3;
	public static final int SHOW_PAUSE = 4;

	static Random ran = new Random();

	/** 游戏宽高 */
	public static final int WIDTH = 666;
	public static final int HEIGHT = 1000;

	/** 游戏状态常量 */
	public static final int START = 0;
	public static final int RUNNING = 1;
	public static final int PAUSE = 2;
	public static final int GAME_DEFEATED = 3;
	public static final int GAME_VICTORY = 4;

	public static int gameState = START; // 游戏状态

	public static int gameLevel = 1; // 游戏关卡,默认第 一 关

	public static int gameTime;// 游戏所经历时间
	public static int levelTime;// 关卡所经历时间

	public static int eX;// 获取鼠标坐标
	public static int eY;

	/** Boss 入场时间固定的 */
	private final static int BOSS_ENTER_TIME = 1000 * 60;

	/** 技能显示图 */
	public static boolean canShowEle = false;
	public static boolean canShowPet = false;
	/** 技能图限制时间 */
	public static int showEleTime = 1200;
	public static int showPetTime = 1200;
	/** 是否可以清空游戏数据 */
	public static boolean canClear = true;

	// 天空
	Sky sky = new Sky();
	// 英雄
	public static Hero hero = new Hero();
	// 追踪的宠物
	Wings petF = new Wings(1);
	// 环绕的宠物
	Wings petR = new Wings(0);

	// 技能单位
	List<Help> helps = new ArrayList<>();// 机甲援军 集合
	List<Skill_ele> eles = new ArrayList<>();// 大招 闪电集合

	// 常规敌人集合,大中小 敌机
	List<Flyer> enemies = new ArrayList<Flyer>();
	// Boss 机
	Boss boss = null;
	// 障碍物集合
	List<Flyer> block = new ArrayList<>();
	// 奖励集合
	List<Reward> rewards = new ArrayList<>();

	// 英雄子弹集合
	List<HeroBullet> heroBullets_S = new ArrayList<>();// 直线的
	List<HeroBullet> heroBullets_T = new ArrayList<>();// 追踪的
	// 僚机子弹集合
	List<WingBulletF> wingBullets_F = new ArrayList<>();// 固定的僚机子弹
	List<WingBulletR> wingBullets_R = new ArrayList<>();// 环绕的僚机子弹

	// 敌人子弹集合
	List<EnemyBu> enemyBullets_S = new ArrayList<>();// 直线的敌机子弹,mid Plean
	List<EnemyBu> enemyBullets_T = new ArrayList<>();// 追踪的敌机子弹,big Plean
	List<BossBullet> bossBullets_S = new ArrayList<>();// 直线 Boss 子弹集合
	List<BossBullet> bossBullets_T = new ArrayList<>();// 追踪 Boss 子弹集合

	public static Random ra = new Random();

	public static AudioClip before;//

	public static AudioClip bgm1;
	public static AudioClip bgm2;
	public static AudioClip bgm3;

	public static AudioClip bgm_boss1;
	public static AudioClip bgm_boss2;
	public static AudioClip bgm_boss3;

	public static AudioClip defeated;// 凉凉
	public static AudioClip bgm_victory;// 胜利
	/** 这些较大的文件,移到主类里加载,防止游戏卡顿...囧 */
	static {
		// System.out.println("正在加载游戏资源...");
		before = getBgm("before_game");

		bgm1 = getBgm("bgm1");
		bgm2 = getBgm("bgm2");
		bgm3 = getBgm("bgm3");

		bgm_boss1 = getBgm("bgm_boss1");
		bgm_boss2 = getBgm("bgm_boss2");
		bgm_boss3 = getBgm("bgm_boss3");

		defeated = getBgm("bgm_defeated");
		bgm_victory = getBgm("bgm_victory");
	}

	private static AudioClip tempAudio;// 临时音频,用于停止原bmg

	private static AudioClip getBgm(String BgmName) {
		AudioClip music = null;
		try {
			music = Applet.newAudioClip//
			(Music.class.getResource("Bgm&Effec/" + BgmName + ".wav"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return music;
	}

	public static final int PLAY = 1;
	public static final int LOOP = 2;

	public static void playBgm(AudioClip bgm, int style) {
		if (tempAudio != null) {
			tempAudio.stop();// 停止原bgm
		}
		tempAudio = bgm;
		switch (style) {
		case World.PLAY:
			tempAudio.play();
			break;
		case World.LOOP:
			tempAudio.loop();
			break;
		}
	}

	/** UI的-核心控制 */
	public void paint(Graphics g) {// 重写paint 方法
		if (World.gameState != World.START) {
			/** 绘制天空 */
			sky.paintObject(g);
			paintHero(g);// 英雄绘制,包括护盾
			petF.paintObject(g);// 固定宠物绘制
			petR.paintObject(g);// 环绕宠物绘制

			/** 单位的相关绘制,包含血条 */
			try {
				paintEnemies(g);// 大众小敌机的绘制
			} catch (Exception e) {
				// System.out.println("敌人绘制...");
			}
			try {
				paintblock(g);// 障碍物的绘制
			} catch (Exception e) {
				// System.out.println("障碍绘制...");
			}
			paintBoss(g);// boss的绘制
			try {
				paintHelps(g);// 援军的绘制
			} catch (Exception e) {
				// System.out.println("援军的绘制...");
			}
			try {
				paintEles(g);// 闪电的绘制
			} catch (Exception e) {
				// System.out.println("闪电的绘制...");
			}
			try {
				paintReward(g);// 奖励 的绘制
			} catch (Exception e) {
				// System.out.println("奖励的绘制...");
			}
			/** 子弹的相关绘制 */
			try {
				paintHeroBullets_S(g);// 英雄 直线子弹的绘制
			} catch (Exception e) {
				// System.out.println("英雄直线子弹绘制...");
			}
			try {
				paintHeroBullets_T(g);// 英雄 追踪子弹的绘制
			} catch (Exception e) {
				// System.out.println("英雄追踪子弹的绘制...");
			}
			try {
				paintWingBullets_F(g);// 固定 僚机的子弹绘制
			} catch (Exception e) {
				// System.out.println("固定僚机子弹绘制...");
			}
			try {
				paintWingBullets_R(g);// 环绕 僚机的子弹绘制
			} catch (Exception e) {
				// System.out.println("环绕僚机子弹绘制...");
			}
			try {
				paintEnemyBullets_S(g);// 敌人追踪子弹的绘制
			} catch (Exception e) {
				// System.out.println("敌人追踪子弹的绘制...");
			}
			try {
				paintEnemyBullets_T(g);// 敌人直线子弹的绘制
			} catch (Exception e) {
				// System.out.println("敌人直线子弹绘制...");
			}
			try {
				paintBossBullets_S(g);// boss直线子弹的绘制
			} catch (Exception e) {
				// System.out.println("boss直线子弹的绘制...");
			}
			try {
				paintBossBullets_T(g);// boss追踪子弹的绘制
			} catch (Exception e) {
				// System.out.println("boss直线追踪的绘制...");
			}

			hero.paintRect(g);// 英雄的血条绘制

			paintScore(g);// 绘制分数,火力

			paintSkill_Boss_Even(g);// 技能 Boss 相关图片

			paintShowSkill(g);// 释放技能的图片,比如神力觉醒

			paintSkill_icons(g);// 英雄技能图标的绘制

			changeGameLevel(g);// 切换游戏关卡的图片

			paintGameOver(g);// 游戏失败或者胜利的绘制
		}

		/** 按钮事件相关绘制 */
		MyButton.paintButtonEvent(g);

	}

	/** 释放技能时候,显示的图 */
	private void paintShowSkill(Graphics g) {
		// 雷电技能图片
		if (World.canShowEle) {
			g.drawImage(GamePng.startEle, 0, 0, null);
		}
		// 宠物技能图片
		if (World.canShowPet) {
			g.drawImage(GamePng.startPet, 0, 0, null);
		}
		World.showEleTime -= 10;// 显示计时
		World.showPetTime -= 10;
		if (showEleTime <= 0) {
			canShowEle = false;
		}
		if (showPetTime <= 0) {
			canShowPet = false;
		}
		// 援军到来,上援军图
		if (helpContinueTime > 3 * 1000 && Hero.skillHelp && //
				World.gameState != World.GAME_DEFEATED) {
			g.drawImage(GamePng.friend_arrive, 0, World.HEIGHT / 4, null);
		}
	}

	public static int hero_protected = 3 * 100;

	/** 英雄及其护盾的绘制 */
	private void paintHero(Graphics g) {
		hero.paintObject(g);
		if (!Hero.canHit) {
			hero_protected -= 10;
			g.drawImage(GamePng.getProtectedImage(), hero.x + 10, hero.y - 20, null);
		}
		if (hero_protected <= 0) {
			Hero.canHit = true;
		}
	}

	/** 胜利或者失败相关绘制 -同时清空游戏数据 */
	private void paintGameOver(Graphics g) {
		if (gameState == World.GAME_DEFEATED) {
			g.drawImage(GamePng.defeated, 0, 0, null);
			showFinallyScore(g);// 显示分数
			clearGame();
		} else if (gameState == World.GAME_VICTORY) {
			g.drawImage(GamePng.victory, 0, 0, null);
			showFinallyScore(g);// 显示分数
			clearGame();
		}
	}

	/** 显示最终分数的方法 */
	private void showFinallyScore(Graphics g) {
		if (gameState == World.GAME_DEFEATED) {
			g.setColor(Color.GREEN);
		} else if (gameState == World.GAME_VICTORY) {
			g.setColor(Color.RED);
		}
		g.setFont(new Font("楷体", Font.BOLD, 30));
		g.drawString("本次得分: ", 180, 300);
		g.setFont(new Font("楷体", Font.BOLD, 40));
		g.drawString(hero.score + "!", 330, 300);
	}

	/** 清空游戏数据 */
	public void clearGame() {
		gameTime = 0;
		levelTime = 0;
		boss = null;
		petF = new Wings(1);
		petR = new Wings(0);
		helps.clear();
		eles.clear();
		enemies.clear();
		block.clear();
		rewards.clear();
		heroBullets_S.clear();
		heroBullets_T.clear();
		wingBullets_F.clear();
		wingBullets_R.clear();
		enemyBullets_S.clear();
		enemyBullets_T.clear();
		bossBullets_S.clear();
		bossBullets_T.clear();
	}

	/** 英雄技能图标绘制 */
	private void paintSkill_icons(Graphics g) {
		g.drawImage(GamePng.iconEle, 0, 700, null);// 闪电
		g.drawImage(GamePng.iconPet, World.WIDTH - GamePng.iconPet.getWidth() - 15, //
				700, null);// 神力

		g.setFont(new Font("楷体", Font.BOLD, 25));
		g.setColor(Color.WHITE);
		g.drawString("x" + Hero.eleAmout, 30, 720);// 雷电绘制
		g.drawString("x" + Hero.petAmount, 600, 720);// 神力绘制

	}

	/** 技能和boss相关图片 boss来袭 警告 */
	public static boolean canPlayWarn;

	private void paintSkill_Boss_Even(Graphics g) {
		// boss 到来前 2s 显示警告
		if (levelTime < BOSS_ENTER_TIME && levelTime > BOSS_ENTER_TIME - 2000) {
			g.drawImage(GamePng.warn, 0, 0, null);
		}
		if (levelTime > BOSS_ENTER_TIME + 100 && canPlayWarn) {
			Music.playEffect("warn", "play");
			canPlayWarn = false;// 播放警告 为false
		}
		if (levelTime < 1000 && boss == null) {
			canPlayWarn = true;// 开启可以播放 警告
		}
	}

	/** 分数和火力的绘制 */
	private void paintScore(Graphics g) {
		// 分数的绘制
		g.setFont(new Font("楷体", Font.PLAIN, 26));
		g.setColor(Color.GREEN);
		g.drawString("积分:" + hero.score + "", 510, 27);
		// 火力值的绘制
		g.setFont(new Font("楷体", Font.PLAIN, 20));
		g.setColor(Color.WHITE);
		g.drawString("火力值" + "", 5, 680);
		g.setColor(Color.RED);
		g.drawString("  " + hero.getFire() + "", 5, 700);
	}

	/** 关卡切换图 */
	static boolean HeroCanMove = true;

	private void changeGameLevel(Graphics g) {
		if (levelTime < 800 && gameLevel != 1) {
			g.drawImage(GamePng.changeLevel, 0, 0, null);
			HeroCanMove = false;
		} else {
			HeroCanMove = true;
		}
	}

	/** Boss 追踪子弹的绘制 */
	private void paintBossBullets_T(Graphics g) {
		for (BossBullet bus : bossBullets_T) {
			bus.paintObject(g);
		}
	}

	/** Boss 直线子弹的绘制 */
	private void paintBossBullets_S(Graphics g) {
		for (BossBullet bus : bossBullets_S) {
			bus.paintObject(g);
		}
	}

	/** 常规敌人追踪子弹的绘制 */
	private void paintEnemyBullets_T(Graphics g) {
		for (EnemyBu b : enemyBullets_T) {
			b.paintObject(g);
		}
	}

	/** 常规敌人直线子弹的绘制 */
	private void paintEnemyBullets_S(Graphics g) {
		for (EnemyBu b : enemyBullets_S) {
			b.paintObject(g);
		}

	}

	/** 环绕僚机的子弹绘制 */
	private void paintWingBullets_R(Graphics g) {
		for (WingBulletR bu : wingBullets_R) {
			bu.paintObject(g);
		}
	}

	/** 固定僚机的子弹绘制 */
	private void paintWingBullets_F(Graphics g) {
		for (WingBulletF bu : wingBullets_F) {
			bu.paintObject(g);
		}
	}

	/** 英雄追踪子弹的绘制 */
	private void paintHeroBullets_T(Graphics g) {
		for (HeroBullet bu : heroBullets_T) {
			bu.paintObject(g);
		}
	}

	/** 英雄 直线子弹的绘制 */
	private void paintHeroBullets_S(Graphics g) {
		for (HeroBullet bu : heroBullets_S) {
			bu.paintObject(g);
		}
	}

	/** 奖励绘制 */
	private void paintReward(Graphics g) {
		for (Reward re : rewards) {
			re.paintObject(g);
		}
	}

	/** 障碍绘制 */
	private void paintblock(Graphics g) {
		for (Flyer flyer : block) {
			flyer.paintObject(g);
		}
	}

	/** Boss的绘制 */
	private void paintBoss(Graphics g) {
		if (boss != null) {
			boss.paintObject(g);
			if (boss != null && boss.isLife())
				boss.paintRect(g);
		}
	}

	/** 常规敌人的绘制 */
	private void paintEnemies(Graphics g) {
		for (Flyer en : enemies) {
			en.paintObject(g);
			// 如果此 常规敌人 不是小敌机 并且非死亡状态 画出其血条
			if (!(en instanceof Plane) && !en.isDead()) {
				en.paintRect(g);
			}
		}
	}

	/** 大招闪电的绘制 */
	private void paintEles(Graphics g) {
		for (Skill_ele ele : eles) {
			ele.paintObject(g);
		}
	}

	/** 援军的绘制 */
	private void paintHelps(Graphics g) {
		for (Help h : helps) {
			h.paintObject(g);
		}
	}

	/** 核心-动作事件执行 */
	public void action() {
		playBgm(before, World.PLAY);
		MouseAdapter l = new MouseAdapter() {
			/** 鼠标事件 */
			@Override
			public void mouseMoved(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				eX = x;
				eY = y;
				/** 只有在游戏运行时,英雄才会随着鼠标移动 */
				if (gameState == RUNNING && HeroCanMove) {
					hero.moveTo(x, y);
					hero.xCentre = hero.x + hero.width / 2;
					hero.yCentre = hero.y + hero.height / 2;
				}
			}

		};
		this.addMouseMotionListener(l);// 支持,移动事件
		this.addMouseListener(l);// 支持单击事件,移入 移出

		Timer timer = new Timer();
		/** 事件核心 调用区 */
		timer.schedule(new TimerTask() {
			int runTime = 0;

			public void run() {
				runTime++;
				if (canClear) {// 清理游戏
					clearGame();
					canClear = false;
				}
				if (World.gameState == World.RUNNING) {
					gameTime++;
					levelTime++;// 关卡时间,boss死亡归0
					if (gameTime % 10 == 0) {
						enemyEnter();// 大中小敌机出场
						blockEnter();// 障碍入场
						bossEnter();// boss 入场
						helpEnter();// 援军入场
					}
					skill_ele_Enter();// 技能入场

					sky.step();// 天空移动
					enemyStep();// 大中小敌机移动
					blockStep();// 障碍移动
					BossStep();// boss 移动
					helpStep();// 援军移动
					rewardStep();// 奖励移动

					friendsShoot();// 友軍射擊-英雄-僚机
					enemiesShoot();// 敵人射擊
					BossShoot();// boss 射擊

					petR.stepRound(hero);// 环绕 宠物移动
					petF.stepTrace(hero);// 固定 宠物移动

					friendsBulletsStep();// 友軍-英雄-僚机 子弹移动
					enemiesBulletsStep();// 敵人子弹移动
					BossBulletsStep();// boss 子弹移动
					// 英雄被撞击,减命 或者 获取奖励,和子弹间的撞击
					heroBang_bulletsBang();

					skill_ele_Remove();// 大招闪电按时移除
					// 删除越界或可移除敵人或boss或奖励,
					enemyutOfBounds_remove();
					friendsOutOfBounds_remove();// 删除越界或可移除友軍-援军

					friendsBulletsOutOfBounds_remove();// 删除越界 或可移除状态 友軍子彈
					enemyBulletsOutOfBounds_remove();// 删除越界 或可移除状态 敵人子彈

					/** 敌人碰撞事件,撞击减命 ,生命为0狗带-同时生成奖励 */
					enemyBang();

					skillBang();// 闪电 或 援军 击中 单位,减少其 生命

					checkGameOver();// 检测游戏 胜利 或 失败

				}
				if (runTime % 10 == 0) {
					try {
						repaint();
					} catch (Exception e) {
						System.out.println("repaint()...");
					}
				}
			}
		}, 1, 1);
	}

	/** 奖励移动 */
	int rewardStepTime = 0;

	private void rewardStep() {
		rewardStepTime++;
		if (rewardStepTime % 10 == 0) {
			for (Reward re : rewards) {
				re.stepToHero(hero);
			}
		}
	}

	/** Boss移动的方法 */
	private void BossStep() {
		if (boss != null) {
			boss.step();
		}
	}

	/** 援军 或 闪电 击中单位 */
	int skillBangTime = 0;

	private void skillBang() {
		skillBangTime++;
		// 临时敌人集合
		List<Flyer> allEnemy = getAllEnemy();
		if (skillBangTime % 300 == 0) {
			for (Flyer ele : eles) {
				for (Flyer en : allEnemy) {
					if (en.hit(ele)) {// 敌人与闪电的碰撞检测
						en.life--;
					}
				}
			}
		}
		if (skillBangTime % 300 == 0) {
			for (Flyer help : helps) {
				for (Flyer en : allEnemy) {
					if (en.hit(help)) {// 敌人与援军的碰撞检测
						en.life--;
					}
				}
			}
		}
	}

	int BossShootTime = 0;

	/** Boss 射击 */
	private void BossShoot() {
		BossShootTime++;
		if (boss != null && BossShootTime % (1600 / gameLevel) == 0) {
			// 临时 Boss 子弹集合
			ArrayList<BossBullet> temp = boss.shoot();
			for (BossBullet bos : temp) {
				if (bos.isTrace()) {// 判断是否追踪子弹
					bossBullets_T.add(bos);
				} else {
					bossBullets_S.add(bos);
				}
			}
		}
	}

	int bossBulletsStepTime = 0;

	/** Boss 子弹移动 */
	private void BossBulletsStep() {
		bossBulletsStepTime++;
		if (bossBulletsStepTime % 8 == 0) {
			for (BossBullet bu : bossBullets_S) {
				bu.step();// Boss 的直线子弹 移动
			}
			for (BossBullet bu : bossBullets_T) {
				bu.stepToFlyer(hero); // Boss 的追踪 子弹 移动
			}
		}
	}

	int enemiesBulletsStepTime = 0;

	/** 敌人 子弹移动 */
	private void enemiesBulletsStep() {
		enemiesBulletsStepTime++;
		if (enemiesBulletsStepTime % 10 == 0) {
			for (EnemyBu b : enemyBullets_S) {
				b.step();// 敌人 直线 子弹 移动
			}
			for (EnemyBu b : enemyBullets_T) {
				b.stepToFlyer(hero);// 敌人追踪子弹移动
			}
		}
	}

	/** 获取 传入子弹 最近的敌人 */
	private Flyer getNearestEnemys(SuperBullet bu) {
		int minDistance = Integer.MAX_VALUE;// 上一个的距离
		Flyer f = null;
		for (Flyer en : enemies) {
			int dis = en.y - bu.y;
			if (dis < minDistance && en.isLife()) {
				// 现在距离小于上一个距离,那么把值赋给f
				f = en;
				// 把值赋给上一个 距离
				minDistance = dis;
			}
		}
		/** 有boss则返回boss,让子弹去追踪boss */
		if (boss != null) {
			return boss;
		}
		return f;
	}

	int friendsBulletsStepTime = 0;

	/** 友军子弹移动 */
	private void friendsBulletsStep() {
		friendsBulletsStepTime++;
		/** 英雄子弹移动 */
		if (friendsBulletsStepTime % 10 == 0) {
			for (HeroBullet bu : heroBullets_T) {
				if (getNearestEnemys(bu) == null) {
					bu.step();// 最近为空则,直走
				} else {
					bu.stepToFlyer(getNearestEnemys(bu));
				}
			}
			for (HeroBullet bu : heroBullets_S) {
				bu.step();
			}
		}
		/** 僚机子弹移动 */
		if (friendsBulletsStepTime % 10 == 0) {
			for (WingBulletF bu : wingBullets_F) {
				bu.step();// 固定 僚机 子弹 移动
			}
			for (WingBulletR bu : wingBullets_R) {
				Flyer en = getNearestEnemys(bu);
				if (en != null) {
					bu.stepToFlyer(getNearestEnemys(bu));// 环绕 僚机 子弹 移动
				} else {
					bu.step();
				}
			}
		}
	}

	/** 检测游戏 victory or defeated */
	public static boolean canPlayDefeated = true;

	private void checkGameOver() {
		// 英雄生命值为0游戏失败
		if (hero.getLife() <= 0) {
			gameState = World.GAME_DEFEATED;
			MyButton.addExit(panel);
			if (canPlayDefeated) {
				// 排行分数植入
				Score.write(hero.score);
				/** 播放失败背景音乐 */
				playBgm(defeated, PLAY);
				canPlayDefeated = false;
			}
		}
	}

	int heroBangTime = 0;

	// 英雄本身碰撞 敌人单位
	// 包括普通敌机 ,障碍 ,boss
	// 普通敌机子弹 ,boss子弹
	// 奖励
	// 减少自己相关属性 和敌机相关属性
	private void heroBang_bulletsBang() {
		heroBangTime++;
		/** 所有的敌人子弹 */
		List<SuperBullet> allEnemyBullets = getAllEnemyBullets();
		for (SuperBullet bs : allEnemyBullets) {
			if (bs.myHit(hero) && bs.isLife() && hero.isLife()) {
				if (Hero.canHit && hero.life >= 0) {
					hero.life -= 2;// 英雄被撞击减少生命
					// 如果这是boss的子弹,英雄额外减少生命值
					if (bs instanceof BossBullet) {
						hero.life -= 6;
					}
				}
				bs.goRemove();// 子弹移除
			}
		}
		// 碰撞boss 互相减少生命,heroBangTime 控制速率
		if (boss != null && hero.hit(boss) && boss.isLife()//
				&& heroBangTime % 400 == 0//
				&& hero.isLife()) {
			if (Hero.canHit && hero.life >= 0) {
				hero.life -= 15;// 英雄被撞击减少生命
			}
			boss.life--;
		}
		/** 所右的敌人 */
		List<Flyer> allEnemy = getAllEnemy();
		allEnemy.remove(boss);
		for (Flyer flyer : allEnemy) {
			if (flyer.hit(hero) && flyer.isLife() && hero.isLife()) {
				flyer.life--;// 敌机减少生命
				if (Hero.canHit && hero.life >= 0) {
					hero.life--;// 英雄被撞击减少生命
					if (flyer instanceof Rusher || flyer instanceof BlockRay) {
						hero.life -= 2;// 如果是障碍物额外减命
					}
				}
			}
		}
		/** 敌人子弹与己方子弹的碰撞 */
		List<SuperBullet> friendsBullets = getFriendBullets();
		allEnemyBullets.removeAll(bossBullets_S);
		allEnemyBullets.removeAll(bossBullets_T);
		for (SuperBullet fb : friendsBullets) {
			for (SuperBullet eb : allEnemyBullets) {
				if (eb.myHit(fb)) {
					eb.goRemove();
					fb.goRemove();
				}
			}
		}
		/** Boss子弹与己方子弹的碰撞 */
		List<BossBullet> bossBus = new ArrayList<>();
		bossBus.addAll(bossBullets_S);
		bossBus.addAll(bossBullets_T);
		for (BossBullet bos : bossBus) {
			for (SuperBullet fb : friendsBullets) {
				if (fb.myHit(bos)) {
					fb.goRemove();
					bos.life--;// boss子弹减命
					if (bos.life <= 1) {
						/** boss子弹被击中4次,删除 */
						bos.goRemove();
					}
				}
			}
		}
		/** 英雄与奖励之间的碰撞,获取相应的奖励 */
		for (Iterator<Reward> it = rewards.iterator(); it.hasNext();) {
			Reward re = it.next();
			if (re.hit(hero)) {
				// 英雄获取相应奖励
				re.payReward(hero);
				// 奖励消失
				it.remove();
			}
		}
	}

	/** 获得所有敌人的子弹 临时集合 */
	public ArrayList<SuperBullet> getAllEnemyBullets() {
		ArrayList<SuperBullet> bs = new ArrayList<>();
		// 把所有敌人子弹 添加入 集合
		bs.addAll(bossBullets_S);
		bs.addAll(bossBullets_T);
		bs.addAll(enemyBullets_S);
		bs.addAll(enemyBullets_T);
		return bs;

	}

	/** 获得所有敌人 临时集合 */
	public ArrayList<Flyer> getAllEnemy() {
		// 临时的敌人集合
		ArrayList<Flyer> allEnemy = new ArrayList<>();
		// 把所有敌人添加入集合
		allEnemy.addAll(enemies);
		allEnemy.addAll(block);
		if (boss != null) {
			allEnemy.add(boss);
		}
		return allEnemy;
	}

	/** 获得所有友军子弹 临时集合 */
	public ArrayList<SuperBullet> getFriendBullets() {
		// 临时的友军子弹 集合
		ArrayList<SuperBullet> friend_Bullets = new ArrayList<>();
		// 把所有友军子弹添加入临时集合
		friend_Bullets.addAll(heroBullets_S);
		friend_Bullets.addAll(heroBullets_T);
		friend_Bullets.addAll(wingBullets_F);
		friend_Bullets.addAll(wingBullets_R);
		return friend_Bullets;
	}

	// 为了防止玩家卡boss,获取奖励作弊所设置
	static boolean canGetSkill = true;

	// 常规 敌人 或 障碍
	// 被 (英雄or宠物的)子弹 击中
	// 减少生命值 生命值小于0的敌人 狗带
	// 为防止玩家卡关卡作弊所设置
	private void enemyBang() {
		// 为了平衡火焰对boss的伤害所增加的变量
		int balance = ran.nextInt(5);
		// 临时的友军子弹 集合
		List<SuperBullet> friend_Bullets = getFriendBullets();
		// 临时的敌人集合
		List<Flyer> allEnemy = getAllEnemy();
		/** 生成奖励的随机数 */
		int randomAward = ran.nextInt(100);// 0-99
		/** 遍历,直线此方法核心操作 */
		for (Flyer f : allEnemy) {
			if (f.life <= 0 && f.isLife()) {
				// 小敌机奖励
				if (f instanceof Plane && randomAward < 5) {
					rewards.add(new Reward(f, Reward.FIRE));
				} else if (f instanceof Plane && randomAward < 10) {
					rewards.add(new Reward(f, Reward.LIFE));
				}
				/** 中敌机奖励 */
				if (f instanceof MidPlane && randomAward < 25) {
					rewards.add(new Reward(f, Reward.FIRE));
				} else if (f instanceof MidPlane && randomAward < 45) {
					rewards.add(new Reward(f, Reward.LIFE));
				} else if (f instanceof MidPlane && randomAward < 60) {
					rewards.add(new Reward(f, Reward.PROTECTED));
				}

				/** 大敌机奖励 */
				if (f instanceof BigPlane && randomAward < 35) {
					rewards.add(new Reward(f, Reward.FIRE));
				} else if (f instanceof BigPlane && randomAward < 65) {
					rewards.add(new Reward(f, Reward.LIFE));
				} else if (f instanceof BigPlane && randomAward < 85) {
					rewards.add(new Reward(f, Reward.PROTECTED));
				} else if (f instanceof BigPlane && randomAward < 90) {
					rewards.add(new Reward(f, Reward.PET));
				} else if (f instanceof BigPlane && randomAward < 94) {
					rewards.add(new Reward(f, Reward.ELE));
				}

				/** 防止玩家作弊,卡主boss一直获取奖励 */
				if (levelTime > (BOSS_ENTER_TIME + 10 * 1000) && //
						(Hero.eleAmout + Hero.petAmount) > 4) {
					canGetSkill = false;
				} else {
					canGetSkill = true;
				}
				if (canGetSkill) {
					/** 激光障碍奖励 */
					if (f instanceof BlockRay && randomAward < 11) {
						rewards.add(new Reward(f, Reward.ELE));
					} else if (f instanceof BlockRay && randomAward < 22) {
						rewards.add(new Reward(f, Reward.PET));
					}
					/** 电球障碍奖励 */
					if (f instanceof Rusher && randomAward < 11) {
						rewards.add(new Reward(f, Reward.ELE));
					} else if (f instanceof Rusher && randomAward < 22) {
						rewards.add(new Reward(f, Reward.PET));
					}
				}
				/** 如果是boss放音效 */
				if (f instanceof Boss) {
					Music.playEffect("bossDead", "play");
					/** 获得Boss 的分数 */
					hero.score += boss.score;
				}
				if (f instanceof BigPlane) {// 如果是大敌机播放死亡音效
					Music.playEffect("enemyBlast", "play");
				}
				f.goDead();// 生命少于0的敌人 狗带
			}

			for (SuperBullet b : friend_Bullets) {
				if (b.myHit(f) && f.isLife()) {
					f.life--;// 敌人减少生命值
					b.goRemove();// 修改子弹状态为可移除
					if (f instanceof Boss && balance != 0) {
						f.life++;
					}
				}
			}
		}
	}

	int enemiesShootTime = 0;

	/** 常规敌人射击 - 中 大敌机 射击 */
	private void enemiesShoot() {
		enemiesShootTime++;// 敌机发射子弹速率
		if (enemiesShootTime % (2000 / gameLevel) == 0) {
			for (Flyer en : enemies) {
				if (en instanceof BigPlane) {
					// 如果是大敌机那么创建两发追踪子子弹
					enemyBullets_T.addAll(((BigPlane) en).shoot());
				} else if (en instanceof MidPlane) {
					// 如果是中敌机那么创建一发直线子弹
					enemyBullets_S.addAll(((MidPlane) en).shoot());
				}
			}
		}
	}

	int friendsShootTime = 0;

	/** 友军射击 */
	/** 友军射击 */
	private void friendsShoot() {
		friendsShootTime++;
		// 英雄 - 每隔一定时间发射子弹
		List<HeroBullet> bsTemp = new ArrayList<>();
		bsTemp.addAll(hero.shoot());// 先把子弹存到临时集合
		for (HeroBullet bu : bsTemp) {
			if (bu.isTrace()) {
				heroBullets_T.add(bu);
			} else {
				heroBullets_S.add(bu);// 把子弹添加入指定集合
			}
		}
		// 僚机每隔一段时间发射子弹
		wingBullets_R.addAll(petR.shoot_Trace());// 环绕僚机 射击
		if (hero.getFire() > Hero.secondfire && !Hero.skillPet) {// 发射高级直线子弹
			if (friendsShootTime % 120 == 0) {
				wingBullets_F.add(new WingBulletF(petF.x + petF.width / 3, petF.y, //
						WingBulletF.STR_A));
			}
		} else {// 发射 宠物 技能 子弹或者 普通 直线 子弹
			wingBullets_F.addAll(petF.shoot_Stra());// 固定(fix)僚机 射击
		}
	}

	/** 友军子弹移除 */

	/** 友军子弹 越界 或击中移除 */
	private void friendsBulletsOutOfBounds_remove() {
		for (Iterator<HeroBullet> bu = heroBullets_S.iterator(); bu.hasNext();) {
			HeroBullet bus = bu.next();
			if ((bus.outOfBounds() || bus.isRemove()) && heroBullets_S.size() > 0) {
				bu.remove();// 删除越界 或可移除 的英雄 直线 子弹
			}
		}
		for (Iterator<HeroBullet> bu = heroBullets_T.iterator(); bu.hasNext();) {
			HeroBullet bus = bu.next();
			if ((bus.outOfBounds() || bus.isRemove()) && heroBullets_T.size() > 0) {
				bu.remove();// 删除越界 或可移除 的英雄 追踪 子弹
			}
		}
		for (Iterator<WingBulletF> bu = wingBullets_F.iterator(); bu.hasNext();) {
			WingBulletF bus = bu.next();
			if ((bus.outOfBounds() || bus.isRemove()) && wingBullets_F.size() > 0) {
				bu.remove();// 删除越界 或可移除 的僚机 直线 子弹
			}
		}
		for (Iterator<WingBulletR> bu = wingBullets_R.iterator(); bu.hasNext();) {
			WingBulletR bus = bu.next();
			if ((bus.outOfBounds() || bus.isRemove()) && wingBullets_R.size() > 0) {
				bu.remove();// 删除越界 或可移除 的僚机 追踪 子弹
			}
		}
	}

	/** 敌人子弹的移除 */

	/** 敌人子弹 越界 或 可移除状态 移除 */
	private void enemyBulletsOutOfBounds_remove() {
		for (Iterator<EnemyBu> it = enemyBullets_T.iterator(); it.hasNext();) {
			EnemyBu bu = it.next();
			if ((bu.outOfBounds() || bu.isRemove()) && enemyBullets_T.size() > 0) {
				it.remove();// 删除越界 或可移除的 敌机 追踪 子弹
			}
		}
		for (Iterator<EnemyBu> it = enemyBullets_S.iterator(); it.hasNext();) {
			EnemyBu bu = it.next();
			if ((bu.outOfBounds() || bu.isRemove()) && enemyBullets_S.size() > 0) {
				it.remove();// 删除越界 或可移除的 敌机 直线 子弹
			}
		}
		for (Iterator<BossBullet> it = bossBullets_S.iterator(); it.hasNext();) {
			BossBullet bu = it.next();
			if ((bu.outOfBounds() || bu.isRemove()) && bossBullets_S.size() > 0) {
				it.remove();// 删除越界 或可移除的 Boss 追踪 子弹
			}
		}
		for (Iterator<BossBullet> it = bossBullets_T.iterator(); it.hasNext();) {
			BossBullet bu = it.next();
			if ((bu.outOfBounds() || bu.isRemove()) && bossBullets_T.size() > 0) {
				it.remove();// 删除越界 或可移除的 Boss 追踪 子弹
			}
		}
	}

	/** 删除越界友军 - 援军 */
	private void friendsOutOfBounds_remove() {
		for (Iterator<Help> he = helps.iterator(); he.hasNext();) {
			Flyer flyer = he.next();
			if (flyer.outOfBounds() || flyer.isRemove()) {
				he.remove();// 删除越界的 或可移除的 援军
			}
		}
	}

	/** 移除越界的,大中小敌机和障碍 和奖励-同时生成奖励 */
	private void enemyutOfBounds_remove() {
		for (Iterator<Flyer> blocks = block.iterator(); blocks.hasNext();) {
			Flyer flyer = blocks.next();
			if (flyer.outOfBounds() && block.size() > 0) {// 越界的 或可移除的 障碍物 进行删除
				blocks.remove();
			} else if (flyer.isRemove() && block.size() > 0) {
				hero.score += flyer.score;// 获得奖励分数
				blocks.remove();
			}

		}
		for (Iterator<Flyer> en = enemies.iterator(); en.hasNext();) {
			Flyer flyer = en.next();
			if (flyer.outOfBounds() && enemies.size() > 0) {// 越界的大中小敌机 或可移除的 删除
				en.remove();
			} else if (flyer.isRemove() && enemies.size() > 0) {
				hero.score += flyer.score;// 获得奖励分数
				en.remove();
			}
		}
		for (Iterator<Reward> re = rewards.iterator(); re.hasNext();) {
			Flyer flyer = re.next();
			if ((flyer.outOfBounds() || flyer.isRemove()) && rewards.size() > 0) {
				re.remove();// 越界的 奖励 或可移除的 删除
			}
		}

		if (boss != null && boss.isRemove()) {
			boss = null;
			if (World.gameLevel < 3 && World.gameLevel > 0) {
				// Boss死亡切换到下一关
				World.gameLevel++;
				/** 播放相应背景音乐 */
				switch (gameLevel) {
				case 1:
					playBgm(bgm1, LOOP);
					break;
				case 2:
					playBgm(bgm2, LOOP);
					break;
				case 3:
					playBgm(bgm3, LOOP);
					break;
				}
			}
		}
	}

	/** 大招闪电 移除 */
	private void skill_ele_Remove() {
		for (Iterator<Skill_ele> iterator = eles.iterator(); iterator.hasNext();) {
			Skill_ele ele = iterator.next();
			if (ele.checkHero_Ele_isDead()) {
				iterator.remove();// 移除到时的闪电
			}
		}
	}

	int helpStepTime = 0;

	/** 援军移动 */
	private void helpStep() {
		helpStepTime++;
		if (helpStepTime % 15 == 0) {
			for (Help h : helps) {
				h.step();
			}
		}
	}

	/** 大招闪电入场 */
	int skill_ele_EnterTime = 0;
	int skill_ele_ContinueTime = 1000 * 5;

	private void skill_ele_Enter() {
		if (Hero.skillEle) {
			skill_ele_EnterTime++;
			skill_ele_ContinueTime--;
			if (skill_ele_EnterTime % 10 == 0) {
				eles.add(new Skill_ele());
			}
		} /** 大招持续5秒 */
		if (skill_ele_ContinueTime <= 0) {
			Hero.skillEle = false;
			skill_ele_ContinueTime = 1000 * 5;
		}
	}

	int helpEnterTime = 0;
	int helpContinueTime = 1000 * 5;

	/** 援军入场 */
	private void helpEnter() {
		/** 当英雄生命值小于 20% 援军入场 */
		if (hero.getLife() * 1.0 / hero.maxLife < 0.2 //
				&& Hero.helpAmout > 0 && Hero.skillHelp == false) {
			Hero.skillHelp = true;
			Hero.helpAmout--;
			Music.playEffect("help", "play");
		}
		if (Hero.skillHelp) {
			helpEnterTime++;
			helpContinueTime -= 10;
			// 援军生成速率
			if (helpEnterTime % 10 == 0) {
				helps.add(new Help());
			}
		}
		/** 援军持续 5s */
		if (helpContinueTime <= 0) {
			Hero.skillHelp = false;
			helpContinueTime = 1000 * 5;
		}

	}

	/** 障碍物移动 */
	private void blockStep() {
		for (Flyer flyer : block) {
			flyer.step();
		}
	}

	int blockEnterTime = 0;

	/** 障碍物入场 */
	private void blockEnter() {
		blockEnterTime++;
		if (blockEnterTime % (700 / gameLevel) == 0) {
			int random = ra.nextInt(3);
			if (random == 1) {
				block.add(new BlockRay());
			} else {
				block.add(new Rusher());
			}
		}
	}

	/** Boss入场 */
	private void bossEnter() {
		// 每一段时间生成Boss
		if (boss == null && levelTime > BOSS_ENTER_TIME) {
			boss = new Boss(gameLevel);
			// 为了平衡,boss出现,给英雄增加火力
			hero.setFire(hero.getFire() + 100);
			/** 调用gc清空上一个音频垃圾 */
			System.gc();
			switch (gameLevel) {
			case 1:
				playBgm(bgm_boss1, LOOP);
				break;
			case 2:
				playBgm(bgm_boss2, LOOP);
				break;
			case 3:
				playBgm(bgm_boss3, LOOP);
				break;
			}
		}
	}

	int enemyEneterTime = 0;

	/** 大 中 小 敌机入场 */
	private void enemyEnter() {
		enemyEneterTime++;
		// 每秒随机一个中小敌机 ----Boss入场后普通敌机木有
		if (enemyEneterTime % (100 / gameLevel) == 0//
				&& levelTime < BOSS_ENTER_TIME) {
			int next = ra.nextInt(100);
			if (next < 80) {
				enemies.add(new Plane());
			} else {
				enemies.add(new MidPlane());
			}
		}
		// 每20秒一个大敌机 ----Boss入场后大敌机木有
		if (enemyEneterTime % (100 * 20 / gameLevel) == 0 //
				&& levelTime < BOSS_ENTER_TIME) {
			enemies.add(new BigPlane());
		}
	}

	int enemyStepTime = 0;

	/** 大 中 小 敌机 移动 */
	private void enemyStep() {
		enemyStepTime++;
		if (enemyStepTime % 15 == 0) {
			for (Flyer flyer : enemies) {
				flyer.step();
			}
		}
	}

	/** ----主方法,面板,框架,键盘,按钮事件,添加处---- */
	public static void main(String[] args) {
		panel = new World();
		JFrame frame = new JFrame();
		frame.setTitle("黯黑-战舰");
		frame.setIconImage(GamePng.iconEle);
		frame.setSize(WIDTH, HEIGHT);
		frame.add(panel);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// 为 框架 添加 按键 事件
		KeyBoard.getKeyControl(panel);
		// 添加开始状态按钮
		MyButton.addStartButton(panel);
		// 把焦点 设置到 面板上，实现键盘监听
		panel.setFocusable(true);
		// 固定窗口尺寸,反作弊!
		frame.setResizable(false);
		frame.setVisible(true);
		panel.action();
	}

	/** 对外界获取面板的方法 */
	public static JPanel getPanel() {
		return World.panel;
	}
}
