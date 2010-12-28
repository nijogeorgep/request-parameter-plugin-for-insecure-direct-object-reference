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

package com.apache.struts2.interceptor;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.digester.Digester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.apache.struts2.data.ObjectMappingVO;
import com.apache.struts2.data.PropertyNameVO;
import com.apache.struts2.data.PropertyVO;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.util.reflection.ReflectionContextState;

/**
 * This class used set nested property to action object from Request parameter.
 *
 * @author $Author$
 * @version $Id$
 * @since 1.5.0
 */

public class ParameterToActionObjectMappingInterceptor extends
		AbstractInterceptor {

	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory
			.getLog(ParameterToActionObjectMappingInterceptor.class);

	private static final String XML_EXTENSION = "-jspObjectMapping.xml";

	/**
	 *
	 */
	public String intercept(ActionInvocation invocation) throws Exception {

		ActionProxy actProxy = invocation.getProxy();
		Object object = actProxy.getAction();

		if (actProxy.getAction().getClass().getResourceAsStream(
				actProxy.getActionName() + XML_EXTENSION) != null) {
			Digester digester = new Digester();
			digester.setValidating(false);

			digester.addObjectCreate("mapping", ObjectMappingVO.class);

			digester.addObjectCreate("mapping/requestParameter",
					PropertyVO.class);
			digester.addSetProperties("mapping/requestParameter", "name",
					"name");

			digester.addObjectCreate("mapping/requestParameter/objectMapping",
					PropertyNameVO.class);
			digester.addBeanPropertySetter(
					"mapping/requestParameter/objectMapping/property", "name");
			digester.addSetNext("mapping/requestParameter/objectMapping",
					"addObjectProperty");

			digester.addSetNext("mapping/requestParameter", "addProperty");

			ObjectMappingVO objectMappingVO = (ObjectMappingVO) digester
					.parse(actProxy.getAction().getClass().getResourceAsStream(
							actProxy.getActionName() + XML_EXTENSION));

			if (log.isDebugEnabled()) {
				log.debug("Object Mapping for Action :"
						+ actProxy.getAction().getClass());
				log.debug("Object Mapping : " + objectMappingVO);
			}

			try {
				processParameters(objectMappingVO, object);
			} catch (IllegalAccessException ie) {
				log.error("IllegalAccessException", ie);
			} catch (InstantiationException ie) {
				log.error("InstantiationException", ie);
			} catch (IntrospectionException ie) {
				log.error("IntrospectionException", ie);
			}
		}
		return invocation.invoke();

	}

	private void processParameters(ObjectMappingVO objectMappingVO,
			Object actionObject) throws InstantiationException,
			IllegalAccessException, IntrospectionException,
			InvocationTargetException {

		HttpServletRequest request = ServletActionContext.getRequest();
		Map<String, Object> contextMap = ActionContext.getContext()
				.getContextMap();

		if (ActionContext.getContext().getParameters() != null) {

			try {
				ReflectionContextState.setCreatingNullObjects(contextMap, true);
				ReflectionContextState.setDenyMethodExecution(contextMap, true);
				ReflectionContextState.setReportingConversionErrors(contextMap,
						true);

				// Iterate all request property configured for Action
				for (PropertyVO propertyVO : objectMappingVO
						.getPropertyVOList()) {
					if (request.getParameter(propertyVO.getName()) != null) {
						Object requestParameterValue = request
								.getParameter(propertyVO.getName());
						System.out.println(request.getParameter(propertyVO
								.getName()));
						if (log.isDebugEnabled()) {
							log.debug("Property :" + propertyVO.getName()
									+ " ; Value :" + requestParameterValue);
						}

						// Set request parameter values in multiple objects
						for (PropertyNameVO propertyNameVO : propertyVO
								.getObjectProperty()) {

							ActionContext.getContext().getValueStack()
									.setValue(propertyNameVO.getName(),
											requestParameterValue);

							if (log.isDebugEnabled()) {
								log.debug("Property :" + propertyVO.getName()
										+ "; set " + propertyNameVO.getName());
							}
						}
					}
				}

			} finally {
				ReflectionContextState
						.setCreatingNullObjects(contextMap, false);
				ReflectionContextState
						.setDenyMethodExecution(contextMap, false);
				ReflectionContextState.setReportingConversionErrors(contextMap,
						false);
			}
		}

	}
}