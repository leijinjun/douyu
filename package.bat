@echo off
title package douyu
color 0a
echo starting package douyu.jar
cd /d %~dp0
@call mvn clean
@call mvn package -Dmaven.test.skip=true
echo package douyu.jar success
del %~dp0target\douyu.jar.original
echo 复制 %~dp0target\douyu.jar to D:\douyu_jar
copy %~dp0target\douyu.jar D:\douyu_jar
pause
