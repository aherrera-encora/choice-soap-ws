package mx.cacho.choice.soapws.config;

import mx.cacho.choice.soapws.service.exception.IllegalServiceArgumentException;
import mx.cacho.choice.soapws.service.exception.IllegalServiceOperationException;
import mx.cacho.choice.soapws.service.exception.ServiceException;
import mx.cacho.choice.soapws.util.ChoiceSoapFaultMappingExceptionResolver;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.soap.server.endpoint.SoapFaultDefinition;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

import java.util.Properties;

@Configuration
@EnableWs
public class ChoiceWebServiceConfig extends WsConfigurerAdapter {

    private static final String UNEXPECTED_SERVER_ERROR = "Unexpected Server Error";

    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, "/ws/*");
    }

    @Bean(name = "choice")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema articlesSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("ChoicePort");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setTargetNamespace("http://choice.cacho.mx/soap-ws/hotels");
        wsdl11Definition.setSchema(articlesSchema);
        return wsdl11Definition;
    }

    @Bean
    public XsdSchema choiceSchema() {
        return new SimpleXsdSchema(new ClassPathResource("schemas/choice.xsd"));
    }

    @Bean
    public SoapFaultMappingExceptionResolver exceptionResolver() {
        SoapFaultMappingExceptionResolver exceptionResolver = new ChoiceSoapFaultMappingExceptionResolver();

        //Configure default Fault with generic Server Error fault string
        SoapFaultDefinition defaultFaultDefinition = new SoapFaultDefinition();
        defaultFaultDefinition.setFaultCode(SoapFaultDefinition.RECEIVER);
        defaultFaultDefinition.setFaultStringOrReason("Unexpected Server Error");
        exceptionResolver.setDefaultFault(defaultFaultDefinition);

        Properties errorMappings = new Properties();
        //Configure plain exceptions to just show generic Server Error as fault string
        errorMappings.setProperty(RuntimeException.class.getName(), String.join(",", SoapFaultDefinition.RECEIVER.toString(), UNEXPECTED_SERVER_ERROR));
        errorMappings.setProperty(Exception.class.getName(), String.join(",", SoapFaultDefinition.RECEIVER.toString(), UNEXPECTED_SERVER_ERROR));

        //Configure Service exceptions to show their message as fault string
        errorMappings.setProperty(ServiceException.class.getName(), SoapFaultDefinition.RECEIVER.toString());
        errorMappings.setProperty(IllegalServiceOperationException.class.getName(), SoapFaultDefinition.RECEIVER.toString());
        errorMappings.setProperty(IllegalServiceArgumentException.class.getName(), SoapFaultDefinition.SENDER.toString());

        exceptionResolver.setExceptionMappings(errorMappings);
        exceptionResolver.setOrder(1);
        return exceptionResolver;
    }
}
