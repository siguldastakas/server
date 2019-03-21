package com.siguldastakas.server.admin;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.siguldastakas.server.ContextHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ModelAndView;
import spark.Route;
import spark.Session;
import spark.TemplateViewRoute;

import javax.naming.NamingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    public static String googleApiClientId;
    static {
        try {
            googleApiClientId = ContextHelper.lookup("google-api-client-id");
        } catch (NamingException e) {
            log.error("Failed to setup AuthController!", e);
        }
    }

    public static TemplateViewRoute form = (req, res) -> new ModelAndView(Collections.singletonMap("googleApiClientId", googleApiClientId), "login.ftl");

    public static Route login = (req, res) -> {
        String token = req.queryParams("token");
        if (token != null) {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                    .setAudience(Collections.singletonList(googleApiClientId))
                    .build();
            GoogleIdToken idToken = verifier.verify(token);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                if (Boolean.TRUE.equals(payload.getEmailVerified())) {
                    String email = payload.getEmail();
                    return email;
                }
            }
        }
        return null;
    };


    public static TemplateViewRoute logout = (req, res) -> {
        Session session = req.session();
        session.invalidate();

        Map<String, Object> data = new HashMap<>();
        data.put("googleApiClientId", googleApiClientId);
        data.put("loginPath", Path.path(req, Path.LOGIN));

        return new ModelAndView(data, "logout.ftl");
    };

}
