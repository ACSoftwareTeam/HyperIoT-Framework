package it.acsoftware.hyperiot.base.test.http;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.acsoftware.hyperiot.base.api.HyperIoTUser;
import it.acsoftware.hyperiot.base.exception.HyperIoTRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Francesco Salerno
 * This is a utility class.
 */
public class HyperIoTHttpUtils {

    private final static Logger log = LoggerFactory.getLogger(HyperIoTHttpUtils.class.getName());

    private final static ObjectMapper mapper =  new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private final static String TOKEN_PROPERTY = "token";

    private final static String hyperiotAdminUsername = "hadmin";

    private final static String hyperiotAdminPassword = "admin";

    public final static String SERVICE_BASE_URL="http://localhost:8182/hyperiot";

     static String getJwtTokenAsAdmin(){
        return getJwtToken(hyperiotAdminUsername, hyperiotAdminPassword);
    }

     static String getJwtToken(HyperIoTUser user){
        return getJwtToken(user.getUsername(), user.getPassword());
    }

    /**
     * This method is for internal use.
     * @param username used to authenticate
     * @param password used to authenticate
     * @return the token used to authenticate request.
     */
     static String getJwtToken(String username, String password){
        try (final CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpUriRequest authenticationRequest = RequestBuilder
                    .post()
                    .setUri(getHyperIoTAuthenticationUri())
                    .setHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                    .setEntity(getAuthenticationFormParameter(username, password))
                    .build();
            log.debug("Executing request {} , to uri {} ",authenticationRequest.getMethod(), authenticationRequest.getURI());
            try (final CloseableHttpResponse response =  httpclient.execute(authenticationRequest)) {
                if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK){
                    throw new HyperIoTRuntimeException();
                }
                JsonNode jsonResponse = mapper.readTree(EntityUtils.toString(response.getEntity()));
                if(jsonResponse.get(TOKEN_PROPERTY) == null || String.valueOf(jsonResponse.get(TOKEN_PROPERTY)).isBlank()){
                    throw new HyperIoTRuntimeException();
                }
                return formatJwtToken(jsonResponse);
            }
        } catch (IOException e) {
            log.debug(e.getMessage(), e);
            throw new HyperIoTRuntimeException();
        }
    }

    private static UrlEncodedFormEntity getAuthenticationFormParameter(String username, String password){
        try {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("username", username));
            params.add(new BasicNameValuePair("password", password));
            return new UrlEncodedFormEntity(params);
        } catch (Exception exc){
            log.debug(exc.getMessage(), exc);
            throw new HyperIoTRuntimeException();
        }
    }

    private static String getHyperIoTAuthenticationUri(){
        return SERVICE_BASE_URL.concat("/authentication/login");
    }

    private static String formatJwtToken(JsonNode jsonResponse){
        return addJwtSchema(String.valueOf(jsonResponse.get(TOKEN_PROPERTY)));
    }

    private static String addJwtSchema(String token){
        return "JWT ".concat(token);
    }


}
