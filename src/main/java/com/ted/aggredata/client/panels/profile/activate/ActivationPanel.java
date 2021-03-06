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

package com.ted.aggredata.client.panels.profile.activate;


import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.ted.aggredata.client.Aggredata;
import com.ted.aggredata.client.panels.profile.gateways.GatewaysPanel;
import com.ted.aggredata.client.resources.lang.DashboardConstants;

import java.util.logging.Logger;

public class ActivationPanel extends Composite {

    static Logger logger = Logger.getLogger(GatewaysPanel.class.toString());

    interface MyUiBinder extends UiBinder<Widget, ActivationPanel> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    DashboardConstants dashboardConstants = GWT.create(DashboardConstants.class);
    @UiField
    Label activationUrlLabel;
    @UiField
    Label activationKeyLabel;
    @UiField
    Label postTimeLabel;

    public ActivationPanel() {
        initWidget(uiBinder.createAndBindUi(this));
        activationUrlLabel.setText(Aggredata.GLOBAL.getServerInfo().getActivationUrl());
        activationKeyLabel.setText(Aggredata.GLOBAL.getSessionUser().getActivationKey());

        StringBuilder finalParagraph = new StringBuilder();
        finalParagraph.append(dashboardConstants.activationInstructions4());
        finalParagraph.append(" ");
        finalParagraph.append(Aggredata.GLOBAL.getServerInfo().getPostDelay());
        finalParagraph.append(" ");
        finalParagraph.append(dashboardConstants.minutes());
        finalParagraph.append(".");
        postTimeLabel.setText(finalParagraph.toString());


    }
}
