
# ankang-report
report 是一个开源的访问层框架，基于java平台，依赖于spring，可用于接口的调试及性能的展示，以及访问控制层的使用，简化控制层的繁琐代码，简化spring mvc的配置，剥离的请求前的一系列操作，耦合性低，REST风格的接口使用简单，report采用责任链模式设计，轻便可控、扩展性强。report的测试统计等功能为你的每一个接口展示一份完美的报告。
 

### **report的关键特性：**

	  a)让所有接口可视化，易于控制开发、并行开发
	  b)为每一个接口提供测试统计以及报表展示
	  c)简化spring mvc繁琐配置
      d)report请求通道采用职责链的模式，扩展性强，完全可控
      e)支持多种数据请求方式并可扩展，默认可选json，xml方式

  

### report使用方式：
    a)maven项目加入report依赖，普通项目则导入jar包
    b)引入spring文件 spring/ankang-report.xml,web.xml依赖springMVC配置
    c)继承ReportStart类，
        @Component//通过spring注入 可设置为单例
        public class BusinessReportConfig extends ReportStart {
        	@Override
        	protected void reginsterReport() {
        		//注册需要被请求的控制层类
        		reginster(OrderHandler.class);
        	}
        	@Override
        	protected void reginsterResolver() {
        		// 注册自定义的解析器 没有自定义可忽略
        		reginster(OrderHandler.class);
        	}
        }
      d)启动项目，访问localhost:8080/项目名/report/console

### report可配置项
    a)自定义配置文件需采用指定资源地址：report/report-config.properties
    b)REPORT_SWITCH:true 则开启，反之关闭 默认开启
    c)FILETER:自定义过滤器，多个以逗号隔开
    d)JSON_BOAY:json数据请求方式key名称，默认为body
    e)XML_BODY:xml数据请求方式key名称，默认为root
    f)MONITOR_FILE_PATH:统计数据保存地址 默认为/report/report.cc
    g)END_INVOKE:请求通道链终端类。 系统默认
    <!--如果不是采用report返回体，则必须配置以下几项，否则影响统计结果-->
    h)IS_DEFINED_RESPONSE:是否自定义返回体结构
    i)RESPONSE_CODE:自定义返回体请求状态码字段名
    j)RESPONSE_MESSAGE:自定义返回体请求消息字段名
    k)RESPONSE_RESULT:自定义返回体数据对象字段名
    l)ENCODE:参数编码格式
    
### report注解解释
    a)@Alias:标注在需要被请求的类，value属性值为请求时使用的服务名，没有标注的report将不予以处理
    b)@HTTP:标注在需要被请求的方法上，value属性值为请求时的方法名，supportMethod属性值为请求类型，默认支持get和post两种，兼容RquestMapping
    c)@ReportParam:标注在需要注入方法的参数上，继承至ReportRequest的参数体不需要标注，HttpServletRequest不需要标注,兼容RequestParam
    d)@Activate:标注在自定义的过滤器上，order属性值为排序值，取值范围int，如有重复则顺延
    


**注：**自定义解析器扩展，过滤器扩展等等参照源码。


