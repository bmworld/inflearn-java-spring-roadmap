package hello.typeconverter;

import hello.typeconverter.converter.IpPortToStringConverter;
import hello.typeconverter.converter.StringToIpPortConverter;
import hello.typeconverter.formatter.MyNumberFormatter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addFormatters(FormatterRegistry registry) {
    // Formatter 내의 숫자<-> String 변환 시, "Converter에게 우선순위"가 밀리기 때문에
    // Formatter 정상작동을 위해 주석처리.


//    registry.addConverter(new StringToIntegerConverter());
//    registry.addConverter(new IntegerToStringConverter());
    registry.addConverter(new StringToIpPortConverter());
    registry.addConverter(new IpPortToStringConverter());


    // 추가
    registry.addFormatter(new MyNumberFormatter());

  }
}
