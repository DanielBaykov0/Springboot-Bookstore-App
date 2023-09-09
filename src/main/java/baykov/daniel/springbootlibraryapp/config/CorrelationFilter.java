package baykov.daniel.springbootlibraryapp.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@WebFilter
public class CorrelationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String correlationId = request.getHeader("X-Correlation-ID");

        try {
            if (correlationId == null || correlationId.isEmpty()) {
                correlationId = generateCorrelationId();
            }

            RequestData.setCorrelationId(correlationId);
            MDC.put("correlationId", correlationId);

            filterChain.doFilter(request, response);
        } finally {
            MDC.remove("correlationId");
            RequestData.clear();
        }
    }

    private String generateCorrelationId() {
        return UUID.randomUUID().toString();
    }
}
