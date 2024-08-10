package com.renyibang.global.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

public class JwtUtil {
  private static final long ttl = 3 * 24 * 60 * 60 * 1000;
  private static final String key = "rybjwtkk";

  private static SecretKey generalKey() {
    byte[] bytes = Base64.getDecoder().decode(key);
    return new SecretKeySpec(bytes, 0, bytes.length, "AES");
  }

  public static String create(String subject) {
    long now = System.currentTimeMillis(), exp = now + ttl;
    return Jwts.builder()
      .setId(UUID.randomUUID().toString().replaceAll("-", ""))
      .setSubject(subject)
      .setIssuer("sg")
      .setIssuedAt(new Date(now))
      .setExpiration(new Date(exp))
      .signWith(SignatureAlgorithm.HS256, generalKey())
      .compact();
  }

  public static String parse(String jwt) throws Exception {
    return Jwts.parser().setSigningKey(generalKey()).parseClaimsJws(jwt).getBody().getSubject();
  }
}
