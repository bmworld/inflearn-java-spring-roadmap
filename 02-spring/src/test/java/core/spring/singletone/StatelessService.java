package core.spring.singletone;

public class StatelessService {

    public int order(String name, int price) {
        System.out.println("name = " + name + " price = " + price);
        return price; // 여기가 문제!
    }

}
