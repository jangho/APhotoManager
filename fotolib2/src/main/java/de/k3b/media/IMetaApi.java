/*
 * Copyright (c) 2016 by k3b.
 *
 * This file is part of AndroFotoFinder.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>
 */

package de.k3b.media;

import java.util.Date;
import java.util.List;

/**
 * Created by k3b on 09.10.2016.
 */
public interface IMetaApi {
    String getPath();
    IMetaApi setPath(String filePath);

    Date getDateTimeTaken();
    IMetaApi setDateTimeTaken(Date value);

    /** latitude, in degrees north. */
    IMetaApi setLatitude(Double latitude);
    /** longitude, in degrees east. */
    IMetaApi setLongitude(Double longitude);
    Double getLatitude();
    Double getLongitude();

    String getTitle();
    IMetaApi setTitle(String title);

    String getDescription();
    IMetaApi setDescription(String description);

    List<String> getTags();
    IMetaApi setTags(List<String> tags);

}