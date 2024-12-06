package com.healthtraze.etraze.api.base.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.healthtraze.etraze.api.base.constant.Constants;
import com.healthtraze.etraze.api.base.constant.StringIteration;
import com.healthtraze.etraze.api.base.model.Event;
import com.healthtraze.etraze.api.base.model.Notification;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.base.repository.EventRepository;
import com.healthtraze.etraze.api.base.service.NotificationService;
import com.healthtraze.etraze.api.base.util.CommonUtil;
import com.healthtraze.etraze.api.security.repository.UserRepository;
import java.util.Collections;

@RestController()
@CrossOrigin
public class NotificationController implements BaseCrudController<Notification, Long> {

	private Logger logger = LogManager.getLogger(NotificationController.class);

	private final NotificationService service;

	private final EventRepository eventRepository;

	@Autowired
	public NotificationController(NotificationService service, EventRepository eventRepository,
			UserRepository repository) {
		this.service = service;
		this.eventRepository = eventRepository;
	}

	@GetMapping(value = "/notifications")
	@Override
	public List<Notification> findAll() {
		try {
			return service.findAll();
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return Collections.emptyList();
	}

	@GetMapping(value = "/mynotifications/{read}")
	public Iterable<Notification> mynotifications(@PathVariable boolean read) {
		return service.myNotification(read);
	}

	@GetMapping(value = "/mynotifications")
	public Iterable<Notification> mynotifications() {
		return service.myNotification();
	}

	@GetMapping(value = "/notifications/{read}")
	public Iterable<Notification> findAllRead(@PathVariable boolean read) {
		try {
			if (read) {
				return service.findAll().stream().filter(n -> n.isRead()).collect(Collectors.toList());
			} else {
				return service.findAll().stream().filter(n -> !n.isRead()).collect(Collectors.toList());
			}
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	@GetMapping(value = "/notifications/latest")
	public Iterable<Notification> findLatest(@PathVariable boolean read) {
		try {
			return service.findAll().stream().filter(n -> !n.isRead()).collect(Collectors.toList());
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	@GetMapping(value = "/notification/{id}")
	@Override
	public Notification findById(@PathVariable Long id) {
		try {
			return service.findById(id);
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	@PostMapping(value = "/notification")
	@Override
	public Result<Notification> create(@RequestBody Notification t) {
		try {

			return service.create(t);
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	@PostMapping(value = "/event-notification")
	public Result<Event> create(@RequestBody Event event) {
		Result<Event> result = new Result<>();
		try {

			if (event == null || event.getRuleId() == null || event.getEventType() == null
					|| event.getDeviceId() == null) {
				result.setCode(StringIteration.ERROR_CODE1);
				result.setMessage("event,roleId,event tpye,deviceId any of the value couldn't empty");
				return result;
			}
			event.setStatus(Constants.NEW);
			event.setId(System.currentTimeMillis());
			event.setCreatedOn(CommonUtil.getLocalDateTime());
			eventRepository.save(event);

			result.setCode(StringIteration.SUCCESS_CODE);
			result.setMessage(StringIteration.SUCCESSFULLYUPDATED);

		} catch (Exception e) {
			result.setCode(StringIteration.ERROR_CODE1);
			logger.error(StringIteration.SPACE, e);
		}
		return result;
	}

	@PutMapping(value = "/notification")
	@Override
	public Result<Notification> update(@RequestBody Notification t) {
		try {

			return service.update(t);
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	@PutMapping(value = "/bulk-update-notification")
	public Result<Notification> updateNotifications(@RequestBody List<Notification> notifications) {
		try {
			return service.updateNotifications(notifications);
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	@DeleteMapping(value = "/notification/{id}")
	@Override
	public Result<Notification> delete(@PathVariable Long id) {
		Result<Notification> result = new Result<>();
		try {
			service.delete(id);
			result.setCode(StringIteration.SUCCESS_CODE);
		} catch (Exception e) {
			result.setCode(StringIteration.ERROR_CODE1);
			logger.error(StringIteration.SPACE, e);
		}
		return result;
	}

	@PostMapping(value = "/bulk-delete-notification")
	public Result<Notification> deleteAll(@RequestBody List<Long> notifications) {
		return service.deleteAll(notifications);
	}

	@PutMapping(value = "/notification/{id}")
	public Result<Notification> updateNotification(@PathVariable Long id) {

		return service.updateNotification(id);
	}

	@GetMapping(value = "/asset-notification/{assetId}")
	public List<Notification> findByAssedtId(@PathVariable String assetId) {
		try {
			return service.findByAssedtId(assetId);
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return Collections.emptyList();
	}

}
