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
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.ted.aggredata.model.Gateway;
import com.ted.aggredata.model.Group;
import com.ted.aggredata.model.MTU;

import java.util.List;

public interface GWTGatewayServiceAsync {
    /**
     * Finds gateways for the  user
     *
     * @return
     */
    void findGateways(AsyncCallback<List<Gateway>> async);

    /**
     * Finds gateways for the  group
     *
     * @return
     */
    void findGateways(Group group, AsyncCallback<List<Gateway>> async);

    void removeGatewayFromGroup(Group group, Gateway gateway, AsyncCallback<Void> async);

    void addGatewayToGroup(Group group, Gateway gateway, AsyncCallback<Void> async);

    void deleteGateway(Gateway gateway, AsyncCallback<Void> async);

    void saveGateway(Gateway gateway, AsyncCallback<Gateway> async);

    void saveMTU(Gateway gateway, MTU mtu, AsyncCallback<MTU> async);

    void findMTU(Gateway gateway, AsyncCallback<List<MTU>> async);

    void deleteMTU(Gateway gateway, MTU mtu, AsyncCallback<Void> async);
}
