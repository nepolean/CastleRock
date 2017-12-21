package com.subsede.user.controllers;

import java.security.Principal;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.subsede.user.events.RegistrationCompleteEvent;
import com.subsede.user.exceptions.EmailAlreadyExistsException;
import com.subsede.user.exceptions.UserAlreadyExistsException;
import com.subsede.user.exceptions.VerificationTokenNotFoundException;
import com.subsede.user.model.user.User;
import com.subsede.user.model.user.UserChangePasswordDTO;
import com.subsede.user.model.user.UserRegistrationDTO;
import com.subsede.user.model.user.UserResetPasswordDTO;
import com.subsede.user.model.user.VerificationToken;
import com.subsede.user.services.user.UserService;

@Controller
@EnableAutoConfiguration
@RequestMapping("/")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	ApplicationEventPublisher eventPublisher;

	@InitBinder("updateUser")
	public void initBinder(WebDataBinder binder) {
		binder.setDisallowedFields("middleName");
	}

	/* End user actions */

	@Secured("IS_AUTHENTICATED_FULLY")
	@GetMapping(value = "/home")
	public ModelAndView goHome(HttpServletRequest request) {
		if(request.isUserInRole("ROLE_USER"))
			return new ModelAndView("redirect:/account/dashboard/");
		if(request.isUserInRole("ROLE_ADMIN"))
			return new ModelAndView("redirect:/account/admin-dashboard/");
		return new ModelAndView("redirect:/login");
	}
	
	@GetMapping("/register")
	public ModelAndView registerForm(Model model) {
		return new ModelAndView("register", "userRegistrationDTO", new UserRegistrationDTO());
	}

	@PostMapping("/register")
	public ModelAndView registerAction(@Valid UserRegistrationDTO userRegistrationDTO, BindingResult result,
			WebRequest request) {
		User user = null;
		if (!result.hasErrors()) {
			try {
				user = userService.register(userRegistrationDTO);
			} catch (UserAlreadyExistsException e) {
				result.reject("username",
						"User with the username, " + userRegistrationDTO.getUsername() + ", already exists.");
			} catch (EmailAlreadyExistsException e) {
				result.reject("email", "User with the email, " + userRegistrationDTO.getEmail() + ", already exists.");
			}
		}
		if (result.hasErrors()) {
			return new ModelAndView("register", "userRegistrationDTO", userRegistrationDTO);
		}
		try {
			String applicationUrl = "http://" + request.getHeader("Host") + request.getContextPath();
			eventPublisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl));
		} catch (Exception e) {
			return new ModelAndView("redirect:/user/register/registrationIncomplete");
		}
		return new ModelAndView("redirect:/user/register/registrationSuccessful");
	}

	@GetMapping(value = "/confirmRegistration")
	public ModelAndView confirmRegistrationAction(@RequestParam("token") String token, WebRequest request,
			Model model) {
		VerificationToken verificationToken = userService.getVerificationToken(token);
		if (verificationToken == null) {
			throw new VerificationTokenNotFoundException();
		}
		Calendar cal = Calendar.getInstance();
		if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
			return new ModelAndView("user/register/verificationTokenExpired", "token", token);
		}
		userService.confirmRegistration(verificationToken);
		return new ModelAndView("user/register/registrationConfirmed");
	}

	@GetMapping(value = "/resendRegistrationToken")
	public ModelAndView resendRegistrationToken(@RequestParam("token") String existingToken, WebRequest request) {
		VerificationToken newToken;
		newToken = userService.generateNewVerificationToken(existingToken);
		User user = newToken.getUser();
		String applicationUrl = "http://" + request.getHeader("Host") + request.getContextPath()
				+ "/confirmRegistration";
		userService.sendVerificationToken(user, applicationUrl, newToken.getToken());
		return new ModelAndView("redirect:/user/register/verificationLinkSent");
	}

	@GetMapping("/forgotPassword")
	public ModelAndView forgotPasswordForm() {
		return new ModelAndView("forgotPassword");
	}

	@PostMapping("/forgotPassword")
	public ModelAndView forgotPasswordAction(@RequestParam String username, @RequestParam String email) {		
		userService.sendPasswordResetLink(username,email);
		return new ModelAndView("redirect:/user/register/resetPasswordLinkSent");
	}

	@GetMapping(value = "/resetPassword")
	public ModelAndView resetPasswordForm(@RequestParam("token") String token) {
		
		VerificationToken verificationToken = userService.getVerificationToken(token);
		if (verificationToken == null) {
			throw new VerificationTokenNotFoundException();
		}
		Calendar cal = Calendar.getInstance();
		if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
			return new ModelAndView("user/register/passwordResetTokenExpired");
		}
		UserResetPasswordDTO userResetPasswordDTO = new UserResetPasswordDTO();
		userResetPasswordDTO.setToken(token);
		return new ModelAndView("resetPassword", "userResetPasswordDTO", userResetPasswordDTO);

	}

	@PostMapping(value = "/resetPassword")
	public ModelAndView resetPasswordAction(@Valid UserResetPasswordDTO userResetPasswordDTO, BindingResult result) {
		if (result.hasErrors()) {
			return new ModelAndView("resetPassword", "userResetPasswordDTO", userResetPasswordDTO);
		}
		VerificationToken verificationToken = userService.getVerificationToken(userResetPasswordDTO.getToken());
		if (verificationToken == null
				|| !verificationToken.getUser().getUsername().equals(userResetPasswordDTO.getUsername())) {
			throw new VerificationTokenNotFoundException();
		}
		Calendar cal = Calendar.getInstance();
		if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
			return new ModelAndView("redirect:/user/register/passwordResetTokenExpired");
		}
		if (userService.resetPassword(verificationToken.getUser(), userResetPasswordDTO.getPassword()) != null)
			return new ModelAndView("redirect:/user/register/passwordResetSuccessful");
		return new ModelAndView("redirect:/oops");

	}

	@Secured("IS_AUTHENTICATED_FULLY")
	@GetMapping(value = "/changePassword")
	public ModelAndView changePasswordForm() {
		UserChangePasswordDTO userChangePasswordDTO = new UserChangePasswordDTO();
		return new ModelAndView("user/changePassword", "userChangePasswordDTO", userChangePasswordDTO);

	}

	@Secured("IS_AUTHENTICATED_FULLY")
	@PostMapping(value = "/changePassword")
	public ModelAndView changePasswordAction(@Valid UserChangePasswordDTO userChangePasswordDTO, BindingResult result,
			Principal principal) {
		if (result.hasErrors()) {
			return new ModelAndView("user/changePassword", "userChangePasswordDTO", userChangePasswordDTO);
		}
		userService.changePassword(principal.getName(), userChangePasswordDTO);
		return new ModelAndView("user/changePasswordSuccessful");

	}

	@GetMapping("/sendUnblockLink")
	public ModelAndView sendUnblockLinkForm() {
		return new ModelAndView("user/register/sendUnblockLink");
	}

	@PostMapping("/sendUnblockLink")
	public ModelAndView sendUnblockLinkAction(@RequestParam String username, @RequestParam String email,
			WebRequest request) {
		User user = userService.findByUsernameAndEmail(username, email);
		VerificationToken token = userService.generateVerificationToken(user);
		String applicationUrl = "http://" + request.getHeader("Host") + request.getContextPath() + "/unblock";
		userService.sendVerificationToken(user, applicationUrl, token.getToken());
		return new ModelAndView("redirect:/user/register/unblockLinkSent");
	}

	@GetMapping(value = "/unblock")
	public ModelAndView unblockAction(@RequestParam("token") String token, WebRequest request,
			Model model) {
		VerificationToken verificationToken = userService.getVerificationToken(token);
		if (verificationToken == null) {
			throw new VerificationTokenNotFoundException();
		}
		Calendar cal = Calendar.getInstance();
		if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
			return new ModelAndView("user/register/unblockTokenExpired", "token", token);
		}
		userService.unblockUser(verificationToken.getUser().getUsername());
		return new ModelAndView("redirect:/user/register/unblockSuccessful");
	}

}