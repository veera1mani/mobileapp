package com.healthtraze.etraze.api.security.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.healthtraze.etraze.api.base.constant.StringIteration;
import com.healthtraze.etraze.api.base.controller.BaseCrudController;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.masters.model.UserPreference;
import com.healthtraze.etraze.api.security.model.MyProfile;
import com.healthtraze.etraze.api.security.model.User;
import com.healthtraze.etraze.api.security.repository.UserRepository;
import com.healthtraze.etraze.api.security.service.UserService;

/**
 * 
 * @author sharan
 *
 */
@RestController
public class UserController implements BaseCrudController<User, String> {


	private final UserService userService;
	
	private final UserRepository userRepository;
	
	@Autowired
	public UserController(UserService userService, UserRepository userRepository) {
		this.userService = userService;
		this.userRepository = userRepository;
	}


	@GetMapping(value = "/users")
	@Override
	public List<User> findAll() {
		return userService.findAll();
	}	
	
	
	@GetMapping(value = "/userss")
	public Result<HashMap<String, Object>> getAllUsers(@RequestParam("page") int page, @RequestParam("sortBy") String  sortBy,@RequestParam("sortDir") String sortDir,  @RequestParam("search") String  search){
		return userService.getAllUsers(page, sortBy, sortDir, search);
	}
	

	@GetMapping(value = "/login/users")
	public Optional<User> findByLoginUserDetails() {
		return userService.findByLoginUserDetails();
	}



	@GetMapping(value = "/user_list_values")
	public Iterable<Map<String, String>> userListValue() {
		return userService.findAll().stream().map(o -> {
			HashMap<String, String> map = new HashMap<>();
			map.put("userId", o.getUserId() + StringIteration.SPACE);
			map.put("userName", o.getFirstName() +StringIteration.SPACE+ o.getLastName());
			return map;
		}).collect(Collectors.toList());
	}

	@GetMapping(value = "/user_list_value/{roleId}")
	public Iterable<Map<String, String>> userListValue(@PathVariable String roleId) {

		List<User> u = userService.findByRoleId(roleId);
		return u.stream().map(o -> {
			HashMap<String, String> map = new HashMap<>();

			map.put("userId", o.getUserId() + StringIteration.SPACE);
			map.put("userName", o.getFirstName() + StringIteration.SPACE + o.getLastName());

			return map;
		}).collect(Collectors.toList());
	}

	@PostMapping("/emailVerification")
	public Result<User> emailExits(@RequestBody User userEmail) {
		return userService.emailExits(userEmail);
	}

	@PostMapping("/phoneNumberVerification")
	public Result<User> phoneNumberExits(@RequestBody User phone) {

		return userService.phoneNumberExits(phone);

	}

	@GetMapping(value = "/user/byuserId/{userId}")
	@Override
	public User findById(@PathVariable String userId) {
		return userService.findById(userId);
	}
	
	

	@GetMapping(value = "/managers") 
	public List<User> findManagers() {
		return userService.findManagers();
	}

	@GetMapping(value = "myProfile")
	public Result<HashMap<String, Object>>  myProfile() {
		return userService.myProfile();
	}
	

	
	@PostMapping(value = "updateMyProfile")
	public Result<MyProfile>  myProfileUpdate(@RequestBody MyProfile t) {
		return userService.myProfileUpdate(t);
	}

	@PostMapping(value = "/user")
	@Override
	public Result<User> create(@RequestBody User t) {
		return userService.signUp(t);
	}
	

	@PostMapping(value = "/user_register")
	public Result<User> register(@RequestBody User t) {
		return userService.create(t);
	}

	@PostMapping("/userpreference")
	public Result<UserPreference> create(@RequestBody UserPreference t) {
		return userService.create(t);
	}

	@PutMapping(value = "/user")
	@Override
	public Result<User> update(@RequestBody User t) {
		return userService.update(t);
	}

	@PutMapping(value = "/user-profile-image")
	public Result<User> updateUserProfileImage(@RequestBody User t) {
		return userService.updateUserProfileImage(t);
	}

	@DeleteMapping(value = "/user/deletebyuserId/{userId}")
	@Override
	public Result<User> delete(@PathVariable String userId) {
		return userService.delete(userId);
	}
    
	
	@GetMapping(value="/user-list")
	public List<User> findUserList(@RequestParam String hierarchyId){
			return userRepository.finduserByTenantId(hierarchyId);
	}		

	

	
	@GetMapping("/user/{pageNo}/{pageSize}")
    public List<User> getPaginatedCountries(@PathVariable int pageNo,@PathVariable int pageSize) {

        return userService.findPaginated(pageNo, pageSize);
    }
	
	
	
	@DeleteMapping("/user/{userId}")
	public Result<User> deactivateUser(@PathVariable("userId") String userId) {
		return userService.deactivateUser(userId);
	}
	
	@PutMapping(value = "/update-user-floor")
	public Result<User> updateUserLocation(@RequestParam("floor") String f) {
		return userService.updateUserLocation(f);
	}
	
	@GetMapping(value ="/get-floor")
	public Result<Object> getFloor(){
		return userService.getFloor();
	}
}
