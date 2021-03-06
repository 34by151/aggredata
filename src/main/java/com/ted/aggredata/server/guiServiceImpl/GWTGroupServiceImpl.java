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

import com.ted.aggredata.client.guiService.GWTGroupService;
import com.ted.aggredata.model.*;
import com.ted.aggredata.server.services.GroupService;
import com.ted.aggredata.server.services.HistoryService;
import com.ted.aggredata.server.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class GWTGroupServiceImpl extends SpringRemoteServiceServlet implements GWTGroupService {

    @Autowired
    GroupService groupService;

    @Autowired
    UserService userService;

    @Autowired
    HistoryService historyService;

    Logger logger = LoggerFactory.getLogger(GWTGroupServiceImpl.class);

    private User getCurrentUser() {
        return (User) getThreadLocalRequest().getSession().getAttribute(UserSessionServiceImpl.USER_SESSION_KEY);
    }

    @Override
    public List<Group> findOwnedGroups() {
        User user = getCurrentUser();
        if (logger.isInfoEnabled()) logger.info("Looking up groups for " + user);
        return groupService.getOwnedByUser(user);
    }

    @Override
    public List<Group> findGroupsWithGateways() {
        User user = getCurrentUser();
        if (logger.isInfoEnabled()) logger.info("Looking up groups for " + user);
        return groupService.getByUserWithGateways(user);
    }


    @Override
    public Group createGroup(String description) {
        User user = getCurrentUser();
        if (logger.isInfoEnabled()) logger.info("creating group" + description + " for " + user);
        return groupService.createGroup(user, description);
    }

    @Override
    public Group saveGroup(Group group) {
        User user = getCurrentUser();
        if (user.getId().equals(group.getOwnerUserId())) {
            if (logger.isInfoEnabled()) logger.info("saving group " + group);
            return groupService.saveGroup(group);
        }
        logger.warn("Security violation. " + user + " attempted to save " + group);
        return group;
    }

    @Override
    public void deleteGroup(Group group) {
        User user = getCurrentUser();
        if (user.getId().equals(group.getOwnerUserId())) {
            if (logger.isInfoEnabled()) logger.info("deleting group " + group);
            groupService.deleteGroup(group);
        } else {
            logger.warn("Security violation. " + user + " attempted to delete " + group);
        }
    }

    @Override
    public EnergyDataHistoryQueryResult getHistory(Enums.HistoryType historyType, Group group, long startTime, long endTime, int interval) {
        User user = getCurrentUser();
        if (group == null || group.getId() == null) {
            logger.debug("No group specified");
            return new EnergyDataHistoryQueryResult();
        }
        //Check group history
        Group userGroup = groupService.getGroup(user, group.getId());
        if (userGroup == null) logger.error("User group is null");

        if (userGroup != null && (userGroup.getRole()== Group.Role.OWNER || userGroup.getRole() == Group.Role.READONLY)) {
            if (logger.isInfoEnabled()) logger.info("retrieving " + historyType +" history for  " + group + " " + startTime+"-"+endTime);
            EnergyDataHistoryQueryResult result = historyService.getHistory(historyType, user, group, startTime, endTime, interval);
            return result;
        }
        logger.warn("Security violation. " + user + " attempted to retrieve history for  " + group);
        return new EnergyDataHistoryQueryResult();
    }

    @Override
    public String exportHistory(Enums.HistoryType historyType, Group group, long startTime, long endTime, int interval) {

        //Rerun the history (in case anything has changed since last request)
        EnergyDataHistoryQueryResult result = getHistory(historyType, group, startTime, endTime, interval);

        //Generate a random key for this history (to prevent XSS access to last history being run)
        SecureRandom random = new SecureRandom();
        String key = new BigInteger(130, random).toString(32);



        //Place the results in session (temporary, CSV servlet will clear this and the key out).
        this.getThreadLocalRequest().getSession().setAttribute(key, result);

        //Return the key
        return key;


    }

    @Override
    public void addUserToGroup(Group group, User user) {
        User sessionUser = getCurrentUser();
        if (group.getOwnerUserId().equals(sessionUser.getId())){
            if (logger.isInfoEnabled()) logger.info(sessionUser + " is adding " + user + "  to  " + group);
            groupService.addUserToGroup(user, group, Group.Role.READONLY);
        } else {
            logger.warn("Security violation. " + sessionUser + " attempted to add " + user + "  to  " + group);
        }

    }

    @Override
    public void removeUserFromGroup(Group group, User user) {
        User sessionUser = getCurrentUser();
        if (group.getOwnerUserId().equals(sessionUser.getId())){
            if (logger.isInfoEnabled()) logger.info(sessionUser + " is removing " + user + "  from  " + group);
            groupService.removeUserFromGroup(user, group);
        } else {
            logger.warn("Security violation. " + sessionUser + " attempted to remove " + user + "  from  " + group);
        }
    }

    @Override
    public List<User> getGroupMembers(Group group) {
        User sessionUser = getCurrentUser();
        if (group.getOwnerUserId().equals(sessionUser.getId())){
            if (logger.isInfoEnabled()) logger.info(sessionUser + " is retrieving a list of users for group " + group);
            return userService.findUsers(group);
        } else {
            logger.warn("Security violation. " + sessionUser + " attempted to retrieve a list of users for group " + group);
            return null;
        }
    }


}
