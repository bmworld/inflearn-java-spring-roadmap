package hello.typeconverter.formatter;

import hello.typeconverter.converter.IpPortToStringConverter;
import hello.typeconverter.converter.StringToIpPortConverter;
import hello.typeconverter.type.IpPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.format.support.DefaultFormattingConversionService;

import static org.assertj.core.api.Assertions.assertThat;

public class FormattingConversionService {

  @Test
  @DisplayName("formattingConversionService")
  public void formattingConversionService() throws Exception {
    // Given
    DefaultFormattingConversionService cs = new DefaultFormattingConversionService();

    // Converter 등록 또한 등록 가능
    cs.addConverter(new StringToIpPortConverter());
    cs.addConverter(new IpPortToStringConverter());

    // Formatter 등록
    cs.addFormatter(new MyNumberFormatter());



    // 컨버터 사용
    IpPort ipPort = cs.convert("127.0.0.1:8080", IpPort.class);
    assertThat(ipPort).isEqualTo(new IpPort("127.0.0.1", 8080));




    // 포매터 사용
    String convertToString = cs.convert(1000, String.class);
    assertThat(convertToString).isEqualTo("1,000");



    Long convertLong = cs.convert("1,000", Long.class);
    assertThat(convertLong).isEqualTo(1000L);


  }
}
