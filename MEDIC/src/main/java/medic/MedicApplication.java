package medic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"medic", "Services", "Controllers"})
@EntityScan(basePackages = "Entities")
@EnableJpaRepositories(basePackages = "Repositories")
public class MedicApplication {
    public static void main(String[] args) {
        SpringApplication.run(MedicApplication.class, args);
    }
}
