package neko.utils.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import neko.entity.Users;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/*
 * 生成jwt并AES加密
 *
 * */
public class Token {

    //密钥
    private static String SECRET = "ThisIsASecret";

    //生成token
    public static String getJwtToken(Users users) {
        Calendar nowTime = Calendar.getInstance();
        Calendar expiresTime = Calendar.getInstance();
        //7天有效期
        expiresTime.add(Calendar.DATE, 7);

        Claims claims = Jwts.claims();

        //添加自定义信息
        claims.put("Name", users.getUname());
        claims.put("Phone", users.getPhone());
        claims.put("Uid", users.getUid());

        //签名的使用者
        claims.setAudience("user");
        //签名生成者
        claims.setIssuer("Neko");
        //生成签名的时间
        claims.setIssuedAt(nowTime.getTime());
        //签名过期的时间
        claims.setExpiration(expiresTime.getTime());

        //生成jwt
        String token = Jwts.builder().setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();

        //AES加密
        token = AESUtil.encrypt(token, SECRET);

        return token;
    }

    //解密
    public static Map<String, String> parseJwtToken(String token) {

        //AES解密
        token = AESUtil.decrypt(token, SECRET);

        Jws<Claims> jws = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
        Claims Claims = jws.getBody();

        //提取信息
        Map<String, String> info = new HashMap<>();
        info.put("Name", Claims.get("Name").toString());
        info.put("Phone", Claims.get("Phone").toString());
        info.put("Uid", Claims.get("Uid").toString());
        info.put("Expiration", Claims.getExpiration().toString());
        info.put("IssuedAt", Claims.getIssuedAt().toString());

        return info;
    }

}
