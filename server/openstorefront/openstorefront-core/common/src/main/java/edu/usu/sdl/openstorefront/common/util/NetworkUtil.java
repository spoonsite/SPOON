/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.usu.sdl.openstorefront.common.util;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class NetworkUtil
{

	private NetworkUtil()
	{
	}

	/**
	 * Get the correct client ip from a request
	 *
	 * @param request
	 * @return client ip or N/A if not found
	 */
	public static String getClientIp(HttpServletRequest request)
	{
		String clientIp = OpenStorefrontConstant.NOT_AVAILABLE;
		if (request != null) {
			clientIp = request.getRemoteAddr();

			//Check for header ip it may be forwarded by a proxy
			String clientIpFromHeader = request.getHeader("x-forwarded-for");
			if (StringUtils.isNotBlank(clientIpFromHeader)) {
				clientIp = clientIp + " Forward for: " + clientIpFromHeader;
			} else {
				clientIpFromHeader = request.getHeader("x-real-ip");
				if (StringUtils.isNotBlank(clientIpFromHeader)) {
					clientIp = clientIp + " X-real IP: " + clientIpFromHeader;
				}
			}
		}
		return clientIp;
	}

}
