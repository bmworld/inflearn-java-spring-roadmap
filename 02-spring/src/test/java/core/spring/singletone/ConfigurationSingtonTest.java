package core.spring.singletone;

import core.spring.config.AppConfig;
import core.spring.repository.MemberRepository;
import core.spring.service.MemberServiceImpl;
import core.spring.service.OrderServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

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
}
