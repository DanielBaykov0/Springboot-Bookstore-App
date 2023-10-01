package baykov.daniel.springbootlibraryapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SpringbootLibraryAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootLibraryAppApplication.class, args);
	}

}
