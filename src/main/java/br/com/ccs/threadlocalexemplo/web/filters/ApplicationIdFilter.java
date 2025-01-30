package br.com.ccs.threadlocalexemplo.web.filters;

import br.com.ccs.threadlocalexemplo.core.ApplicationIdHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ApplicationIdFilter extends OncePerRequestFilter {

    private static final String APPLICATION_ID = "Exemplo ThreadLocal";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            ApplicationIdHolder.set(APPLICATION_ID);
            filterChain.doFilter(request, response);
        } finally {
            ApplicationIdHolder.remove();
        }
    }
}
