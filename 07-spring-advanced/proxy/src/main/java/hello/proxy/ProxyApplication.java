package hello.proxy;

import hello.proxy.config.v1_proxy.ConcreteProxyConfig;
import hello.proxy.config.v1_proxy.InterfaceProxyConfig;
import hello.proxy.trace.logtrace.LogTrace;
import hello.proxy.trace.logtrace.ThreadLocalLogTrace;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;


//@Import(AppV1Config.class)
//@Import({AppV1Config.class, AppV2Config.class})
//@Import(InterfaceProxyConfig.class)
@Import(ConcreteProxyConfig.class)
@SpringBootApplication(scanBasePackages = "hello.proxy.app") //주의
public class ProxyApplication {
	public static void main(String[] args) {
		SpringApplication.run(ProxyApplication.class, args);
	}


	/**
	 * 공통으로 적용되는 Configuration을 손쉽게 적용하기위해...아래와 같은 방법으로 Bean 등록을 함.
	 * 실무에서는 아래와 같이 하지 말 것.
	 */
	@Bean
	public LogTrace logTrace() {
		System.out.println("AppCommonConfig 등록됨!!");
		return new ThreadLocalLogTrace();
	}

}
