package hello.typeconverter.converter;

import hello.typeconverter.type.IpPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.support.DefaultConversionService;

import static org.assertj.core.api.Assertions.assertThat;

public class ConversionServiceTest {
  @Test
  @DisplayName("conversionService")
  public void conversionService() throws Exception {
    // Given
    DefaultConversionService cs = new DefaultConversionService();
    cs.addConverter(new StringToIntegerConverter());
    cs.addConverter(new IntegerToStringConverter());
    cs.addConverter(new StringToIpPortConverter());
    cs.addConverter(new IpPortToStringConverter());

    // Then
    Integer IntResult = cs.convert("10", Integer.class);
    assertThat(IntResult).isEqualTo(10);

    // Then
    String StringResult = cs.convert(123, String.class);
    assertThat(StringResult).isEqualTo("123");


    // Then
    String StringIpPort = "127.0.0.1:8080";
    IpPort IpPortResult = cs.convert(StringIpPort, IpPort.class);
    IpPort targetIpPort = new IpPort("127.0.0.1", 8080);
    assertThat(IpPortResult).isEqualTo(targetIpPort);


    // Then
    String IpPortAsStringResult = cs.convert(targetIpPort, String.class);
    assertThat(IpPortAsStringResult).isEqualTo(StringIpPort);

  }
}
