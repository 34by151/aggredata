/*
 * Copyright (c) 2012. The Energy Detective. All Rights Reserved
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.ted.aggredata.server.guiServiceImpl;

import com.google.gwt.user.client.Window;
import com.ted.aggredata.client.guiService.GWTUserService;
import com.ted.aggredata.model.User;
import com.ted.aggredata.server.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class GWTUserServiceImpl extends SpringRemoteServiceServlet implements GWTUserService {

    @Autowired
    UserService userService;

    Logger logger = LoggerFactory.getLogger(GWTUserServiceImpl.class);
    public static final String USER_SESSION_KEY = "AGGREDATA_USER";

    @Override
    public User saveUser(User user) {
        logger.info("Saving user information");
        user = userService.saveUser(user);
        return user;
    }

    @Override
    public User changePassword(User user, String Password) {
        logger.info("Updating Password");
        userService.changePassword(user, Password);
        return user;
    }

    @Override
    public User changeUsername(User user, String username) {
        logger.info("Updating username");
        User newUser = userService.changeUserName(user, username);
        return newUser;
    }

    @Override
    public List<User> findUsers() {
        User requestingUser = (User)getThreadLocalRequest().getSession().getAttribute(USER_SESSION_KEY);
        if (logger.isInfoEnabled()) logger.info(requestingUser + " is requesting a list of all users");
        if (requestingUser != null && requestingUser.getRole().equals(User.ROLE_ADMIN)){
            if (logger.isInfoEnabled()) logger.info("Looking up all users");
            return userService.findUsers();
        } else {
            logger.warn(requestingUser + " is attempting to get a list of users but is not an admin!");
            return new ArrayList<User>();
        }

    }



    @Override
    public void deleteUser(User user){
        User requestingUser = (User)getThreadLocalRequest().getSession().getAttribute(USER_SESSION_KEY);
        if (logger.isInfoEnabled()) logger.info(requestingUser + " is deleting user " + user);
        if (requestingUser != null && requestingUser.getRole().equals(User.ROLE_ADMIN) && requestingUser.getId() != user.getId()) {
            userService.deleteUser(user);
        }
    }

    @Override
    public List<User> findUsers(String substring) {
        if (logger.isInfoEnabled()) logger.info("Looking up users with substring " + substring);
        return userService.findUsers(substring);
    }
    
    @Override
    public User getUserByUserName(String username){
        if (logger.isInfoEnabled()) logger.info("Looking up user by username" + username);
        return userService.getUserByUserName(username);
    }

    @Override
    public User changeUserStatus(User entity, boolean enabled){
        if (logger.isInfoEnabled()) logger.info("Changing user status for " + entity.getUsername());
        return userService.changeUserStatus(entity, enabled);
    }

    @Override
    public User newUser(String username, String password, User user) {

        User requestingUser = (User)getThreadLocalRequest().getSession().getAttribute(USER_SESSION_KEY);
        if (logger.isInfoEnabled()) logger.info(requestingUser + " is requesting a list of all users");
        if (requestingUser != null && requestingUser.getRole().equals(User.ROLE_ADMIN)){

            User savedUser = new User();
            //Check dupe email
            User dupeUser = userService.getUserByUserName(user.getUsername());
            //Check to make sure the user doesn't exist and hasn't already been activated.
            if (dupeUser != null && dupeUser.getAccountState() != User.STATE_WAITING_ACTIVATION) return savedUser;

            logger.info("Creating new user " + user);
            if (user.getRole() == null) user.setRole(User.ROLE_USER);
            savedUser = userService.createUser(user, User.STATE_ENABLED);
            userService.changePassword(savedUser, password);

            return savedUser;
        } else {
            logger.error(requestingUser + " is attempting to create a new user but is not an ADMIN");
            return null;
        }


    }
}
