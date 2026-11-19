package com.example.fpvfleet.ui;

import com.example.fpvfleet.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.net.URI;
import java.util.List;

@ControllerAdvice(basePackageClasses = UiControllerAdvice.class)
public class UiControllerAdvice {

    private static final String ROLE_PREFIX = "ROLE_";

    @ModelAttribute("currentUser")
    public CurrentUser currentUser(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof OidcUser user)) {
            return null;
        }
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> authority.startsWith(ROLE_PREFIX))
                .map(authority -> authority.substring(ROLE_PREFIX.length()))
                .distinct()
                .sorted()
                .toList();
        return new CurrentUser(user.getPreferredUsername(), user.getFullName(), user.getEmail(), roles);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleAccessDenied(AccessDeniedException ex, Authentication authentication,
                                           HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("forbidden", HttpStatus.FORBIDDEN);
        mav.addObject("currentUser", currentUser(authentication));
        mav.addObject("reason", ex.getMessage());
        mav.addObject("path", request.getMethod() + " " + request.getRequestURI());
        return mav;
    }

    @ExceptionHandler({ResourceNotFoundException.class, IllegalArgumentException.class})
    public String handleBusinessError(RuntimeException ex, HttpServletRequest request) {
        return redirectBack(request, ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public String handleDataIntegrity(HttpServletRequest request) {
        return redirectBack(request, "Порушення цілісності даних: перевірте унікальність та обов'язкові поля");
    }

    private String redirectBack(HttpServletRequest request, String error) {
        RequestContextUtils.getOutputFlashMap(request).put("error", error);
        String referer = request.getHeader("Referer");
        String target = "/ui";
        if (referer != null) {
            String path = URI.create(referer).getPath();
            if (path != null && path.startsWith("/ui")) {
                target = path;
            }
        }
        return "redirect:" + target;
    }
}
