package org.example.taskmanager.utill;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Component
public class JwtUtils {

    private SecretKey Key;
    private  static  final long EXPIRATION_TIME = 86400000; //24hours

    public JwtUtils(){
        String secreteString = "5a5d6e97b9a65eaeb0ea8ead740492ce51d9f5d2cc53264015ce388378eb8e32a5e706bd0e1383de6dca343960f9d631a11de7b9d0aa64dbae212e3f87d37446f76947d87ecc58ec74c8b41c323d274d47347aa58217a8455b5b1d3002d00626b5819dd7558fec388f1b5484aaf3f4041f4894617e52f1e2926fe0895c1fa704a565352b338fdb75dd88f890c7bffbff331a844c72d4fa42eec67080b419dd3775c88c194b636c3913b8180bd6fa5e9dbbf99579902c977f81415ba7af95eb5346cc61e2146c1ee5e809adef267742f7002abdbe81b2a282e8e24863de8355ab7618fc42daab519c95cc92e8f8114124117bdb46b27454c415e9708bd91b4aed";
        byte[] keyBytes = Base64.getDecoder().decode(secreteString.getBytes(StandardCharsets.UTF_8));
        this.Key = new SecretKeySpec(keyBytes, "HmacSHA256");
    }


    public String generateToken(UserDetails userDetails){
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(Key)
                .compact();
    }
    public String generateRefreshToken(HashMap<String, Object> claims, UserDetails userDetails){
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(Key)
                .compact();
    }

    public String extractUsername(String token){
        return extractClaims(token, Claims::getSubject);
    }
    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction){
        return claimsTFunction.apply(Jwts.parser().verifyWith(Key).build().parseSignedClaims(token).getPayload());
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    public boolean isTokenExpired(String token){
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }

}