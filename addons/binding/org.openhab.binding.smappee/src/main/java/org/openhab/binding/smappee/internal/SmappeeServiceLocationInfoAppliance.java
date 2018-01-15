/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.smappee.internal;

/**
 * The result of a smappee service location info reading
 * This contains info about the appliance that smappee has detected on your net
 *
 * @author Niko Tanghe - Initial contribution
 */

public class SmappeeServiceLocationInfoAppliance {
    public String id;
    public String name;
    public String type;
}
