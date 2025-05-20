package me.springboot_todo.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CookieUtil {

    private final EnvUtil envUtil;

    public void setCookie(String name,
                          String value,
                          String path,
                          int maxAge,
                          HttpServletResponse response) {

        boolean isProd = envUtil.isProd();

        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(isProd);
        cookie.setPath(path);
        cookie.setMaxAge(maxAge);
        cookie.setAttribute("SameSite", "None");

        response.addCookie(cookie);
    }

    public String getCookie(Cookie[] cookies, String name) {

        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }

    public void removeCookie(String name, String path, HttpServletResponse response) {

        Cookie cookie = new Cookie(name, null);
        cookie.setHttpOnly(true);
        cookie.setPath(path);
        cookie.setMaxAge(0);

        boolean isProd = envUtil.isProd();
        cookie.setSecure(isProd);

        response.addCookie(cookie);
    }
}
