package hello.typeconverter.converter;

import hello.typeconverter.type.IpPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ConverterTest {
  @Test
  @DisplayName("StringToInteger")
  public void StringToInteger() throws Exception {
    // Given
    String target = "10";
    Integer expected = Integer.valueOf(target);

    StringToIntegerConverter cvt = new StringToIntegerConverter();
    Integer result = cvt.convert(target);
    assertThat(result).isEqualTo(expected);

  }


  @Test
  @DisplayName("IntegerToString")
  public void IntegerToString() throws Exception {
    // Given
    Integer target = 10;
    String expected = String.valueOf(target);

    IntegerToStringConverter cvt = new IntegerToStringConverter();
    String result = cvt.convert(target);
    assertThat(result).isEqualTo(expected);



  }



  @Test
  @DisplayName("StringToIpPort")
  public void StringToIpPort() throws Exception {
    // Given

    IpPortToStringConverter cvt = new IpPortToStringConverter();
    String ip = "127.0.0.1";
    int port = 8080;
    String ipWithPort = ip + ":"+ port;
    IpPort source = new IpPort(ip, port);
    String result = cvt.convert(source);

    assertThat(result).isEqualTo(ipWithPort);

  }


  @Test
  @DisplayName("IpPortToString")
  public void IpPortToString() throws Exception {
    // Given
    String ip = "127.0.0.1";
    int port = 8080;
    String ipWithPort = ip + ":"+ port;
    StringToIpPortConverter cvt = new StringToIpPortConverter();
    IpPort result = cvt.convert(ipWithPort);


    assertThat(result.getIp()).isEqualTo(ip);
    assertThat(result.getPort()).isEqualTo(port);
    assertThat(result).isEqualTo(new IpPort(ip, port)); // EqualsAndHashCode는 내부 값이 동일한 경우, 동등처리 함.

  }
}
