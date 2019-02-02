package friends;

import java.awt.image.BufferedImage;

import core.Flyer;
import core.World;

/** 封装援军类 */
public class Help extends Flyer {
	private static BufferedImage images[];
	static {
		images = new BufferedImage[3];
		for (int i = 0; i < images.length; i++) {
			images[i] = loadImage("shoot/audio_png/pictures&/help/", "help_" + i);
		}
	}

	public Help() {
		super(images[0].getWidth(), images[0].getHeight(), //
				0, 0, 1);
		this.x = ran.nextInt(World.WIDTH - this.width);
		this.y = World.HEIGHT+this.height;
		this.xSpeed = 0;
		this.ySpeed = -3;
	}

	public void step() {
		x += this.xSpeed;
		y += this.ySpeed;
	}

	int index = 0;
	int time = 0;

	public BufferedImage getImage() {
		time++;
		if (time % 10 == 0) {
			index++;
		}
		return images[index%images.length];
	}

	public boolean outOfBounds() {
		return y < -this.height * 2;
	}

}
