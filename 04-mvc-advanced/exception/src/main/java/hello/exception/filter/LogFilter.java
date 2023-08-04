package hello.exception.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

/**
 * <h1>DispatcherType</h1>
 * <pre>
 *   Filter 로그 찍어보면....
 *   1. DispatcherType = REQUEST
 *     log => REQUEST  [815b4bf9-889d-48c2-ab64-3bd35ed68c63][REQUEST][/error-ex]
 *
 *   [[[[[[ 서버 내부에서 에러 터짐 ]]]]]]
 *
 *   2. DispatcherType = ERROR
 *    log => REQUEST [959e57b5-0223-4c04-a6ad-de50f7f9a651][ERROR][/error-page/500]
 * </pre>
 *
 */
@Slf4j
public class LogFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("log filter init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        String uuid = UUID.randomUUID().toString();

        try {
            log.info("REQUEST  [{}][{}][{}]", uuid, request.getDispatcherType(), requestURI);
            chain.doFilter(request, response);
        } catch (Exception e) {
            log.info("---- EXCEPTION ERROR MESSAGE =  {}", e.getMessage());
            throw e;
        } finally {
            log.info("RESPONSE [{}][{}][{}]", uuid, request.getDispatcherType(), requestURI);
        }

    }

    @Override
    public void destroy() {
        log.info("log filter destroy");
    }
}
