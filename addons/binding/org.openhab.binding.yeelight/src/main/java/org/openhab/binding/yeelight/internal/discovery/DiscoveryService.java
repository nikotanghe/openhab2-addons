/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.yeelight.internal.discovery;

import org.eclipse.smarthome.config.discovery.AbstractDiscoveryService;
import org.eclipse.smarthome.config.discovery.DiscoveryResult;
import org.eclipse.smarthome.config.discovery.DiscoveryResultBuilder;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.openhab.binding.yeelight.YeelightBindingConstants;
import org.openhab.binding.yeelight.internal.YeelightHandlerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yeelight.sdk.device.DeviceBase;
import com.yeelight.sdk.listeners.DeviceListener;
import com.yeelight.sdk.services.DeviceManager;

/**
 * The {@link DiscoveryService} is responsible for search and discovery of new devices.
 *
 * @author Coaster Li - Initial contribution
 */
public class DiscoveryService extends AbstractDiscoveryService implements DeviceListener {

    private final Logger logger = LoggerFactory.getLogger(DiscoveryService.class.getSimpleName());

    public DiscoveryService() {
        super(YeelightHandlerFactory.SUPPORTED_THING_TYPES_UIDS, 2, false);
    }

    @Override
    protected void startScan() {
        logger.debug("Starting Scan");
        DeviceManager.getInstance().registerDeviceListener(this);
        DeviceManager.getInstance().startDiscovery();
    }

    @Override
    protected synchronized void stopScan() {
        logger.debug(": stopScan");
        DeviceManager.getInstance().stopDiscovery();
        DeviceManager.getInstance().unregisterDeviceListener(this);
    }

    @Override
    public void onDeviceFound(DeviceBase device) {
        logger.info("onDeviceFound, id: {}", device.getDeviceId());
        ThingUID thingUID = getThingUID(device);

        if (thingUID == null) {
            // We don't know about this thing type
            logger.info("Skipping device {}, unknown type.", device.getDeviceId());
            return;
        }

        ThingTypeUID thingTypeUID = getThingTypeUID(device);
        String deviceName = device.getDeviceName().isEmpty() ? DeviceManager.getDefaultName(device)
                : device.getDeviceName();

        DiscoveryResult discoveryResult = DiscoveryResultBuilder.create(thingUID).withThingType(thingTypeUID)
                .withLabel(deviceName).withProperties(device.getBulbInfo())
                .withProperty(YeelightBindingConstants.PARAMETER_DEVICE_ID, device.getDeviceId()).build();

        thingDiscovered(discoveryResult);
    }

    @Override
    public void onDeviceLost(DeviceBase device) {
        logger.debug("onDeviceLost, id: {}", device.getDeviceId());
    }

    private ThingUID getThingUID(DeviceBase device) {
        switch (device.getDeviceType()) {
            case ceiling:
                return new ThingUID(YeelightBindingConstants.THING_TYPE_CEILING, device.getDeviceId());
            case color:
                return new ThingUID(YeelightBindingConstants.THING_TYPE_WONDER, device.getDeviceId());
            case mono:
                return new ThingUID(YeelightBindingConstants.THING_TYPE_DOLPHIN, device.getDeviceId());
            case stripe:
                return new ThingUID(YeelightBindingConstants.THING_TYPE_STRIPE, device.getDeviceId());
            default:
                return null;
        }
    }

    private ThingTypeUID getThingTypeUID(DeviceBase device) {
        switch (device.getDeviceType()) {
            case ceiling:
                return YeelightBindingConstants.THING_TYPE_CEILING;
            case color:
                return YeelightBindingConstants.THING_TYPE_WONDER;
            case mono:
                return YeelightBindingConstants.THING_TYPE_DOLPHIN;
            case stripe:
                return YeelightBindingConstants.THING_TYPE_STRIPE;
            default:
                return null;
        }
    }
}
