package core.spring.singletone;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * <h1>Spring Bean은 항상 '무상태'(stateless)로 설계하시라</h1>
 */
class StatelessServiceTest {

    @Test
    @DisplayName("stateful Service as Singleton")
    public void statefulServiceAsSingleton() {

        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
        // 1. 조회: 호출할 때마다 객체 생성
        StatelessService statefulService1 = ac.getBean("statelessService", StatelessService.class);
        StatelessService statefulService2 = ac.getBean("statelessService", StatelessService.class);

        // ThreadA 사용자 주문: A 사용자 10,000원 주문을 한다.
        int userAPrice = 10000;
        int priceA = statefulService1.order("userA", userAPrice);


        // ThreadB 사용자 주문: B 사용자 20,000원 주문을 한다.
        int userBPrice = 20000;
        int priceB = statefulService2.order("userB", userBPrice);


        //Thread A: 사용자 A 주문금액 조회
        assertThat(statefulService1).isSameAs(statefulService2);
        assertThat(priceA).isEqualTo(userAPrice);
        assertThat(priceB).isEqualTo(userBPrice);
        assertThat(priceA).isNotEqualTo(priceB);


    }



    static class TestConfig {
        @Bean
        public StatelessService statelessService() {
            return new StatelessService();
        }
    }


}
