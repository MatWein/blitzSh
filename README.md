# blitzSh
## Description
blitzSh is a graphical interface for various terminals or consoles.

## Features
- Dynamic configuration  
![Alt text](/blitzsh.app/screenshots/settings.png?raw=true "Settings")
- Adjustable colors
- Transparency support  
![Alt text](/blitzsh.app/screenshots/terminals.png?raw=true "Terminals")
- SSH Remote Terminals
- Fullscreen (F11)  
![Alt text](/blitzsh.app/screenshots/fullscreen.png?raw=true "Fullscreen")
- TrayIcon  
![Alt text](/blitzsh.app/screenshots/trayicon.png?raw=true "TrayIcon")
- Platform independent
- Search (Ctrl+F)  
![Alt text](/blitzsh.app/screenshots/search.png?raw=true "Search")
- Group terminal configurations by creating folders / sub folders
- UI support for multiple languages (currently English and German)

## Requirements
- Java Runtime Version 11
- Windows / Linux / MacOS

## Run application
To run the application download a binary jar at the release-section of github and double click on the file or run it via: 
```
java -jar blitzsh.app-full.jar
```

## Known issues
If the application does not show a tray icon on linux try to add the following jvm args:  
```
-Djdk.gtk.version=3
```
