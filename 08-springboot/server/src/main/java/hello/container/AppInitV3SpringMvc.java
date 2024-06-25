package hello.container;

import hello.controller.HelloConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration.Dynamic;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class AppInitV3SpringMvc implements WebApplicationInitializer {

  @Override
  public void onStartup(ServletContext servletContext) throws ServletException {
    System.out.println("AppInitV3SpringMvc.onStartup");

    // Spring container 생성
    AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
    //
    appContext.register(HelloConfig.class);

    // 스프링 MVC Dispatcher Servlet 생성 & 스프링 Container 연결
    DispatcherServlet dispatcher = new DispatcherServlet(appContext);

    // Dispatcher Servlet 등록 (이름 주의! => 이름 중복 시, 오류 발생)
    Dynamic servlet = servletContext.addServlet("dispatcherV3", dispatcher);

    // 모든 요청을 해당 Dispatcher Servlet 통하여 요청받도록 함.
    servlet.addMapping("/");
  }
}
