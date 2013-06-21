Für die Verwendung der RefereeBox-Gui

1. $sudo apt-get install ros-electric-client-rosjava-jni

2. Auf github-seite des b-it-bots teams(https://github.com/b-it-bots/RoboCupAtWorkRefereeBox/) die "Installation for development"(text unten) durchgehen bis auf "Install OMQ Python Binding". Paketdateien, die heruntergeladen werden müssen, sind schon im Paket RoboCup/RefereeBox

3. aus dem RefereeBox ordner in der console die RefereeBoxGui mit folgendem Befehl starten: 

$java -Djava.library.path=/usr/local/lib -jar RefereeBox.jar

4. config file im gui laden (/RefereeBox/config/config.txt). Serverip, Port und auch Areas, Objects etc. werden in der config.txt festgelegt.

5. Richtigen clienten in der clientnode einstellen: datei: youbot_RC_2dnav/src/command_encoder.cpp, in der mail bei Funktion obtainTaskSpecFromServer("...") ServerIP und Port eintragen

6. client verbinden, encoder für gesendete Strings starten: 

$rosrun youbot_RC_2dnav youbot_RC_commencoder

im Gui sollte der client als verbunden angezeigt werden. Falls nicht:
- überprüfen, ob das GUI Fehler ausgegeben hat. Eventuell prüfen, ob Java richtige version hat oder zeromq mit bindings richtig installiert wurde.

7. Task specification zusammenklicken und speichern

8. "Timer Start", "Send Spec"

9. Der command_encoder encoded den spec_string. 

Für BNT wird die message /youbot_RC_2dnav/locations gesendet  
Für BMT, BTT und CTT wird die message /youbot_RC_2dnav/BMT gesendet

10. Marina anrufen, vielleicht weiß die ja Rat :)
