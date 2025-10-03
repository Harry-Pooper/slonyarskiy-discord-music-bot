# Slonyarskiy discord music bot
Huge shoutout to: 

[sedmelluq/lavaplayer](https://github.com/sedmelluq/lavaplayer)

[discord-jda/JDA](https://github.com/discord-jda/JDA)

[lavalink-devs/lavaplayer](https://github.com/lavalink-devs/lavaplayer)

Simple discord music bot to play music (for now only source for music is youtube).

Sole purpose of this bot is to play music and nothing else, because every bot in the internet costs money, and me being cheapskate I created this bot. Code that was written for this bot isn't good, but it does this job and I'm ok with it.

## Requirements
Java 21+ (could be lowered in pom.xml)

OR Docker

## Installment
### By .jar executable
1. Pull all the latest changes
2. Package the project using maven (mvn clean package)
3. Set system environment %token% with your bot's token
4. Set system environment %configFilePath% to specify where config file will be saved (defaulted by "app/config")
5. Run output .jar file

### By Docker
Run docker image harrypooper/slonyarskiy-discord-bot:latest
```
docker run -d -e "token=*****" harrypooper/slonyarskiy-discord-bot:latest
```

## Usage
List of available commands:
* s!help - Prints all available commands for bot
* s!admin - Shows commands for administrators of guild (used to configure roles that can use this bot)
* s!play [url] - Insert song to current queue [url of song from youtob]
* s!skip [count] - Skip current song [count - count of songs to skip (optional)]
* s!stop - Stop playing songs
* s!playlist [page] - Show current playlist [page - number of page of playlist (optional)]
* s!np - Show currently playing song

## Disclaimer
There can be bugs, if bug occurs - reset docker container, and you will be good to go.

I will try to use this bot as much as I can to test and find bugs. If anything occurs, feel free to create an issue
