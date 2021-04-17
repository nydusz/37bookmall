
package sanqibookmall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@MapperScan("sanqibookmall.dao")
@SpringBootApplication
public class sanqibookmallApplication {
    public static void main(String[] args) {
        SpringApplication.run(sanqibookmallApplication.class, args);
    }
}
