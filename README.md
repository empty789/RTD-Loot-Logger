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
* [Nox Player](https://www.bignox.com/)
* Right nox settings, see below
* Nox resolution set to 1280x720 or 1920x1080
* Nox Players windowtitle needs to contain "NoxPlayer" (can be different when using multiple nox instances)

# Settings Nox

Select 1280x720 or 1920x1080.
![NoxSettings2](https://i.imgur.com/jJUclJm.png)

I highly recommend locking the resolution, so you dont accidently change it.
![NoxSettings](https://i.imgur.com/wwD5lxb.png)

# How to use it

1. Download the package from [release section](https://github.com/empty789/RTD-Loot-Logger/releases), unzip it and keep the folder structure (there needs to be a "img" and a "tessdata" Folder in the same folder as the .jar), start the .jar
2. Go to the Adventure you want to farm and start it
3. Enter the stage you are farming in the textbox down left
3. Press start

# How it works

You can start the logger everytime, while farming. It will wait for the next reward screen and after that start logging time and rewards.
Time is measured from start to start, the image its looking for to determine that is the "ON Battery save" icon in the right.
When the reward is on screen, it will take multiple screenshots for 4 seconds and check them all afterwards to increase accuracy of the values.

Dont block the reward region (1. red square) and the on battery save icon (2. red square).
This is how I setup Nox and the logger.
![noxsettings5](https://i.imgur.com/x0b8uBy.png)
