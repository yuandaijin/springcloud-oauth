# springcloud-oauth

#### 介绍
springcloud G版集成最新版oauth2。支持微信，手机验证码，集成验证码，单点登录。
权限控制由springcloud gateway统一鉴权。

项目结构

		auth               
		  auth-client	   ---鉴权客户端
		  auth-server	   ---认证授权服务
		gateway   		   ---网关	
		other			   ---脚本
		user               
		  client           ---用户服务对外api 
		  common		   ---用户服务对外api实体	
		  server		   ---用户服务	
		util			   ---工具包
		
		




