package com.example.fpvfleet.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class SecurityConfigTest {

    private static final Instant ISSUED_AT = Instant.parse("2026-11-19T10:00:00Z");
    private static final Instant EXPIRES_AT = ISSUED_AT.plusSeconds(300);

    private final SecurityConfig securityConfig = new SecurityConfig();

    @Test
    void jwtAuthenticationConverter_rolesClaim_mapsToRoleAuthoritiesAndUsername() {
        Jwt jwt = jwt(Map.of("preferred_username", "admin", "roles", List.of("ADMIN", "PILOT")));

        AbstractAuthenticationToken authentication = securityConfig.jwtAuthenticationConverter().convert(jwt);

        assertThat(authentication.getName()).isEqualTo("admin");
        assertThat(roleAuthorities(authentication)).containsExactlyInAnyOrder("ROLE_ADMIN", "ROLE_PILOT");
    }

    @Test
    void jwtAuthenticationConverter_noRolesClaim_grantsNoAuthorities() {
        Jwt jwt = jwt(Map.of("preferred_username", "viewer"));

        AbstractAuthenticationToken authentication = securityConfig.jwtAuthenticationConverter().convert(jwt);

        assertThat(authentication.getName()).isEqualTo("viewer");
        assertThat(roleAuthorities(authentication)).isEmpty();
    }

    @Test
    void oidcUserService_idTokenWithRoles_addsRoleAuthorities() {
        OidcUserRequest request = oidcRequest(Map.of("roles", List.of("PILOT")));

        OidcUser user = securityConfig.oidcUserService().loadUser(request);

        assertThat(user.getName()).isEqualTo("pilot");
        assertThat(user.getAuthorities()).extracting(GrantedAuthority::getAuthority).contains("ROLE_PILOT");
    }

    @Test
    void oidcUserService_idTokenWithoutRoles_addsNoRoleAuthorities() {
        OidcUserRequest request = oidcRequest(Map.of());

        OidcUser user = securityConfig.oidcUserService().loadUser(request);

        assertThat(user.getAuthorities()).extracting(GrantedAuthority::getAuthority)
                .noneMatch(authority -> authority.startsWith("ROLE_"));
    }

    private static List<String> roleAuthorities(AbstractAuthenticationToken authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> authority.startsWith("ROLE_"))
                .toList();
    }

    private static Jwt jwt(Map<String, Object> claims) {
        return Jwt.withTokenValue("token")
                .header("alg", "none")
                .subject("user-1")
                .issuedAt(ISSUED_AT)
                .expiresAt(EXPIRES_AT)
                .claims(map -> map.putAll(claims))
                .build();
    }

    private static OidcUserRequest oidcRequest(Map<String, Object> extraClaims) {
        ClientRegistration registration = ClientRegistration.withRegistrationId("keycloak")
                .clientId("fpv-ui")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
                .authorizationUri("http://localhost:8081/auth")
                .tokenUri("http://localhost:8081/token")
                .userNameAttributeName("preferred_username")
                .scope("openid")
                .build();
        Map<String, Object> claims = new HashMap<>(extraClaims);
        claims.put("sub", "user-2");
        claims.put("preferred_username", "pilot");
        OidcIdToken idToken = new OidcIdToken("id-token", ISSUED_AT, EXPIRES_AT, claims);
        OAuth2AccessToken accessToken = new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER, "access-token", ISSUED_AT, EXPIRES_AT, Set.of("openid"));
        return new OidcUserRequest(registration, accessToken, idToken);
    }
}
