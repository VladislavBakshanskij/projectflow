package io.amtech.projectflow.service.token;

import io.amtech.projectflow.config.properties.TokenProperty;
import io.amtech.projectflow.error.AuthException;
import io.amtech.projectflow.model.auth.Token;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisTokenServiceImpl implements TokenService {
    private static final String ACCESS_TOKEN_KEY = "access:";
    private static final String REFRESH_TOKEN_KEY = "refresh:";

    private final RedisSerializer<Token> redisSerializer = new Jackson2JsonRedisSerializer<>(Token.class);
    private final RedisConnectionFactory redisConnectionFactory;
    private final TokenProperty property;

    @Override
    public Token getByAccess(final String access) {
        log.debug("Try to load info about access token :: {}", access);
        return getTokenByKey(ACCESS_TOKEN_KEY + access);
    }

    @Override
    public Token getByRefresh(final String refresh) {
        return getTokenByKey(REFRESH_TOKEN_KEY + refresh);
    }

    private Token getTokenByKey(final String key) {
        log.debug("Try to load info for :: {}", key);
        try (RedisConnection connection = redisConnectionFactory.getConnection()) {
            final byte[] serializedKey = key.getBytes();
            final byte[] tokenFromRedis = connection.get(serializedKey);
            log.debug("Redis return info for key {} :: {}", key, tokenFromRedis);
            return redisSerializer.deserialize(tokenFromRedis);
        }
    }

    @Override
    public void store(final Token token) {
        Objects.requireNonNull(token.getAccess());
        Objects.requireNonNull(token.getRefresh());

        log.debug("Try to store info into redis :: {}", token);

        try (RedisConnection connection = redisConnectionFactory.getConnection()) {
            final byte[] serializedToken = redisSerializer.serialize(token);
            final byte[] accessTokenKey = (ACCESS_TOKEN_KEY + token.getAccess()).getBytes();
            connection.set(accessTokenKey,
                           serializedToken,
                           Expiration.from(Duration.of(property.getAccessTokenTimeToLive(), ChronoUnit.MILLIS)),
                           RedisStringCommands.SetOption.ifAbsent());

            final byte[] refreshTokenKey = (REFRESH_TOKEN_KEY + token.getRefresh()).getBytes();
            connection.set(refreshTokenKey,
                           serializedToken,
                           Expiration.from(Duration.of(property.getRefreshTokenTimeToLive(), ChronoUnit.MILLIS)),
                           RedisStringCommands.SetOption.ifAbsent());
        }
    }

    @Override
    public void remove(final String accessToken) {
        final Token token = Optional.ofNullable(getByAccess(accessToken))
                .orElseThrow(() -> new AuthException(String.format("Токен %s является недействительным", accessToken)));
        remove(token);
    }

    @Override
    public void remove(final Token token) {
        log.debug("Try to remove info for token :: {}", token);
        try (RedisConnection connection = redisConnectionFactory.getConnection()) {
            final byte[] accessTokenKey = (ACCESS_TOKEN_KEY + token.getAccess()).getBytes();
            final byte[] refreshTokenKey = (REFRESH_TOKEN_KEY + token.getRefresh()).getBytes();

            connection.del(accessTokenKey, refreshTokenKey);
        }
    }
}
