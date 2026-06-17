package medic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication(
    scanBasePackages = {"medic", "Services", "Controllers"},
    exclude = {SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class}
)

@EntityScan(basePackages = "Entities")
@EnableJpaRepositories(basePackages = "Repositories")
public class MedicApplication {
    public static void main(String[] args) {
        SpringApplication.run(MedicApplication.class, args);
    }

    @Bean 
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}