# ankang-report
report 是一个开源的访问层框架，基于java平台，依赖于spring，以Mozilla Public License 2.0 协议发布。适用于目前所有的分布式开发模式下的接口测试，以及访问控制层的使用，简化控制层的繁琐代码，简化spring mvc的配置，让控制层和服务接口一样简单易写，极大的提高项目开发速度。report是一个轻便可控、扩展性强的框架。report的测试统计、监控等功能为你的每一个接口展示一份完美的报告。
  report的关键特性：
    a)让所有接口可视化清晰可见，易于服务层的开发
    b)为每一个接口提供测试统计以及报表展示
    c)简化spring mvc繁琐配置，让控制层像service一样清晰明了
    d)report请求通道采用职责链的模式，扩展性强，完全可控
    e)支持多种数据请求方式并可扩展，默认可选json，xml方式
  
  report使用方式：
    a)maven项目加入report依赖，普通项目则导入jar包
    b)加载spring文件ankang-report.xml
    c)继承ReportStart类，
        @Component//通过spring注入
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
  report可配置项
    
