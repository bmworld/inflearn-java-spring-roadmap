package core.spring.singletone;

import core.spring.config.AppConfig;
import core.spring.repository.MemberRepository;
import core.spring.service.MemberServiceImpl;
import core.spring.service.OrderServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

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
}
