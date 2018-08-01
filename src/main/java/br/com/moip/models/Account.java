package br.com.moip.models;

import br.com.moip.api.request.RequestMaker;
import br.com.moip.api.request.RequestProperties;
import br.com.moip.api.request.RequestPropertiesBuilder;
import org.apache.http.entity.ContentType;

import java.util.Map;

public class Account {

    private static final String ENDPOINT = "/v2/accounts";
    private static final ContentType CONTENT_TYPE = ContentType.APPLICATION_JSON;
    private RequestMaker requestMaker;

    /**
     * This method is used to validate the argument of bellow method, if probably it's a tax document or not.
     *
     * @param   argument
     *          {@code String} the received argument.
     *
     * @return  {@code boolean}
     */
    private boolean isTaxDocument(String argument) {
        try {
            Integer.parseInt(argument.substring(0,1));
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    /**
     * This method allows you to check if a person already has a Moip Account, by it's tax document or e-mail.
     * The tax document must be write with punctuation, for example: 123.456.789-00.
     *
     * @param   argument
     *          {@code String} the person's tax document or e-mail.
     *
     * @param   setup
     *          {@code Setup} the setup object.
     *
     * @return  {@code Map<String, Object>}
     */
    public Map<String, Object> checkExistence(String argument, Setup setup) {
        this.requestMaker = new RequestMaker(setup);

        if (isTaxDocument(argument)) {
            RequestProperties props = new RequestPropertiesBuilder()
                    .method("GET")
                    .endpoint(String.format("%s/exists?tax_document=%s", ENDPOINT, argument))
                    .type(Account.class)
                    .contentType(CONTENT_TYPE)
                    .build();

            return requestMaker.doRequest(props);
        }

        else {
            RequestProperties props = new RequestPropertiesBuilder()
                    .method("GET")
                    .endpoint(String.format("%s/exists?email=%s", ENDPOINT, argument))
                    .type(Account.class)
                    .contentType(CONTENT_TYPE)
                    .build();

            return requestMaker.doRequest(props);
        }
    }

    /**
     * This method allows you to create a Moip Account (classical or transparent). To differentiate the
     * two account types you have to set the boolean attribute {@code transparentAccount}, <b>true</b> value
     * (you will create a transparent account) or <b>false</b> value (you will create a classical account).
     *
     * @param   body
     *          {@code Map<String, Object>} the request body.
     *
     * @param   setup
     *          {@code Setup} the setup object.
     *
     * @return  {@code Map<String, Object>}
     */
    public Map<String, Object> create(Map<String, Object> body, Setup setup) {
        this.requestMaker = new RequestMaker(setup);
        RequestProperties props = new RequestPropertiesBuilder()
                .method("POST")
                .endpoint(ENDPOINT)
                .body(body)
                .type(Account.class)
                .contentType(CONTENT_TYPE)
                .build();

        return requestMaker.doRequest(props);
    }

    /**
     * This method is used to get a created account by Moip Account external ID.
     *
     * @param   id
     *          {@code String} the Moip Account external ID.
     *
     * @param   setup
     *          {@code Setup} the setup object.
     *
     * @return  {@code Map<String, Object>}
     */
    public Map<String, Object> get(String id, Setup setup) {
        this.requestMaker = new RequestMaker(setup);
        RequestProperties props = new RequestPropertiesBuilder()
                .method("GET")
                .endpoint(String.format("%s/%s", ENDPOINT, id))
                .type(Account.class)
                .contentType(CONTENT_TYPE)
                .build();

        return requestMaker.doRequest(props);
    }
}
