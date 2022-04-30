package app.botted.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.apache.commons.dbcp2.BasicDataSource;

@Configuration
public class WebConfiguration {

    @Value("${spring.thymeleaf.prefix}")
    private String prefix;

    @Value("${spring.thymeleaf.suffix}")
    private String suffix;

    // Will map to the JSP page: "WEB-INF/views/index.jsp"
    @Bean
    public InternalResourceViewResolver jspViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix(prefix);
        resolver.setSuffix(suffix);
        resolver.setViewClass(JstlView.class);
        return resolver;
    }

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setCacheable(false);
        templateResolver.setPrefix(prefix);
        templateResolver.setSuffix(suffix);
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine springTemplateEngine = new SpringTemplateEngine();
        springTemplateEngine.addTemplateResolver(templateResolver());
        return springTemplateEngine;
    }

    @Bean
    public BasicDataSource dataSource() throws NullPointerException {
        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        //String dbUrl = "jdbc:postgresql://ec2-34-194-158-176.compute-1.amazonaws.com:5432/da2g0o7m136sp5?password=7b04e1735374fcb6ba8f984fdcbcaaf5bada71f4d85df12c0e62cab2ca2b4022&sslmode=require&user=fzbeyehwmqhuxn";
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setUrl(dbUrl);
        return basicDataSource;
    }
    public String getDbUrl () {
        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        return dbUrl;
    }
}