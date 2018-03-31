
下载地址：
	百度云：https://pan.baidu.com/s/1zY0zLrjq2SbmapvcWjYEMw
	密码：rnr6
使用指南：
	1.修改配置文件config.properties
		blogUrl为学生评论（blog url+学号+github url）的blog地址
		saveDir保存下载的目录
		cacheSize缓存的数度，会影响下载速度，以k为单位默认1024K，一般可根据网速设置。
	2.运行：java -Xmx2048m -jar  tool.jar  或者双击run.bat  
	推荐使用2G内存运行，也可使用更多的内存，自需要更改最大堆内存即可。
	
	注：
		download.txt：记录下载完成的学生学号
		err.txt：记录下载出现异常的学生学号