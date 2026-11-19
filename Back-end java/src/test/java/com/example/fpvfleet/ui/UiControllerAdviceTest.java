package com.example.fpvfleet.ui;

import com.example.fpvfleet.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.ModelAndView;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class UiControllerAdviceTest {

    private static final Instant ISSUED_AT = Instant.parse("2026-11-19T10:00:00Z");

    private UiControllerAdvice advice;
    private MockHttpServletRequest request;
    private FlashMap flashMap;

    @BeforeEach
    void setUp() {
        advice = new UiControllerAdvice();
        request = new MockHttpServletRequest("POST", "/ui/drones/1/delete");
        flashMap = new FlashMap();
        request.setAttribute(DispatcherServlet.OUTPUT_FLASH_MAP_ATTRIBUTE, flashMap);
    }

    @Test
    void currentUser_oidcUserWithRoles_returnsSortedRolesWithoutPrefix() {
        OAuth2AuthenticationToken authentication = oidcAuthentication(
                new SimpleGrantedAuthority("ROLE_PILOT"),
                new SimpleGrantedAuthority("ROLE_ADMIN"),
                new SimpleGrantedAuthority("SCOPE_openid"));

        CurrentUser user = advice.currentUser(authentication);

        assertThat(user.username()).isEqualTo("admin");
        assertThat(user.fullName()).isEqualTo("Fleet Admin");
        assertThat(user.email()).isEqualTo("admin@fpv.local");
        assertThat(user.roles()).containsExactly("ADMIN", "PILOT");
        assertThat(user.hasRole("ADMIN")).isTrue();
        assertThat(user.hasRole("VIEWER")).isFalse();
    }

    @Test
    void currentUser_oidcUserWithoutRoles_returnsEmptyRoleList() {
        CurrentUser user = advice.currentUser(oidcAuthentication(new SimpleGrantedAuthority("SCOPE_openid")));

        assertThat(user.roles()).isEmpty();
    }

    @Test
    void currentUser_anonymous_returnsNull() {
        CurrentUser user = advice.currentUser(null);

        assertThat(user).isNull();
    }

    @Test
    void currentUser_nonOidcPrincipal_returnsNull() {
        CurrentUser user = advice.currentUser(new TestingAuthenticationToken("jwt-user", "n/a", "ROLE_ADMIN"));

        assertThat(user).isNull();
    }

    @Test
    void handleAccessDenied_returnsForbiddenViewWithReasonAndRequest() {
        OAuth2AuthenticationToken authentication = oidcAuthentication();

        ModelAndView mav = advice.handleAccessDenied(new AccessDeniedException("Access Denied"), authentication, request);

        assertThat(mav.getViewName()).isEqualTo("forbidden");
        assertThat(mav.getStatus()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(mav.getModel()).containsEntry("reason", "Access Denied")
                .containsEntry("path", "POST /ui/drones/1/delete");
        assertThat(mav.getModel().get("currentUser")).isInstanceOf(CurrentUser.class);
    }

    @Test
    void handleBusinessError_refererInsideUi_redirectsBackWithFlashError() {
        request.addHeader("Referer", "http://localhost:8080/ui/drones/5/edit");

        String view = advice.handleBusinessError(new ResourceNotFoundException("Пілота з ID 9 не знайдено"), request);

        assertThat(view).isEqualTo("redirect:/ui/drones/5/edit");
        assertThat(flashMap.get("error")).isEqualTo("Пілота з ID 9 не знайдено");
    }

    @Test
    void handleBusinessError_refererOutsideUi_redirectsToDashboard() {
        request.addHeader("Referer", "http://evil.example/phishing");

        String view = advice.handleBusinessError(new IllegalArgumentException("bad"), request);

        assertThat(view).isEqualTo("redirect:/ui");
    }

    @Test
    void handleBusinessError_noReferer_redirectsToDashboard() {
        String view = advice.handleBusinessError(new IllegalArgumentException("bad"), request);

        assertThat(view).isEqualTo("redirect:/ui");
        assertThat(flashMap.get("error")).isEqualTo("bad");
    }

    @Test
    void handleDataIntegrity_redirectsBackWithGenericFlashError() {
        request.addHeader("Referer", "http://localhost:8080/ui/pilots");

        String view = advice.handleDataIntegrity(request);

        assertThat(view).isEqualTo("redirect:/ui/pilots");
        assertThat((String) flashMap.get("error")).contains("цілісності");
    }

    private static OAuth2AuthenticationToken oidcAuthentication(GrantedAuthority... authorities) {
        OidcIdToken idToken = new OidcIdToken("id-token", ISSUED_AT, ISSUED_AT.plusSeconds(300), Map.of(
                "sub", "user-1",
                "preferred_username", "admin",
                "name", "Fleet Admin",
                "email", "admin@fpv.local"));
        List<GrantedAuthority> list = List.of(authorities);
        DefaultOidcUser user = new DefaultOidcUser(list, idToken, "preferred_username");
        return new OAuth2AuthenticationToken(user, list, "keycloak");
    }
}
