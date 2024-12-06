package com.healthtraze.etraze.api.masters.service;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.healthtraze.etraze.api.base.constant.Constants;
import com.healthtraze.etraze.api.base.constant.StringIteration;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.base.service.BaseService;
import com.healthtraze.etraze.api.base.service.NotificationService;
import com.healthtraze.etraze.api.base.util.ConfigUtil;
import com.healthtraze.etraze.api.masters.documents.Manufacturer;
import com.healthtraze.etraze.api.masters.dto.TenantDTO;
import com.healthtraze.etraze.api.masters.model.Tenant;
import com.healthtraze.etraze.api.masters.model.TenantManufacture;
import com.healthtraze.etraze.api.masters.repository.BlockEmailRepository;
import com.healthtraze.etraze.api.masters.repository.ManufacturerRepository;
import com.healthtraze.etraze.api.masters.repository.TenantManufactureRepository;
import com.healthtraze.etraze.api.masters.repository.TenantRepository;
import com.healthtraze.etraze.api.security.model.EmailTemplate;
import com.healthtraze.etraze.api.security.model.NotificationTemplate;
import com.healthtraze.etraze.api.security.model.User;
import com.healthtraze.etraze.api.security.repository.UserRepository;
import com.healthtraze.etraze.api.security.service.EmailService;
import com.healthtraze.etraze.api.security.service.EmailTemplateService;
import com.healthtraze.etraze.api.security.service.NotificationTemplateService;
import com.healthtraze.etraze.api.security.service.UserService;
import com.healthtraze.etraze.api.security.util.SecurityUtil;

@Service
public class TenantService implements BaseService<Tenant, String> {

	private Logger logger = LogManager.getLogger(TenantService.class);

	private final TenantRepository tenantRepository;
	private final TenantManufactureRepository tenantManufactureRepository;
	private final UserService userService;
	private final UserRepository userRepository;
	private final UserRepository repository;
	private final EmailTemplateService emailTemplateService;
	private final EmailService emailService;
	private final NotificationTemplateService notificationTemplateService;
	private final NotificationService notificationService;
	private final BlockEmailRepository blockEmailRepository;
	private final ManufacturerRepository manufacturerRepository;

	@Autowired
	public TenantService(TenantRepository tenantRepository, TenantManufactureRepository tenantManufactureRepository,
			UserService userService, UserRepository userRepository, UserRepository repository,
			EmailTemplateService emailTemplateService, EmailService emailService,
			NotificationTemplateService notificationTemplateService, NotificationService notificationService,
			BlockEmailRepository blockEmailRepository, ManufacturerRepository manufacturerRepository) {

		this.tenantRepository = tenantRepository;
		this.tenantManufactureRepository = tenantManufactureRepository;
		this.userService = userService;
		this.userRepository = userRepository;
		this.repository = repository;
		this.emailTemplateService = emailTemplateService;
		this.emailService = emailService;
		this.notificationTemplateService = notificationTemplateService;
		this.notificationService = notificationService;
		this.blockEmailRepository = blockEmailRepository;
		this.manufacturerRepository = manufacturerRepository;
	}

	@Override
	public List<Tenant> findAll() {
		try {
			return tenantRepository.findAll();
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return Collections.emptyList();
	}

	public Page<Tenant> findAll(int page, String search) {
		try {
			Pageable pageable = PageRequest.of(page, Constants.PAGE_SIZE_5);
			return tenantRepository.findAll(search, pageable);
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	public Result<TenantManufacture> updateAllManufacturer() {
		Result<TenantManufacture> result = new Result<>();
		try {
			List<TenantManufacture> tm = tenantManufactureRepository.findAll();
			for (TenantManufacture m : tm) {
				m.setCheckedNotGrrn(0);
				m.setDeadlineClaim(0);
				m.setDeadlineOrder(0);
				m.setDeadlineTicket(0);
				m.setDispachedToDelivired(0);
				m.setGrrnNotSecondCheque(0);
				m.setInvoiceNotDispached(0);
				m.setNotInvoiced(0);
				m.setOrderNotFulfiled(0);
				m.setRecivedNotChecked(0);
				m.setSecondCheck(true);
				tenantManufactureRepository.save(m);
			}
			result.setCode("0000");
			result.setMessage("sucess");
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return result;
	}

	@Override
	public Tenant findById(String id) {
		try {
			Optional<User> userOptional = userRepository.findById(SecurityUtil.getUserName());
			if (userOptional.isPresent()) {
				User user = userOptional.get();
				if (user.getRoleName().equals("BUSINESS ADMIN")) {
					return findByIdForBusinessAdmin(id);
				}
			}
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	public Tenant findByLogin() {
		try {
			Optional<User> userOptional = userRepository.findById(SecurityUtil.getUserName());
			if (userOptional.isPresent()) {
				User user = userOptional.get();
				Optional<Tenant> tn = tenantRepository.findById(user.getTenantId());
				if (tn.isPresent()) {
					return tn.get();
				}
			}
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return new Tenant();
	}

	private Tenant findByIdForBusinessAdmin(String id) {
		List<TenantManufacture> tenantManufactures = new ArrayList<>();
		List<TenantManufacture> tm = tenantManufactureRepository.getTenantManufacturesById(id);

		Optional<Tenant> tenantOptional = tenantRepository.findById(id);
		if (tenantOptional.isPresent()) {
			Tenant tenant = tenantOptional.get();
			for (TenantManufacture t : tm) {
				Optional<Manufacturer> manufacturerOptional = manufacturerRepository.findById(t.getManufacturerId());
				if (manufacturerOptional.isPresent()) {
					Manufacturer manufacturer = manufacturerOptional.get();
					t.setManufacturerName(manufacturer.getManufacturerName());
					tenantManufactures.add(t);
				}
			}
			tenant.setManufacture(tenantManufactures);
			return tenant;
		}
		return null;
	}

	public Tenant findById() {
		try {
			Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
			if (us.isPresent()) {
				User u = us.get();
				List<TenantManufacture> tenantManufactures = new ArrayList<>();

				List<TenantManufacture> tm = tenantManufactureRepository
						.getTenantManufacturesByIdByStatus(u.getTenantId());
				Optional<Tenant> option = tenantRepository.findById(u.getTenantId());
				if (option.isPresent()) {
					Tenant tenant = option.get();

					for (TenantManufacture t : tm) {
						Optional<Manufacturer> man = manufacturerRepository.findById(t.getManufacturerId());
						if (man.isPresent()) {
							Manufacturer manufacturer = man.get();
							t.setManufacturerName(manufacturer.getManufacturerName());
							tenantManufactures.add(t);
						}
					}
					tenant.setManufacture(tenantManufactures);
					return tenant;
				}

			}
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	@Override
	public Result<Tenant> create(Tenant t) {
		Result<Tenant> result = new Result<>();

		try {
			if (t != null && t.getTenantName() == null) {
				result.setCode("1111");
				result.setMessage("Tenant Name Required");
				return result;
			}
			if (t != null) {
				String cleanedTenantName = t.getTenantName().replaceAll("[^a-zA-Z0-9]", "");

				Optional<Tenant> option = tenantRepository.findByTenantName(cleanedTenantName);
				if (option.isPresent()) {
					result.setCode("1111");
					result.setMessage("Tenant Already Available");
					return result;
				}

				t.setTenantId(UUID.randomUUID().toString());
				t.setStatus("PENDING");
				t.setIsSecondCheck(false);
				t.setWmsed(false);

				String tenantIdPrefix;
				String firstThreeChars = cleanedTenantName.substring(0, Math.min(cleanedTenantName.length(), 3))
						.toUpperCase();
				Optional<Tenant> option1 = tenantRepository.findByTenantCode(firstThreeChars);
				if (option1.isPresent()) {
					List<Character> charList = new ArrayList<>();
					for (char c : firstThreeChars.toCharArray()) {
						charList.add(c);
					}
					Collections.shuffle(charList);

					StringBuilder shuffledPrefixBuilder = new StringBuilder();
					for (char c : charList) {
						shuffledPrefixBuilder.append(c);
					}
					tenantIdPrefix = shuffledPrefixBuilder.toString().toUpperCase();
					t.setTenantCode(tenantIdPrefix);
				} else {
					t.setTenantCode(firstThreeChars);
					tenantIdPrefix = firstThreeChars;
				}

				String sequenceNumber = String.format("%04d", 1);
				String userId = tenantIdPrefix + "U" + sequenceNumber;
				User user = new User();
				tenantCreation(user, userId, t, result);

			}
		} catch (Exception e) {
			result.setCode("1111");
			result.setMessage(e.getLocalizedMessage());
		}

		return result;
	}

	private User tenantCreation(User user, String userId, Tenant t, Result<Tenant> result) {
		user.setUserId(userId);
		user.setEmail(t.getEmailId());
		user.setFirstName(t.getFirstName());
		user.setLastName(t.getLastName());
		user.setPhoneNo(t.getPhoneNumber());
		user.setIsUserOnboarded(false);
		user.setOtpVerified(false);
		user.setNewUserValidateWeb(true);
		user.setRoleId("1");
		user.setTenantId(t.getTenantId());

		Result<User> isUserCreated = userService.signUp(user);
		if (isUserCreated != null && isUserCreated.getCode().equals("0000")) {
			Tenant tenant = tenantRepository.save(t);
			result.setData(tenant);
			result.setCode("0000");
			result.setMessage("Tenant Added Successfully");
		} else {
			result.setCode("1111");
			result.setMessage("Error When Creating User for Tenant");
		}
		return user;
	}

	@Override
	public Result<Tenant> update(Tenant t) {
		Result<Tenant> result = new Result<>();
		try {
			Optional<Tenant> option = tenantRepository.findById(t.getTenantId());
			if (option.isPresent()) {
				Tenant tn = option.get();
				tn.setTenantName(t.getTenantName());
				tn.setFirstName(t.getFirstName());
				tn.setLastName(t.getLastName());
				tn.setEmailId(t.getEmailId());
				tn.setPhoneNumber(t.getPhoneNumber());
				tn.setStatus(t.getStatus());
				tn.setLogo(t.getLogo());
				Tenant ten = tenantRepository.save(tn);
				result.setCode(StringIteration.SUCCESS_CODE);
				result.setData(ten);
				result.setMessage(StringIteration.UPDATEDSUCCESSFULLY);
				return result;
			}
		} catch (Exception e) {
			result.setCode(StringIteration.ERROR_CODE1);
			result.setMessage(e.getMessage());
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	public Result<Tenant> updateTenantStatus(String tenantId) {
		Result<Tenant> result = new Result<>();
		try {
			Optional<Tenant> option = tenantRepository.findById(tenantId);
			if (option.isPresent()) {
				Tenant tenant = option.get();
				tenant.setStatus("APPROVED");
				Tenant updatedTenant = tenantRepository.save(tenant);
				List<TenantManufacture> list = tenantManufactureRepository.findByTenantId(tenantId);

				for (TenantManufacture tenantManufacture : list) {

					tenantManufactureRepository.save(tenantManufacture);
				}

				result.setData(updatedTenant);
				result.setCode("0000");
				result.setMessage("Approved Successfully");
			} else {
				result.setCode("1111");
				result.setMessage("Teneant Not Found");
			}
		} catch (Exception e) {
			result.setCode("1111");
			result.setMessage(e.getLocalizedMessage());
			logger.error(e);
		}
		return result;
	}

	public Result<User> updateTenantManufacture(List<TenantManufacture> tenantManufactures) {
		Result<User> result = new Result<>();
		try {
			Optional<User> op = repository.findByUserId(SecurityUtil.getUserName());
			if (op.isPresent()) {
				User us = op.get();
				Optional<Tenant> option = tenantRepository.findById(us.getTenantId());
				if (option.isPresent()) {
					Tenant tenant = option.get();

					for (TenantManufacture tm : tenantManufactures) {
						Optional<TenantManufacture> m = tenantManufactureRepository
								.findByManufactureIdNotStatus(tm.getManufacturerId(), us.getTenantId());
						if (m.isPresent()) {
							result.setCode(StringIteration.ERROR_CODE3);
							result.setMessage("Manufacturer Already Exist");
							return result;
						}
					}

					for (TenantManufacture tenantManufacture : tenantManufactures) {
						tenantManufacture.setId(System.currentTimeMillis() + "");
						logger.info("Processing TenantManufacture: {}", tenantManufacture);
						tenantManufacture.setTenantId(tenant.getTenantId());
						tenantManufacture.setStatus("INACTIVE");
						tenantManufactureRepository.save(tenantManufacture);
					}
					us.setIsUserOnboarded(true);
					repository.save(us);
					if (tenant.getStatus().equals("PENDING")) {
						tenant.setStatus("ONBOARDED");
						tenantRepository.save(tenant);
					}
					onboardCompleteMail(us);
					onboardCompletedNotification(us);
					result.setCode("0000");
					result.setMessage("Update Successfully");
					result.setData(us);
					return result;

				} else {
					result.setCode("1111");
					result.setMessage("Teneant Not Found");
				}
			}
		} catch (Exception e) {
			result.setCode("1111");
			result.setMessage(e.getLocalizedMessage());
			logger.error(e);
		}
		return result;
	}

	/*
	 * Onboard completed mail for tenant
	 */
	public void onboardCompleteMail(User user) {
		EmailTemplate emailTemplate = emailTemplateService.findById("ONBOARD_COMPLETE");
		List<String> name = new ArrayList<>();
		Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
		if (us.isPresent()) {
			if (emailTemplate != null) {
				Optional<Tenant> tenant = tenantRepository.findById(us.get().getTenantId());
				if (tenant.isPresent()) {
					name.add(tenant.get().getTenantName());
				}
				String[] s = { user.getEmail() };
				VelocityContext context = new VelocityContext();

				context.put("origin", ConfigUtil.getAppLink());
				context.put("language", "en");
				context.put("fullname", user.getFirstName() + " " + user.getLastName());
				StringWriter writer = new StringWriter();
				String templateStr = emailTemplate.getMailTemplate();
				Velocity.evaluate(context, writer, Constants.LOGTAGNAME, templateStr);
				emailService.sendEmails(s, writer.toString(), emailTemplate.getSubject(), name);
			}
		}
	}

	/*
	 * Onboard completed mail for tenant
	 */

	public void onboardCompletedNotification(User user) {
		Optional<User> uid = userRepository.findByUserId(SecurityUtil.getUserName());
		NotificationTemplate template = notificationTemplateService.findById("ONBOARD_COMPLETED");
		String tid = null;
		if (uid.isPresent()) {
			tid = uid.get().getTenantId();
		}
		if (template != null) {
			notificationService.notifications(user.getUserId(), template.getSubject(),
					template.getNotificationTemplate(), null, tid);
		}
	}

	@Override
	public Result<Tenant> delete(String id) {
		Result<Tenant> result = new Result<>();
		try {
			Optional<Tenant> te = tenantRepository.findById(id);
			if (te.isPresent()) {
				tenantRepository.delete(te.get());
				result.setCode(StringIteration.SUCCESS_CODE);
				result.setMessage(id + StringIteration.DELETEDSUCCESSFULLY);
				return result;
			}
		} catch (Exception e) {
			result.setCode(StringIteration.ERROR_CODE1);
			result.setMessage(e.getMessage());

		}
		return result;
	}

	public Result<TenantDTO> updateTenantAndTenantManufacture(TenantDTO t) {
		Result<TenantDTO> result = new Result<>();
		try {
			Optional<Tenant> option = tenantRepository.findById(t.getTenantId());
			if (option.isPresent()) {
				Tenant tn = option.get();
				tn.setTenantName(t.getTenantName());
				tn.setFirstName(t.getFirstName());
				tn.setLastName(t.getLastName());
				tn.setEmailId(t.getEmailId());
				tn.setPhoneNumber(t.getPhoneNumber());
				tn.setStatus(t.getStatus());
				tn.setLogo(t.getLogo());

				for (TenantManufacture man : t.getManufacture()) {

					Optional<TenantManufacture> tmt = tenantManufactureRepository.findById(man.getId());
					if (tmt.isPresent()) {
						TenantManufacture tm = tmt.get();
						tm.setEmailId(man.getEmailId());
						tm.setManufacturerId(man.getManufacturerId());
						tm.setTenantId(man.getTenantId());
						tm.setLicenceNumber(man.getLicenceNumber());
						tm.setStatus(man.getStatus());
						tm.setLocation(man.getLocation());
						tenantManufactureRepository.save(tm);

					}
				}
				tenantRepository.save(tn);
				result.setCode(StringIteration.SUCCESS_CODE);

				result.setMessage(StringIteration.UPDATEDSUCCESSFULLY);
				return result;
			}
		} catch (Exception e) {
			result.setCode(StringIteration.ERROR_CODE1);

			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	public List<Tenant> findAllTenantWithManufacture() {
		try {
			List<Tenant> tenant = tenantRepository.findApprovedTenants();
			tenant.stream().forEach(t -> {
				List<TenantManufacture> man = tenantManufactureRepository.findByTenantIdAndStatus(t.getTenantId());
				man.stream().forEach(tm -> {
					List<String> blockEmails = blockEmailRepository.findAllBlockEmailByManufacturerAndType(
							StringIteration.EMAIL, tm.getId(), tm.getTenantId());
					List<String> blockSubject = blockEmailRepository.findAllBlockEmailByManufacturerAndType(
							StringIteration.SUBJECT, tm.getId(), tm.getTenantId());
					tm.setBlockEmails(blockEmails);
					tm.setBlockSubject(blockSubject);
				});

				t.setManufacture(man);

			});

			return tenant;
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return Collections.emptyList();

	}

	public List<TenantManufacture> findAllTenantWithManufactureN() {
		try {
			List<TenantManufacture> man = tenantManufactureRepository.findByTenantIdAndStatus();
			man.stream().forEach(tm -> {
				List<String> blockEmails = blockEmailRepository
						.findAllBlockEmailByManufacturerAndType(StringIteration.EMAIL, tm.getId(), tm.getTenantId());
				List<String> blockSubject = blockEmailRepository
						.findAllBlockEmailByManufacturerAndType(StringIteration.SUBJECT, tm.getId(), tm.getTenantId());
				tm.setBlockEmails(blockEmails);
				tm.setBlockSubject(blockSubject);
			});

			return man;
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return Collections.emptyList();

	}

	public List<TenantManufacture> findAllTenantManufacturer() {
		try {

			Optional<User> us = userRepository.findByUserId(SecurityUtil.getUserName());
			if (us.isPresent()) {
				List<TenantManufacture> tm = tenantManufactureRepository
						.getTenantManufacturesByIdByStatus(us.get().getTenantId());
				for (TenantManufacture t : tm) {
					if (t.getManufacturerId() != null) {
						Optional<Manufacturer> m = manufacturerRepository.findById(t.getManufacturerId());
						if (m.isPresent()) {
							t.setManufacturerName(m.get().getManufacturerName());
						}
					}
				}
				return tm;
			}
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return Collections.emptyList();

	}

	public Result<TenantManufacture> updateDeadline(TenantManufacture t) {
		Result<TenantManufacture> result = new Result<>();
		try {

			Optional<TenantManufacture> tm = tenantManufactureRepository.findById(t.getId());

			if (tm.isPresent()) {
				TenantManufacture m = tm.get();
				m.setCheckedNotGrrn(t.getCheckedNotGrrn());
				m.setDeadlineClaim(t.getDeadlineClaim());
				m.setDeadlineOrder(t.getDeadlineOrder());
				m.setDeadlineTicket(t.getDeadlineTicket());
				m.setDispachedToDelivired(t.getDispachedToDelivired());
				m.setGrrnNotSecondCheque(t.getGrrnNotSecondCheque());
				m.setInvoiceNotDispached(t.getInvoiceNotDispached());
				m.setNotInvoiced(t.getNotInvoiced());
				m.setOrderNotFulfiled(t.getOrderNotFulfiled());
				m.setRecivedNotChecked(t.getRecivedNotChecked());
				m.setSecondCheck(t.isSecondCheck());
				m.setWmsed(t.isWmsed());
				m.setMultiQr(t.isMultiQr());
				TenantManufacture manufacture = tenantManufactureRepository.save(m);
				result.setCode(StringIteration.SUCCESS_CODE);
				result.setMessage(StringIteration.UPDATEDSUCCESSFULLY);
				result.setData(manufacture);
			} else {
				result.setCode(StringIteration.ERROR_CODE1);
				result.setMessage("Manufacturer Not Found");
			}

		} catch (Exception e) {
			result.setCode(StringIteration.ERROR_CODE1);
			logger.error(StringIteration.SPACE, e);
		}
		return result;
	}

	public Result<Tenant> updateTenantconfigure(Tenant t) {
		Result<Tenant> result = new Result<>();
		try {
			Optional<Tenant> option = tenantRepository.findById(t.getTenantId());

			if (option.isPresent()) {
				Tenant tn = option.get();
				tn.setTenantId(t.getTenantId());
				tn.setIsSecondCheck(t.isSecondCheck());
				tn.setWmsed(t.isWmsed());
				tenantRepository.save(tn);
				result.setCode(StringIteration.SUCCESS_CODE);
				result.setMessage(StringIteration.UPDATEDSUCCESSFULLY);
				result.setData(tn);
			}
		} catch (Exception e) {
			result.setCode(StringIteration.ERROR_CODE1);
			logger.error(StringIteration.SPACE, e);
		}
		return result;
	}

	public Result<TenantManufacture> updateTenantManufacturer(TenantManufacture t) {
		Result<TenantManufacture> result = new Result<>();
		try {

			Optional<User> us = userRepository.findByUserId(SecurityUtil.getUserName());

			if (us.isPresent()) {
				Optional<TenantManufacture> option = tenantManufactureRepository
						.findByManufactureId(t.getManufacturerId(), us.get().getTenantId());

				if (option.isPresent()) {
					TenantManufacture tn = option.get();
					tn.setManufacturerId(t.getManufacturerId());
					tn.setManufacturerName(t.getManufacturerName());
					tn.setEmailId(t.getEmailId());
					tn.setPort(t.getPort());
					tn.setLicenceNumber(t.getLicenceNumber());
					tn.setHost(t.getHost());
					tn.setPassword(t.getPassword());
					tn.setCityId(t.getCityId());
					tn.setCountry(t.getCountry());
					tn.setStateId(t.getStateId());
					tn.setPinCode(t.getPinCode());
					tn.setDistributionModel(t.getDistributionModel());
					tn.setEmailDate(t.getEmailDate());
					tenantManufactureRepository.save(tn);
					result.setCode(StringIteration.SUCCESS_CODE);
					result.setMessage(StringIteration.UPDATEDSUCCESSFULLY);
					result.setData(tn);
				}
			}
		} catch (Exception e) {
			result.setCode(StringIteration.ERROR_CODE1);
			logger.error(StringIteration.SPACE, e);
		}
		return result;
	}

}
