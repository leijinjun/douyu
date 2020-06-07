# 斗鱼弹幕抓取程序
斗鱼弹幕连接客户端是一个快速连接斗鱼弹幕服务器，获取指定主播房间弹幕、送礼等消息的应用程序。
# 应用介绍
1. douyu-front应用为展示斗鱼弹幕分析数据。
2. douyu-admin应用为管理弹幕连接服务器。
# 安装所需依赖
1. jdk,最低版本1.8
2. Elasticsearch组件，版本为6.2.4。需要安装IK分词器，下载地址：https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v6.2.4/elasticsearch-analysis-ik-6.2.4.zip
3. mysql
# 连接指定房间
URL:/admin/room/client/login/{room}
