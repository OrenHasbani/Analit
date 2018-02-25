# Benchuk

    import feign.RequestInterceptor;
    import io.benchuk.interceptors.FeignRequestInterceptor;
    import io.benchuk.interceptors.SpringRequestInterceptor;
    import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
    import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
    import org.springframework.context.annotation.Configuration;
    
    @Configuration
    public class ServiceConfiguration extends WebMvcConfigurerAdapter {

        @Autowired
        SpringRequestInterceptor springRequestInterceptor;
        
        @Bean
        RequestInterceptor requestInterceptor() {
            return new TestRequestInterceptor();
        }
    
        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(springRequestInterceptor);
        }

    }