package core.spring.singletone;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * <h1>Spring Bean은 항상 '무상태'(stateless)로 설계하시라</h1>
 */
class StatefulServiceTest {

    @Test
    @DisplayName("stateful Service as Singleton")
    public void statefulServiceAsSingleton() {

        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
        // 1. 조회: 호출할 때마다 객체 생성
        StatefulService statefulService1 = ac.getBean("statefulService", StatefulService.class);
        StatefulService statefulService2 = ac.getBean("statefulService", StatefulService.class);

        // ThreadA 사용자 주문: A 사용자 10,000원 주문을 한다.
        int userAPrice = 10000;
        statefulService1.order("userA", userAPrice);



        // ThreadB 사용자 주문: B 사용자 20,000원 주문을 한다.
        int anotherUserPrice = 20000;
        statefulService2.order("userB", anotherUserPrice); // ! stateful로 인하여, 유저 A의 Price도 변경됨




        //Thread A: 사용자 A 주문금액 조회
        int user1Price = statefulService1.getPrice();
        System.out.println("user1Price = " + user1Price);
        assertThat(statefulService1).isSameAs(statefulService2);
        assertThat(statefulService1.getPrice()).isEqualTo(anotherUserPrice);
        assertThat(statefulService2.getPrice()).isEqualTo(anotherUserPrice);


    }



    static class TestConfig {
        @Bean
        public StatefulService statefulService() {
            return new StatefulService();
        }
    }


}
