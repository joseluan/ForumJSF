/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filtros;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import utils.ValidatorUtil;

/**
 *
 * @author Luan
 */
@WebFilter(filterName = "LoginFilter", urlPatterns = {"/*"})
public class LoginFilter implements Filter {

    private final String[] urlsPermitidas = {};

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        String URLpublica = "/forum-generico/publico/";
        String URLautenticada = "/forum-generico/autenticado/";
        
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        HttpSession session = request.getSession();
        String reqUrl = request.getRequestURI();

        boolean permitido = false;

        if (reqUrl.contains("/publico/") || reqUrl.equals("/forum-generico/") || reqUrl.contains(".faces.")) {
            permitido = true;
        }

        if (!ValidatorUtil.isEmpty(session.getAttribute("usuario")) && reqUrl.contains("login")) {
            response.sendRedirect(URLautenticada+"mural.xhtml");
        }
        
        //deixa permitido se uma url estiver na lista de url permitidas
        if (!permitido) {
            for (String url : urlsPermitidas) {
                if (reqUrl.contains(url)) {
                    permitido = true;
                    break;
                }
            }
        }

        if (!ValidatorUtil.isEmpty(session.getAttribute("usuario")) || permitido) {
            chain.doFilter(request, response);
        } else {
            response.sendRedirect(URLpublica+"sessao_expirada.html");
        }

    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {

    }
}
