package com.healthtraze.etraze.api.base.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthtraze.etraze.api.base.constant.StringIteration;
import com.healthtraze.etraze.api.base.model.Notification;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.base.repository.NotificationRepository;
import com.healthtraze.etraze.api.base.util.CommonUtil;
import com.healthtraze.etraze.api.security.model.User;
import com.healthtraze.etraze.api.security.repository.UserRepository;
import com.healthtraze.etraze.api.security.util.SecurityUtil;

@Component
public class NotificationService implements BaseService<Notification, Long> {

	private Logger logger = LogManager.getLogger(NotificationService.class);


	private final NotificationRepository repository;

	private final UserRepository userRepository;

	private  final SimpMessagingTemplate template;
 

	public NotificationService(NotificationRepository repository, UserRepository userRepository,
			SimpMessagingTemplate template) {
		super();
		this.repository = repository;
		this.userRepository = userRepository;
		this.template = template;
	}

	@Override
	public List<Notification> findAll() {
		try {
			return repository.findAll();
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return new ArrayList<>();
	}

	public List<Notification> myNotification() {
		try {
			Optional<User> u = userRepository.findByUserId(SecurityUtil.getUserName());
			if (u.isPresent()) {
				return repository.findByUserId(u.get().getUserId());
			}

		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return new ArrayList<>();
	}

	public List<Notification> myNotification(boolean read) {
		try {
			Optional<User> u = userRepository.findByUserId(SecurityUtil.getUserName());
			if (u.isPresent()) {

				if (read) {
					List<Notification> l = repository.findByUserId(u.get().getUserId()).stream()
							.filter(Notification::isRead).collect(Collectors.toList());
					if (!l.isEmpty()) {
						return l;
					}
				} else {
					List<Notification> l = repository.findByUserId(u.get().getUserId()).stream().filter(n -> !n.isRead())
							.collect(Collectors.toList());
					if (!l.isEmpty()) {
						return l;
					}
				}

			}
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return new ArrayList<>();
	}

	@Override
	public Notification findById(Long id) {
		try {
			Optional<Notification> v = repository.findById(id);
			if (v.isPresent()) {
				return v.get();
			}
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	@Override
	public Result<Notification> create(Notification t) {
		Result<Notification> result = new Result<>();
		try {
			t.setId(System.currentTimeMillis());
			t.setCreatedOn(CommonUtil.getLocalDateTime());

			Notification c = repository.save(t);

			result.setCode(StringIteration.SUCCESS_CODE);
			result.setMessage(StringIteration.NOTIFICATIONCREATEDSUCCESSFULLY);
			result.setData(c);
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return result;
	}

	@Override
	public Result<Notification> update(Notification t) {
		Result<Notification> result = new Result<>();
		try {
			Notification c = repository.save(t);
			result.setCode(StringIteration.SUCCESS_CODE);
			result.setMessage(StringIteration.SUCCESSFULLYUPDATED);
			result.setData(c);
			return result;
		} catch (Exception e) {
			result.setCode(StringIteration.ERROR_CODE1);
			result.setMessage(e.getMessage());
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	public Result<Notification> updateNotifications(List<Notification> t) {
		Result<Notification> result = new Result<>();
		try {
			for (Notification notification : t) {
				repository.save(notification);
			}
			result.setCode(StringIteration.SUCCESS_CODE);
			result.setMessage(StringIteration.SUCCESSFULLYUPDATED);
			return result;
		} catch (Exception e) {
			result.setCode(StringIteration.ERROR_CODE1);
			result.setMessage(e.getMessage());
			logger.error(StringIteration.SPACE, e);
		}
		return result;
	}

	public Result<Notification> deleteAll(List<Long> notifications) {
		Result<Notification> result = new Result<>();
		try {
			if (notifications == null) {
				result.setCode(StringIteration.ERROR_CODE1);
				result.setMessage(StringIteration.BADREQUEST);
			}

			for (Long long1 : notifications) {
				Optional<Notification> tr = repository.findById(long1);
				if (tr.isPresent()) {
					repository.delete(tr.get());
				}
			}
			result.setCode(StringIteration.SUCCESS_CODE);
			result.setMessage(StringIteration.DELETEDSUCCESSFULLY);
		} catch (Exception e) {
			result.setCode(StringIteration.ERROR_CODE1);
			result.setMessage(e.getMessage());
			logger.error(StringIteration.SPACE, e);
		}
		return result;
	}

	@Override
	public Result<Notification> delete(Long id) {
		Result<Notification> result = new Result<>();
		try {

			Optional<Notification> tr = repository.findById(id);

			if (tr.isPresent()) {
				repository.delete(tr.get());
				result.setCode(StringIteration.SUCCESS_CODE);
				result.setMessage(id + StringIteration.DELETEDSUCCESSFULLY);

				return result;
			}

		} catch (Exception e) {
			result.setCode(StringIteration.ERROR_CODE1);
			result.setMessage(e.getMessage());
			logger.error(StringIteration.SPACE, e);
		}
		return result;
	}

	public Result<Notification> notify(String userId, String s, String d) {
		Result<Notification> result = new Result<>();
		try {
			Notification note = new Notification();
			note.setUserId(userId);
			note.setSubject(s);
			note.setDescription(d);
			note.setId(System.currentTimeMillis());
			repository.save(note);
			result.setCode(StringIteration.SUCCESS_CODE);
			result.setMessage(StringIteration.SAVEDSUCCESSFULLY);
		} catch (Exception e) {
			result.setCode(StringIteration.ERROR_CODE1);
			result.setMessage(e.getLocalizedMessage());
		}
		return result;
	}

	public Result<Notification> updateNotification(Long id) {
		Result<Notification> result = new Result<>();
		try {
			Optional<Notification> optional = repository.findById(id);
			if (optional.isPresent()) {
				optional.get().setRead(true);
				repository.save(optional.get());
				result.setCode(StringIteration.SUCCESS_CODE);
				result.setMessage(StringIteration.SUCCESSFULLYUPDATED);
			}

		} catch (Exception e) {
			result.setCode(StringIteration.ERROR_CODE1);
			logger.error(StringIteration.SPACE, e);
		}
		return result;

	}

	public void notifications(String userId, String subject, String description,String assetId,String tenantId) {
		try {
			Notification notification = new Notification();
			notification.setUserId(userId);
			notification.setTenantId(tenantId);
			notification.setSubject(subject);
			notification.setId(System.currentTimeMillis());
			notification.setDescription(description);
			notification.setCreatedOn(CommonUtil.getLocalDateTime());
			notification.setAssetId(assetId);
			
			repository.save(notification);

				ObjectMapper objectMapper = new ObjectMapper();
				this.template.convertAndSend("/notifications/" + notification.getUserId(),
						objectMapper.writeValueAsString(notification));
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
	}

	public List<Notification> findByAssedtId(String assetId) {
		try {
			List<Notification> notify = repository.findByAssetId(assetId);
			if(!notify.isEmpty()) {
				return notify;
			}
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return Collections.emptyList();
	}
}
