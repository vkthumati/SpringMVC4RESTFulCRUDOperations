package com.ws.crud.operations.util;

/**
 * A collection of constants that are used by many services.
 * <p>
 *     NOTE: This is NOT a dumping ground for all your service's constants.  If a constant is used by only one or two services, please give a moment to think if there is a better place for the constant to reside before just shoving it into this file.  In many cases, you will find that a constant belongs in a different shared library, or that the constant should not be shared in the first place.
 * </p>
 * <p>
 * If you have a constant, you need to consider the following:
 * <ol>
 *     <li>Should it reside in the class it is related to?</li>
 *     <li>Should it live in a constants class in the library/service it is used with?</li>
 *     <li>Are you really, really, <i>really</i> sure that it is globally common?</li>
 * </ol>
 * </p>
 */
public interface ServiceConstants {
	public static final String REQUEST_HEADER_NS_KEY = "requestHeaderNamespace";
	
    public static final String REQUEST_HEADER = "requestHeader";
    
    public static final String SAML_HEADER = "Assertion";
    
    public static final String SAML_NAMESPACE = "urn:oasis:names:tc:SAML:1.0:assertion";

    public static final String COMMON_NAMESPACE_URI_BASE = "http://xml.comcast.com/";

    public static final String WS_ADDRESSING_NAMESPACE = "http://www.w3.org/2005/08/addressing";
    
    public static final String WS_ADDRESSING_PREFIX = "wsa";
    
    public static final String RESPONSE_HEADER = "responseHeader";

    public static final String TIMESTAMP = "timestamp";

    public static final String SOURCE_SERVER_ID = "sourceServerId";

    public static final String SOURCE_SYSTEM_ID = "sourceSystemId";

    public static final String TRACKING_ID = "trackingId";
        
    public static final String USER_ID = "sourceSystemUserId";

    public static final String WS_USER_ID = "wsSecurityUser";
    
    public static final String WS_CREDENTIAL = "wsSecurityCredential";
    
    public static final String WS_USER_GROUPS = "wsUserGroups";
    
    public static final String TEXT_CREDENTIAL_TYPE = "TEXT";
    
    public static final String DIGEST_CREDENTIAL_TYPE = "DIGEST";
    
    public static final String WS_CREDENTIAL_TYPE = "wsSecurityCredentialType";
    
    public static final String NAMESPACE = "namespace";
    
    public static final String SERVICE_VERSION = "serviceVersion";
    
    public static final String CSG_ENVIRONMENT = "csgEnvironment";
    
    public static final String AWD_ENVIRONMENT = "awdEnvironment";
    
    public static final String USE_NON_REALTIME_DATA = "useNonRealTimeData";
    
    public static final String MESSAGE_ID = "messageId";
    
    public static final String SOAP_ATTACHMENTS = "soapAttachments";
    
    public static final String CSG_CONNECTION_METHOD = "csgConnectionMethod";
    public static final String CSG_CONNECTION_METHOD_HTTP = "HTTP";
    public static final String CSG_CONNECTION_METHOD_HTTPS = "HTTPS";
    public static final String CSG_CONNECTION_METHOD_EIP = "EIP";
    
    public static final String DISABLE_CACHING = "disableCaching";
    
    public static final String TRANSACTION_AUDIT_RECORD = "transactionAuditRecord";
    
    public static final String BYPASS_CACHE_DB_ENABLED_FLAG = "BYPASS_CACHE_DB_ENABLED_FLAG";
    public static final String BYPASS_CACHE_CALL_PER_REQUEST_FLAG = "BYPASS_CACHE_PER_REQUEST_FLAG";
    public static final String BYPASS_DB_CALL_PER_REQUEST_FLAG = "BYPASS_DB_PER_REQUEST_FLAG";
       
    public static final String TAS_LOGGING_LEVEL = "tasLoggingLevel";
    public static final String TAS_LOGGED = "tasLogTransaction";
    public static final String SERVICE_OPERATION = "serviceOperation";
    public static final String SERVER_NODE = "serverNode";
    public static final String SERVICE = "service";
    public static final String MANAGED_NODE = "managedNode";
    public static final String START_TIME = "startTime";
    public static final String SOAP_FAULT_EXCEPTION = "soapFaultException";
    public static final String LOGGING_IN_THRESHOLD = "loggingInThreshold";    
    public static final String ALT_LOGGING_IN_THRESHOLD = "altLoggingInThreshold";    
    public static final String TXN_KEY = "transactionKey";
    public static final String MARKET = "market";
    public static final String BILLING_SYSTEM = "billingSystem";
    public static final String ASPECTS = "aspects";
    public static final String PERCENTAGE_LOGGING = "percentageLogging";
    public static final String ALT_PERCENTAGE_LOGGING = "altPercentageLogging";
    public static final String REQUEST_SIZE = "requestSize";
    public static final String RESPONSE_SIZE= "responseSize";
    public static final String BACKEND_TRACE_INFO= "backendTraceInfo";
    
	public static final String CORE_ERROR_1 = "CORE0001";
	public static final String CORE_ERROR_1_MSG = "RequestHeader is required";
	public static final String CORE_ERROR_2 = "CORE0002";
	public static final String CORE_ERROR_2_MSG = "SourceServerId is required in RequestHeader";
	public static final String CORE_ERROR_3 = "CORE0003";
	public static final String CORE_ERROR_3_MSG = "SourceSystemId is required in RequestHeader";
	public static final String CORE_ERROR_4 = "CORE0004";
	public static final String CORE_ERROR_4_MSG = "Timestamp is required in RequestHeader";
	public static final String CORE_ERROR_5 = "CORE0005";
	public static final String CORE_ERROR_5_MSG = "TrackingId is required in RequestHeader";
	public static final String CORE_ERROR_6 = "CORE0006";
	public static final String CORE_ERROR_6_MSG = "RequestHeader namespace is invalid: ";
	
	// WS Addressing Fields
	public static final String WSA_REPLY_TO = "ReplyTo";
	public static final String WSA_ACTION = "Action";
	public static final String WSA_MESSAGE_ID = "MessageID";
	public static final String WSA_RELATES_TO = "RelatesTo";
	public static final String WSA_ADDRESS = "Address";
	public static final String WSA_FROM = "From";
	public static final String WSA_FAULT_TO = "FaultTo";
	public static final String ADD_WSA_HEADER = "AddWsaHeader";
	
	// BETA Router Fields
	public static final String ENVIRONMENT_ID = "environmentId";
	public static final String SESSION_ID = "sessionId";
	
	// Biller API Name (Used in conjunction with disableCaching
	public static final String BILLER_API_NAME = "billerAPIName";
	
	// Original Biller API Name (Used in setting the correct response header)
	public static final String ORIGINAL_BILLER_API_NAME = "originalBillerAPIName";
	
	//CUSTOM_OPERATOR CORP 
	public static final String CUSTOM_OPR_LEGACY_MARKET_ID = "customOprLegacyMarketId";
	
	public static final String CACHE_DIRTY = "cacheDirty";

	public static final String RETURN_AFTER_DIRTY_MARKER_CHECK = "returnAfterDirtyMarkerCheck";
	
	//BILLING_DATA_SOURCE, header name and possible values
	public static final String BILLING_DATA_SOURCE = "billingDataSource";
	public static final String BILLING_DATA_SOURCE_CACHE = "cache";
	public static final String BILLING_DATA_SOURCE_EDDP = "eddp";
	public static final String BILLING_DATA_SOURCE_EDG = "edg";
	public static final String BILLING_DATA_SOURCE_DDP = "ddp";
	public static final String BILLING_DATA_SOURCE_CSG = "csg";
	
	//Head values from Msg
	public static final String MSG_HEAD_CLIENT  = "client";
	public static final String MSG_HEAD_ROUTINGAREA  = "routingArea";
	public static final String MSG_HEAD_REGION  = "region";
	public static final String MSG_HEAD_SOURCE  = "source";
	public static final String MSG_HEAD_DESTINATION  = "destination";
	public static final String MSG_HEAD_ORIGIN  = "origin";
	public static final String MSG_HEAD_VERSION  = "version";
	public static final String MSG_HEAD_ROUNDTRIP  = "roundTrip";
	public static final String MSG_HEAD_USERID  = "userId";
	public static final String MSG_HEAD_OPERATOR  = "operator";
	public static final String MSG_HEAD_DOWNSTREAMNOTIFICATION  = "downstreamNotification";
	public static final String MSG_HEAD_CONVERSATIONID  = "conversationId";
	public static final String MSG_HEAD_CLIENTTIMEOUT  = "clientTimeOut";
	public static final String MSG_HEAD_TRANSACTIONID  = "transactionId";
	public static final String MSG_HEAD_SERVERID  = "serverId";
	public static final String MSG_HEAD_TRACEKEY  = "traceKey";
	public static final String MSG_HEAD_TRIGGERDATE  = "triggerDate";
	public static final String MSG_HEAD_TRIGGERTIME  = "triggerTime";
	
	//RETURN_MSG
	public static final String CSG_RETURN_MSG  = "csgReturnMsg";

	public static final String ACCOUNT_NUMBER = "accountNumber";
	public static final String CUSTOMER_ID = "customerId";

    public static final String GLOBAL_HEADERS = "globalHeaders";
    public static final String GLOBAL_NAMESPACE = "global-";
}
