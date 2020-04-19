package com.venues.lt.demo.config;


import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
/*
    application上不使用ServletComponentScan注解时，需要有这个类来注册servlet和filter

    Druid框架需要两个部分 servlet 和 filter 在这个包下的另外两个文件DruidStatFilter和DruidStatViewServlet分别完成了这两部分，
    是通过@WebFilter注解和@WebServlet注解标注的，如果启动类上加了@ServletComponentScan注解，就会自动装配这两个类来完成Druid的配置，这算自动装配。
    当然了，还需要DruidDataSourceConfig来导入Druid的属性。
    第二种方式就是启动类不加@ServletComponentScan注解，这时需要写个类把Druid需要的WebStatFilter和StatViewServlet装配进来，就是当前类，类的开头有个@SpringBootConfiguration，
    来让spring装配这个类，用@Bean把需要的东西注入框架，这算手动完成配置。By the way 配置方方式除了这两种还有读取xml文件的方式，这种方式需要在yml文件中指定该xml文件的位置才行。

    @Bean把类交给spring管理后，可以用@Autowired来快速得到实例，或者说可以用@Autowired来得到所有spring管理的类的一个实例。
    以这个类举例，当别的地方调用
    @Autowired
    ServletRegistrationBean servletRegistrationBean;  的时候，实际上执行的是servletRegistrationBean() 方法来得到一个servletRegistrationBean实例，也就是调用构造方法，
    说是构造方法可能不是很准确，因为这个函数名可以任意取，而构造方法是必须和类同名，总之是通过这个可以得到类实例的方法来实现@Autowired注入的。
    记录这个主要是因为刚接触spring 不知道为啥spring可以吧别的类用@Autowired注入进来得到实例的，原来是调用了构造方法。
 */
@SpringBootConfiguration
public class DruidMonitorConfig {

    private static final Logger logger = LoggerFactory.getLogger(DruidMonitorConfig.class);

    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
       // logger.info("init Druid Monitor Servlet ...");
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(),
                "/druid/*");
        // IP白名单
        servletRegistrationBean.addInitParameter("allow", "192.168.1.101,127.0.0.1");
        // IP黑名单(共同存在时，deny优先于allow)
        servletRegistrationBean.addInitParameter("deny", "192.168.1.100");
        // 控制台管理用户
        servletRegistrationBean.addInitParameter("loginUsername", "admin");
        servletRegistrationBean.addInitParameter("loginPassword", "admin");
        // 是否能够重置数据 禁用HTML页面上的“Reset All”功能
        servletRegistrationBean.addInitParameter("resetEnable", "false");
        return servletRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }

}
