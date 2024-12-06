package com.healthtraze.etraze.api.security.controller;

import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.WebRequest;

import com.healthtraze.etraze.api.base.constant.Constants;
import com.healthtraze.etraze.api.base.constant.StringIteration;
import com.healthtraze.etraze.api.base.exception.InvalidOldPasswordException;
import com.healthtraze.etraze.api.base.exception.PasswordExpiredException;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.base.service.NotificationService;
import com.healthtraze.etraze.api.base.util.CommonUtil;
import com.healthtraze.etraze.api.base.util.ConfigUtil;
import com.healthtraze.etraze.api.security.model.ChangePassword;
import com.healthtraze.etraze.api.security.model.EmailTemplate;
import com.healthtraze.etraze.api.security.model.LoginResponse;
import com.healthtraze.etraze.api.security.model.NotificationTemplate;
import com.healthtraze.etraze.api.security.model.OneTimePassword;
import com.healthtraze.etraze.api.security.model.PasswordDto;
import com.healthtraze.etraze.api.security.model.PasswordResetToken;
import com.healthtraze.etraze.api.security.model.Register;
import com.healthtraze.etraze.api.security.model.User;
import com.healthtraze.etraze.api.security.model.UserAuth;
import com.healthtraze.etraze.api.security.model.UserMapper;
import com.healthtraze.etraze.api.security.model.UserSession;
import com.healthtraze.etraze.api.security.model.VerificationToken;
import com.healthtraze.etraze.api.security.repository.OneTimePasswordRepository;
import com.healthtraze.etraze.api.security.repository.PasswordResetTokenRepository;
import com.healthtraze.etraze.api.security.repository.UserAuthRepository;
import com.healthtraze.etraze.api.security.repository.UserRepository;
import com.healthtraze.etraze.api.security.repository.UserSessionRepository;
import com.healthtraze.etraze.api.security.repository.VerificationTokenRepository;
import com.healthtraze.etraze.api.security.service.EmailService;
import com.healthtraze.etraze.api.security.service.EmailTemplateService;
import com.healthtraze.etraze.api.security.service.MobilePinService;
import com.healthtraze.etraze.api.security.service.NotificationTemplateService;
import com.healthtraze.etraze.api.security.service.SecurityService;
import com.healthtraze.etraze.api.security.service.UserService;
import com.healthtraze.etraze.api.security.util.JwtUtil;
import com.healthtraze.etraze.api.security.util.SecurityUtil;

@CrossOrigin
@RestController
public class SecurityController implements Constants {

    private Logger logger = LogManager.getLogger(SecurityController.class);

    private final SecurityService securityService;

    private final UserService myUserDetailsService;
    
    private final MobilePinService mobilePinService;

    private final JwtUtil jwtTokenUtil;

    private final UserRepository userRepository;

    private final UserAuthRepository userAuthRepository;

    private final AuthenticationManager authenticationManager;
   
    private VerificationTokenRepository verificationTokenRepository;

    private final UserSessionRepository userSessionRepository;

    private final EmailTemplateService emailTemplateService;

    private final NotificationTemplateService notificationTemplateService;

    private final NotificationService notificationService;
   
    private final PasswordResetTokenRepository resetTokenRepository;
    
    private final OneTimePasswordRepository oneTimePasswordRepository;

    
    @Autowired
    public SecurityController(SecurityService securityService, UserService myUserDetailsService,
			MobilePinService mobilePinService, JwtUtil jwtTokenUtil, UserRepository userRepository,
			UserAuthRepository userAuthRepository, AuthenticationManager authenticationManager,
			VerificationTokenRepository verificationTokenRepository,
			UserSessionRepository userSessionRepository, EmailTemplateService emailTemplateService,
			EmailService emailService, NotificationTemplateService notificationTemplateService,
			NotificationService notificationService, PasswordResetTokenRepository resetTokenRepository,
			OneTimePasswordRepository oneTimePasswordRepository) {
	
		this.securityService = securityService;
		this.myUserDetailsService = myUserDetailsService;
		this.mobilePinService = mobilePinService;
		this.jwtTokenUtil = jwtTokenUtil;
		this.userRepository = userRepository;
		this.userAuthRepository = userAuthRepository;
		this.authenticationManager = authenticationManager;
		this.verificationTokenRepository = verificationTokenRepository;
		this.userSessionRepository = userSessionRepository;
		this.emailTemplateService = emailTemplateService;
		this.notificationTemplateService = notificationTemplateService;
		this.notificationService = notificationService;
		this.resetTokenRepository = resetTokenRepository;
		this.oneTimePasswordRepository = oneTimePasswordRepository;
		
	}

	@GetMapping(value = "/hello")
    public String hello() {
        return " Hello World ";
    }

    @PostMapping(value = "/register")
    public Result<User> signUp(@RequestBody Register t) {

        User user = new User();
        user.setRoleId("1680260529425");
        user.setEmail(t.getEmail());
        user.setFirstName(t.getFirstName());
        user.setLastName(t.getLastName());
        user.setPhoneNo(t.getPhoneNo());
        return myUserDetailsService.signUp(user);
    }

    @GetMapping(value = "/login")
    public Result<LoginResponse> doLogin(@RequestHeader HttpHeaders request, HttpServletRequest r)
            throws PasswordExpiredException, NullPointerException {
        Result<LoginResponse> result = new Result<>();

        try {
            List<String> channelList = request.get(Constants.CHANNEL);
            List<String> list = request.get(Constants.AUTHORIZATION);

            if (list == null) {
                logger.error(Constants.LISTISEMPTY);
               
                result.setCode("1111");
                result.setMessage(Constants.LISTISEMPTY);
                return result;

            } else {
                String encodeCredential = list.get(0);
             
                logger.info(encodeCredential);
                String[] basicSplited = encodeCredential.split(StringIteration.SPACE1);
                String credential = new String(Base64.getDecoder().decode(basicSplited[1]));

                String[] spliteds = credential.split(":");
                Optional<User> userId = userRepository.findByUserId(spliteds[0]);

                if (userId.isPresent()) {
                    List<UserSession> users = userSessionRepository.findByUserIdAndStatus(userId.get().getUserId(),
                            Constants.LOGIN);
                    if (!users.isEmpty()) {
                        for (UserSession us : users) {
                            us.setStatus(Constants.LOGOUT);
                            userSessionRepository.save(us);
                        }
                    }
                }

                if (channelList == null) {
                    logger.error(Constants.CHANNELLISTISEMPTY);
                   
                    result.setCode("1111");
                    result.setMessage(Constants.LISTISEMPTY);
                    return result;
                } else {

                    String channel = channelList.get(0);
                    logger.info(channel);
                    String[] splited = credential.split(":");
                    logger.info(splited);
                    final UserDetails userDetails = myUserDetailsService.loadUserByUsername(splited[0]);

                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(splited[0], splited[1]));

                    Optional<UserAuth> a = userAuthRepository.findByUserId(splited[0]);
                    UserAuth auth = new UserAuth();
                    if (a.isPresent()) {
                        auth = a.get();
                        Optional<User> user = userRepository.findById(auth.getUserId());
                        if (user.isPresent()) {
                            User u = user.get();
                            u.setOnline(true);
                            userRepository.save(u);
                        }
                    } else {
                        logger.error(Constants.AUTHISABSENT);
                    }
                    String sessionId = updateUserSession(request, auth, r);
                    Map<String, Object> m = new HashMap<>();
                    m.put(SESSIONID, sessionId);
                    final String token = jwtTokenUtil.createToken(m, spliteds[0]);
                    LoginResponse userMapper = myUserDetailsService.findUserById(auth.getUserId());
                    userMapper.setToken(token);
                    result.setCode("0000");
                    result.setMessage("Login Successfully");
                    result.setData(userMapper);
                    return result;
                }
            }
           

        } catch (BadCredentialsException e) {
            logger.error(LOGINERROR, e);
            throw new PasswordExpiredException(Constants.INCORRECTUSERNAMEORPASSWORD, e);
        } catch (PasswordExpiredException e) {
            logger.error(LOGINERROR, e);
            result.setCode(StringIteration.ERROR_CODE1);
            result.setMessage(StringIteration.PASSWORDEXPIRED);
            
        } catch (DisabledException d) {
            logger.error(LOGINERROR, d);
            throw new PasswordExpiredException(StringIteration.USERHASBEENDISABLEDPLEASECONTACTADMINISTRATION, d);
        }
        return result;

    }
    
    @GetMapping(value = "/login-pin")
    public Result<LoginResponse> doLoginMobile(@RequestHeader HttpHeaders request, HttpServletRequest r)
            throws PasswordExpiredException, NullPointerException {
        try {
            Result<LoginResponse> result = new Result<>();
            List<String> channelList = request.get(Constants.CHANNEL);
            List<String> list = request.get(Constants.AUTHORIZATION);

          
            if (list == null) {
                logger.error(Constants.LISTISEMPTY);
                result.setCode("1111");
                result.setMessage(Constants.LISTISEMPTY);
                return result;

            } else {
                String encodeCredential = list.get(0);               
                logger.info(encodeCredential);
                String[] basicSplited = encodeCredential.split(StringIteration.SPACE1);
                String credential = new String(Base64.getDecoder().decode(basicSplited[1]));
                String[] tenantPin = credential.split(":");
                String[] spliteds = mobilePinService.loginpin(tenantPin[0],tenantPin[1]);
                if(spliteds.length<2) {
                	 result.setCode(StringIteration.ERROR_CODE1);
                	 result.setMessage("Invalid user");                	 
                	 return result;
                }
               
                myUserDetailsService.loadUserByUsername(spliteds[0]);
                
                Optional<User> userId = userRepository.findByUserId(spliteds[0]);

               
                if (userId.isPresent()) {
                    List<UserSession> users = userSessionRepository.findByUserIdAndStatus(userId.get().getUserId(),
                            Constants.LOGIN);
                    if (!users.isEmpty()) {
                        for (UserSession us : users) {
                            us.setStatus(Constants.LOGOUT);
                            userSessionRepository.save(us);
                        }
                    }
                }

                if (channelList == null) {
                    logger.error(Constants.CHANNELLISTISEMPTY);
                    result.setCode("1111");
                    result.setMessage(Constants.LISTISEMPTY);
                    return result;
                } else {

                    String channel = channelList.get(0);
                    logger.info(channel);
                    String[] splited = spliteds;
                    logger.info(splited);
                    final UserDetails userDetails = myUserDetailsService.loadUserByUsername(splited[0]);

                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(splited[0], splited[1]));

                    Optional<UserAuth> a = userAuthRepository.findByUserId(splited[0]);
                    UserAuth auth = new UserAuth();
                    if (a.isPresent()) {
                        auth = a.get();
                        Optional<User> user = userRepository.findById(auth.getUserId());
                        if (user.isPresent()) {
                            User u = user.get();
                            u.setOnline(true);
                            userRepository.save(u);
                        }
                    } else {
                        logger.error(Constants.AUTHISABSENT);
                    }
                    String sessionId = updateUserSession(request, auth, r);
                    Map<String, Object> m = new HashMap<>();
                    m.put(SESSIONID, sessionId);
                    final String token = jwtTokenUtil.createToken(m, spliteds[0]);
                    LoginResponse userMapper = myUserDetailsService.findUserById(auth.getUserId());
                    userMapper.setToken(token);
                    result.setCode("0000");
                    result.setMessage("Login Successfully");
                    result.setData(userMapper);
                    return result;
                }
            }
           

        } catch (BadCredentialsException e) {
            logger.error(LOGINERROR, e);
            throw new PasswordExpiredException(Constants.INCORRECTUSERNAMEORPASSWORD, e);
        } catch (PasswordExpiredException e) {
            logger.error(LOGINERROR, e);
            throw new PasswordExpiredException(e);
        } catch (DisabledException d) {
            logger.error(LOGINERROR, d);
            throw new PasswordExpiredException(StringIteration.USERHASBEENDISABLEDPLEASECONTACTADMINISTRATION, d);
        }

    }

    private String updateUserSession(HttpHeaders request, UserAuth auth, HttpServletRequest r) {

        UserSession session = new UserSession();
        session.setCityName(StringIteration.SPACE);
        session.setCountryName(StringIteration.SPACE);
        session.setPostal(StringIteration.SPACE);
        session.setState(StringIteration.SPACE);
        session.setCreatedBy(auth.getUserId());

        session.setLastUpdatedOn(CommonUtil.getLocalDateTime(auth.getUserId()));

        String orgin = request.getOrigin();
        if (orgin != null) {
            String[] s = orgin.split("/");
            String[] address = s[2].split(":");
            session.setIp(address[0]);

        }

        String ip = com.healthtraze.etraze.api.security.util.HttpUtils.getRequestIP(r);
        logger.error(ip);

        session.setSessionId(CommonUtil.getID());

        session.setStatus(Constants.LOGIN);
        session.setUserId(auth.getUserId());
        session.setUserAgent(r.getHeader(Constants.USERAGENT));
        userSessionRepository.save(session);
        return session.getSessionId();
    }

    @PutMapping(value = "/signout")
    public Result<Object> doLogout(@RequestHeader HttpHeaders request, HttpServletRequest r) {
        Result<Object> result = new Result<>();
        final String authorizationHeader = r.getHeader(Constants.AUTHORIZATION);
        logger.error(authorizationHeader);
        try {
            if (authorizationHeader != null && authorizationHeader.startsWith(Constants.BEARER)) {
                String token = authorizationHeader.substring(7);
                Map<?, ?> u = jwtTokenUtil.extractAllClaims(token);
                String sessionId = (String) u.get(Constants.SESSIONID);
                logger.error(token);
                logger.error(sessionId);

                Optional<UserSession> usOption = userSessionRepository.findById(sessionId);

                if (usOption.isPresent()) {
                    UserSession us = usOption.get();
                    us.setStatus(Constants.LOGOUT);
                    us.setModifiedBy(SecurityUtil.getUserName());
                    us.setModifiedOn(new Date());
                    userSessionRepository.save(us);

                    Optional<User> optionUser = userRepository.findById(us.getUserId());
                    if (optionUser.isPresent()) {
                        User userupdate = optionUser.get();
                        userupdate.setOnline(false);
                        userRepository.save(userupdate);
                    }

                }

                result.setCode(StringIteration.SUCCESS_CODE);
                result.setMessage(Constants.LOGOUTDONE);
            } else {
                result.setCode(StringIteration.ERROR_CODE1);
                result.setMessage(Constants.TOKENREQUIRED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(StringIteration.ERROR_CODE1);
            result.setMessage(Constants.TOKENREQUIRED);
            return result;
        }
        return result;
    }

   

    @SuppressWarnings("unused")
    @PostMapping(value = "/set-mobile-pin")
    public Result<String> updateMobilePin(@RequestBody UserAuth authendication) {
        Result<String> result = new Result<>();
        try {
            UserAuth data = null;
            if (data == null) {
                result.setCode(StringIteration.ERROR_CODE1);
                result.setMessage(USERNOTFOUND);
            } else {
                result.setCode(StringIteration.SUCCESS_CODE);
                result.setMessage(StringIteration.MOBILEPINSETSUCCESSFULLY);
            }
        } catch (Exception e) {
            result.setCode(StringIteration.ERROR_CODE1);
            result.setMessage(StringIteration.ERRORWHILEUPDATEMOBILEPIN);
            logger.error(StringIteration.UPDATEMOBILEPIN, e);
        }
        return result;
    }

    @GetMapping(value = "/check-mobile-pin-status/{emailId}")
    public Result<String> checkMobilePinStatus(@PathVariable String emailId) {
        Result<String> result = new Result<>();
        try {
            Optional<UserAuth> data = userAuthRepository.findByUserId(emailId);
            if (!data.isPresent()) {
                result.setCode(StringIteration.ERROR_CODE1);
                result.setMessage(StringIteration.USERNOTFOUND);
            } else {
                if (data.get().getPassword() != null && data.get().getPasscode() != null) {
                    result.setCode(StringIteration.SUCCESS_CODE2);
                    result.setMessage(StringIteration.PINUPDATED);
                } else {
                    result.setCode(StringIteration.ERROR_CODE7);
                    result.setMessage(StringIteration.PINNOTUPDATED);
                }
            }
        } catch (Exception e) {
            result.setCode(StringIteration.ERROR_CODE1);
            result.setMessage(ERROR);
            logger.error(StringIteration.CHECKMOBILEPINSTATUS, e);
        }
        return result;
    }

    @GetMapping(value = "/check-user-exist/{email}")
    public Result<UserMapper> validateUsername(@PathVariable String email) {
        Result<UserMapper> result = new Result<>();
        try {
            Optional<User> user = userRepository.findByUserId(email);
            if (user.isPresent()) {
                final String token = jwtTokenUtil.generateToken(user.get().getEmail());
                UserMapper um = new UserMapper();
                um.setToken(token);
                um.setUser(user.get());
                result.setCode(StringIteration.SUCCESS_CODE);
                result.setMessage(StringIteration.USERISVALID);
                result.setData(um);
            } else {
                result.setCode(StringIteration.ERROR_CODE1);
                result.setMessage(StringIteration.USERNOTFOUND);
            }
        } catch (Exception e) {
            result.setCode(StringIteration.ERROR_CODE1);
            result.setMessage(ERROR + e);

            logger.error(StringIteration.VALIDATEUSERNAME, e);
        }
        return result;
    }

   

    @GetMapping(value = "/validate-otp")
    public Result<User> validateOtp(@RequestParam("otp") String otp) {
        Result<User> result = new Result<>();
        try {

            OneTimePassword o = oneTimePasswordRepository.findByUserId(SecurityUtil.getUserName());
           
            if (!o.getOtp().equals(otp)) {
                result.setCode(StringIteration.ERROR_CODE1);
                result.setMessage(Constants.INVALIDOTP);
                return result;
            }


            Optional<User> op = userRepository.findByUserId(SecurityUtil.getUserName());

            if (op.isPresent()) {
User u = op.get();
                u.setIsUserOnboarded(true);
                u.setStatus(Constants.ACTIVE);
                u.setOtpVerified(true);
                userRepository.save(u);
               
                result.setCode(StringIteration.SUCCESS_CODE);
                result.setMessage(StringIteration.OTPVALIDATEDSUCCESSFULLY);
                result.setData(u);
                return result;
            } else {
                result.setCode(StringIteration.ERROR_CODE1);
                result.setMessage(Constants.INVALIDOTP);
            }

        } catch (Exception e) {
            result.setCode(StringIteration.ERROR_CODE1);
            result.setMessage(Constants.ERROR);
            logger.error(StringIteration.SPACE, e);
        }
        return result;
    }

    public void sendNotificationtoDoctorForApproval(User user) {

        NotificationTemplate notificationTemplate = notificationTemplateService.findById("DR ONBOARDING_COMPLETED");
        Optional<User> uid=userRepository.findByUserId(SecurityUtil.getUserName());
        List<User> admins = userRepository.findByRoleId(ConfigUtil.getAdminRoleId());
        String tid=null;
        for (User us : admins) {
            if (us != null) {
                VelocityContext context = new VelocityContext();
                context.put(StringIteration.FULLNAME, user.getFirstName() + " " + user.getLastName());
                StringWriter swout = new StringWriter();
                String templateStr = notificationTemplate.getNotificationTemplate();
                Velocity.evaluate(context, swout, StringIteration.LOGTAGNAME, templateStr);
                if(uid.isPresent()) {
        			tid=uid.get().getTenantId();
        		}
                notificationService.notifications(us.getUserId(), notificationTemplate.getSubject(), swout.toString(),
                        " ",tid);
            }

        }
    }

    /*
     * 
     * Notification to admin when caretaker onboarded
     * 
     */
    public void sendNotificationtoAdminForCaretaker(User user) {

        NotificationTemplate notificationTemplate = notificationTemplateService.findById("CT ONBOARDING_COMPLETED");
        Optional<User> uid=userRepository.findByUserId(SecurityUtil.getUserName());
        List<User> admins = userRepository.findByRoleId(ConfigUtil.getAdminRoleId());
        String tid=null;

        for (User us : admins) {
            if (us != null) {
                VelocityContext context = new VelocityContext();
                context.put(StringIteration.FULLNAME, user.getFirstName() + " " + user.getLastName());
                StringWriter swout = new StringWriter();
                String templateStr = notificationTemplate.getNotificationTemplate();
                Velocity.evaluate(context, swout, StringIteration.LOGTAGNAME, templateStr);
                if(uid.isPresent()) {
        			tid=uid.get().getTenantId();
        		}
                notificationService.notifications(us.getUserId(), notificationTemplate.getSubject(), swout.toString(),
                        " ",tid);
            }
        }
    }

    @GetMapping(value = "/generate-otp")
    public Result<String> generateOtp() {
        Result<String> result = new Result<>();
        try {

            Optional<UserAuth> ua = userAuthRepository.findByUserId(SecurityUtil.getUserName());

            if (ua.isPresent()) {
                UserAuth userAuth = ua.get();

                OneTimePassword o = new OneTimePassword();
                o.setEmailId(userAuth.getEmailId());
                List<OneTimePassword> list = oneTimePasswordRepository.findAll(Example.of(o));

                if (!list.isEmpty()) {
                    for (OneTimePassword op : list) {
                        oneTimePasswordRepository.delete(op);
                    }
                }


                RestTemplate restTemplate = new RestTemplate();

                HttpHeaders headers = new HttpHeaders();
                headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                HttpEntity<String> entity = new HttpEntity<>(headers);

                String otp = "123456";

                OneTimePassword oneTimePassword = new OneTimePassword(otp, null, userAuth.getUserId(),
                        userAuth.getEmailId());
                oneTimePassword.setId(System.currentTimeMillis());
                if(ua.isPresent()) {
                	oneTimePassword.setTenantId(ua.get().getTenantId());
                }
                oneTimePasswordRepository.save(oneTimePassword);
                result.setCode(StringIteration.SUCCESS_CODE);
                result.setMessage(StringIteration.OTPGENERATEDSUCCESSFULLY);

            } else {
                result.setCode(StringIteration.ERROR_CODE1);
                result.setMessage(Constants.INVALIDUSER);
            }

        } catch (Exception e) {
            result.setCode(StringIteration.ERROR_CODE1);
            result.setMessage(Constants.ERROR);
            logger.error(e);
        }
        return result;
    }

    @PostMapping(value = "/change_password")
    public Result<String> doPasswordChange(@RequestHeader HttpHeaders request, @RequestBody ChangePassword password) {
        List<String> channelList = request.get(Constants.CHANNEL);
        Result<String> result = new Result<>();
        result.setCode(StringIteration.ERROR_CODE1);
        if (password == null || channelList == null)
            return result;
        boolean b = securityService.doPasswordChange(channelList.get(0), password);
        if (b) {
            result.setCode(StringIteration.SUCCESS_CODE);
            result.setMessage(StringIteration.PASSWORDCHANGEDSUCCESSFULLY);
        } else {
            result.setCode(StringIteration.ERROR_CODE1);
            result.setMessage(StringIteration.ERRORWHILECHANGINGTHEPASSWORD);
        }
        return result;
    }

   
    @PostMapping("/updatePassword")
    public Result<String> changeUserPassword(final Locale locale, @RequestBody @Valid PasswordDto passwordDto) {

        Result<String> r = new Result<>();
        logger.error(passwordDto.getNewPassword());
        try {
            final UserAuth user = myUserDetailsService.findUserByEmail(SecurityUtil.getUserName());
            if (!myUserDetailsService.checkIfValidOldPassword(user, passwordDto.getOldPassword())) {
                r.setCode(StringIteration.ERROR_CODE1);
                r.setMessage(StringIteration.INVALIDOLDPASSWORD);
                throw new InvalidOldPasswordException();
            }
            Result<String> result = myUserDetailsService.changeUserPassword(user, passwordDto.getNewPassword());
            if (result.getCode().equals(StringIteration.ERROR_CODE4)) {
                return result;
            }
            r.setCode(StringIteration.SUCCESS_CODE);
        } catch (Exception e) {
            r.setCode(StringIteration.ERROR_CODE1);
            logger.error(StringIteration.SPACE, e);
        }

        return r;
    }

   
    @GetMapping("/resendRegistrationToken")
    public Result<String> resendRegistrationToken(final HttpServletRequest request,
            @RequestParam("token") final String existingToken) {
        Result<String> result = new Result<>();
        try {
            logger.error(existingToken);
            final VerificationToken newToken = myUserDetailsService.generateNewVerificationToken(existingToken);
            myUserDetailsService.constructResendVerificationTokenEmail(newToken.getToken(), newToken.getUser(),
                    Constants.EN);
            result.setCode(StringIteration.SUCCESS_CODE);
            result.setMessage(StringIteration.SPACE);
        } catch (Exception e) {
            result.setCode(StringIteration.ERROR_CODE1);
            result.setMessage(StringIteration.SPACE);
            logger.error(StringIteration.RESENDREGISTRATIONTOKEN, e);
        }
        return result;
    }

    
    
    @PostMapping("/resetPassword")
    public Result<String> resetPassword(final HttpServletRequest request,
            @RequestParam("userId") final String userId) {
        Result<String> result = new Result<>();
        Optional<User> us = userRepository.findByUserId(userId.toUpperCase());
        
        if (us.isPresent()) { 
            Optional<UserAuth> user = myUserDetailsService.checkUserAuthByEmail(us.get().getUserId());
            if (user.isPresent()) {
                UserAuth u = user.get();
                final String token = UUID.randomUUID().toString();
                myUserDetailsService.createPasswordResetTokenForUser(u, token);
                result.setMessage(StringIteration.SPACE);
                result.setCode(StringIteration.SUCCESS_CODE);
                return result;
            } else {
                result.setMessage(StringIteration.EMAILNOTEXIST);
                result.setCode(StringIteration.ERROR_CODE1);
            }
        } else {
            result.setMessage("User not found"); 
            result.setCode("ERROR_CODE2"); 
        }
        return result;
    }

    

    @PostMapping("/userConfirm")
    public Result<String> confirmRegistration(WebRequest request, @RequestBody @Valid PasswordDto passwordDto) {

        Result<String> result = new Result<>();

        try {
            VerificationToken verificationToken = myUserDetailsService.getVerificationToken(passwordDto.getToken());

            if (verificationToken == null) {
                result.setMessage(Constants.INVALIDTOKEN);
                result.setCode(StringIteration.ERROR_CODE5);
                return result;
            }
            if (verificationToken.getStatus() != null && verificationToken.getStatus().equals(Constants.ACTIVE)) {
                result.setCode(StringIteration.ERROR_CODE3);
                result.setMessage(Constants.TOKENINACTIVE);
                return result;
            }
            UserAuth user = verificationToken.getUser();

            Calendar.getInstance();
            if ((Timestamp.valueOf(verificationToken.getExpiryDate()).getTime()
                    - Timestamp.valueOf(CommonUtil.getLocalDateTime()).getTime()) <= 0) {
                verificationToken.setStatus(Constants.EXPIRED);
                result.setMessage(Constants.TOKENEXPIRED);
                result.setCode(StringIteration.ERROR_CODE1);
                return result;
            }
      
            result = myUserDetailsService.createUserPassword(user, passwordDto.getNewPassword());
            if (result.getCode().equals(StringIteration.ERROR_CODE4)) {
                return result;
            }
     

            verificationToken.setStatus(Constants.ACTIVE);
            verificationTokenRepository.save(verificationToken);
            result.setMessage(Constants.SUCCESS);
            result.setCode(StringIteration.SUCCESS_CODE);

        } catch (Exception e) {
            logger.error(e);
        }

        return result;
    }

    /*
     * 
     * remainder mail for Incompleted onboard process to all veteran.
     * 
     */

    public void userSignUpComplete(User u) {
        EmailTemplate emailTemplate = emailTemplateService.findById("EMPLOYE_ACTIVE");

        if (emailTemplate != null) {
            
            
              
            }
        }
    

    /*
     * 
     */
    public void userSignUpCompleteNotify(User u) {
        String tid=null;
       

    }
    

 
    @PostMapping("/savePassword")
    public Result<String> savePassword(final Locale locale, @RequestBody @Valid PasswordDto passwordDto) {
        Result<String> result = new Result<>();
        logger.info(Constants.ENTER);
        logger.error(locale.getCountry());

        logger.error(passwordDto.getToken());
        final String output = myUserDetailsService.validatePasswordResetToken(passwordDto.getToken());
        if (output != null) {
            result.setMessage(output);
            result.setCode(StringIteration.ERROR_CODE1);
            return result;
        }
        Optional<UserAuth> user = myUserDetailsService.getUserByPasswordResetToken(passwordDto.getToken());
        if (user.isPresent()) {
            UserAuth u = user.get();

            Optional<UserAuth> currentRow = userAuthRepository.findByUserId(u.getUserId());
            result = myUserDetailsService.changeUserPassword(currentRow.get(), passwordDto.getNewPassword());
            if (result.getCode().equals(StringIteration.ERROR_CODE4)) {
                return result;
            }
            VerificationToken verificationToken = myUserDetailsService.getVerificationToken(passwordDto.getToken());
            PasswordResetToken verification = myUserDetailsService.getPasswordResetToken(passwordDto.getToken());
            if (verification != null) {
                verification.getUser().setAuthEnabled(false);
                resetTokenRepository.save(verification);
            }
            if (result.getCode().equals(StringIteration.ERROR_CODE3)) {
                return result;
            }
            if (verificationToken != null) {
                verificationToken.setStatus(Constants.INACTIVE);
                verificationTokenRepository.save(verificationToken);
            }

            result.setCode(StringIteration.SUCCESS_CODE);

            return result;
        } else {
            result.setMessage(Constants.ERROR);
            result.setCode(StringIteration.ERROR_CODE1);
            return result;
        }
    }

    @GetMapping("/validate_reset_password_token")
    public Result<String> showChangePasswordPage(Locale locale, @RequestParam("token") String token) {
        Result<String> r = new Result<>();
        String result = myUserDetailsService.validatePasswordResetToken(token);
        if (result != null) {
            r.setCode(StringIteration.ERROR_CODE1);
            r.setMessage(result);
        }
        VerificationToken verificationToken = myUserDetailsService.getVerificationToken(token);
        if (verificationToken != null) {
            if (!verificationToken.getUser().isAuthEnabled()) {
                r.setCode(StringIteration.ERROR_CODE3);
                r.setMessage(Constants.TOKENINACTIVE);
            } else if (verificationToken.getUser().isAuthEnabled()
                    && (Timestamp.valueOf(verificationToken.getExpiryDate()).getTime()
                            - Timestamp.valueOf(CommonUtil.getLocalDateTime()).getTime()) <= 0) {
                r.setMessage(Constants.TOKENEXPIRED);
                r.setCode(StringIteration.ERROR_CODE4);
            } else {
                r.setCode(StringIteration.SUCCESS_CODE);
                r.setMessage(Constants.VALIDTOKEN);
            }
        }
        return r;

    }

    @GetMapping("/validate_user_actiation_token")
    public Result<String> validateUserActivationToken(Locale locale, @RequestParam("token") String token) {
        Result<String> result = new Result<>();
        VerificationToken verificationToken = myUserDetailsService.getVerificationToken(token);
        if (verificationToken == null) {
            result.setMessage(Constants.INVALIDTOKEN);
            result.setCode(StringIteration.ERROR_CODE1);
            return result;
        }

        if ((Timestamp.valueOf(verificationToken.getExpiryDate()).getTime()
                - Timestamp.valueOf(CommonUtil.getLocalDateTime()).getTime()) <= 0) {
            if (verificationToken.getStatus().equals(Constants.INACTIVE)) {
                result.setMessage(Constants.TOKENEXPIRED);
                result.setCode(StringIteration.ERROR_CODE4);
                return result;
            }
        }
        if (verificationToken.getStatus().equals(Constants.ACTIVE)) {
            result.setCode(StringIteration.ERROR_CODE3);
            result.setMessage(Constants.TOKENINACTIVE);
            return result;
        }

        return result;
    }

}