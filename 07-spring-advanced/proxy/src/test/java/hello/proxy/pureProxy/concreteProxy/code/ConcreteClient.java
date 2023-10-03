package hello.proxy.pureProxy.concreteProxy.code;

/**
 * <h1>구체Class Proxy 적용의 핵심</h1>
 * <pre>
 *   - 부모인 concreteLogic 뿐만 아닌, 해당 부모를 상속받은 자식(proxy)를 주입할 수 있다.
 *
 *   - 따라서, Interface 없이, Class 기반의 Proxy 적용 가능 (by java polymorphism)
 * </pre>
 */
public class ConcreteClient {
  private ConcreteLogic concreteLogic;

  public ConcreteClient(ConcreteLogic concreteLogic) {
    this.concreteLogic = concreteLogic;
  }

  public void execute() {
    concreteLogic.operation();
  }
}
