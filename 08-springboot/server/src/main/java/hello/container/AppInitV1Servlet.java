package hello.container;

import hello.servlet.HelloServlet;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration.Dynamic;

public class AppInitV1Servlet implements AppInit {

  /**
   *
   *
   * <h1>Servlet 등록 (Programming Ver.)</h1>
   */
  @Override
  public void onStartup(ServletContext servletContext) {
    System.out.println("AppInitV1Servlet.onStartup");
    System.out.println("servletContext = " + servletContext);

    // 순수 Servlet Code 등록
    Dynamic helloServlet = servletContext.addServlet("helloServlet", new HelloServlet());

    // url mapping
    helloServlet.addMapping("/hello-servlet");
  }
}
