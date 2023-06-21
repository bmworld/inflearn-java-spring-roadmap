package core.spring.singletone;

import core.spring.repository.MemberRepository;
import core.spring.repository.MemoryMemberRepository;
import core.spring.service.MemberService;
import core.spring.service.MemberServiceImpl;
import core.spring.service.OrderService;
import core.spring.service.OrderServiceImpl;
import core.spring.service.discount.DiscountPolicy;
import core.spring.service.discount.RateDiscountPolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * <p>@Bean 붙은 메서드마다, 이미 Spring Bean이 존재하면, 존재하는 Bean 을 반환하고,</p>
 * <p>Spring Bean 없으면 신규 생성해서 Spring Bean으로 등록하고 반환하는 code가 동적으로 만들어진다.</p>
 */
public class ConfigurationSingtonTest {
    @Test
    @DisplayName("@Configuration으로 등록한 Bean의 Singleton 점검 => 각기 다른 Service에서 new 생성자로 생성했으나, Spring이 singleton으로 관리해준다!")
    public void configurationTest() {
        // Given / When
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
        MemberServiceImpl memberService = ac.getBean("memberService", MemberServiceImpl.class);
        OrderServiceImpl orderService = ac.getBean("orderService", OrderServiceImpl.class);

        MemberRepository instanceFromMemberService = memberService.getMemberRepository();
        MemberRepository instanceFromOrderService = orderService.getMemberRepository();

        MemberRepository memberRepository = ac.getBean("memberRepository", MemberRepository.class);
        // Then
        System.out.println("mrFromMemberService = " + instanceFromMemberService);
        System.out.println("memberRepositoryFromOrderService = " + instanceFromOrderService);
        System.out.println("originMemberRepository = " + memberRepository);

        assertThat(instanceFromMemberService).isSameAs(instanceFromOrderService);
        assertThat(instanceFromMemberService).isSameAs(memberRepository);
        assertThat(memberRepository).isSameAs(instanceFromOrderService);

    }


    @Test
    @DisplayName("@Configuration으로 등록한 Bean의 Singleton 점검 => 각기 다른 Service에서 new 생성자로 생성했으나, Spring이 singleton으로 관리해준다!")
    public void configurationDEEEEEEPTest() {
        // Given / When
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
        AppConfig bean = ac.getBean(AppConfig.class);


        // Spring 등록 Bean => 내가 만든 것이 아니다.
        // Spring이 CGLIB Library 사용하여, `Byte Code` 조작 => AppConfig.class를 상속받은 `임의의 다른 Class` 생성 => 이것을 Spring Bean으로 등록
        // AppConfig$$EnhancerBySpringCGLIB$$aafd389d
        // .......CGLIB $$...
        // CGLIB를 보시라.
        System.out.println("bean.getClass() = " + bean.getClass()); // => class core.spring.config.AppConfig$$EnhancerBySpringCGLIB$$aafd389d

    }


    @Configuration
    public static class AppConfig {

        @Bean(name = "renamed_discountPolicy") // Spring Container 에 등록할 이름 지정가능 / But, 왠만하면 그냥 쓰시라.
        public DiscountPolicy discountPolicy() {
            return new RateDiscountPolicy();
        }

        @Bean  // Spring Container 의 관리대상이 된다.
        public MemberService memberService () {
            System.out.println("call AppConfig.memberService");
            return new MemberServiceImpl(memberRepository()); // 생성자 주입 (DI)
        }

        @Bean
        public OrderService orderService() {
            System.out.println("call AppConfig.orderService");
            return new OrderServiceImpl(memberRepository(), discountPolicy());

        }

        @Bean
        public MemoryMemberRepository memberRepository() {
            System.out.println("call AppConfig.memberRepository");
            return new MemoryMemberRepository();
        }
    }
}
