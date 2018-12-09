package Services;

import org.mockito.InjectMocks;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.OutService.ConsoleService;
import ru.otus.ResourceService.ResourceServiceImpl;
import ru.otus.Scanner.IScanner;

@Configuration
public class TestConfiguration {
    @Bean
    public ResourceServiceImpl getResourceService() {
        return new ResourceServiceImpl();
    }

//    @Bean
//    public ConsoleService getConsoleService() {
//        return new ConsoleService(scanner);
//    }
}
