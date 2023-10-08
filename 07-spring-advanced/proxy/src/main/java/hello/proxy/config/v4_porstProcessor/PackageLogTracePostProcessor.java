package hello.proxy.config.v4_porstProcessor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * <h1>postProcessAfterInitialization 등록 시, Package 제한을 두는 이유</h1>
 * <pre>
 *    SpringBoot package 등 모---든 Bean이 다 넘어오므로,
 *    필요한 것만 filtering 하기 위함.
 *  </pre>
 */
@Slf4j
public class PackageLogTracePostProcessor implements BeanPostProcessor {
  private final String basePackage;
  private final Advisor advisor;

  public PackageLogTracePostProcessor(String basePackage, Advisor advisor) {
    this.basePackage = basePackage;
    this.advisor = advisor;
  }

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    log.info("[postProcessAfterInitialization] check param >>> bean.getClass()={} beanName={}", bean.getClass(), beanName);

    // Proxy 적용 대상 여부 체크: Proxy 적용 대상 아닐 시, 원본 그대로 등록
    String packageName = bean.getClass().getPackageName();
    if (!packageName.startsWith(basePackage)) {
      return bean;
    }

    // Proxy 대상일 경우, Proxy 생성 후 반환
    ProxyFactory proxyFactory = new ProxyFactory(bean);
    proxyFactory.addAdvisor(advisor);

    Object proxy = proxyFactory.getProxy();
    log.info("[postProcessAfterInitialization] create proxy >>> target={}, proxy={}", bean.getClass(), proxy.getClass());
    return proxy;
  }
}
