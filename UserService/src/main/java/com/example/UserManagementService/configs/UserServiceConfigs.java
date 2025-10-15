package com.example.UserManagementService.configs;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class UserServiceConfigs {
    @Bean
    public BCryptPasswordEncoder getBCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public ApplicationRunner runner(RequestMappingHandlerMapping mapping) {
//        return args -> mapping.getHandlerMethods()
//                .forEach((key, value) -> System.out.println(key + " -> " + value));
//    }

//    @Bean
//    public ApplicationRunner runner(@Qualifier("requestMappingHandlerMapping") RequestMappingHandlerMapping mapping) {
//        return args -> {
//            System.out.println("=== ALL REGISTERED ENDPOINTS ===");
//            mapping.getHandlerMethods()
//                    .forEach((key, value) -> System.out.println(key + " -> " + value));
//            System.out.println("=== END ENDPOINTS ===");
//        };
//    }

}
