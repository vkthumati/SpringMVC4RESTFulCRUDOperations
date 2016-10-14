package com.ws.crud.operations.util;

import static java.util.stream.Stream.of;

/**
 * An enumeration of the allowed headers, both HTTP and Comcast Global.  
 * If you need to have all services pass through a specific HTTP-spec header, add it here.
 */
public enum  AllowedHeaders {

    AUTHORIZATION("Authorization",true),
    SERVICE("service"),
    SOURCE_SYSTEM_ID("sourceSystemId"),
    SOURCE_SYSTEM_USER_ID("sourceSystemUserId"),
    SERVICE_OPERATION("serviceOperation"),
    SERVICE_VERSION("serviceVersion"),
    TIMESTAMP("timestamp"),
    SOURCE_SERVER_ID("sourceServerId"),
    TRACKING_ID("trackingId"),
    CSG_ENVIRONMENT("csgEnvironment"),
    AWD_ENVIRONMENT("awdEnvironment"),
    USE_NON_REALTIME_DATA("useNonRealTimeData"),
    CSG_CONNECTION_METHOD("csgConnectionMethod"),
    DISABLE_CACHING("disableCaching"),
    SESSION_ID("sessionId"),
    ENVIRONMENT_ID("environmentId"),
    BILLER_API_NAME("billerAPIName"),
    CUSTOM_OPR_LEGACY_MARKET_ID("customOprLegacyMarketId"),
    TRANSACTION_KEY("transactionKey"),
    MARKET("market"),
    LOGCONTROL_ALL("logcontrol-all");

    private String headerName;
    private boolean isHttp = false;

    AllowedHeaders(String headerName) {
        this.headerName = headerName;
    }

    AllowedHeaders(String headerName,boolean isHttp) {
        this.headerName = headerName;
        this.isHttp = isHttp;
    }

    public String headerName() {
        return headerName;
    }

    public static boolean isAllowed(String headerName) {
        return of(values()).anyMatch(header -> header.headerName().equalsIgnoreCase(headerName));
    }

    /**
     * @param headerName The header name for which to retrieve the AllowedHeader enum.
     * @return The AllowedHeaders enum that matches the argumented header name, or <code>null</code> if no match is found.
     */
    public static AllowedHeaders get(String headerName) {
        for(AllowedHeaders header : values()) {
            if(header.headerName().equalsIgnoreCase(headerName)) {
                return header;
            }
        }
        return null;
    }

    /**
     *
     * @return <code>true</code> if the allowed header is an HTTP specification header and not a Comcast header. Otherwise, returns <code>false</code>.
     */
    public boolean isHttpHeader() {
        return this.isHttp;
    }
}

