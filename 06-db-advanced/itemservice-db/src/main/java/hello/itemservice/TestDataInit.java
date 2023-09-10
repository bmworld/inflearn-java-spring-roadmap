package hello.itemservice;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@Slf4j
@RequiredArgsConstructor
public class TestDataInit {

    private final ItemRepository itemRepository;

    /**
     * <h1>확인용 초기 데이터 추가</h1>
     * <pre>
     *   - 이 기능이 없으면, 서버 실행할 때마다, 초기 데이터를 입력해야한다.
     *   - `@EventListener(ApplicationReadyEvent.class)`
     *     : AOP 포함한 Spring Container 완전히 초기화된 이후 호출됨
     *
     *   - cf)
     *     `@PostConstruct` 사용 시, AOP 같은 부분이 아직 처리완료되지 않은 시점에 호출될 수 있음.
     *     ex.  @Transactional 과 관련된 AOP 미적용 상태에서 호출
     * </pre>
     *
     */
    @EventListener(ApplicationReadyEvent.class)
    public void initData() {
        log.info("test data init");
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }

}
