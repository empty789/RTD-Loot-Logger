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
	 Pattern goldPattern;
	 Pattern expPattern;
	 Pattern morPattern;
	 Pattern gloryPattern;
	 Pattern quartzPattern;
	 Pattern refinedPattern;
	 Pattern rewardPattern;
	 Pattern onPattern;
	Tesseract tesseract;
	Model model;
	int index = 0;

	public Thread thread;

	public Logger(Model model) {
		tesseract = new Tesseract();
		tesseract.setDatapath("tessdata/");
		tesseract.setTessVariable("user_defined_dpi", "300");
		this.model = model;
		
		
		  goldPattern = new Pattern("gold.png").similar(.8f);
		  expPattern = new Pattern("exp.png").similar(.8f);
		  morPattern = new Pattern("mor.png").similar(.8f);
		  gloryPattern = new Pattern("glory.png").similar(.8f);
		  quartzPattern = new Pattern("quartz.png").similar(.8f);
		  refinedPattern = new Pattern("refined.png").similar(.8f);
		  rewardPattern = new Pattern("rewards.png").similar(.8f);
		  onPattern = new Pattern("on.png").similar(.8f);
	}

	public Region findNox() {
		Settings.MoveMouseDelay = 0;
		final Rectangle rect = new Rectangle(0, 0, 0, 0); // needs to be final or effectively final for lambda
		WindowUtils.getAllWindows(true).forEach(desktopWindow -> {
			if (desktopWindow.getTitle().contains("NoxPlayer")) {
				rect.setRect(desktopWindow.getLocAndSize());
			}
		});

		Region r = new Region(rect);

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

				w.setStatus("Waiting for rewards...");
				while (findNox().exists(rewardPattern) == null) {
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				w.setStatus("Waiting for new run...");
				while (findNox().exists(onPattern) == null) {
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

					w.setStatus("Waiting for rewards...");
					while (findNox().exists(rewardPattern) == null) {
						try {
							Thread.sleep(20);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					try {
						Thread.sleep(50);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					// rewards
					w.setStatus("Taking screenshots...");

					// take lootimages
					model.runs++;
					model.resetRewardImages();
					long startImageTime = System.currentTimeMillis();
					long avgTime = 0;
					long diff = 0;
					while (diff < 4000 - avgTime) {
						getLootImages();
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
					while (findNox().exists(onPattern) == null) {
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
					if (model.expSaved == 0) {
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
					} else {
						expValue = model.expSaved;
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

					if (model.glorySaved == 0) {
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
					} else {
						gloryValue = model.glorySaved;
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
					model.glorySaved = (int) gloryValue;
					model.expSaved = expValue;

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

	public void getLootImages() {

		Region reg = createRegion(0, 300, 1300, 500);
		Match regMatch = null;
		File origImage = null;
		Region findReg;
		BufferedImage img = null;

		// gold
		try {
			regMatch = reg.wait(goldPattern, .01);

			if (regMatch != null) {
				findReg = new Region(regMatch.getX() - 20, regMatch.getY() + 60, 142, 35);
				origImage = new File(findReg.saveScreenCapture());
			}
		} catch (FindFailed e) {
			// TODO Auto-generated catch block

		}
		if (regMatch != null) {
			try {
				img = ImageIO.read(origImage);
			} catch (IOException e) {
				img = null;
			}

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
			model.goldImages.add(img);
		}

		// exp
		if (model.expSaved == 0) {
			regMatch = null;
			origImage = null;
			findReg = null;
			img = null;
			try {
				regMatch = reg.wait(expPattern, .01);

				findReg = new Region(regMatch.getX() - 24, regMatch.getY() + 65, 142, 32);
				origImage = new File(findReg.saveScreenCapture());
			} catch (FindFailed e) {
				// TODO Auto-generated catch block

			}
			if (regMatch != null) {
				try {
					img = ImageIO.read(origImage);
				} catch (IOException e) {
					img = null;
				}

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
				model.expImages.add(img);
			}
		}
		regMatch = null;
		origImage = null;
		findReg = null;
		img = null;

		// mor
		try {
			regMatch = reg.wait(morPattern, .01);

			findReg = new Region(regMatch.getX() + 10, regMatch.getY() + 55, 40, 35);
			origImage = new File(findReg.saveScreenCapture());
		} catch (FindFailed e) {
			// TODO Auto-generated catch block

		}
		if (regMatch != null) {
			try {
				img = ImageIO.read(origImage);
			} catch (IOException e) {
				img = null;
			}
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
			model.morImages.add(img);
		}
		if (model.glorySaved == 0) {
			regMatch = null;
			origImage = null;
			findReg = null;
			img = null;

			// glory
			try {
				regMatch = reg.wait(gloryPattern, .01);

				findReg = new Region(regMatch.getX() + 15, regMatch.getY() + 50, 50, 30);
				origImage = new File(findReg.saveScreenCapture());
			} catch (FindFailed e) {
				// TODO Auto-generated catch block

			}
			if (regMatch != null) {
				try {
					img = ImageIO.read(origImage);
				} catch (IOException e) {
					img = null;
				}
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
				model.gloryImages.add(img);
			}
		}

		regMatch = null;
		origImage = null;
		findReg = null;
		img = null;

		// quartz
		try {
			regMatch = reg.wait(quartzPattern, .01);

			findReg = new Region(regMatch.getX() + 15, regMatch.getY() + 45, 50, 35);
			origImage = new File(findReg.saveScreenCapture());
		} catch (FindFailed e) {
			// TODO Auto-generated catch block

		}
		if (regMatch != null) {
			try {
				img = ImageIO.read(origImage);
			} catch (IOException e) {
				img = null;
			}
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
			model.quartzImages.add(img);
		}

		regMatch = null;
		origImage = null;
		findReg = null;
		img = null;

		// refined
		try {
			regMatch = reg.wait(refinedPattern, .01);

			findReg = new Region(regMatch.getX() - 5, regMatch.getY() + 55, 70, 35);
			origImage = new File(findReg.saveScreenCapture());
		} catch (FindFailed e) {
			// TODO Auto-generated catch block

		}
		if (regMatch != null) {
			try {
				img = ImageIO.read(origImage);
			} catch (IOException e) {
				img = null;
			}
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
			model.refinedImages.add(img);
		}

		regMatch = null;
		origImage = null;
		findReg = null;
		img = null;

	}



}
