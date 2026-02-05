package org.isuncy.wtet_backend.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Date;
import java.util.Map;

public class JwtHelper {
    private final static String signKey="as0f156!21A0D6F@CYsN%luxa";



    public static String createJWT(Map<String, Object> claims){
        String jwt= Jwts.builder().signWith(SignatureAlgorithm.HS256,signKey)
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis()+3600*1000*4))
                .compact();
        return jwt;
    }
    public static String createJWT(Map<String, Object> claims,Long remainTime){
        String jwt= Jwts.builder().signWith(SignatureAlgorithm.HS256,signKey)
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis()+remainTime))
                .compact();
        return jwt;
    }
    public static Claims parseJWT(String jwt){
        try {
            return Jwts.parser().setSigningKey(signKey).parseClaimsJws(jwt).getBody();
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
