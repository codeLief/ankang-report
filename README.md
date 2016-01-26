@@ -1,13 +1,21 @@
# ankang-report
report 是一个开源的访问层框架，基于java平台，依赖于spring，以Mozilla Public License 2.0 协议发布。适用于目前所有的分布式开发模式下的接口测试，以及访问控制层的使用，简化控制层的繁琐代码，简化spring mvc的配置，让控制层和服务接口一样简单易写，极大的提高项目开发速度。report是一个轻便可控、扩展性强的框架。report的测试统计、监控等功能为你的每一个接口展示一份完美的报告。
  report的关键特性：
    a)让所有接口可视化清晰可见，易于服务层的开发
    b)为每一个接口提供测试统计以及报表展示
    c)简化spring mvc繁琐配置，让控制层像service一样清晰明了
    d)report请求通道采用职责链的模式，扩展性强，完全可控
    e)支持多种数据请求方式并可扩展，默认可选json，xml方式
 report 是一个开源的访问层框架，基于java平台，依赖于spring，以Mozilla Public License2.0协议发布。适用于目前所有的分布式开发模式下的接口测试，以及访问控制层的使用，简化控制层的繁琐代码，简化spring mvc的配置，剥离控制层的一系列操作，极大的提高项目开发速度。report是一个轻便可控、扩展性强的框架。report的测试统计、监控等功能为你的每一个接口展示一份完美的报告。
 


----------


### **report的关键特性：**

	  a)让所有接口可视化，易于控制开发、并行开发
	  b)为每一个接口提供测试统计以及报表展示
	  c)简化spring mvc繁琐配置
      d)report请求通道采用职责链的模式，扩展性强，完全可控
      e)支持多种数据请求方式并可扩展，默认可选json，xml方式

  
  report使用方式：
### report使用方式：
    a)maven项目加入report依赖，普通项目则导入jar包
    b)引入spring文件 spring/ankang-report.xml,web.xml依赖springMVC配置
    c)继承ReportStart类，
@@ -25,7 +33,7 @@ report 是一个开源的访问层框架，基于java平台，依赖于spring，
        	}
        }
      d)启动项目，访问localhost:8080/项目名/report/test
  report可配置项
### report可配置项
    a)自定义配置文件需采用指定资源地址：report/report-config.properties
    b)REPORT_SWITCH:true 则开启，反之关闭 默认开启
    c)FILETER:自定义过滤器，多个以逗号隔开
@@ -39,9 +47,15 @@ report 是一个开源的访问层框架，基于java平台，依赖于spring，
    j)RESPONSE_MESSAGE:自定义返回体请求消息字段名
    k)RESPONSE_RESULT:自定义返回体数据对象字段名
    
  report注解解释
### report注解解释
    a)@Alias:标注在需要被请求的类，value属性值为请求时使用的服务名，没有标注的report将不予以处理
    b)@HTTP:标注在需要被请求的方法上，value属性值为请求时的方法名，supportMethod属性值为请求类型，默认支持get和post两种，兼容RquestMapping
    c)@ReportParam:标注在需要注入方法的参数上，继承至ReportRequest的参数体不需要标注，HttpServletRequest不需要标注,兼容RequestParam
    d)@Activate:标注在自定义的过滤器上，order属性值为排序值，取值范围int，如有重复则顺延
    

    enter code here

**注：**自定义解析器扩展，过滤器扩展等等参照源码。


