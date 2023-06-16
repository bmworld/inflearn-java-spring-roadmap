package core.spring.singletone;

public class StatefulService {
    private int price; // 상태를 유지하는 필드 ( 싱글톤 서비스의 안 좋은 사례 테스트에 사용)

    public void order(String name, int price) {
        System.out.println("name = " + name + " price = " + price);
        this.price = price; // 여기가 문제!
    }

     public int getPrice() {
        return price;
    }
}
