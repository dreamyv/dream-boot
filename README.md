雷霆网关服务:

thunderbolt-gateway-server
网关服务,主要负责接受Tbox雷霆协议数据,完成简单的注册激活,进行登入登出简单校验做出响应。将数据转发到dispatcher中

thunderbolt-gateway-tcu
自模拟网关客户端,主要用来测试

thunderbolt-gateway-dispatcher
处理来自网关的雷霆协议数据,将协议解码,转换对应国标协议发送国家。
将信号数据标准封装发送到status
将位置数据标准封装发送到location
将行程充电数据标准封装发送到driver
将告警/故障/报警数据封装标准发送到fault

thunderbolt-gateway-protocol
雷霆协议公用抽取

thunderbolt-core
雷霆核心包

thunderbolt-ev-gb-center
模拟国标平台

thunderbolt-ev-gb-uploader
国标平台客户端