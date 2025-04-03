package academy.devdojo.config;

import external.dependency.Connection;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ConnectionBeanConfiguration {
    private final ConnectionConfigurationProperties configurationProperties;

    @Bean
    public Connection connectionMySql() {
        return new Connection(configurationProperties.url(),
                configurationProperties.username(),
                configurationProperties.password());
    }

    @Bean(name = "connectionMongoDB")
//    @Primary
    public Connection connectionMongo() {
        return new Connection(configurationProperties.url(),
                configurationProperties.username(),
                configurationProperties.password());
    }
}
