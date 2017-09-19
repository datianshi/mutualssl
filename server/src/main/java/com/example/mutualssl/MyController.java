package com.example.mutualssl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class MyController {
    protected final Log logger = LogFactory.getLog(this.getClass());

    @Value("${CF_INSTANCE_INTERNAL_IP}")
    String ip;

    @GetMapping("/hello")
    public String sayHello(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return String.format("Thanks for authenticating with X509, certificate CN is: %s", user.getUsername());
    }
    @GetMapping("/nomutual")
    public String nomutual(HttpServletRequest request){
        logger.info("X-Forward-Client-Cert: " +request.getHeader("X-Forwarded-Client-Cert"));
        return "this has no mutual auth. My Internal ip is:" + ip;
    }
}
