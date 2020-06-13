import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


import javax.imageio.ImageIO;

public class Model {
	
	public final String version = "0.1";

	Long gold = 0L, exp = 0L, time = 0L, startTime , expSaved = 0L;
	int mor = 0, glory = 0, runs = 0, quartz = 0, refined = 0, glorySaved = 0;
	Double goldAvg, expAvg, gloryAvg ,timeAvg, quartzAvg, refinedAvg;
	float morAvg;
	
	ArrayList<BufferedImage> goldImages = new ArrayList<BufferedImage>();
	ArrayList<BufferedImage> expImages = new ArrayList<BufferedImage>();
	ArrayList<BufferedImage> morImages = new ArrayList<BufferedImage>();
	ArrayList<BufferedImage> gloryImages = new ArrayList<BufferedImage>();
	ArrayList<BufferedImage> quartzImages = new ArrayList<BufferedImage>();
	ArrayList<BufferedImage> refinedImages = new ArrayList<BufferedImage>();
	
	public void calc() {
		goldAvg = (double) (gold / runs);
		expAvg = (double) (exp / runs);
		timeAvg = (double) (time / runs);
		
		if(mor == 0)
			morAvg = 0.0f;
		else
		morAvg = (float) (mor / runs);

		if(glory == 0)
			gloryAvg = 0.0;
		else
		gloryAvg = (double) (glory / runs);
		
		if(quartz == 0)
			quartzAvg = 0.0;
		else
			quartzAvg = (double) (quartz / runs);
		
		if(refined == 0)
			refinedAvg = 0.0;
		else
			refinedAvg = (double) (refined / runs);
	}
	
	public void resetStats() {
		gold = 0L; exp = 0L; time = 0L; startTime = 0L; expSaved = 0L;
		mor = 0; glory = 0; runs = 0; quartz = 0; refined = 0; glorySaved = 0;
		goldAvg = 0.0; expAvg= 0.0; gloryAvg = 0.0; timeAvg= 0.0; quartzAvg= 0.0; refinedAvg= 0.0;
		resetRewardImages();
	}
	
	public void resetRewardImages() {
		goldImages.clear();
		expImages.clear();
		morImages.clear();
		gloryImages.clear();
		quartzImages.clear();
		refinedImages.clear();
	}
	
	public void saveRewardImages() {
		for(int i = 0; i < goldImages.size(); i++) {
			try {
				ImageIO.write(goldImages.get(i), "png", new File("img/debug/gold" + i + ".png"));
			} catch (IOException e3) {
			}
		}
		for(int i = 0; i < expImages.size(); i++) {
			try {
				ImageIO.write(expImages.get(i), "png", new File("img/debug/exp" + i + ".png"));
			} catch (IOException e3) {
			}
		}
		for(int i = 0; i < morImages.size(); i++) {
			try {
				ImageIO.write(morImages.get(i), "png", new File("img/debug/mor" + i + ".png"));
			} catch (IOException e3) {
			}
		}
		for(int i = 0; i < gloryImages.size(); i++) {
			try {
				ImageIO.write(gloryImages.get(i), "png", new File("img/debug/glory" + i + ".png"));
			} catch (IOException e3) {
			}
		}
		for(int i = 0; i < quartzImages.size(); i++) {
			try {
				ImageIO.write(quartzImages.get(i), "png", new File("img/debug/quartz" + i + ".png"));
			} catch (IOException e3) {
			}
		}
		for(int i = 0; i < refinedImages.size(); i++) {
			try {
				ImageIO.write(refinedImages.get(i), "png", new File("img/debug/refined" + i + ".png"));
			} catch (IOException e3) {
			}
		}
	}

}
