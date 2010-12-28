/*
 * $Id$
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.apache.struts2.data;

/**
 * This class used to store all property values in List.
 * @author $Author$
 * @version $Id$
 * @since 1.5.0
 */

import java.util.ArrayList;
import java.util.List;

public class PropertyVO {
	private static final long serialVersionUID = 1L;
	private String name;
	private List<PropertyNameVO> objectProperty;

	public PropertyVO() {
		objectProperty = new ArrayList<PropertyNameVO>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<PropertyNameVO> getObjectProperty() {
		return objectProperty;
	}

	public void setObjectProperty(List<PropertyNameVO> objectProperty) {
		this.objectProperty = objectProperty;
	}

	public void addObjectProperty(PropertyNameVO objectPropertyValue) {
		objectProperty.add(objectPropertyValue);
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{PropertyVO=>\t");
		sb.append("name : " + name + ";\n");
		sb.append("ObjectProperty :" + objectProperty + "\n}");

		return sb.toString();
	}
}
