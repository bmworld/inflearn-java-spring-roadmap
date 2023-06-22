package core.spring.DI;

import core.spring.domain.Grade;
import core.spring.domain.Member;
import core.spring.domain.Order;
import core.spring.repository.MemoryMemberRepository;
import core.spring.service.OrderServiceImpl;
import core.spring.service.discount.FixDiscountPolicy;
import core.spring.service.discount.FixedDiscountVar;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * <h1>"생성자" 주입의 장점</h1>
 * <pre>
 *     1. final keyword를  사용할 수 있다.
 *       => 생성자에서 초기값 미할당 시, 컴파일 시점에 오류 잡을 수 있다.
 *     2. 주입받은 field 불변을 보장함. (생성자에서만 값을 세팅하고, 값을 변경할 수 없음)
 * </pre>
 */
public class AutowiredAtConstructorTest {

    @Test
    @DisplayName("set을 사용한 DI 단위테스트")
    public void autowiredAtConstructor() throws Exception {
        // Given
        MemoryMemberRepository memoryMemberRepository = new MemoryMemberRepository();
        Member member = new Member(1L, "name", Grade.VIP);
        memoryMemberRepository.save(member);
        FixDiscountPolicy fixDiscountPolicy = new FixDiscountPolicy();
        OrderServiceImpl orderService = new OrderServiceImpl(memoryMemberRepository, fixDiscountPolicy);

        // When
        Order order = orderService.createOrder(1L, "item1", 100000);
        // Then
        Assertions.assertThat(order.getDiscountPrice()).isEqualTo(FixedDiscountVar.discountAmount); // VIP 일경우 1,000원 할인

    }
}
