package mk.ukim.finki.usersmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "mk.ukim.finki.quizmanagement",
        "mk.ukim.finki.ordersmanagement"
})
public class UsersManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(UsersManagementApplication.class, args);
    }

}
