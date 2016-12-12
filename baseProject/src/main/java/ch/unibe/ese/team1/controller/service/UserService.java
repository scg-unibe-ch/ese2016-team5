package ch.unibe.ese.team1.controller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.unibe.ese.team1.model.User;
import ch.unibe.ese.team1.model.dao.UserDao;
import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Handles all database actions concerning users.
 */
@Service
public class UserService {
	
	@Autowired
	private UserDao userDao;

	/** Gets the user with the given username. */
	@Transactional
	public User findUserByUsername(String username) {
		return userDao.findByUsername(username);
	}
	
	/** Gets the user with the given id. */
	@Transactional
	public User findUserById(long id) {
		return userDao.findUserById(id);
	}
        
        @Transactional
        public String createPassword() {
            SecureRandom random = new SecureRandom();
            return new BigInteger(130, random).toString(32).substring(0,12);
        }
        
        @Transactional
        public void changePremium(User user) {
            if (user.isPremiumUser()) {
                user.setUserRole("ROLE_USER");
            }
            else {
                user.setUserRole("ROLE_PREMIUM");
            }
            userDao.save(user);
        }
        
}
