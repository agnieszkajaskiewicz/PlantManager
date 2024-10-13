package com.ajaskiewicz.PlantManager.web.utils;

import jakarta.servlet.http.HttpServletRequest;

public class WebUtil {
    public static String getSiteURL(HttpServletRequest request) {
        var siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }
}
