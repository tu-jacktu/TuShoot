package score;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;

/** 向外界提供分数 读入写出的方法 */
public class Score {
	private static BufferedReader read;
	private static PrintWriter pw;
	private static File f;
	private static ArrayList<Integer> scoreList;

	/** 写入分数的方法 */
	public static void write(int score) {
		notFoundFile();
		boolean append = true;
		if (score < 0) {
			append = false;
		}
		try {
			// 写入流
			pw = new PrintWriter(//
					new BufferedWriter(//
							new OutputStreamWriter(//
									new FileOutputStream("score", append), "UTF-8")),
					true);
			// 把获得的分数写入 文件
			if (score < 0) {
				pw.write("");
			} else {
				pw.write(score + "");
				pw.println();// 换行
			}
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 读出分数的方法 (看排行榜) */
	public static ArrayList<Integer> read() {
		notFoundFile();
		ArrayList<Integer> list = new ArrayList<>();
		try {
			// 读取流
			read = new BufferedReader(//
					new InputStreamReader(//
							new FileInputStream("score"), "UTF-8"));
			String temp;
			while ((temp = read.readLine()) != null) {
				list.add(Integer.parseInt(temp));
			}
			// 将分数从大到小排序
			list.sort(new Comparator<Integer>() {
				public int compare(Integer o1, Integer o2) {
					return o2 - o1;
				}
			});
			// 必须先关!
			read.close();
			// 多于 50个分数 重新写25个
			if (list.size() > 50) {
				write(-2);
				for (int i = 0; i < 25; i++) {
					write(list.get(i));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/** 为防止找不到文件所做 */
	private static void notFoundFile() {
		f = new File("score");
		try {
			// 如果没有这个文件新建一个文件
			if (!f.exists()) {
				f.createNewFile();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 向外界提供排行榜 */
	public static ArrayList<Integer> getScoreList() {
		return scoreList;
	}

	/** 向外界提供设置,排行榜的方法 */
	public static void setScoreList() {
		scoreList = read();
	}
}
