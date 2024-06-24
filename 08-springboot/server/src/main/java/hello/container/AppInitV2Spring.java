package hello.container;

import hello.controller.HelloConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration.Dynamic;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 *
 *
 * <h1>Spring 도움없이, Servlet 초기화를 수동으로 해보자.</h1>
 */
public class AppInitV2Spring implements AppInit {

  @Override
  public void onStartup(ServletContext servletContext) {
    System.out.println("AppInitV2Spring.onStartup");
    System.out.println("servletContext = " + servletContext);

    // Spring container 생성
    AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
    //
    appContext.register(HelloConfig.class);

    // 스프링 MVC Dispatcher Servlet 생성 & 스프링 Container 연결
    DispatcherServlet dispatcher = new DispatcherServlet(appContext);

    // Dispatcher Servlet 등록 (이름 주의! => 이름 중복 시, 오류 발생)
    Dynamic servlet = servletContext.addServlet("dispatcherV2", dispatcher);

    // Dispatcher Servlet 은 SpringController 을 다음 패턴으로 찾아서, 실행한다.
    // 이때, "/spring" 제외한 path (아래에서는 *)가 매핑된 Controller 의 Method 를 찾아서 실행한다.
    servlet.addMapping("/spring/*");
  }
}
