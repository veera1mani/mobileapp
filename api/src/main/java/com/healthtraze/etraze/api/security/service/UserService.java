package com.healthtraze.etraze.api.security.service;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.amazonaws.util.StringUtils;
import com.healthtraze.etraze.api.base.constant.Constants;
import com.healthtraze.etraze.api.base.constant.StringIteration;
import com.healthtraze.etraze.api.base.exception.PasswordExpiredException;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.base.service.BaseService;
import com.healthtraze.etraze.api.base.util.CommonUtil;
import com.healthtraze.etraze.api.base.util.ConfigUtil;
import com.healthtraze.etraze.api.masters.documents.Manufacturer;
import com.healthtraze.etraze.api.masters.model.ManagerManufacturerMapping;
import com.healthtraze.etraze.api.masters.model.Tenant;
import com.healthtraze.etraze.api.masters.model.TenantManufacture;
import com.healthtraze.etraze.api.masters.model.UserManager;
import com.healthtraze.etraze.api.masters.model.UserPreference;
import com.healthtraze.etraze.api.masters.repository.DeadlineListRepository;
import com.healthtraze.etraze.api.masters.repository.DeadlinesRepository;
import com.healthtraze.etraze.api.masters.repository.ManagerManufacturerMappingRepository;
import com.healthtraze.etraze.api.masters.repository.ManufacturerRepository;
import com.healthtraze.etraze.api.masters.repository.TenantManufactureRepository;
import com.healthtraze.etraze.api.masters.repository.TenantRepository;
import com.healthtraze.etraze.api.masters.repository.UserManagerRepository;
import com.healthtraze.etraze.api.masters.repository.UserPreferanceRepository;
import com.healthtraze.etraze.api.masters.repository.UserRoleServiceRepository;
import com.healthtraze.etraze.api.security.model.EmailTemplate;
import com.healthtraze.etraze.api.security.model.LoginResponse;
import com.healthtraze.etraze.api.security.model.MobilePin;
import com.healthtraze.etraze.api.security.model.MyProfile;
import com.healthtraze.etraze.api.security.model.NewLocationToken;
import com.healthtraze.etraze.api.security.model.PasswordDetails;
import com.healthtraze.etraze.api.security.model.PasswordHistoryModel;
import com.healthtraze.etraze.api.security.model.PasswordResetToken;
import com.healthtraze.etraze.api.security.model.Role;
import com.healthtraze.etraze.api.security.model.RoleServices;
import com.healthtraze.etraze.api.security.model.SendMail;
import com.healthtraze.etraze.api.security.model.User;
import com.healthtraze.etraze.api.security.model.UserAuth;
import com.healthtraze.etraze.api.security.model.UserRoleService;
import com.healthtraze.etraze.api.security.model.VerificationToken;
import com.healthtraze.etraze.api.security.repository.EmailRepository;
import com.healthtraze.etraze.api.security.repository.MobilePinRepository;
import com.healthtraze.etraze.api.security.repository.PasswordHistoryRepository;
import com.healthtraze.etraze.api.security.repository.PasswordResetTokenRepository;
import com.healthtraze.etraze.api.security.repository.RoleRepository;
import com.healthtraze.etraze.api.security.repository.RoleServiceRepository;
import com.healthtraze.etraze.api.security.repository.UserAuthRepository;
import com.healthtraze.etraze.api.security.repository.UserRepository;
import com.healthtraze.etraze.api.security.repository.VerificationTokenRepository;
import com.healthtraze.etraze.api.security.util.SecurityUtil;

@Service
public class UserService implements BaseService<User, String>, UserDetailsService, IUserService, ISecurityUserService {

	private Logger logger = LogManager.getLogger(UserService.class);
	
	private static final String PREFIX="U";

	private final UserRepository userRepository;
	private final VerificationTokenRepository verificationTokenRepository;
	private final UserPreferanceRepository userPreferanceRepository;
	private final UserAuthRepository userAuthRepository;
	private final EmailRepository emailRepo;
	private final PasswordHistoryRepository passwordHistoryRepository;
	private final RoleRepository roleRepository;
	private final EmailTemplateService emailTemplateService;
	private final UserPreferanceRepository preferanceRepository;
	private final PasswordResetTokenRepository passwordTokenRepository;
	private final TenantRepository tenantRepository;
	private final UserManagerRepository userManagerRepository;
	private final ManagerManufacturerMappingRepository managerManufacturerMappingRepository;
	private final ManufacturerRepository manufacturerRepository;
	private final UserRoleServiceRepository userRoleServiceRepository;
	private final MobilePinRepository mobilePinRepository;
	private final RoleServiceRepository roleServiceRepository;
	private final VerificationTokenRepository tokenRepository;
	private final TenantManufactureRepository tenantManufactureRepository;
	
	

	@Autowired
	public UserService(UserRepository userRepository, VerificationTokenRepository verificationTokenRepository,
			UserPreferanceRepository userPreferanceRepository, UserAuthRepository userAuthRepository,
			EmailRepository emailRepo, PasswordHistoryRepository passwordHistoryRepository,
			RoleRepository roleRepository, EmailTemplateService emailTemplateService,
			UserPreferanceRepository preferanceRepository, PasswordResetTokenRepository passwordTokenRepository,TenantRepository tenantRepository
			,UserManagerRepository userManagerRepository,ManagerManufacturerMappingRepository managerManufacturerMappingRepository
			,ManufacturerRepository manufacturerRepository,UserRoleServiceRepository userRoleServiceRepository, DeadlineListRepository deadlineListRepository,DeadlinesRepository deadlinesRepository,
			MobilePinRepository mobilePinRepository,RoleServiceRepository roleServiceRepository, VerificationTokenRepository tokenRepository,
			TenantManufactureRepository tenantManufactureRepository) {
		this.userRepository = userRepository;
		this.verificationTokenRepository = verificationTokenRepository;
		this.userPreferanceRepository = userPreferanceRepository;
		this.userAuthRepository = userAuthRepository;
		this.emailRepo = emailRepo;
		this.passwordHistoryRepository = passwordHistoryRepository;
		this.roleRepository = roleRepository;
		this.emailTemplateService = emailTemplateService;
		this.preferanceRepository = preferanceRepository;
		this.passwordTokenRepository = passwordTokenRepository;
		this.tenantRepository = tenantRepository;
		this.userManagerRepository = userManagerRepository;
		this.managerManufacturerMappingRepository = managerManufacturerMappingRepository;
		this.manufacturerRepository = manufacturerRepository ;
		this.userRoleServiceRepository=userRoleServiceRepository;
		this.mobilePinRepository = mobilePinRepository;
		this.roleServiceRepository= roleServiceRepository;
		this.tokenRepository=tokenRepository;
		this.tenantManufactureRepository = tenantManufactureRepository;
	}
	

	public static final String TOKEN_INVALID = "invalidToken";
	public static final String TOKEN_EXPIRED = "expired";
	public static final String TOKEN_VALID = "valid";

	public static final String QR_PREFIX = "https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=";
	public static final String APP_NAME = "SpringRegistration";

	@Override
	public List<User> findAll() {
		try {
			Optional<User> u = userRepository.findByUserId(SecurityUtil.getUserName());
			if (u.isPresent()) {
				List<User> user = userRepository.findByTenantId(u.get().getTenantId());
				if (!user.isEmpty()) {
					return user.stream().sorted(Comparator.comparing(User::getCreatedOn, Comparator.reverseOrder()))
							.collect(Collectors.toList());
				}
			}
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return new ArrayList<>();
	}
	
	
	public Result<HashMap<String, Object>> getAllUsers(int page, String sortBy, String sortDir, String search) {
	    Result<HashMap<String, Object>> result = new Result<>();
	    try {
	        HashMap<String,Object> map = new HashMap<>();
	        
	        Optional<User> u = userRepository.findByUserId(SecurityUtil.getUserName());
	        if(u.isPresent()) { 
	            int size = 10;
	            
	            if(StringUtils.isNullOrEmpty(sortBy)) {
	                sortBy = "user_id";
	            }
	            if(StringUtils.isNullOrEmpty(sortDir)) {
	                sortDir = "ASC";
	            }
	            
	            User user = u.get();
	           
	            int totalItems = userRepository.findUserAllByTenant(user.getTenantId(), search).size();
	            
	            Pageable paging = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
	            
	            List<User> users = userRepository.findUserAllByTenant(user.getTenantId(), search, paging);
	            List<User> res = new ArrayList<>();
	            for(User s : users) {
	                if(s.getRoleName().equals(StringIteration.MANAGER)) {
	                    List<ManagerManufacturerMapping> mm = managerManufacturerMappingRepository.findManufacturBYUserId(s.getUserId(), s.getTenantId());
	                    s.setManagerManufactureMapping(mm);
	                    res.add(s);
	                }
	                
	                if(s.getRoleName().equals("USER")) {
	                    List<UserRoleService> uu = userRoleServiceRepository.findByUser(s.getUserId(), u.get().getTenantId());
	                    s.setUserRoleServices(uu);
	                    res.add(s);
	                }
	            }

	            map.put("rowData", users);
	            map.put("totalCount", totalItems);
	            result.setData(map);
	            result.setCode(StringIteration.SUCCESS_CODE);
	            result.setMessage("success");

	        } else {
	            result.setCode(StringIteration.ERROR_CODE1);
	            result.setMessage("User not found"); 	        }

	    } catch (Exception e) {
	        logger.error(StringIteration.SPACE, e);
	        result.setCode(StringIteration.ERROR_CODE1);
	        result.setMessage("An error occurred while processing the request"); 
	    }
	    return result;
	}

	
	

	public List<User> findByRoleId(String roleId) {
		try {
			return userRepository.findByRoleId(roleId);
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return new ArrayList<>();
	}

	public Long count() {
		try {
			return userRepository.count();
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;

	}

	@Override
	public User findById(String userId) {
		try {

			Optional<User> option = userRepository.findByUserId(userId);
			if (option.isPresent()) {
				return option.get();
			}

		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	public User getUserByUserId(String userId) {
		try {
			Optional<User> user = userRepository.findByUserId(userId);
			if (user.isPresent()) {
				return user.get();
			}

		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	public LoginResponse findUserById(String id) {
		try {
			Optional<User> optional = userRepository.findByUserId(id);
			if (optional.isPresent()) {
				User u = optional.get();
				Optional<Role> r = roleRepository.findByRoleId(u.getRoleId());
				LoginResponse mapper = new LoginResponse();
				if(r.isPresent()) {
					Optional<Set<RoleServices>> services=roleServiceRepository.findByRoleId(r.get().getRoleId());
					if(services.isPresent()) {
					r.get().setRoleServices(services.get());	
					}
					
					mapper.setRole(r.get());
				}
				
				mapper.setUser(u);

				Optional<UserPreference> option = preferanceRepository.findById(u.getUserId());
				if (option.isPresent()) {
					mapper.setLanguage(option.get().getLanguage());
				} else {
					mapper.setLanguage(Constants.EN);
				}

				return mapper;
			}
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	private Optional<User> verifyPhoneNo(String phoneNo) {
		try {
			return userRepository.findByPhoneNo(phoneNo);
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return Optional.empty();
	}

	@Override
	public Result<User> update(User t) {
		Result<User> result = new Result<>();
		try {
			if (t != null) {

				Optional<User> us = userRepository.findByUserId(t.getUserId());
				if (us.isPresent()) {
					User u = us.get();
					u.setRoleId(t.getRoleId());
					u.setFirstName(t.getFirstName());
					u.setEmail(t.getEmail());
					u.setCountryCode(t.getCountryCode());
					u.setEnable(t.isEnable());
					u.setUserAppType(t.getUserAppType());
					u.setChecked(t.isChecked());
					u.setPicked(t.isPicked());
					u.setPacked(t.isPacked());
					u.setDispatched(t.isDispatched());
					u.setSaleable(t.isSaleable());
					u.setNonSaleable(t.isNonSaleable());
					if(t.getRoleName().equals("MANAGER")) {
						
						List<ManagerManufacturerMapping> mn= managerManufacturerMappingRepository.findManufacturBYUserId(u.getUserId(),u.getTenantId());
						 for(ManagerManufacturerMapping mp : mn) {
							 managerManufacturerMappingRepository.deleteById(mp.getId());
						 }
						
						List<ManagerManufacturerMapping> manufactures = t.getManagerManufactureMapping();
						for (ManagerManufacturerMapping manufacture : manufactures) {
							manufacture.setId(System.currentTimeMillis() + "");
							manufacture.setUserId(u.getUserId());
							Optional<Manufacturer> man = manufacturerRepository.findById(manufacture.getManufacturerid());
							manufacture.setManufactureName(man.get().getManufacturerName());
							manufacture.setTenantId(u.getTenantId());
						    managerManufacturerMappingRepository.save(manufacture);							    
						}
						}
					if(t.getRoleName().equals("USER")) {
						u.setHierarachyId(t.getHierarachyId());
						
					List<UserRoleService> li=userRoleServiceRepository.findAllByUserId(t.getUserId(),us.get().getTenantId());
					for(UserRoleService r : li) {
						userRoleServiceRepository.deleteById(r.getId());
					}
						List<UserRoleService> userRoleService = t.getUserRoleServices();
						
						for (UserRoleService use : userRoleService) {
							if(!StringUtils.isNullOrEmpty(use.getScreenId())) {
								
								use.setId(UUID.randomUUID().toString());
								use.setUserId(t.getUserId());
								use.setTenantId(us.get().getTenantId());
								userRoleServiceRepository.save(use);
								
							}
							
						}
					}
					 
					
					
					u.setLastName(t.getLastName());
					u.setPhoneNo(t.getPhoneNo());
					u.setStatus(t.getStatus());
					u.setImageUrl(t.getImageUrl());
					CommonUtil.setModifiedOn(u);
					if (t.getRoleId() == null) {
						t.setRoleId(ConfigUtil.getSuperAdminRoleId());
					}

					Optional<Role> optionRole = roleRepository.findById(t.getRoleId());
					if (optionRole.isPresent()) {
						t.setRoleName(optionRole.get().getRoleName());
					}
					
	
					User usr = userRepository.save(u);
					result.setCode(StringIteration.SUCCESS_CODE);
					result.setMessage(Constants.SUCCESSFULLYUPDATED);
					result.setData(usr);
				}
			}
		} catch (Exception e) {
			result.setCode(StringIteration.ERROR_CODE1);
			result.setMessage(e.getLocalizedMessage());
		}
		return result;
	}

	
	
	public Result<User> updateUserProfileImage(User t) {
		Result<User> result = new Result<>();
		try {
			if (t != null) {
				Optional<User> us = userRepository.findByUserId(t.getUserId());
				if (us.isPresent()) {
					User u = us.get();
					u.setImageUrl(t.getImageUrl());
					CommonUtil.setModifiedOn(u);
					User usr = userRepository.save(u);
					result.setCode(StringIteration.SUCCESS_CODE);
					result.setMessage(Constants.SUCCESSFULLYUPDATED);
					result.setData(usr);
				}
			}
		} catch (Exception e) {
			result.setCode(StringIteration.ERROR_CODE1);
			result.setMessage(e.getLocalizedMessage());
			logger.error(StringIteration.SPACE, e);
		}
		return result;
	}

	@Override
	public Result<User> delete(String userId) {
		Result<User> result = new Result<>();
		try {
			userRepository.deleteByUserId(userId);
			userAuthRepository.deleteByUserId(userId);
			result.setCode(StringIteration.SUCCESS_CODE);
			result.setMessage(StringIteration.SUCCESSFULLYDELETED);
		} catch (Exception e) {
			result.setCode(StringIteration.ERROR_CODE1);
			result.setMessage(e.getLocalizedMessage());
			logger.error(StringIteration.SPACE, e);
		}
		return result;
	}

	



	public String createPasswordResetTokenForUser(final UserAuth user, final String token) {

		PasswordResetToken p = new PasswordResetToken();
		UserAuth us = new UserAuth();
		us.setEmailId(user.getEmailId());
		us.setAuthEnabled(true);
		p.setUser(us);

		List<PasswordResetToken> op = passwordTokenRepository.findAll(Example.of(p));

		for (PasswordResetToken passwordResetToken : op) {
			passwordTokenRepository.delete(passwordResetToken);
		}

		final PasswordResetToken myToken = new PasswordResetToken(token, user);

		passwordTokenRepository.save(myToken);

		Optional<User> u = userRepository.findById(user.getUserId());
		if (u.isPresent()) {

			Optional<UserPreference> upOption = userPreferanceRepository.findById(user.getUserId());
			String language = Constants.EN;
			if (upOption.isPresent()) {
				language = upOption.get().getLanguage();
			}
			constructResetTokenEmail(token, u.get(), language);

		}
		return token;
	}

	private void constructResetTokenEmail(final String token, final User user, final String language) {

		EmailTemplate emailTemplate = emailTemplateService.findById("RESET_PASSWORD");
		SendMail mail = new SendMail();
		mail.setFrom(ConfigUtil.getFromEmail());
		mail.setSubject("HealthTraze - Password Reset");
		String[] s = { user.getEmail() };
		mail.setTo(s);
		mail.setToName(user.getFirstName());
		if(user.getRoleName().equals(StringIteration.SUPERADMIN)) {
			Optional<Tenant> ten=tenantRepository.findById(user.getTenantId());
			if(ten.isPresent()) {
				mail.setToName(ten.get().getTenantName());
			}
		}
		mail.setStatus(Constants.PENDING);
		mail.setId(System.currentTimeMillis() + "");
		VelocityContext context = new VelocityContext();
		context.put("user", user);
		context.put("token", token);
		context.put("name", user.getFirstName() + " " + user.getLastName());
		context.put("origin", ConfigUtil.getAppLink());
		context.put("language", language);

		StringWriter writer = new StringWriter();
		String templateStr = emailTemplate.getMailTemplate();
		Velocity.evaluate(context, writer, Constants.LOGTAGNAME, templateStr);
		mail.setMessage(writer.toString());

		emailRepo.save(mail);
	}

	public static void main(String[] args) {
		UserService service = new UserService(null, null, null, null, null, null, null, null, null,null, null,null,null,null,null,null,null,null,null,null,null);
		UserAuth user = new UserAuth();

		user.setEmailId("a@gmail.com");
		service.constructResendVerificationTokenEmail(Constants.ASDASDAS, user, Constants.EN);
	}

	public void constructResendVerificationTokenEmail(final String token, final UserAuth user, final String language) {
		EmailTemplate emailTemplate = emailTemplateService.findById("WELCOME");
		if (emailTemplate == null) {
			logger.error("WELCOME is empty");
		}
		Optional<User> u = userRepository.findByUserId(user.getUserId());
		if (u.isPresent() && emailTemplate != null) {

			User user2 = u.get();

			SendMail mail = new SendMail();
			mail.setFrom(ConfigUtil.getFromEmail());
			mail.setSubject(emailTemplate.getSubject());
			String[] s = { user2.getEmail() };
			mail.setTo(s);
			mail.setToName(user2.getFirstName());
			mail.setStatus(Constants.PENDING);
			mail.setId(CommonUtil.getID());
			VelocityContext context = new VelocityContext();
			context.put("user", user);
			context.put("token", token);
			context.put("origin", ConfigUtil.getAppLink());
			context.put("language", language);
			if(user2.getRoleName().equalsIgnoreCase("STOCKIST") || user2.getRoleName().equalsIgnoreCase("TRANSPORT")) {
				context.put(StringIteration.FULLNAME, user2.getFirstName());
			}else if(user2.getRoleName().equalsIgnoreCase(StringIteration.SUPERADMIN)) {
				Optional<Tenant> tn =tenantRepository.findById(user2.getTenantId());
				tn.ifPresent(m->mail.setToName(m.getTenantName()));
				context.put(StringIteration.FULLNAME, user2.getFirstName());
			}
			else {
				context.put(StringIteration.FULLNAME, user2.getFirstName() + StringIteration.SPACE + user2.getLastName());
			}
			Optional<Role> roles = roleRepository.findByRoleId(user2.getRoleId());

			if (roles.isPresent()) {
				context.put("role", roles.get().getDescription());
			}
			context.put("email", user2.getEmail());
			context.put("userId", user2.getUserId());
			context.put("phoneNo", user2.getPhoneNo());
			StringWriter swOut = new StringWriter();
			String templateStr = emailTemplate.getMailTemplate();
			Velocity.evaluate(context, swOut, Constants.LOGTAGNAME, templateStr);
			mail.setMessage(swOut.toString());
			mail.setTenantId(user.getTenantId());
			CommonUtil.setCreatedOn(mail);
			mail.setDeliverDate(new Date());
			emailRepo.save(mail);

			logger.info(Constants.DONE);

		}

	}

	/**
	 * 
	 * 
	 * 
	 */
	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException, PasswordExpiredException {

		boolean accountNonExpired = true;
		boolean credentialsNonExpired = true;
		boolean accountNonLocked = true;
		boolean userActive = false;

		try {
			Optional<UserAuth> auth = userAuthRepository.findByUserId(userId);
			if (!auth.isPresent()) {
				throw new UsernameNotFoundException(userId + StringIteration.NOTFOUND);
			}
			UserAuth a = auth.get();
			byte[] p = Base64.getDecoder().decode(a.getPassword().getBytes());
			Optional<User> user = userRepository.findById(a.getUserId());

			if (user.isPresent()) {
				Optional<Role> role = roleRepository.findById(user.get().getRoleId());
				if (role.isPresent()) {
					User a1 = user.get();
					Integer date = CommonUtil.dateDifferance(a.getPasswordExpiryDate());
					if (a1.getStatus().equalsIgnoreCase(Constants.ACTIVE)) {
						userActive = true;
					}

					if (!a.isCredentialsNonExpired()) {
						throw new PasswordExpiredException(StringIteration.PASSWORDEXPIRED);
					}

					if (date <= 0) {
						a.setCredentialsNonExpired(false);
						

						Optional<UserAuth> userAuth = userAuthRepository.findByUserId(a.getUserId());

						if (userAuth.isPresent()) {
							UserAuth userauth = userAuth.get();
							userauth.setCredentialsNonExpired(false);
							userAuthRepository.save(userauth);
						}


						logger.info(StringIteration.TODAYYOURPASSWORDEXPIRED);
					}

					if (date < 5) {
						logger.info(StringIteration.PASSWORDISEXPIREDON, date, StringIteration.DAYS);
					}
					logger.info(StringIteration.SETCREDENTIALNONEXPIRED, a.isCredentialsNonExpired());
					return new org.springframework.security.core.userdetails.User(a.getUserId(), new String(p),
							userActive, accountNonExpired, credentialsNonExpired, accountNonLocked,
							Arrays.asList(new SimpleGrantedAuthority(role.get().getRoleName())));
				}
			}

		} catch (CredentialsExpiredException e) {
			logger.info(e.getMessage());

		}
		return null;

	}

	@Override
	public UserAuth getUser(String verificationToken) {
		return null;
	}

	@Override
	public void createVerificationTokenForUser(UserAuth user, String token) {
		VerificationToken token2 = new VerificationToken(token, user);
		token2.setId(System.currentTimeMillis());
		token2.setStatus(Constants.INACTIVE);

		verificationTokenRepository.save(token2);
		tokenRepository.save(token2);
	}

	@Override
	public VerificationToken getVerificationToken(String verificationToken) {
		return tokenRepository.findByToken(verificationToken);
	}

	
	

	@Override
	public VerificationToken generateNewVerificationToken(String existingVerificationToken) {
		VerificationToken vToken = tokenRepository.findByToken(existingVerificationToken);

		if (vToken != null) {
			vToken.updateToken(UUID.randomUUID().toString());
			return tokenRepository.save(vToken);

		}
		return vToken;
	}

	@Override
	public UserAuth findUserByEmail(String email) {
		Optional<UserAuth> option = userAuthRepository.findByUserId(email);
		if (option.isPresent()) {
			return option.get();
		}
		return null;
	}

	@Override
	public PasswordResetToken getPasswordResetToken(String token) {
		return null;
	}

	@Override
	public Optional<UserAuth> getUserByPasswordResetToken(String token) {
		return Optional.ofNullable(passwordTokenRepository.findByToken(token).getUser());
	}

	@Override
	public Optional<User> getUserByID(String id) {
		return userRepository.findById(id);
	}

	@Override
	public Result<String> changeUserPassword(UserAuth user, String password) {
		Result<String> result = new Result<>();

		boolean isPasswordInTheHistory = checkPasswordFromPasswordHistory(user.getUserId(), password);
		if (isPasswordInTheHistory) {
			result.setCode(StringIteration.ERROR_CODE4);
			result.setMessage(StringIteration.YOURNEWPASSWORDALREADYUSEDINTHEPASSWORDHISTORY);
			return result;
		}

		byte[] p = Base64.getEncoder().encode(password.getBytes());
		

		Optional<UserAuth> userAuth = userAuthRepository.findByUserId(user.getUserId());

		if (userAuth.isPresent()) {
			UserAuth ua = userAuth.get();
			ua.setPassword(new String(p));
			ua.setPasswordExpiryDate(CommonUtil.getPasswordExpiryDate());
			ua.setCredentialsNonExpired(true);
			userAuthRepository.save(ua);
		}



		updatePasswordHistory(user.getUserId(), new String(p));

		result.setCode(StringIteration.SUCCESS_CODE);
		result.setMessage(Constants.SUCCESS);
		return result;

	}

	/**
	 * 
	 * 
	 * @param user
	 * @param password
	 * @return
	 */
	public Result<String> createUserPassword(UserAuth user, String password) {
		Result<String> result = new Result<>();
		boolean isPasswordInTheHistory = checkPasswordFromPasswordHistory(user.getUserId(), password);
		
		if (isPasswordInTheHistory) {
			result.setCode(StringIteration.ERROR_CODE4);
			result.setMessage(StringIteration.YOURNEWPASSWORDALREADYUSEDINTHEPASSWORDHISTORY);
			return result;
		}

		byte[] p = Base64.getEncoder().encode(password.getBytes());
		
		Optional<UserAuth> n = userAuthRepository.findByUserId(user.getUserId());

		if (n.isPresent()) {
			UserAuth userAuth = n.get();
			userAuth.setPassword(new String(p));
			userAuth.setAuthEnabled(true);
			userAuth.setCredentialsNonExpired(true);
			userAuth.setPasswordExpiryDate(CommonUtil.getPasswordExpiryDate());
			userAuthRepository.save(userAuth);
		}


		Optional<User> op = userRepository.findByUserId(user.getUserId());

		if (op.isPresent()) {
			User currentUser = op.get();
			currentUser.setNewUserValidateWeb(false);
			currentUser.setStatus(Constants.ACTIVE);
			userRepository.save(currentUser);
		}

		updatePasswordHistory(user.getUserId(), new String(p));
		result.setCode(StringIteration.SUCCESS_CODE);
		return result;
	}

	/**
	 * to update the password history
	 * 
	 * 
	 * @param userId
	 * @param password
	 */

	public void updatePasswordHistory(String userId, String password) {
		Optional<PasswordHistoryModel> option = passwordHistoryRepository.findById(userId);
		PasswordHistoryModel m = null;
		if (option.isPresent()) {
			m = option.get();
			if (m.getPasswordDetail().size() == 3) {
				m.getPasswordDetail().remove(0);
			}
			m.getPasswordDetail().add(new PasswordDetails(new Date(), password));
		} else {
			ArrayList<PasswordDetails> list = new ArrayList<>();
			list.add(new PasswordDetails(new Date(), password));
			m = new PasswordHistoryModel(userId, list);
		}
		passwordHistoryRepository.save(m);
	}

	/**
	 * If password matched in the password history then need not be allowed. If not,
	 * then allow
	 * 
	 * 
	 * 
	 */
	public boolean checkPasswordFromPasswordHistory(String userId, String password) {
		PasswordHistoryModel passmodel = passwordHistoryRepository.findByUserId(userId);

		if (passmodel != null && passmodel.getPasswordDetail() != null) {
			for (PasswordDetails p : passmodel.getPasswordDetail()) {
				byte[] ps = Base64.getDecoder().decode(p.getPassword().getBytes());
				String np = new String(ps);
				if (password.equals(np)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean checkIfValidOldPassword(UserAuth user, String oldPassword) {

		if (user == null) {
			return false;
		}

		byte[] p = Base64.getDecoder().decode(user.getPassword().getBytes());

		if (new String(p).equals(oldPassword)) {
			return true;
		}

		return false;
	}

	@Override
	public String validateVerificationToken(String token) {
		return null;
	}

	@Override
	public String generateQRUrl(User user) throws UnsupportedEncodingException {
		return null;
	}

	@Override
	public User updateUser2FA(boolean use2fa) {
		return null;
	}

	@Override
	public List<String> getUsersFromSessionRegistry() {
		return Collections.emptyList();
	}

	@Override
	public NewLocationToken isNewLoginLocation(String username, String ip) {
		return null;
	}

	@Override
	public String isValidNewLocationToken(String token) {
		return null;
	}
	

	@Override
	public String validatePasswordResetToken(String token) {
	    final PasswordResetToken passToken = passwordTokenRepository.findByToken(token);

	    if (!isTokenFound(passToken)) {
	        return Constants.INVALIDTOKEN;
	    } else if (isTokenExpired(passToken)) {
	        return Constants.EXPIRED;
	    } else {
	        return null;
	    }
	}

	

	private boolean isTokenFound(PasswordResetToken passToken) {
		return passToken != null;
	}

	private boolean isTokenExpired(PasswordResetToken passToken) {
		return passToken.getExpiryDate().isBefore(CommonUtil.getLocalDateTime());
	}

	public Optional<User> findByLoginUserDetails() {
		return userRepository.findByUserId(SecurityUtil.getUserName());
	}

	public Result<User> emailExits(User userEmail) {

		Result<User> result = new Result<>();

		if (userEmail.getEmail() == null) {
			result.setCode(StringIteration.ERROR_CODE1);
			result.setMessage(StringIteration.EMAILSHOULDNOTBEEMPTY);
			return result;
		}

		String regex = "^(.+)@(.+)$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(userEmail.getEmail());

		if (!matcher.matches()) {
			result.setCode(StringIteration.ERROR_CODE1);
			result.setMessage(StringIteration.PLEASEENTERVALIDEMAILADDRESS);
			return result;
		}

		Optional<User> u = userRepository.findByUserId(userEmail.getUserId());

		if (u.isPresent()) {

			result.setCode(StringIteration.ERROR_CODE1);
			result.setMessage(StringIteration.EMAILADDRESSISALREADYEXISTS);

			return result;

		} else {
			result.setCode(StringIteration.SUCCESS_CODE);
			result.setMessage(StringIteration.NOTEXISTS);
		}

		return result;
	}
	

	public Result<User> phoneNumberExits(User phone) {

		Result<User> result = new Result<>();

		if (phone.getPhoneNo() == null) {
			result.setCode(StringIteration.ERROR_CODE1);
			result.setMessage(StringIteration.PHONENUMBERSHOULDNOTBEEMPTY);
			return result;
		}

		Pattern ptrn = Pattern.compile("(0/91)?[5-9]\\d{9}");

		Matcher match = ptrn.matcher(phone.getPhoneNo());

		if (!match.matches()) {
			result.setCode(StringIteration.ERROR_CODE1);
			result.setMessage(StringIteration.PLEASEENTERVALIDPHONENUMBER);
			return result;
		}

		Optional<User> u = userRepository.findByPhoneNo(phone.getPhoneNo());
		 if (u.isPresent()) {

			result.setCode(StringIteration.ERROR_CODE1);
			result.setMessage(StringIteration.PHONENUMBERALREADYEXISTS);
			return result;
		}

		else {
			result.setCode(StringIteration.SUCCESS_CODE);
			result.setMessage(StringIteration.NOTEXISTS);
		}

		return result;
	}

	
	public Result<UserPreference> create(UserPreference t) {

		Result<UserPreference> result = new Result<>();
		Optional<User> userpreferance = userRepository.findById(SecurityUtil.getUserName());
		if (userpreferance.isPresent()) {
			UserPreference up = userPreferanceRepository.findByUserId(userpreferance.get().getUserId());
			if (up != null) {
				if (t.getLanguage() != null) {
					up.setLanguage(t.getLanguage());
					up.setUserId(userpreferance.get().getUserId());
				}
				if (t.getCurrency() != null) {
					up.setCurrency(t.getCurrency());
					up.setUserId(userpreferance.get().getUserId());
				}
				if (t.getTimeZone() != null) {
					up.setTimeZone(t.getTimeZone());
				}
				if (t.getDateFormat() != null) {
					up.setDateFormat(t.getDateFormat());
				}
				


				userPreferanceRepository.save(up);
				result.setCode(StringIteration.SUCCESS_CODE);
				result.setMessage(StringIteration.UPDATEDSUCCESSFULLY);
				result.setData(up);
			}
		}
		return result;
	}

	
	public Result<HashMap<String, Object>> myProfile() {	
		Result<HashMap<String, Object>> result = new Result<>();
		HashMap<String, Object> map = new HashMap<>();		     
		Optional<User> us = userRepository.findById(SecurityUtil.getUserName());	
		if (us.isPresent()) {
			User u  = us.get();
			if(u.getRoleName().equals(StringIteration.BUSINESS_ADMIN)) {
				MyProfile myProfile = new MyProfile();
				myProfile.setUser(u);
				map.put("profile", myProfile);
				result.setCode(StringIteration.SUCCESS_CODE);
				result.setMessage(StringIteration.USERISPRESENT);
				result.setData(map);
			}else {
		    	MobilePin m = mobilePinRepository.updatePin(u.getUserId(),u.getTenantId());
		    	MyProfile myProfile = new MyProfile();
		    	myProfile.setUser(u); 
				if(m!=null) {
					map.put("pin", m.getPin());
				} else {
					map.put("pin", "");
				}
		    	map.put("profile", myProfile);
			    result.setCode(StringIteration.SUCCESS_CODE);
		    	result.setMessage(StringIteration.USERISPRESENT);
		    	result.setData(map);
			}
		}		
		return result;
}

	
	public Result<MyProfile> myProfileUpdate(MyProfile t) {

		Result<MyProfile> result = new Result<>();

		Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
		
		
		MyProfile myProfile = new MyProfile();
		
		if (us.isPresent() ) {
			User u = us.get();
			if(u.getRoleName().equals(StringIteration.BUSINESS_ADMIN)) {
				u.setFirstName(t.getUser().getFirstName());
				u.setLastName(t.getUser().getLastName());
				u.setPhoneNo(t.getUser().getPhoneNo());
				u.setImageUrl(t.getUser().getImageUrl());
				userRepository.save(u);			
				myProfile.setUser(u);
				result.setCode(StringIteration.SUCCESS_CODE);
				result.setMessage(StringIteration.USERUPDATEDSUCCESSFULLY);
				result.setData(myProfile);
				return result;
			}else {
			
			MobilePin m = mobilePinRepository.updatePin(u.getUserId(), u.getTenantId());
			u.setFirstName(t.getUser().getFirstName());
			u.setLastName(t.getUser().getLastName());
			u.setPhoneNo(t.getUser().getPhoneNo());
			u.setImageUrl(t.getUser().getImageUrl());
			if(!t.getUser().getGeneratePin().isEmpty()) {
				m.setPin(t.getUser().getGeneratePin());
				mobilePinRepository.save(m);
			}		
			userRepository.save(u);			
			myProfile.setUser(u);
			result.setCode(StringIteration.SUCCESS_CODE);
			result.setMessage(StringIteration.USERUPDATEDSUCCESSFULLY);
			result.setData(myProfile);
			return result;
			}
		}
		return result;

	}
	
	

	public VerificationToken update(VerificationToken t) {
		verificationTokenRepository.save(t);
		return t;
	}

	public List<User> findPaginated(int pageNo, int pageSize) {
		PageRequest paging = PageRequest.of(pageNo, pageSize);
		org.springframework.data.domain.Page<User> pagedResult = userRepository.findAll(paging);
		return pagedResult.toList();
	}

	@Override
	public void saveRegisteredUser(User user) {
   // TODO document why this method is empty
 }

	@Override
	public void deleteUser(User user) {
   // TODO document why this method is empty
 }

	@Override
	public void addUserLocation(User user, String ip) {
   // TODO document why this method is empty
 }

	public Result<User> signUp(User t) {
		User user = new User();
		Result<User> result = new Result<>();
		try {
			
			String emailId = t.getEmail();
			
			if (emailId != null) {
				emailId = emailId.trim();
			}

			t.setNewUserValidateMobile(true);
			
			if (t.getUserId() == null) {			
				Optional<User> userOptional = userRepository.findByUserId(SecurityUtil.getUserName());
			
				Optional<Tenant> tenantOptional= tenantRepository.findByEmail(userOptional.get().getTenantId());				
				if(tenantOptional.isPresent()) {
				String id =tenantOptional.get().getTenantCode();
				BigInteger currentSequentialNumber = userRepository.getNextSequentialNumberForUser(tenantOptional.get().getTenantId());
				if(currentSequentialNumber!=null) {
				BigInteger nextSequentialNumber = currentSequentialNumber.add(BigInteger.ONE);

				String userID = String.format("%sU%04d", id, nextSequentialNumber);

				t.setUserId(userID);
				
				}
				else {
					String userID = String.format("%sU%04d", id, 0001);

					t.setUserId(userID);
				}
				}
				else {
					String sequenceNumber ="";
					
					String	currentSequentialNumber = userRepository.getNextSequentialNumberForTenant();
					if(currentSequentialNumber !=null) {
						sequenceNumber= String.format("%s%04d", PREFIX, new BigInteger(currentSequentialNumber).add(BigInteger.ONE));
					}else {
						sequenceNumber= String.format("%s%04d", PREFIX,0001);
					}
					t.setUserId(sequenceNumber);
				}					
				
			}
			t.setNewUserValidateWeb(true);
			t.setStatus(Constants.INACTIVE);
			CommonUtil.setCreatedOn(t);
				Optional<Role> rl =	roleRepository.findByRoleId(t.getRoleId());
					if(rl.isPresent()) {
						t.setRoleName(rl.get().getRoleName());
					}
			t.setWebChannel(true);
			Optional<User> userOptional = userRepository.findByUserId(SecurityUtil.getUserName());
			if (userOptional.isPresent()) {
			    User users = userOptional.get();
			    String tenant = users.getTenantId();
			    if (tenant != null) {
			        t.setTenantId(tenant);
			    }
			}
			if(t.getRoleName().equals("USER")) {						
				t.setHierarachyId(t.getHierarachyId());
			}
			
			
			
			 user = userRepository.save(t);
			result.setCode(StringIteration.SUCCESS_CODE);
			result.setMessage(StringIteration.USERCREATEDSUCCESSFULLY);
			UserManager userManager = new UserManager();
			userManager.setId(System.currentTimeMillis() + "");
			userManager.setManager(user.getHierarachyId());
			userManager.setUserId(user.getUserId());
			if (userOptional.isPresent()) {
				userManager.setTenantId(userOptional.get().getTenantId());
			}
			userManagerRepository.save(userManager);
			if(t.getRoleName().equals("MANAGER")) {
			List<ManagerManufacturerMapping> manufactures = t.getManagerManufactureMapping();
			for (ManagerManufacturerMapping manufacture : manufactures) {
				manufacture.setId(System.currentTimeMillis() + "");
				manufacture.setUserId(user.getUserId());
				Optional<Manufacturer> man = manufacturerRepository.findById(manufacture.getManufacturerid());
				manufacture.setManufactureName(man.get().getManufacturerName());
				if (userOptional.isPresent()) {
					manufacture.setTenantId(userOptional.get().getTenantId());
				}
			    managerManufacturerMappingRepository.save(manufacture);							    
			}
			}
			if(t.getRoleName().equals("USER")) {
				List<UserRoleService> userRoleService = t.getUserRoleServices();
				for (UserRoleService userRole : userRoleService) {
						userRole.setId(UUID.randomUUID().toString());
						userRole.setUserId(user.getUserId());
						if (userOptional.isPresent()) {
							userRole.setTenantId(userOptional.get().getTenantId());
						}
						userRoleServiceRepository.save(userRole);
				}
			}
			UserAuth auth = new UserAuth();
			auth.setId(System.currentTimeMillis() + "");
			auth.setEmailId(user.getEmail());
			auth.setUserId(user.getUserId());
			auth.setAuthEnabled(false);
			auth.setTenantId(user.getTenantId());
			CommonUtil.setCreatedOn(auth);
			userAuthRepository.save(auth);

			final String token = UUID.randomUUID().toString();

			createVerificationTokenForUser(auth, token);

			Optional<UserPreference> upOption = userPreferanceRepository.findById(user.getUserId());
			String language = Constants.EN;
			String timeZone = "Asia/Kolkata";
			String dateFormat = "MM/DD/YY";
			if (upOption.isPresent()) {
				language = upOption.get().getLanguage();
			} else {
				UserPreference preference = new UserPreference();
				preference.setLanguage(Constants.EN);
				preference.setUserId(user.getUserId());
				preference.setTimeZone(timeZone);
				preference.setDateFormat(dateFormat);
				preference.setPreferenceId(System.currentTimeMillis() + "");
				if (userOptional.isPresent()) {
					preference.setTenantId(userOptional.get().getTenantId());
				}
				userPreferanceRepository.save(preference);
			}

			constructResendVerificationTokenEmail(token, auth, language);
			result.setCode(StringIteration.SUCCESS_CODE);
			result.setMessage(StringIteration.SUCCESS_MESSAGE);

			result.setData(user);
			return result;
		

		} catch (Exception e) {
			result.setCode(StringIteration.ERROR_CODE1);
		}
		return result;
	}
	
//	public void errorCreatingUser(String userId,String tenantId){
//		Optional<User> us = userRepository.findById(userId);
//		if(us.isPresent()) {		
//			if(us.get().getRoleName().equals(StringIteration.MANAGER)) {
//				List<ManagerManufacturerMapping> mmm = managerManufacturerMappingRepository.findAllManufacturBYUserId(userId,tenantId);
//				if(!mmm.isEmpty()) {
//					managerManufacturerMappingRepository.deleteAll(mmm);
//				}
//			}else if(us.get().getRoleName().equals(StringIteration.USER)) {
//				List<UserRoleService> rs = userRoleServiceRepository.findAllByUserId(userId, tenantId);
//				if(!rs.isEmpty()) {
//					userRoleServiceRepository.deleteAll(rs);
//				}
//			}	
//			userRepository.delete(us.get());
//			Optional<UserAuth> ua = userAuthRepository.findByUserId(userId);
//			if(ua.isPresent()) {
//				userAuthRepository.delete(ua.get());
//			}				
//		}
//	}

	@Override
	public Result<User> create(User t) {

		return null;
	}
	

	public List<User> findManagers() {
	    try {
	        Optional<User> user1 = userRepository.findByUserId(SecurityUtil.getUserName());
	        if (user1.isPresent()) {
	            Optional<Tenant> tn = tenantRepository.findById(user1.get().getTenantId());
	            if (tn.isPresent()) {
	                List<User> us = userRepository.findManagers(tn.get().getTenantId());
	                for (User user : us) {
	                    List<ManagerManufacturerMapping> mn = managerManufacturerMappingRepository.findManufacturBYUserId(user.getUserId(), user.getTenantId());
	                    user.setManagerManufactureMapping(mn);
	                }
	                return us;
	            }
	        }
	    } catch (Exception e) {
	    	 logger.error(e);
	    }
	    return Collections.emptyList();
	}
	
	
	public Result<User> deactivateUser(String userId) {
		Result<User> result = new Result<>();
		try {
			Optional<User> us = userRepository.findByUserId(userId);
			
			if(us.isPresent()) {
				if(us.get().isEnable()) {
					userRepository.deactivateUserDeactive(userId);
				}
				else {
					userRepository.deactivateUserActive(userId);
				}
			}
			
			
			result.setMessage("deleted");
			result.setCode("0000");
 
		} catch (Exception ex) {
			result.setCode(StringIteration.ERROR_CODE1);
			result.setMessage(ex.getMessage());
		}
 
		return result;
	}


	public Optional<UserAuth> checkUserAuthByEmail(String userId) {
		 try {
	            return userAuthRepository.findByUserId(userId);
	        } catch (Exception e) {
	            logger.error(StringIteration.SPACE, e);
	        }
	        return Optional.empty();
	}
	
	public Result<User> updateUserLocation(String floor){
		Result<User> result = new Result<>();
		try {
			Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
			if(us.isPresent()) {
				User u = us.get();
				u.setWarehouseLocation(floor);
				userRepository.save(u);
				result.setCode(StringIteration.SUCCESS_CODE);
				result.setMessage(StringIteration.UPDATEDSUCCESSFULLY);
				result.setData(u);
				return result;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.setCode(StringIteration.ERROR_CODE1);
		}
		return result;
	}
	
	public Result<Object> getFloor(){
		Result<Object> result = new Result<>();
		try {
			Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
			if(us.isPresent()) {
				User u = us.get();
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("floor", u.getWarehouseLocation());
				String id = u.getRoleName().equals(StringIteration.USER) ? u.getHierarachyId() : u.getUserId();
				Optional<TenantManufacture> optional = tenantManufactureRepository.findWmsByUserId(id, u.getTenantId());
				if(optional.isPresent()) {
					map.put("wms", true);
				}else {
					map.put("wms", false);
				}
				result.setCode(StringIteration.SUCCESS_CODE);
				result.setData(map);
				return result;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.setCode(StringIteration.ERROR_CODE1);
		}
		return result;
	}
		
}
