package ma.emsi.oussama.volservice;
    
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class VolServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(VolServiceApplication.class, args);
    }
}
