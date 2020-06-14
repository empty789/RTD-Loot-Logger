import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


import javax.imageio.ImageIO;

import org.sikuli.script.Pattern;
import org.sikuli.script.Region;

public class Model {
	
	public final String version = "0.1";

	Long gold = 0L, exp = 0L, time = 0L, startTime;
	int mor = 0, glory = 0, runs = 0, quartz = 0, refined = 0;
	Double goldAvg, expAvg, gloryAvg ,timeAvg, quartzAvg, refinedAvg;
	float morAvg;
	

	
	ArrayList<BufferedImage> goldImages = new ArrayList<BufferedImage>();
	ArrayList<BufferedImage> expImages = new ArrayList<BufferedImage>();
	ArrayList<BufferedImage> morImages = new ArrayList<BufferedImage>();
	ArrayList<BufferedImage> gloryImages = new ArrayList<BufferedImage>();
	ArrayList<BufferedImage> quartzImages = new ArrayList<BufferedImage>();
	ArrayList<BufferedImage> refinedImages = new ArrayList<BufferedImage>();
	
	ArrayList<Pattern> patterns;
	Pattern onPattern, rewardPattern;
	
	Rectangle noxReward1080 = new Rectangle(0, 300, 1300, 500);

	Rectangle noxGold1080Offset = new Rectangle(-28, 58, 153, 39);
	Rectangle noxExp1080Offset = new Rectangle(-28, 70, 155, 35);
	Rectangle noxMor1080Offset = new Rectangle(13, 63, 40, 40);
	Rectangle noxGlory1080Offset = new Rectangle(23, 55, 45, 38);
	Rectangle noxQuartz1080Offset = new Rectangle(20, 48, 56, 39);
	Rectangle noxRefined1080Offset = new Rectangle(-5, 60, 80, 39);

	ArrayList<Rectangle> offsets = new ArrayList<Rectangle>();
	Region rewardReg;


	
	public void setResolution(int res){
		
		if(patterns == null)
			patterns = new ArrayList<Pattern>();
		else
			patterns.clear();
		
		patterns.add(new Pattern("gold" + res + ".png").similar(.8f));
		patterns.add(new Pattern("exp" + res + ".png").similar(.8f));
		patterns.add(new Pattern("mor" + res + ".png").similar(.8f));
		patterns.add(new Pattern("glory" + res + ".png").similar(.8f));
		patterns.add(new Pattern("quartz" + res + ".png").similar(.8f));
		patterns.add(new Pattern("refined" + res + ".png").similar(.8f));
		
		
		this.rewardPattern = new Pattern("rewards" + res + ".png").similar(.8f);
		this.onPattern = new Pattern("on" + res + ".png").similar(.8f);
		
		offsets.clear();
		if(res == 1080) {
			offsets.add(noxGold1080Offset);
			offsets.add(noxExp1080Offset);
			offsets.add(noxMor1080Offset);
			offsets.add(noxGlory1080Offset);
			offsets.add(noxQuartz1080Offset);
			offsets.add(noxRefined1080Offset);
		}else if(res == 720) {
			offsets.add(new Rectangle((int)(noxGold1080Offset.x*0.67),(int)(noxGold1080Offset.y*0.67),(int)(noxGold1080Offset.width*0.67),(int)(noxGold1080Offset.height*0.67)));
			offsets.add(new Rectangle((int)(noxExp1080Offset.x*0.67),(int)(noxExp1080Offset.y*0.67),(int)(noxExp1080Offset.width*0.67),(int)(noxExp1080Offset.height*0.67)));
			offsets.add(new Rectangle((int)(noxMor1080Offset.x*0.67),(int)(noxMor1080Offset.y*0.67),(int)(noxMor1080Offset.width*0.67),(int)(noxMor1080Offset.height*0.67)));
			offsets.add(new Rectangle((int)(noxGlory1080Offset.x*0.67),(int)(noxGlory1080Offset.y*0.67),(int)(noxGlory1080Offset.width*0.67),(int)(noxGlory1080Offset.height*0.67)));
			offsets.add(new Rectangle((int)(noxQuartz1080Offset.x*0.67),(int)(noxQuartz1080Offset.y*0.67),(int)(noxQuartz1080Offset.width*0.67),(int)(noxQuartz1080Offset.height*0.67)));
			offsets.add(new Rectangle((int)(noxRefined1080Offset.x*0.67),(int)(noxRefined1080Offset.y*0.67),(int)(noxRefined1080Offset.width*0.67),(int)(noxRefined1080Offset.height*0.67)));
		}
		
		
	}
	
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
		gold = 0L; exp = 0L; time = 0L; startTime = 0L;
		mor = 0; glory = 0; runs = 0; quartz = 0; refined = 0;
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
