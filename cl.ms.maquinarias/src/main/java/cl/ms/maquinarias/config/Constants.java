package cl.ms.maquinarias.config;

import java.util.HashMap;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "constants")
@Data
public class Constants {


    private HashMap<String,String> security;    
    private HashMap<String, String> jwt;
}
