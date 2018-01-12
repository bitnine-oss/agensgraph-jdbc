/*
 * Copyright (c) 2014-2018, Bitnine Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.bitnine.agensgraph.util;

/**
 * The Driver info.
 */
public final class DriverInfo {
    private DriverInfo() {
    }

    /**
     * The driver name.
     */
    public static final String DRIVER_NAME = "AgensGraph JDBC Driver";

    /**
     * The driver version.
     */
    public static final String DRIVER_VERSION = "1.4.2";

    /**
     * The driver full name.
     */
    public static final String DRIVER_FULL_NAME = DRIVER_NAME + " " + DRIVER_VERSION;

    /**
     * The major version.
     */
    public static final int MAJOR_VERSION = 1;

    /**
     * The minor version.
     */
    public static final int MINOR_VERSION = 4;

    /**
     * The patch version.
     */
    public static final int PATCH_VERSION = 2;
}
