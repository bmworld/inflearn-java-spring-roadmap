package core.spring.singletone;

public class SingleTonService {
    private static final SingleTonService instance = new SingleTonService();

    private SingleTonService() {
    }

    public static SingleTonService getInstance() {
        return instance;
    }

    public void logic() {
        System.out.println("Singleton 객체 Logic 호출!");
    }
}
