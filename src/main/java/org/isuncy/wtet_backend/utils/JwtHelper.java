package org.isuncy.wtet_backend.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

public class JwtHelper {
    private final static String signKey="sa070610nan060212qgcl247618xxxfqywyc";
    private final static SecretKey key = Keys.hmacShaKeyFor(signKey.getBytes(StandardCharsets.UTF_8));

    public static String createJWT(Map<String, Object> claims){
        String jwt= Jwts.builder().signWith(key, Jwts.SIG.HS256)
                .claims(claims)
                .expiration(new Date(System.currentTimeMillis()+3600*1000*4))
                .compact();
        return jwt;
    }
    public static String createJWT(Map<String, Object> claims,Long remainTime){
        String jwt= Jwts.builder().signWith(key, Jwts.SIG.HS256)
                .claims(claims)
                .expiration(new Date(System.currentTimeMillis()+remainTime))
                .compact();
        return jwt;
    }
    public static Claims parseJWT(String jwt){
        try {
            return Jwts.parser().verifyWith(key).build().parseSignedClaims(jwt).getPayload();
        } catch (Exception e) {
            return null;
        }
    }

    public static Claims getAuthentication(HttpServletRequest httpServletRequest) {
        String Authentication=httpServletRequest.getHeader("Authentication");
        if(Authentication==null||Authentication.isEmpty()) {
            return null;
        } else {
            return JwtHelper.parseJWT(Authentication);
        }
    }

    public static String getUserId(HttpServletRequest request) {
        String jwt = request.getHeader("Authentication");
        if (!StringUtils.hasLength(jwt)) return null;
        Map<String,Object> claims = JwtHelper.parseJWT(jwt);
        if (claims != null) {
            return (String) claims.get("id");
        }
        return null;
    }

//    public static User getUser(HttpServletRequest request) {
//        String Authentication=request.getHeader("Authentication");
//        if(Authentication==null||Authentication.isEmpty()) {
//            return null;
//        } else {
//            Claims claims=JwtHelper.parseJWT(Authentication);
//            User user=new User();
//            user.setId(claims.get("id", String.class));
//            user.setUsername(claims.get("username", String.class));
//            user.setPhoneNumber(claims.get("phonenumber", String.class));
//            user.setRealname(claims.get("realname", String.class));
//            user.setType(claims.get("type", String.class));
//            return user;
//        }
//    }
}
