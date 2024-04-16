package mc.project1.restapi.srvice;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope
public class HelloService
{
    public String hello()
    {
        return "Hello World";
    }
}
