package com.company.services.user;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.exceptions.EmailAlreadyExistsException;
import com.company.exceptions.UserAlreadyExistsException;
import com.company.exceptions.UserNotFoundException;
import com.company.exceptions.VerificationTokenNotFoundException;
import com.company.model.user.Role;
import com.company.model.user.User;
import com.company.model.user.UserChangePasswordDTO;
import com.company.model.user.UserProfileDTO;
import com.company.model.user.UserRegistrationDTO;
import com.company.model.user.VerificationToken;
import com.company.repository.user.RoleRepository;
import com.company.repository.user.UserRepository;
import com.company.repository.user.VerificationTokenRepository;
import com.company.services.mailer.MailerService;

@Service
public class UserService {

	@Autowired
	private UserRepository<User> uRepository;

	@Autowired
	private RoleRepository rRepository;

	@Autowired
	private VerificationTokenRepository vRepository;

	@Autowired
	private MailerService mailerService;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private HttpServletRequest request;

	public User register(UserRegistrationDTO userRegistrationDTO)
			throws UserAlreadyExistsException, EmailAlreadyExistsException {
		User user = uRepository.findByUsername(userRegistrationDTO.getUsername());
		if (user != null) {
			throw new UserAlreadyExistsException();
		}
		user = uRepository.findByEmail(userRegistrationDTO.getEmail());
		if (user != null) {
			throw new EmailAlreadyExistsException();
		}
		user = new User();
		user.setUsername(userRegistrationDTO.getUsername());
		user.setEmail(userRegistrationDTO.getEmail());
		user.setFirstName(userRegistrationDTO.getFirstName());
		user.setMiddleName(userRegistrationDTO.getMiddleName());
		user.setLastName(userRegistrationDTO.getLastName());
		user.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));
		user.addRole(rRepository.findByName("USER"));
		uRepository.save(user);
		return user;
	}

	@Transactional
	public void confirmRegistration(VerificationToken verificationToken) {
		User user = verificationToken.getUser();
		user.setAccountLocked(false);
		uRepository.save(user);
		verificationToken.setVerified(true);
		vRepository.save(verificationToken);
	}
	
	public User createUserAccount(UserProfileDTO userProfileDTO)
			throws UserAlreadyExistsException, EmailAlreadyExistsException {
		User user = uRepository.findByUsername(userProfileDTO.getUsername());
		if (user != null) {
			throw new UserAlreadyExistsException();
		}
		user = uRepository.findByEmail(userProfileDTO.getEmail());
		if (user != null) {
			throw new EmailAlreadyExistsException();
		}
		user = new User();
		user.setUsername(userProfileDTO.getUsername());
		user.setEmail(userProfileDTO.getEmail());
		user.setFirstName(userProfileDTO.getFirstName());
		user.setMiddleName(userProfileDTO.getMiddleName());
		user.setLastName(userProfileDTO.getLastName());
		User manager = uRepository.findByUsername(request.getUserPrincipal().getName());
		user.setManager(manager);
		String randomPassword = UUID.randomUUID().toString().substring(0, 8);
		System.out.println("Random password is " + randomPassword);
		user.setPassword(passwordEncoder.encode(randomPassword));
		user.addRole(rRepository.findByName("USER"));
		uRepository.save(user);
		return user;
	}

	public User resetPassword(User user, String password) {
		user.setPassword(passwordEncoder.encode(password));
		return uRepository.save(user);
	}

	public User changePassword(String username, UserChangePasswordDTO userChangePasswordDTO) {
		User user = this.findByUsername(username);
		if (!passwordEncoder.matches(userChangePasswordDTO.getOldPassword(), user.getPassword()))
			throw new BadCredentialsException("Password is invalid");
		user.setPassword(passwordEncoder.encode(userChangePasswordDTO.getPassword()));
		return uRepository.save(user);
	}

	public VerificationToken generateVerificationToken(User user) {
		String token = UUID.randomUUID().toString();
		VerificationToken verificationToken = vRepository.findByUser(user);
		if (verificationToken == null)
			verificationToken = new VerificationToken(user, token);
		else {
			verificationToken.setToken(token);
			verificationToken.setExpiryDate();
		}
		return vRepository.save(verificationToken);
	}

	public VerificationToken generateNewVerificationToken(String existingToken)
			throws VerificationTokenNotFoundException {
		VerificationToken verificationToken = vRepository.findByToken(existingToken);
		if (verificationToken == null)
			throw new VerificationTokenNotFoundException();
		verificationToken.setToken(UUID.randomUUID().toString());
		verificationToken.setExpiryDate();
		return vRepository.save(verificationToken);
	}

	public VerificationToken getVerificationToken(String token) {
		return vRepository.findByToken(token);
	}

	public void sendVerificationToken(User user, String applicationUrl, String token) {
		// String recipientAddress = user.getEmail();
		// String subject = "Registration Confirmation";
		String confirmationUrl = applicationUrl + "?token=" + token;
		String message = "Message " + confirmationUrl;
		// Send an email
		mailerService.sendMail(message);
	}

	public User findByUsernameAndEmail(String username, String email) {
		User user = uRepository.findByUsernameAndEmail(username, email);
		if (user == null) {
			throw new UserNotFoundException();
		}
		return user;
	}

	public User findByUsername(String username) {
		User user = uRepository.findByUsername(username);
		if (user == null) {
			throw new UserNotFoundException();
		}
		return user;
	}

	public Page<User> findAll(Pageable pageable) {
		return uRepository.findAll(pageable);
	}
	
	public Page<User> findAllUsersByUsername(Pageable pageable, String username) {
		return uRepository.findAllByUsername(pageable,username);
	}
	
	public Page<User> findAllUsersByUserType(Pageable pageable, String userType) {
		if(userType!=null  && !userType.equals("ALL"))
			return uRepository.findAllByUserType(pageable,userType);
		return this.findAll(pageable);
	}

	public User updateUser(String username, UserProfileDTO userProfileDTO) {
		User user = this.findByUsername(username);
		user.setFirstName(userProfileDTO.getFirstName());
		user.setMiddleName(userProfileDTO.getMiddleName());
		user.setLastName(userProfileDTO.getLastName());
		return uRepository.save(user);
	}

	public void deleteUser(String username) {
		User user = this.findByUsername(username);
		uRepository.delete(user);
	}
	
	public void enableUser(String username) {
		User user = this.findByUsername(username);
		user.setEnabled(true);
		uRepository.save(user);
	}

	public void disableUser(String username) {
		User user = this.findByUsername(username);
		user.setEnabled(false);
		uRepository.save(user);
	}

	public void lockUser(String username) {
		User user = this.findByUsername(username);
		user.setAccountLocked(true);
		uRepository.save(user);
	}
	
	public void unblockUser(String username) {
		User user = this.findByUsername(username);
		user.setAccountLocked(false);
		uRepository.save(user);
	}
	

	public void setRoles(String username, Set<String> roleNames) {
		User user = this.findByUsername(username);
		Set<Role> roles = new HashSet<Role>();
		for(String roleName: roleNames) {
			roles.add(rRepository.findByName(roleName));	
		}
		user.setRoles(roles);
		uRepository.save(user);
	}
	
	public Page<Role> findAllRoles(Pageable pageable) {
		return rRepository.findAll(pageable);
	}

	public void sendPasswordResetLink(String username, String email) {
		User user = this.findByUsernameAndEmail(username, email);
		VerificationToken token = this.generateVerificationToken(user);
		String applicationUrl = "http://localhost:8080/user/resetPassword";
		this.sendVerificationToken(user, applicationUrl, token.getToken());
	}
	
	public void setUserManager(String username, String managerName) {
		User user = this.findByUsername(username);
		User manager = this.findByUsername(managerName);
		user.setManager(manager);
		uRepository.save(user);
	}

}