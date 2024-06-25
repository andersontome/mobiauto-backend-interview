package com.api.revenda.mobiauto.security.keys;

import java.security.Key;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

//Responsável por criar um token qualquer apenas para testes
public class KeyGenerator {
    public static void main(String[] args) {
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        String secret = Encoders.BASE64.encode(key.getEncoded());
        System.out.println(secret);
    }
}
