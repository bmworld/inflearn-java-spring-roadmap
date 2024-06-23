package hello.container;

import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.HandlesTypes;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

/**
 *
 *
 * <h1>Servlet 등록 (Annotation Ver.)</h1>
 *
 * @HandleTypes Annotation 에 App init interface 지정 -> Set<Class<?>> Param에 지정된 Class 를 받을 수 있다.</?>
 */
@HandlesTypes(AppInit.class) // interface 구현체 모두를 가져온다.
public class MyContainerV2 implements ServletContainerInitializer {
  //
  @Override
  public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
    System.out.println("MyContainerV2.onStartup");
    System.out.println("c = " + c);
    System.out.println("ctx = " + ctx);

    for (Class<?> appInitClass : c) {
      try {
        // 아래코드는 다음 코드와 같음 => new AppInitV1Servlet()
        AppInit appInit = (AppInit) appInitClass.getDeclaredConstructor().newInstance();

        // 구현체의 메서드 실행시킴
        appInit.onStartup(ctx);

      } catch (InstantiationException e) {
        throw new RuntimeException(e);
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      } catch (InvocationTargetException e) {
        throw new RuntimeException(e);
      } catch (NoSuchMethodException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
