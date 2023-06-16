package core.spring.singletone;


import core.spring.config.AppConfig;
import core.spring.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

public class SingletonTest {

    @Test
    @DisplayName("스프링이 아닌 순수 DI Container - 신규 요청이 들어올 때마다, 객체를 `신규 생성` -> 메모리 낭비가 심하다.")
    public void pureContainer() {
        AppConfig appConfig = new AppConfig();
        // 1. 조회: 호출할 때마다 객체 생성
        MemberService memberService1 = appConfig.memberService();

        // 2. 조회: 호출할 때마다 객체 생성
        MemberService memberService2 = appConfig.memberService();



        // 참조값 동일 여부 확인
        System.out.println("memberService1 = " + memberService1);
        System.out.println("memberService2 = " + memberService2);

        assertThat(memberService1).isNotEqualTo(memberService2);
        assertThat(memberService1).isNotSameAs(memberService2);

        // isSameAs : 참조값(reference 동일성) 비교
        // isEqualTo: value 비교
    }

    /**
     * 1. static 영역에 객체 instance를 미리 하나를 생성해서 올린다.
     * 2. 이 객체 Instance 필요 시, 오직 `getInstance` Method를 통해서만 조회할 수 있다.
     * 이 메서드를 호출하면 항상 '동일한 Instance'를 반환한다!
     * 3. 딱 1개의 객체 Instance만 존재해야 하므로, 생성자를 `private`으로 막아서
     *  외부에서 `new` Keyword 사용하여 객체 instance 생성을 방지한다.
     */
    @Test
    @DisplayName("SingleTon 패턴 적용한 객체 사용 - Application 시작 시 생성된 Instance 하나만 사용한다.")
    public void singleTon() {

        SingleTonService instance1 = SingleTonService.getInstance();
        SingleTonService instance2 = SingleTonService.getInstance();

        System.out.println("SingleTon instance1 = " + instance1);
        System.out.println("SingleTon instance2 = " + instance2);
        assertThat(instance1).isSameAs(instance2);;

        // isSameAs : 참조값(reference 동일성) 비교
        // isEqualTo: value 비교

    }


    /**
     * Spring의 기본 Bean  등록 방식은 Singleton 이다.<br/>
     * BUT<p>
     * 요청할 때마다, 새로운 객체 생성하여반환하는 기능도 제공한다.<br/>
     *
     * <h1>주의 사항</h1>
     * 1. 상태를 유지하지 않아야 한다.=> 싱글톤 방식은 여러 클라이언트가 '하나의 같은 객체 Instance'를 공유하기 때문<br/>
     * 2. 특정 클라이언트가 값을 변경할 수 있는 필드가 있으면 안 된다 <br/>
     * 3. 읽기만 가능해야한다 (가급적)<br/>
     * 4. Field 대신 Java에서 공유되지 않은, 지역변수, 파라미터 ThreadLocal 등을 사용해야 한다.<br/>
     */
    @Test
    @DisplayName("스프링 컨테이너와 싱글톤")
    public void springContainer() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
        // 1. 조회: 호출할 때마다 객체 생성
        MemberService memberService1 = ac.getBean("memberService", MemberService.class);

        // 2. 조회: 호출할 때마다 객체 생성
        MemberService memberService2 = ac.getBean("memberService", MemberService.class);

        // 참조값 동일 여부 확인
        System.out.println("memberService1 = " + memberService1);
        System.out.println("memberService2 = " + memberService2);


        assertThat(memberService1).isSameAs(memberService2);

        // isSameAs : 참조값(reference 동일성) 비교
        // isEqualTo: value 비교

    }
}
