package hello.boot;

import java.util.List;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class MySpringApplication {
  public static void run(Class configClass, String[] args) {
    System.out.println("MySpringApplication.run");
    System.out.println("List.of(args) = " + List.of(args));
    // Settings - Tomcat
    Tomcat tomcat = new Tomcat();
    Connector connector = new Connector();
    connector.setPort(8080);
    tomcat.setConnector(connector);

    // Create Spring Container
    AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
    appContext.register(configClass);

    // Create Spring MVC Dispatcher Servlet & connect to Spring Container with Servlet
    DispatcherServlet dispatcher =
        new DispatcherServlet(appContext); // 서블릿에다가 스프링컨테이너를 인식할 수 있게 해주는 거임

    // Register Dispatcher Servlet
    Context context = tomcat.addContext("", "/");
    tomcat.addServlet("", "dispatcher", dispatcher);

    // Servlet 에 pattern 인식 후, 해당 dispatcher servlet 은 Spring container 에서 Controller 찾아서, 호출함.
    context.addServletMappingDecoded("/", "dispatcher");

    // Start tomcat
    try {
      tomcat.start();
    } catch (LifecycleException e) {
      throw new RuntimeException(e);
    }
  }
}
