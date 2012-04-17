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
import com.ted.aggredata.model.Group;
import com.ted.aggredata.model.User;
import com.ted.aggredata.server.services.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class GWTGroupServiceImpl extends SpringRemoteServiceServlet implements GWTGroupService {

    @Autowired
    GroupService groupService;


    Logger logger = LoggerFactory.getLogger(GWTGroupServiceImpl.class);

    private User getCurrentUser() {
        return (User) getThreadLocalRequest().getSession().getAttribute(UserSessionServiceImpl.USER_SESSION_KEY);
    }

    @Override
    public List<Group> findGroups() {
        User user = getCurrentUser();
        if (logger.isInfoEnabled()) logger.info("Looking up groups for " + user);
        return groupService.getByUser(user);
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
}
