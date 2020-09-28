# Backend
后端主要使用Springboot/JPA/Redis等技术，数据库使用Oracle。
Springboot/JPA/Redis are used in backend,the database uses Oracle.

## Development server
运行DemoApplication文件启动服务

## Packages
|Package                              | Description                               |
|:------------------------------------|:------------------------------------|
|annotation                              |自定义注释|
|aop | 定义切面拦截|
|&ensp;- CheckLoginAspect | 验证是否登录 |
|&ensp;- CheckRightAspect | 验证是否有权限 |
|bean | API接参实体|
|config | 项目配置|
|&ensp;- RedisConfig | RedisTemplate配置 |
|ctrl | 控制器|
|cusinterface | 数据占用检查|
|entity | 数据库表实体|
|exception | 自定义错误|
|&ensp;- BaseException | 自定义异常 |
|&ensp;- ExceptionLog | 异常日志记录 |
|&ensp;- SMPException | 全局异常捕捉 |
|repository| 数据访问层|
|util | 工具|

## Configuration
Oracle数据库连接配置+Redis服务配置
