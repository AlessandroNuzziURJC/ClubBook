package clubbook.backend.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.util.*;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;


/**
 * Service for handling JSON Web Token (JWT) operations.
 * This service is responsible for generating, validating, and managing JWT tokens.
 */
@Service
public class JwtService {
    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    private final Set<String> blacklistedTokens = new HashSet<>();

    /**
     * Extracts the username from the given JWT token.
     *
     * @param token The JWT token from which to extract the username.
     * @return The username contained in the JWT token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts a specific claim from the JWT token using a claims resolver function.
     *
     * @param token The JWT token from which to extract claims.
     * @param claimsResolver A function that defines how to extract the desired claim from the claims.
     * @param <T> The type of the claim being extracted.
     * @return The extracted claim of type T.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Generates a new JWT token for the given user details with no additional claims.
     *
     * @param userDetails The user details for which to generate the token.
     * @return The generated JWT token as a string.
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Generates a new JWT token for the given user details with additional claims.
     *
     * @param extraClaims Additional claims to include in the token.
     * @param userDetails The user details for which to generate the token.
     * @return The generated JWT token as a string.
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    /**
     * Retrieves the expiration time for the JWT tokens.
     *
     * @return The expiration time in milliseconds.
     */
    public long getExpirationTime() {
        return jwtExpiration;
    }

    /**
     * Builds a JWT token with the specified claims, subject (username), and expiration time.
     *
     * @param extraClaims Additional claims to include in the token.
     * @param userDetails The user details for which to generate the token.
     * @param expiration The expiration time for the token.
     * @return The generated JWT token as a string.
     */
    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Validates the given JWT token against the provided user details.
     *
     * @param token The JWT token to validate.
     * @param userDetails The user details to check against.
     * @return True if the token is valid; otherwise, false.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        if (blacklistedTokens.contains(token))
            return false;
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Checks if the given JWT token is expired.
     *
     * @param token The JWT token to check.
     * @return True if the token is expired; otherwise, false.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts the expiration date from the given JWT token.
     *
     * @param token The JWT token from which to extract the expiration date.
     * @return The expiration date as a Date object.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts all claims from the given JWT token.
     *
     * @param token The JWT token from which to extract claims.
     * @return The claims contained in the token.
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        //Jwts.parser().setSigningKey(getSignInKey()).parseClaimsJws(token).getBody()
        //Jwts.parserBuilder()
        //                .setSigningKey(getSignInKey())
        //                .build()
        //                .parseClaimsJws(token)
        //                .getBody();
    }

    /**
     * Retrieves the signing key used to create the JWT tokens.
     *
     * @return The secret key used for signing as a SecretKey object.
     */
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Invalidates the given JWT token by adding it to the blacklist.
     *
     * @param jwtToken The JWT token to invalidate.
     */
    public void invalidateToken(String jwtToken) {
        blacklistedTokens.add(jwtToken);
    }
}
