# RTD-Loot-Logger
A loot logger for android mobile game Raid the Dungeon. Im by far no professional programmer this is a hobby project, code is messy.

![rtdPreview](https://i.imgur.com/F58YOy6.png)

It uses [SikuliX](http://sikulix.com/) to determine images on the screen and [Tesseract](https://github.com/tesseract-ocr/tesseract) to read values from the images. It was made for Android emulators like Nox Player and only tested with this.

# Features
* logging loot
  * Gold
  * Exp
  * MoR
  * Quartz
  * Refined stones
  * Glory points
* logging time of runs
* export the stats to .csv


# Prerequisites
* java 1.8
* [Latest Visual C++ Redistributable](https://support.microsoft.com/de-de/help/2977003/the-latest-supported-visual-c-downloads)
* A Monitor with a resolution 1920x1080 or higher (I guess)
* [Nox Player](https://www.bignox.com/)
* Right nox settings, see below
* Nox in fullscreen, click the square in the right corner ![noxSettings3](https://i.imgur.com/rGe5u5C.png)
* Nox Players windowtitle needs to contain "NoxPlayer" (can be different when using multiple nox instances)

# Settings Nox

You need to set the resolution to tablet and 1920x1080, for the images to be found correctly. This has to be done, cause the letters of big numbers get very tiny and every pixel counts ;D
![NoxSettings2](https://i.imgur.com/jJUclJm.png)

I highly recommend locking the resolution, so you dont accidently change it.
![NoxSettings](https://i.imgur.com/wwD5lxb.png)

# How to use it

1. Download the Archive from release section, unzip it and keep the folder structure (there needs to be a "img" and a "tessdata" Folder in the same folder as the .jar), start the .jar
2. Go to the Adventure you want to farm and start it
3. Enter the stage you are farming in the textbox down left
3. Press start

# How it works

You can start the logger everytime, while farming. It will wait for the next reward screen and after that start logging time and rewards.
Time is measured from start to start, the image its looking for to determine that is the "ON Battery save" icon in the right.

Dont block the reward region (1. red square) and the on battery save icon (2. red square).
This is how I setup Nox and the logger.
![noxsettings5](https://i.imgur.com/x0b8uBy.png)
