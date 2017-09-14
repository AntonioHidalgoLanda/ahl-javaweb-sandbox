/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package product;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author antonio
 */
@SpringBootApplication(scanBasePackages = {"service"})
@EnableOAuth2Sso
@RestController
@Configuration
// We need to extends WebSecurityConfigurerAdapter to avoid the dogy CSRF 
public class Application  extends WebSecurityConfigurerAdapter{
    
    // We need to override this method from WebSecurityConfigurerAdapter to avoid the dogy CSRF
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable();
    }
    
    @RequestMapping("/signin")
    public String signin() {
        SessionMediator.validateSessionUser();
        return SessionMediator.sessionToString();
    }
  
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {

            System.out.println("Let's inspect the beans provided by Spring Boot:");

            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                System.out.println(beanName);
            }

        };
    }

}