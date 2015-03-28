# SwissKnightMinecraft

Minecraft.net stuff from the Swiss Knights, both the junior and senior ;)

How to create a Bukkit Server
===

1. mvn package
1. http://www.spigotmc.org/wiki/buildtools/
1. wget https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar
1. java -jar BuildTools.jar 
1. http://wiki.bukkit.org/Setting_up_a_server 
1. java -Xmx1024M -jar craftbukkit-1.8.3.jar --help
1. java -Xmx1024M -jar craftbukkit-1.8.3.jar
1. cp target/*.jar ../plugins/
1. ln -s target/*.jar ../plugins/plugin1-0.0.1-SNAPSHOT.jar
1. java -Xmx1024M -jar craftbukkit-1.8.3.jar
