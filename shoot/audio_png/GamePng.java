package audio_png;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GamePng {
	/** 游戏界面,事件 图片 */
	public static BufferedImage start;// 开始

	public static BufferedImage pause;// 暂停
	public static BufferedImage defeated;// 结束
	public static BufferedImage victory;// 胜利

	public static BufferedImage top;// 排行
	public static BufferedImage help;// 操作指南
	public static BufferedImage about;// 游戏相关信息

	public static BufferedImage other_bg;// 上 三种,背景

	public static BufferedImage changeLevel;// 关卡切换

	public static BufferedImage warn;// 警告

	public static BufferedImage startEle;// 大招 技能
	public static BufferedImage startPet;// 宠物觉醒 技能
	public static BufferedImage friend_arrive;// 援军到来 图

	public static BufferedImage iconEle;// 大招 技能图标
	public static BufferedImage iconPet;// 宠物觉醒 技能图标

	public static BufferedImage hero_rect;// 英雄血条

	public static BufferedImage shields[];// 英雄血条

	/** 加载区 */
	static {
		String path = "shoot/audio_png/pictures&/event/";
		try {
			hero_rect = ImageIO.read(audio_png.GamePng.class.getResource("pictures&/hero/hero_rect.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		start = load(path, "bg_start");
		pause = load(path, "pause");
		defeated = load(path, "defeated");
		victory = load(path, "victory");
		top = load(path, "top");
		help = load(path, "help");
		about = load(path, "about");

		other_bg = load(path, "otherBg");

		startEle = load(path, "hero_skill");
		startPet = load(path, "petWake");

		iconEle = load(path, "superEle");
		iconPet = load(path, "superPet");

		changeLevel = load(path, "changeLevel");

		warn = load(path, "warn");

		shields = new BufferedImage[4];
		for (int i = 0; i < shields.length; i++) {
			try {
				shields[i] = ImageIO
						.read(audio_png.GamePng.class.getResource("pictures&/hero/" + "Shield_" + i + ".png"));
			} catch (IOException e) {
				System.out.println("读取图片...");
			}
		}
		friend_arrive = load(path, "friend_arrive");
	}

	/** 对外提供英雄护盾的图片 */
	static int protectedTime = 0;
	static int index = 0;

	public static BufferedImage getProtectedImage() {
		protectedTime++;
		if (protectedTime % 10 == 0) {
			index++;
		}
		return shields[index % shields.length];
	}

	/** 加载图片的方法 */
	private static BufferedImage load(String path, String fileName) {
		BufferedImage ima;
		try {
			ima = ImageIO.read(audio_png.GamePng.class.getResource("pictures&/event/" + fileName + ".png"));
			return ima;
		} catch (Exception e) {
			System.out.println("读取图片...");
			return null;
		}
	}
}
