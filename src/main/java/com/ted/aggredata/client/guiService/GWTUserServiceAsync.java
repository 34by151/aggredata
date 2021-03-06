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

package com.ted.aggredata.client.guiService;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.ted.aggredata.model.User;

import java.util.List;

public interface GWTUserServiceAsync {
    void saveUser(User user, AsyncCallback<User> async);

    void changePassword(User user, String Password, AsyncCallback<User> async);

    void changeUsername(User user, String username, AsyncCallback<User> async);

    void findUsers(AsyncCallback<List<User>> async);
    
    void deleteUser(User user, AsyncCallback<Void> async );

    void getUserByUserName(String username, AsyncCallback<User> async);

    void changeUserStatus(User user, boolean enabled, AsyncCallback<User> async);
    /**
     * Used to find a list of users based on a substring. We match on name and email address.
     *
     * @param substring
     * @return
     */
    void findUsers(String substring, AsyncCallback<List<User>> async);

    /**
     * Creates a new user with the specified password. The user is enabled by default.
     *
     * @param username
     * @param password
     * @param user
     * @return
     */
    void newUser(String username, String password, User user, AsyncCallback<User> async);
}
