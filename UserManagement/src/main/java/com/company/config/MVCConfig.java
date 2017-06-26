package com.company.config;

import java.util.List;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.company.controllers.UserController;
import com.company.controllers.rest.user.UserRestController;
import com.company.eventListeners.RegistrationCompleteEventListener;
import com.company.exception.handlers.GenericExceptionHandler;
import com.company.services.mailer.MailerService;
import com.company.services.user.UserService;

@Configuration
@ComponentScan(basePackageClasses = { UserController.class, UserRestController.class, UserService.class, GenericExceptionHandler.class,
		RegistrationCompleteEventListener.class, MailerService.class })

public class MVCConfig extends WebMvcConfigurerAdapter {
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("index");
		registry.addViewController("/home").setViewName("home");
		registry.addViewController("/login").setViewName("login");
		registry.addViewController("/index").setViewName("index");
//		registry.addViewController("/oops").setViewName("oops");
		registry.addViewController("/user/dashboard").setViewName("user/dashboard");
		registry.addViewController("/user/register/registrationSuccessful").setViewName("user/register/registrationSuccessful");
		registry.addViewController("/user/register/registrationIncomplete").setViewName("user/register/registrationIncomplete");
		registry.addViewController("/user/register/verificationLinkSent").setViewName("user/register/verificationLinkSent");
		registry.addViewController("/user/register/invalidUser").setViewName("user/register/invalidUser");
		registry.addViewController("/user/register/resetPasswordLinkSent").setViewName("user/register/resetPasswordLinkSent");
		registry.addViewController("/user/register/passwordResetTokenExpired").setViewName("user/register/passwordResetTokenExpired");
		registry.addViewController("/user/register/passwordResetSuccessful").setViewName("user/register/passwordResetSuccessful");
		registry.addViewController("/user/register/unblockLinkSent").setViewName("user/register/unblockLinkSent");
		registry.addViewController("/user/register/unblockSuccessful").setViewName("user/register/unblockSuccessful");
		
	}
	
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        resolver.setMaxPageSize(3);
        argumentResolvers.add(resolver);
        super.addArgumentResolvers(argumentResolvers);
    }

}