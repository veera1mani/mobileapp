package com.healthtraze.etraze.api.security.service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.security.model.NewLocationToken;
import com.healthtraze.etraze.api.security.model.PasswordResetToken;
import com.healthtraze.etraze.api.security.model.User;
import com.healthtraze.etraze.api.security.model.UserAuth;
import com.healthtraze.etraze.api.security.model.VerificationToken;



public interface IUserService {


	UserAuth getUser(String verificationToken);

    void saveRegisteredUser(User user);

    void deleteUser(User user);

    void createVerificationTokenForUser(UserAuth user, String token);


    VerificationToken getVerificationToken(String verificationToken);


    VerificationToken generateNewVerificationToken(String token);

    String createPasswordResetTokenForUser(UserAuth user, String token);

    UserAuth findUserByEmail(String email);

    PasswordResetToken getPasswordResetToken(String token);

    Optional<UserAuth> getUserByPasswordResetToken(String token);

    Optional<User> getUserByID(String id);

    Result<String> changeUserPassword(UserAuth user, String password);

    boolean checkIfValidOldPassword(UserAuth user, String password);

    String validateVerificationToken(String token);

    String generateQRUrl(User user) throws UnsupportedEncodingException;

    User updateUser2FA(boolean use2FA);

    List<String> getUsersFromSessionRegistry();

    NewLocationToken isNewLoginLocation(String username, String ip);

    String isValidNewLocationToken(String token);

    void addUserLocation(User user, String ip);
}
