
	
import java.io.File;
import java.io.IOException;
import java.security.Key;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;  

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.crypto.MacProvider;
public class queryAEDash {
	//Sample method to construct a JWT

	public static void main() {
		String s = Jwts.builder().setSubject("Joe").signWith(SignatureAlgorithm.HS512, (Key) (new File("/users/richardcurtis/AEDASH_API_KEY/private.key"))).compact();
		assert Jwts.parser().setSigningKey(s).parseClaimsJws(s).getBody().getSubject().equals("Joe");
	
		try {

		    Jwts.parser().setSigningKey(s);

		    //OK, we can trust this JWT

		} catch (SignatureException e) {

		    //don't trust the JWT!
		}
		
		
	}
	

}
