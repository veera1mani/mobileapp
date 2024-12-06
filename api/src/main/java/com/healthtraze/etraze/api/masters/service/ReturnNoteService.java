package com.healthtraze.etraze.api.masters.service;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthtraze.etraze.api.base.constant.StringIteration;
import com.healthtraze.etraze.api.base.model.BooleanResult;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.base.service.BaseService;
import com.healthtraze.etraze.api.base.util.CommonUtil;
import com.healthtraze.etraze.api.masters.model.Return;
import com.healthtraze.etraze.api.masters.model.ReturnNote;
import com.healthtraze.etraze.api.masters.repository.ReturnNoteRepository;
import com.healthtraze.etraze.api.masters.repository.ReturnRepository;
import com.healthtraze.etraze.api.security.model.User;
import com.healthtraze.etraze.api.security.repository.UserRepository;
import com.healthtraze.etraze.api.security.util.SecurityUtil;

@Service
public class ReturnNoteService implements BaseService<ReturnNote,String> {
	
	private Logger logger = LogManager.getLogger(ReturnNoteService.class);
	
	private final UserRepository userRepository;
	
	private final ReturnNoteRepository returnNoteRepository;
	
	
	private final ReturnRepository returnRepository;
	
	@Autowired
	public ReturnNoteService(UserRepository userRepository, ReturnNoteRepository returnNoteRepository,
			ReturnRepository returnRepository) {
		super();
		this.userRepository = userRepository;
		this.returnNoteRepository = returnNoteRepository;
		this.returnRepository = returnRepository;
	}
	
	@Override
	public List<ReturnNote> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReturnNote findById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result<ReturnNote> create(ReturnNote t) {
		Result<ReturnNote> result = new Result<>();
		try {
			Optional<User> optional = userRepository.findById(SecurityUtil.getUserName());
			if(optional.isPresent()) {
				User user = optional.get();
				CommonUtil.setCreatedOn(t);
				t.setTenantId(user.getTenantId());
				t.setId(System.currentTimeMillis()+"");
				returnNoteRepository.save(t);
				result.setCode(StringIteration.SUCCESS_CODE);
				result.setMessage(StringIteration.CREATEDSUCCESSFULLY);
			}
			result.setCode(StringIteration.ERROR_CODE1);
			result.setMessage(StringIteration.INVALID_USER);
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			result.setCode(StringIteration.ERROR_CODE1);
			result.setMessage(StringIteration.ERROR_WHILE_CREATE);
		}
		return result;
	}
	
	public BooleanResult checkDuplicateGrrnNumber(List<ReturnNote> returnNotes, User us) {
		BooleanResult result = new BooleanResult();
		try {
			Optional<Return> ret = returnRepository.findById(returnNotes.get(0).getReturnId());
			if (ret.isPresent()) {
				for (ReturnNote rn : returnNotes) {
					if(rn.getNoteType().equals("GRRN")) {					
						Optional<ReturnNote> note = returnNoteRepository.findGrrnNumberDuplicateByManufacturer(
								us.getTenantId(), ret.get().getManufacturer(), rn.getNoteNumber());
						if (note.isPresent()) {
							result.setFlag(true);
							result.setMessage(note.get().getNoteNumber());
						}
					} else {
						result.setFlag(false);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.setFlag(false);
		}
		return result;
	}
	
	public BooleanResult checkDuplicateCnNumber(List<ReturnNote> returnNotes, User us) {
		BooleanResult result = new BooleanResult();

		try {
				for (ReturnNote rn : returnNotes) {
					if(rn.getNoteType().equals(StringIteration.CN)) {					
						Optional<ReturnNote> note = returnNoteRepository.findCnDuplicate(
								us.getTenantId(),rn.getReturnId(), rn.getNoteNumber());
						if (note.isPresent()) {
							result.setFlag(true);
							result.setMessage(note.get().getNoteNumber());
						}
					} else {
						result.setFlag(false);
					}
				}
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.setFlag(false);
		}
		return result;
	}
	
	public BooleanResult checkDuplicateGrrnNumber(ReturnNote returnNote, User us) {
		BooleanResult result = new BooleanResult(false);

		try {
			Optional<Return> ret = returnRepository.findById(returnNote.getReturnId());
			if (ret.isPresent()) {
				Optional<ReturnNote> note = returnNoteRepository.findGrrnNumberDuplicateByManufacturer(us.getTenantId(),
						ret.get().getManufacturer(), returnNote.getNoteNumber());
				if (note.isPresent() && !returnNote.getId().equals(note.get().getId())) {
					result.setFlag(true);
					result.setMessage(note.get().getNoteNumber());
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return result;
	}
	
	public BooleanResult checkDuplicateCNNumber(ReturnNote rn, User us) {
		BooleanResult result = new BooleanResult(false);
		try {
			Optional<Return> ret = returnRepository.findById(rn.getReturnId());
			if (ret.isPresent()) {
				Optional<ReturnNote> note = returnNoteRepository.findCnDuplicate(
						us.getTenantId(),rn.getReturnId(), rn.getNoteNumber());
				if (note.isPresent()&& !rn.getId().equals(note.get().getId())) {
					result.setFlag(true);
					result.setMessage(note.get().getNoteNumber());
				}

			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return result;
	}
	
	public Result<ReturnNote> create(List<ReturnNote> list) {
		Result<ReturnNote> result = new Result<>();
		try {
			Optional<User> optional = userRepository.findById(SecurityUtil.getUserName());
			if(optional.isPresent()) {
				User user = optional.get();
				
				BooleanResult grrn =checkDuplicateGrrnNumber(list, user);
				if(Boolean.TRUE.equals(grrn.isFlag())) {
					result.setCode(StringIteration.ERROR_CODE1);
					result.setMessage("Duplicate GRRN Number "+grrn.getMessage());
					return result;
				}
				
				BooleanResult cn =checkDuplicateCnNumber(list, user);
				if(Boolean.TRUE.equals(cn.isFlag())) {
					result.setCode(StringIteration.ERROR_CODE1);
					result.setMessage("Duplicate CN Number "+cn.getMessage());
					return result;
				}
				
				list.forEach(t ->{
					CommonUtil.setCreatedOn(t);
					t.setTenantId(user.getTenantId());
					t.setId(System.currentTimeMillis()+"");
					returnNoteRepository.save(t);
				});
				result.setCode(StringIteration.SUCCESS_CODE);
				result.setMessage(StringIteration.CREATEDSUCCESSFULLY);
			}
			else {
				result.setCode(StringIteration.ERROR_CODE1);
				result.setMessage(StringIteration.INVALID_USER);
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			result.setCode(StringIteration.ERROR_CODE1);
			result.setMessage(StringIteration.ERROR_WHILE_CREATE);
		}
		return result;
	}

	@Override
	public Result<ReturnNote> update(ReturnNote t) {

		Result<ReturnNote> result = new Result<>();
		try {
			Optional<User> optional = userRepository.findById(SecurityUtil.getUserName());			
			if(optional.isPresent()) {
				User user = optional.get();				
				Optional<ReturnNote> returnNote = returnNoteRepository.findById(t.getId(),user.getTenantId());			
				if(returnNote.isPresent()) {
					ReturnNote rn = returnNote.get();
					String _type=t.getNoteType();
					if(_type.equals(StringIteration.GRRN)) {
						BooleanResult grrn = checkDuplicateGrrnNumber(t, user);
						if(grrn.isFlag()) {
							result.setCode(StringIteration.ERROR_CODE1);
							result.setMessage("Duplicate Grrn Number "+grrn.getMessage());
							return result;
						}
						
					}else if(_type.equals(StringIteration.CN)) {
						BooleanResult cn =checkDuplicateCNNumber(t, user);
						if(cn.isFlag()) {
							result.setCode(StringIteration.ERROR_CODE1);
							result.setMessage("Duplicate Cn Number "+cn.getMessage());
							return result;
						}
					}
					
					CommonUtil.setModifiedOn(rn);
					rn.setNoteNumber(t.getNoteNumber());
					rn.setNoteDate(t.getNoteDate());
					rn.setClaimType(t.getClaimType());
					rn.setLineItem(t.getLineItem());
					returnNoteRepository.save(rn);
					result.setCode(StringIteration.SUCCESS_CODE);
					result.setMessage(StringIteration.UPDATEDSUCCESSFULLY);
				}
				
			}else {
				result.setCode(StringIteration.ERROR_CODE1);
				result.setMessage(StringIteration.INVALID_USER);
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			result.setCode(StringIteration.ERROR_CODE1);
			result.setMessage(StringIteration.ERROR_WHILE_CREATE);
		}
		return result;
	
	}

	@Override
	public Result<ReturnNote> delete(String id) {
		// TODO Auto-generated method stub
		return null;
	}

}
