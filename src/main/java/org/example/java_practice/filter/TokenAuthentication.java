package org.example.java_practice.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.example.java_practice.model.dto.RestResponse;
import org.example.java_practice.util.Constants;
import org.example.java_practice.util.MyPageUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

public class TokenAuthentication {
    private static final long TOKEN_EXPIRATION_TIME = 3600000; // 1 hour = 3600 milliseconds
    private static final String TOKEN_SECRET = "IvzYAfmBIodx6RJInpOfUFj8qKNQXGNj";
    private static final String REFRESH_TOKEN_SECRET = "KUTyJ07Dl6GWKDuahuyZs4Ch8Q3SurNX";
    static final String TOKEN_PREFIX = "Bearer";
    static final String HEADER_STRING = "Authorization";

    /**
     * Create a token for a user
     *
     * @param servletResponse servletResponse
     * @throws IOException exception
     */
    public static void addAuthentication(HttpServletResponse servletResponse, Authentication authResult) throws IOException {
        String userName = authResult.getName();
        List<String> authorityList = authResult.getAuthorities().stream().map(Object::toString).collect(
                Collectors.toList());
        String JWT = Jwts.builder().setSubject(userName)
                .setExpiration(new Date(System.currentTimeMillis() + Constants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, Constants.SECRET).compact();
        servletResponse.setContentType(Constants.JSON_TYPE);

        String[] names = {"token", "authorities"};
        RestResponse response = MyPageUtils
                .getInstance().createSuccessResponse("jwt token create", names, JWT, authorityList);
        String jSon = new ObjectMapper().writeValueAsString(response);
        servletResponse.getOutputStream().print(jSon);
    }

    /**
     * Create a token for a user
     *
     * @param userName username
     */
    public static String createToken(String userName) {
        return Jwts.builder().setSubject(userName)
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, TOKEN_SECRET).compact();
    }

    public static String createRefreshToken(String userName) throws IOException {
        // 2 hours = 7200
        long REFRESH_TOKEN_EXPIRATION_TIME = 7200000;
        return Jwts.builder().setSubject(userName)
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, REFRESH_TOKEN_SECRET).compact();
    }

    /**
     * Get UserID from Login Token
     *
     * @param servletRequest servletRequest
     * @return {@link Authentication}
     */
    public static Authentication getAuthentication(HttpServletRequest servletRequest) {
        //Get the token from the request's header
        String token = servletRequest.getHeader(HEADER_STRING);
        if (StringUtils.isNotEmpty(token)) {
            // parse the token.
            String user = Jwts.parser().setSigningKey(TOKEN_SECRET)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody()
                    .getSubject();
            return user != null ? new UsernamePasswordAuthenticationToken(user, null, emptyList()) : null;
        }
        return null;
    }

    /**
     * Get UserID from RefreshToken
     *
     * @param refreshToken refreshToken
     * @return User
     */
    public static String getUserFromRefreshToken(String refreshToken) {
        return Jwts.parser().setSigningKey(REFRESH_TOKEN_SECRET).parseClaimsJws(refreshToken).getBody()
                .getSubject();
    }
}
