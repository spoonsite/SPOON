/*
 * Copyright 2014 Space Dynamics Laboratory - Utah State University Research Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.usu.sdl.openstorefront.common.manager;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.util.SortedProperties;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;

/**
 * Provides central access to system properties
 *
 * @author dshurtleff
 */
public class PropertiesManager
        implements Initializable {

    private static final Logger LOG = Logger.getLogger(PropertiesManager.class.getName());

    public static final String PW_PROPERTY = ".pw";

    public static final String KEY_ENABLE_WEBSOCKETS = "websockets.enabled";

    public static final String KEY_USE_REST_PROXY = "service.rest.proxy";
    public static final String KEY_DB_USE_MONGO = "db.use.mongo";
    public static final String KEY_DB_CONNECT_MIN = "db.connectionpool.min";
    public static final String KEY_DB_CONNECT_MAX = "db.connectionpool.max";
    public static final String KEY_DB_USER = "db.user";
    public static final String KEY_DB_AT = "db.pw";
    public static final String KEY_EXTERNAL_HOST_URL = "external.host.url";
    public static final String KEY_MAX_ERROR_TICKETS = "errorticket.max";
    public static final String KEY_SEARCH_SERVER = "search.server";

    //MONGO Properties
    //See http://mongodb.github.io/mongo-java-driver/3.9/driver/tutorials/connect-to-mongodb/
    //https://docs.mongodb.com/manual/reference/connection-string/
    public static final String KEY_MONGO_CONNECTION_URL = "mongo.connection.url";
    public static final String KEY_MONGO_DATABASE = "mongo.database";

    public static final String KEY_ELASTIC_HOST = "elastic.server.host";
    public static final String KEY_ELASTIC_PORT = "elastic.server.port";
    public static final String KEY_ELASTIC_SEARCH_POOL = "elastic.connectionpool.size";
    public static final String KEY_ELASTIC_CONNECTION_WAIT_TIME = "elastic.connection.wait.seconds";

    public static final String HELP_URL = "help.url";

    public static final String KEY_MAX_AGE_TRACKING_RECORDS = "trackingrecords.max.age.days";
    public static final String KEY_EXTERNAL_USER_MANAGER = "external.usermanager";
    public static final String KEY_EXTERNAL_SYNC_ACTIVATE = "external.sync.activate";
    public static final String KEY_DBLOG_MAX_RECORD = "dblog.maxrecords";
    public static final String KEY_DBLOG_ON = "dblog.on";
    public static final String KEY_DBLOG_LOG_SECURITY = "dblog.logSecurityFilter";
    public static final String KEY_FILE_HISTORY_KEEP_DAYS = "filehistory.max.days";
    public static final String KEY_NOTIFICATION_MAX_DAYS = "notification.max.days";
    public static final String TEMPORARY_MEDIA_KEEP_DAYS = "temporary.media.keep.days";
    public static final String KEY_REPORT_LIFETIME = "report.lifetime";
    public static final String KEY_TEST_EMAIL = "test.email";
    public static final String KEY_SYSTEM_ARCHIVE_MAX_PROCESSMINTUES = "system.archive.maxprocessminutes";
    public static final String KEY_MAX_POST_SIZE = "max.post.size"; // in MB

    public static final String KEY_UI_IDLETIMEOUT_MINUTES = "ui.idletimeout.minutes";
    public static final String KEY_UI_IDLETIMEGRACE_MINUTES = "ui.idlegraceperiod.minutes";

    public static final String KEY_OPENAM_URL = "openam.url";
    public static final String KEY_LOGOUT_URL = "logout.url";
    public static final String KEY_OPENAM_HEADER_USERNAME = "openam.header.username";
    public static final String KEY_OPENAM_HEADER_FIRSTNAME = "openam.header.firstname";
    public static final String KEY_OPENAM_HEADER_LASTNAME = "openam.header.lastname";
    public static final String KEY_OPENAM_HEADER_EMAIL = "openam.header.email";
    public static final String KEY_OPENAM_HEADER_PHONE = "openam.header.phone";
    public static final String KEY_OPENAM_HEADER_GROUP = "openam.header.group";
    public static final String KEY_OPENAM_HEADER_LDAPGUID = "openam.header.ldapguid";
    public static final String KEY_OPENAM_HEADER_ORGANIZATION = "openam.header.organization";
    public static final String KEY_OPENAM_HEADER_ADMIN_GROUP = "openam.header.admingroupname";

    public static final String KEY_SECURITY_DEFAULT_ADMIN_GROUP = "role.admin";

    public static final String KEY_TOOLS_USER = "tools.login.user";
    public static final String KEY_TOOLS_CREDENTIALS = "tools.login.pw";

    public static final String KEY_JOB_WORKING_STATE_OVERRIDE = "job.working.state.override.minutes";
    public static final String KEY_FEEDBACK_EMAIL = "feedback.email";

    public static final String KEY_MAIL_SERVER = "mail.smtp.url";
    public static final String KEY_MAIL_SERVER_USER = "mail.server.user";
    public static final String KEY_MAIL_SERVER_PW = "mail.server.pw";
    public static final String KEY_MAIL_SERVER_PORT = "mail.smtp.port";
    public static final String KEY_MAIL_USE_SSL = "mail.use.ssl";
    public static final String KEY_MAIL_USE_TLS = "mail.use.tls";
    public static final String KEY_MAIL_FROM_NAME = "mail.from.name";
    public static final String KEY_MAIL_FROM_ADDRESS = "mail.from.address";
    public static final String KEY_MAIL_REPLY_NAME = "mail.reply.name";
    public static final String KEY_MAIL_REPLY_ADDRESS = "mail.reply.address";
    public static final String KEY_MAIL_ATTACH_FILE = "mail.attach.file";
    public static final String KEY_MESSAGE_KEEP_DAYS = "message.archive.days";
    public static final String KEY_MESSAGE_MIN_QUEUE_MINUTES = "message.queue.minmintues";
    public static final String KEY_MESSAGE_MAX_RETRIES = "message.maxretires";
    public static final String KEY_MESSAGE_RECENT_CHANGE_DAYS = "message.recentchanges.days";

    public static final String KEY_APPLICATION_TITLE = "app.title";
    public static final String KEY_MAX_TASK_POOL_SIZE = "task.pool.size";
    public static final String KEY_MAX_TASK_COMPLETE_EXPIRE = "task.complete.expireminutes";
    public static final String KEY_MAX_TASK_ERROR_EXPIRE = "task.error.expireminutes";

    public static final String KEY_LDAP_MANAGER_URL = "ldapmanager.url";
    public static final String KEY_LDAP_MANAGER_USER_DN_TEMPLATE = "ldapmanager.userDnTemplate";
    public static final String KEY_LDAP_MANAGER_AUTHM = "ldapmanager.authenticationMechanism";
    public static final String KEY_LDAP_MANAGER_SASL_REALM = "ldapmanager.security.sasl.realm";
    public static final String KEY_LDAP_MANAGER_BIND_DN = "ldapmanager.binddn";
    public static final String KEY_LDAP_MANAGER_CREDENTIALS = "ldapmanager.pw";
    public static final String KEY_LDAP_MANAGER_CONNECT_TIMEOUT = "ldapmanager.connectionTimeout";
    public static final String KEY_LDAP_MANAGER_CONTEXT_ROOT = "ldapmanager.contextRoot";
    public static final String KEY_LDAP_MANAGER_ATTRIB_USERNAME = "ldapmanager.attribute.username";
    public static final String KEY_LDAP_MANAGER_ATTRIB_EMAIL = "ldapmanager.attribute.email";
    public static final String KEY_LDAP_MANAGER_ATTRIB_PHONE = "ldapmanager.attribute.phone";
    public static final String KEY_LDAP_MANAGER_ATTRIB_FULLNAME = "ldapmanager.attribute.fullname";
    public static final String KEY_LDAP_MANAGER_ATTRIB_ORGANIZATION = "ldapmanager.attribute.organization";
    public static final String KEY_LDAP_MANAGER_ATTRIB_GUID = "ldapmanager.attribute.guid";

    public static final String KEY_USER_REVIEW_AUTO_APPROVE = "userreview.autoapprove";

    public static final String REPORT_HISTORY_DAYS_TO_LIVE = "180";

    public static final String KEY_NODE_NAME = "node.name";
    public static final String DEFAULT_NODE_NAME = "PrimaryNode";

    private AtomicBoolean started = new AtomicBoolean(false);
    private SortedProperties properties;
    protected FileSystemManager fileSystemManager;
    private String propertiesFile = "openstorefront.properties";
    private String versionFile = "/filter/version.properties";

    private SortedProperties defaults = new SortedProperties();
    private static final ReentrantLock LOCK = new ReentrantLock();

    protected static PropertiesManager singleton = null;

    public static PropertiesManager getInstance() {
        if (singleton == null) {
            singleton = new PropertiesManager(FileSystemManager.getInstance());
        }
        return singleton;
    }

    public static PropertiesManager getInstance(FileSystemManager fileSystemManager) {
        if (singleton == null) {
            singleton = new PropertiesManager(fileSystemManager);
        }
        singleton.setFileSystemManager(fileSystemManager);
        return singleton;
    }

    protected PropertiesManager(FileSystemManager fileSystemManager) {
        this.fileSystemManager = fileSystemManager;
    }

    private String getDefault(String key) {
        return defaults.getProperty(key);
    }

    private String getDefault(String key, String defaultValue) {
        return defaults.getProperty(key, defaultValue);
    }

    public String getApplicationVersion() {
        String key = "app.version";
        return getValue(key);
    }

    public String getModuleVersion() {
        loadVersionProperties();

        String key = "app.module.version";
        String moduleVersion = properties.getProperty(key);

        //Make sure it's a valid osgi module version (only likes 2.0.2 )
        StringBuilder version = new StringBuilder();
        for (int c = 0; c < moduleVersion.length(); c++) {
            if (StringUtils.isNumeric("" + moduleVersion.charAt(c))
                    || moduleVersion.charAt(c) == '.') {
                version.append(moduleVersion.charAt(c));
            }
        }
        return version.toString();
    }

    public String getValueDefinedDefault(String key) {
        return getProperties().getProperty(key, getDefault(key));
    }

    public String getValueDefinedDefault(String key, String defaultValue) {
        return getProperties().getProperty(key, getDefault(key, defaultValue));
    }

    public String getValue(String key) {
        return getProperties().getProperty(key);
    }

    public void removeProperty(String key) {
        Object valueRemoved = getProperties().remove(key);
        if (valueRemoved != null) {
            LOG.log(Level.INFO, MessageFormat.format("Property removed: {0}", key));
        }
        saveProperties();
    }

    /**
     * Note: this will trim the value....the extra spaces can cause issues
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public String getValue(String key, String defaultValue) {
        String value = getProperties().getProperty(key, defaultValue);
        if (value != null) {
            value = value.trim();
        }
        return value;
    }

    public void setProperty(String key, String value) {
        getProperties().setProperty(key, value);
        saveProperties();
    }

    public Map<String, String> getAllProperties() {
        Map<String, String> propertyMap = new HashMap<>();
        for (String key : getProperties().stringPropertyNames()) {
            propertyMap.put(key, getProperties().getProperty(key));
        }
        return propertyMap;
    }

    private Properties getProperties() {
        if (properties == null) {
            initialize();
        }
        return properties;
    }

    private void loadVersionProperties() {
        try (InputStream in = fileSystemManager.getApplicationResourceFile(getVersionFile())) {
            Properties versionProperties = new Properties();
            versionProperties.load(in);
            if (properties == null) {
                properties = new SortedProperties();
            }
            properties.putAll(versionProperties);
        } catch (IOException e) {
            throw new OpenStorefrontRuntimeException(e);
        }
    }

    private void saveProperties() {
        LOCK.lock();
        String propertiesFilename = fileSystemManager.getConfig(getPropertiesFile()).getPath();
        try (BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(propertiesFilename))) {
            properties.store(bout, "Open Storefront Properties");
        } catch (IOException e) {
            throw new OpenStorefrontRuntimeException(e);
        } finally {
            LOCK.unlock();
        }
    }

    /**
     * This just return the localhost information (It may not represent the
     * external id)
     *
     * @return
     */
    public String getNodeName() {
        String nodeName = getValue(PropertiesManager.KEY_NODE_NAME, DEFAULT_NODE_NAME);
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            nodeName = nodeName + "-" + inetAddress.toString();
        } catch (UnknownHostException ex) {
            LOG.log(Level.WARNING, "Unable to get information on localhost.  Node name may not be unique. This may not be an issue if there is only one node.");
            LOG.log(Level.FINER, "Unable to get local node information", ex);
        }
        return nodeName;
    }

    @Override
    public void initialize() {
        LOCK.lock();
        try {
            //Set defaults
            defaults.put(KEY_NOTIFICATION_MAX_DAYS, "7");
            defaults.put(KEY_FILE_HISTORY_KEEP_DAYS, "180");
            defaults.put(KEY_MAX_ERROR_TICKETS, "5000");
            defaults.put(KEY_JOB_WORKING_STATE_OVERRIDE, "30");
            defaults.put(KEY_EXTERNAL_HOST_URL, "http://localhost:8080/openstorefront");
            defaults.put(KEY_DBLOG_MAX_RECORD, "50000");
            defaults.put(KEY_DBLOG_ON, "false");
            defaults.put(TEMPORARY_MEDIA_KEEP_DAYS, "1");
            defaults.put(KEY_SYSTEM_ARCHIVE_MAX_PROCESSMINTUES, "60");
            defaults.put(KEY_REPORT_LIFETIME, REPORT_HISTORY_DAYS_TO_LIVE);
            defaults.put(KEY_MAIL_ATTACH_FILE, Boolean.FALSE);
            defaults.put(KEY_MAX_POST_SIZE, "1024"); // 1GB
            defaults.put(KEY_MONGO_CONNECTION_URL, "mongodb://localhost:27017");

            String mongoSystemVar = System.getenv("MONGO_URL");
            if(mongoSystemVar != null){
                defaults.put(KEY_MONGO_CONNECTION_URL, mongoSystemVar);
                LOG.log(Level.INFO, "Updated Mongo connection url to: " + mongoSystemVar);
            }

            String propertiesFilename;
            try {
                propertiesFilename = fileSystemManager.getConfig(getPropertiesFile()).getPath();
            } catch (OpenStorefrontRuntimeException ex) {
                LOG.log(Level.SEVERE, ex.getMessage());
                LOG.log(Level.SEVERE, "Could not get or copy existing config file path. Using {0} instead", getPropertiesFile());
                propertiesFilename = getPropertiesFile();
            }

            if (Paths.get(propertiesFilename).toFile().createNewFile()) {
                LOG.log(Level.WARNING, "Open Storefront properties file was missing from location a new file was created.  Location: {0}", propertiesFilename);
            }
            try (BufferedInputStream bin = new BufferedInputStream(new FileInputStream(propertiesFilename))) {
                properties = new SortedProperties();
                properties.load(bin);
            } catch (IOException e) {
                throw new OpenStorefrontRuntimeException(e);
            }

            loadVersionProperties();

        } catch (IOException e) {
            throw new OpenStorefrontRuntimeException(e);
        } finally {
            LOCK.unlock();
        }

        started.set(true);
    }

    @Override
    public void shutdown() {
        started.set(false);
    }

    @Override
    public boolean isStarted() {
        return started.get();
    }

    public void setFileSystemManager(FileSystemManager fileSystemManager) {
        this.fileSystemManager = fileSystemManager;
    }

    public String getPropertiesFile() {
        return propertiesFile;
    }

    public void setPropertiesFile(String propertiesFile) {
        this.propertiesFile = propertiesFile;
    }

    public String getVersionFile() {
        return versionFile;
    }

    public void setVersionFile(String versionFile) {
        this.versionFile = versionFile;
    }

}
