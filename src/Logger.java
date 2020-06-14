import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.sikuli.basics.Settings;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Finder;
import org.sikuli.script.ImagePath;
import org.sikuli.script.Match;
import org.sikuli.script.Pattern;
import org.sikuli.script.Region;
import org.sikuli.script.Screen;

import com.sun.jna.platform.WindowUtils;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class Logger {

	final boolean debugMode = true;

	Tesseract tesseract;
	Model model;
	int index = 0;

	public Thread thread;

	public Logger(Model model) {
		tesseract = new Tesseract();
		tesseract.setDatapath("tessdata/");
		tesseract.setTessVariable("user_defined_dpi", "320");
		this.model = model;
		
		

	}

	public Region findNox() {
		Settings.MoveMouseDelay = 0;
		final Rectangle rect = new Rectangle(-9999, 0, 0, 0); // needs to be final or effectively final for lambda
		WindowUtils.getAllWindows(true).forEach(desktopWindow -> {
			if (desktopWindow.getTitle().contains("NoxPlayer")) {
				rect.setRect(desktopWindow.getLocAndSize());
			}
			
		});

		Region r = new Region(rect);
		//System.out.println("Nox size: "+r.w+"x"+r.h);
		// 720 1284x754
		// 1080 1924x1114

		return r;
	}
	
	

	public Region createRegion(int x, int y, int w, int h) {
		Region r = findNox();

		return new Region(r.getX() + x, r.getY() + y, w, h);
	}


	public static String removeNonDigits(final String str) {
		if (str == null || str.length() == 0) {
			return "";
		}
		return str.replaceAll("[^0-9]+", "");
	}

	public void startRun(MainWindow w) {
		thread = new Thread("RunThread") {
			public void run() {
				int resolution = 720;
				if(w.rdbtnmntmx.isSelected())
					resolution = 720;
				else if(w.rdbtnmntmx_1.isSelected())
					resolution = 1080;
				
				Region noxReg = findNox();
				if(noxReg.getX() == -9999) {
					JOptionPane.showMessageDialog(w.frmRtdLootLogger, "There is no Nox visible on screen.", "Nox where are you...",
							JOptionPane.ERROR_MESSAGE);
					w.btnToggle = !w.btnToggle;
					w.btnStart.setText("Start");
					this.stop();
				}else if(resolution == 720 &&(noxReg.getW() != 1284 || noxReg.getH() != 754)) {
					JOptionPane.showMessageDialog(w.frmRtdLootLogger, "You have selected 1280x720, but nox seems to be a different resolution (or minimized). \nPlease set the resolution to 1280x720, restart nox and try again.", "Ups",
							JOptionPane.ERROR_MESSAGE);
					w.btnToggle = !w.btnToggle;
					w.btnStart.setText("Start");
					this.stop();
				}else if(resolution == 1080 &&(noxReg.getW() != 1924 || noxReg.getH() != 1114)) {
					JOptionPane.showMessageDialog(w.frmRtdLootLogger, "You have selected 1920x1080, but nox seems to be a different resolution (or minimized). \nPlease set the resolution to 1920x1080, restart nox and try again.", "Ups",
							JOptionPane.ERROR_MESSAGE);
					w.btnToggle = !w.btnToggle;
					w.btnStart.setText("Start");
					this.stop();
				}
				model.setResolution(resolution);
				if(resolution == 1080)
					model.rewardReg = createRegion(model.noxReward1080.x, model.noxReward1080.y, model.noxReward1080.width, model.noxReward1080.height);
				else
					model.rewardReg = createRegion((int)(model.noxReward1080.x*0.67), (int)(model.noxReward1080.y*0.67), (int)(model.noxReward1080.width*0.67), (int)(model.noxReward1080.height*0.67));

				w.setStatus("Waiting for rewards...");
				while (findNox().exists(model.rewardPattern) == null) {
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				w.setStatus("Waiting for new run...");
				while (findNox().exists(model.onPattern) == null) {
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				w.resetTimer();
				w.startTimer();
				model.startTime = System.currentTimeMillis();
				while (true) {
					index++;
					// start

					model.resetRewardImages();
					w.setStatus("Waiting for rewards...");
					while (findNox().exists(model.rewardPattern) == null) {
						try {
							Thread.sleep(20);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					try {
						Thread.sleep(500);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					// rewards
					w.setStatus("Taking screenshots...");

					// take lootimages
					model.runs++;
					
					long startImageTime = System.currentTimeMillis();
					long avgTime = 0;
					long diff = 0;
					while (diff < 4000 - avgTime) {
						//getLootImages();
						getLootImages(resolution, model.patterns, model.rewardReg, model.offsets, false);
						try {
							Thread.sleep(20);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						diff = System.currentTimeMillis() - startImageTime;

						if (avgTime == 0)
							avgTime = diff;
					}

					w.setStatus("Waiting for new run to start...");
					while (findNox().exists(model.onPattern) == null) {
						try {
							Thread.sleep(20);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					long tmpTime = System.currentTimeMillis() - model.startTime;

					String pretty = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(tmpTime),
							TimeUnit.MILLISECONDS.toSeconds(tmpTime)
									- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(tmpTime)));
					long gValue = 0, goldMax = 0, expValue = 0, expMax = 0, morValue = 0, morMax = 0, gloryValue = 0,
							gloryMax = 0, quartzValue = 0, quartzMax = 0, refinedValue = 0, refinedMax = 0, tmpLong;
					HashMap<Long, Integer> goldMap = new HashMap<Long, Integer>();
					HashMap<Long, Integer> expMap = new HashMap<Long, Integer>();
					HashMap<Long, Integer> morMap = new HashMap<Long, Integer>();
					HashMap<Long, Integer> gloryMap = new HashMap<Long, Integer>();
					HashMap<Long, Integer> quartzMap = new HashMap<Long, Integer>();
					HashMap<Long, Integer> refinedMap = new HashMap<Long, Integer>();
					w.setStatus("Analyzing loot...");
					// gold ocr
					tmpLong = 0;
					for (int i = 0; i < model.goldImages.size(); i++) {
						tmpLong = ocrImage(model.goldImages.get(i));

						if (goldMap.get(tmpLong) == null)
							goldMap.put(tmpLong, 1);
						else
							goldMap.put(tmpLong, goldMap.get(tmpLong) + 1);
					}
					Set set = goldMap.entrySet();
					Iterator iterator = set.iterator();
					while (iterator.hasNext()) {
						Map.Entry<Long, Integer> mentry = (Map.Entry) iterator.next();
						System.out.print("gold: " + mentry.getKey() + " count: ");
						System.out.println(mentry.getValue());
						if (mentry.getValue() > goldMax) {
							goldMax = mentry.getValue();
							gValue = mentry.getKey();
						}
					}

						// exp ocr
						tmpLong = 0;
						for (int i = 0; i < model.expImages.size(); i++) {
							tmpLong = ocrImage(model.expImages.get(i));

							if (expMap.get(tmpLong) == null)
								expMap.put(tmpLong, 1);
							else
								expMap.put(tmpLong, expMap.get(tmpLong) + 1);
						}
						set = expMap.entrySet();
						iterator = set.iterator();
						while (iterator.hasNext()) {
							Map.Entry<Long, Integer> mentry = (Map.Entry) iterator.next();
							System.out.print("exp: " + mentry.getKey() + " count: ");
							System.out.println(mentry.getValue());
							if (mentry.getValue() > expMax) {
								expMax = mentry.getValue();
								expValue = mentry.getKey();
							}
						}

					// mor ocr
					tmpLong = 0;
					for (int i = 0; i < model.morImages.size(); i++) {
						tmpLong = ocrImage(model.morImages.get(i));

						if (morMap.get(tmpLong) == null)
							morMap.put(tmpLong, 1);
						else
							morMap.put(tmpLong, morMap.get(tmpLong) + 1);
					}
					set = morMap.entrySet();
					iterator = set.iterator();
					while (iterator.hasNext()) {
						Map.Entry<Long, Integer> mentry = (Map.Entry) iterator.next();
						System.out.print("mor: " + mentry.getKey() + " count: ");
						System.out.println(mentry.getValue());
						if (mentry.getValue() > morMax) {
							morMax = mentry.getValue();
							morValue = mentry.getKey();
						}
					}


						// glory ocr
						tmpLong = 0;
						for (int i = 0; i < model.gloryImages.size(); i++) {
							tmpLong = ocrImage(model.gloryImages.get(i));

							if (gloryMap.get(tmpLong) == null)
								gloryMap.put(tmpLong, 1);
							else
								gloryMap.put(tmpLong, gloryMap.get(tmpLong) + 1);
						}
						set = gloryMap.entrySet();
						iterator = set.iterator();
						while (iterator.hasNext()) {
							Map.Entry<Long, Integer> mentry = (Map.Entry) iterator.next();
							System.out.print("glory: " + mentry.getKey() + " count: ");
							System.out.println(mentry.getValue());
							if (mentry.getValue() > gloryMax) {
								gloryMax = mentry.getValue();
								gloryValue = mentry.getKey();
							}
						}


					// quartz ocr
					tmpLong = 0;
					for (int i = 0; i < model.quartzImages.size(); i++) {
						tmpLong = ocrImage(model.quartzImages.get(i));

						if (quartzMap.get(tmpLong) == null)
							quartzMap.put(tmpLong, 1);
						else
							quartzMap.put(tmpLong, quartzMap.get(tmpLong) + 1);
					}
					set = quartzMap.entrySet();
					iterator = set.iterator();
					while (iterator.hasNext()) {
						Map.Entry<Long, Integer> mentry = (Map.Entry) iterator.next();
						System.out.print("quartz: " + mentry.getKey() + " count: ");
						System.out.println(mentry.getValue());
						if (mentry.getValue() > quartzMax) {
							quartzMax = mentry.getValue();
							quartzValue = mentry.getKey();
						}
					}

					// refined ocr
					tmpLong = 0;
					for (int i = 0; i < model.refinedImages.size(); i++) {
						tmpLong = ocrImage(model.refinedImages.get(i));

						if (refinedMap.get(tmpLong) == null)
							refinedMap.put(tmpLong, 1);
						else
							refinedMap.put(tmpLong, refinedMap.get(tmpLong) + 1);
					}
					set = refinedMap.entrySet();
					iterator = set.iterator();
					while (iterator.hasNext()) {
						Map.Entry<Long, Integer> mentry = (Map.Entry) iterator.next();
						System.out.print("refined: " + mentry.getKey() + " count: ");
						System.out.println(mentry.getValue());
						if (mentry.getValue() > refinedMax) {
							refinedMax = mentry.getValue();
							refinedValue = mentry.getKey();
						}
					}

					DungeonRun d = new DungeonRun(tmpTime, pretty, gValue, expValue, (int) morValue, (int) gloryValue,
							(int) quartzValue, (int) refinedValue);
					model.time += tmpTime;
					model.startTime = System.currentTimeMillis();
					model.gold += gValue;
					model.exp += expValue;
					model.mor += morValue;
					model.glory += gloryValue;
					model.quartz += quartzValue;
					model.refined += refinedValue;


					w.addRun(d);
					w.setStatus("Added new run");
					w.updateGui(true);
					model.saveRewardImages();
				}

			}
		};
		thread.start();
	}

	public long ocrImage(BufferedImage img) {
		// OCR
		long rewardAmount = 0l;
		String text;
		try {

			text = tesseract.doOCR(img);

			try {
				String formatNum = removeNonDigits(text);
				rewardAmount = Long.valueOf(formatNum);
			} catch (Exception e) {
				rewardAmount = -99;

			}
		} catch (TesseractException e) {
			rewardAmount = -99;
			return rewardAmount;
		}

		if (rewardAmount == -99) {
			System.out.println("Found no number! --> " + text);
			return 0L;
		} else {
			return rewardAmount;
		}
	}



	public void getLootImages(int res, ArrayList<Pattern> patterns, Region reg, ArrayList<Rectangle> rects, boolean debug) {
		for (int i = 0; i < patterns.size(); i++) {
			

			
			Match regMatch = null;
			File origImage = null;
			BufferedImage img = null;
			Region findReg = null;

			try {
				regMatch = reg.wait(patterns.get(i), .01);
				if(debug)
					regMatch.highlight();
				
				if (regMatch != null) {
					
					findReg = new Region(regMatch.getX() + rects.get(i).x, regMatch.getY() + rects.get(i).y,
						rects.get(i).width, rects.get(i).height);

					
					if(debug)
						findReg.highlight();
				
					origImage = new File(findReg.saveScreenCapture());
				}
			} catch (FindFailed e) {
				System.out.println("Pattern " + i + " not found");

				regMatch = null;
			}
			if (regMatch != null) {
				try {
					img = ImageIO.read(origImage);
				} catch (IOException e) {
					img = null;
				}
				if (i == 0) {
					for (int y = 0; y < img.getHeight(); y++) {

						for (int x = 0; x < img.getWidth(); x++) {

							Color pixel = new Color(img.getRGB(x, y));

							if (pixel.getRed() > pixel.getBlue() && pixel.getBlue() > 90 && pixel.getGreen() < 222
									&& pixel.getRed() >= 245) {
								img.setRGB(x, y, Color.BLACK.getRGB());
							} else {
								img.setRGB(x, y, Color.WHITE.getRGB());
							}

						}

					}


				}else if(i==1) {
					for (int y = 0; y < img.getHeight(); y++) {

						for (int x = 0; x < img.getWidth(); x++) {

							Color pixel = new Color(img.getRGB(x, y));

							if (pixel.getRed() > pixel.getBlue() && pixel.getBlue() > 70 && pixel.getGreen() < 220
									&& pixel.getRed() >= 215) {
								img.setRGB(x, y, Color.BLACK.getRGB());
							} else {
								img.setRGB(x, y, Color.WHITE.getRGB());
							}
						}

					}
				}else if(i>=2) {
					for (int y = 0; y < img.getHeight(); y++) {

						for (int x = 0; x < img.getWidth(); x++) {

							Color pixel = new Color(img.getRGB(x, y));

							if (pixel.getRed() > pixel.getBlue() && pixel.getBlue() > 90 && pixel.getGreen() < 225
									&& pixel.getRed() >= 250) {
								img.setRGB(x, y, Color.BLACK.getRGB());
							} else {
								img.setRGB(x, y, Color.WHITE.getRGB());
							}
						}

					}
				}


				
				if(i==0) {
					model.goldImages.add(img);
				}else if(i==1) {
					model.expImages.add(img);
				}else if(i==2) {
					model.morImages.add(img);
				}else if(i==3) {
					model.gloryImages.add(img);
				}else if(i==4) {
					model.quartzImages.add(img);
				}else if(i==5) {
					model.refinedImages.add(img);
				}
					
				if(debug)
					regMatch.highlight();
				
				if(debug)
					findReg.highlight();
				
			}
		}
		
	}


}
