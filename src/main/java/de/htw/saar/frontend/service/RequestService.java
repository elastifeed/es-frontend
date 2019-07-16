package de.htw.saar.frontend.service;

import de.htw.saar.frontend.model.User;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.primefaces.json.JSONObject;

import java.util.ArrayList;

public class RequestService
{
    public static final String host = "http://localhost:8090/api/v1/";

    public String userRegister(String email, String password, String name) throws Exception
    {
        ArrayList<BasicNameValuePair> parameterlist = new ArrayList<>();
        parameterlist.add(new BasicNameValuePair("email",email));
        parameterlist.add(new BasicNameValuePair("password",password));
        parameterlist.add(new BasicNameValuePair("name",name));

        JSONObject result = sendRequestPost("register",parameterlist,null);

        if(result == null)
        {
            throw new Exception("Der Service antwortet nicht!");
        }

        if(result.has("id"))
        {
            return "success";
        }
        else
        {
            if(result.has("messages"))
            {
                throw new Exception("Die Registrierung ist fehlgeschlagen! Nutzer bereits registriert!");
            }
            else
            {
                throw new Exception("Die Registrierung ist fehlgeschlagen!");
            }
        }
    }

    public String userLogin(String email, String password) throws Exception
    {
        ArrayList<BasicNameValuePair> parameterlist = new ArrayList<>();
        parameterlist.add(new BasicNameValuePair("email",email));
        parameterlist.add(new BasicNameValuePair("password",password));

        JSONObject result = sendRequestPost("login",parameterlist,null);

        // Check Result
        if(result == null)
        {
            throw new Exception("Der Service antwortet nicht!");
        }

        if(result.has("token"))
        {
            return result.getString("token");
        }
        else
        {
            if(result.has("messages"))
            {
                throw new Exception(result.getString("messages"));
            }
            else
            {
                throw new Exception("Der login ist fehlgeschlagen!");
            }
        }

    }

    public User getUser(String token) throws Exception
    {
        JSONObject result = sendRequestGet("me", null, token);

        if(result == null)
        {
            throw new Exception("Der Service antwortet nicht!");
        }

        if(result.has("name") && result.has("email"))
        {
            return new User(token,result.getString("name"),result.getString("email"), result.getInt("id"));
        }
        else
        {
            if(result.has("messages"))
            {
                throw new Exception(result.getString("messages"));
            }
            else
            {
                throw new Exception("Die Benutzerinformationen konnten trotz erfolgreichem Login nicht vom Server abgerufen werden!");
            }
        }
    }


    /**
     * get request to endpoint
     * @param endpoint
     * @param params
     * @param token
     * @return
     */
    private JSONObject sendRequestGet(String endpoint,ArrayList<BasicNameValuePair> params, String token)
    {
        try{
            CloseableHttpClient httpclient = HttpClients.createDefault();

            HttpGet httpget = new HttpGet(getEndpointWithQuery(endpoint, params));

            if(token != null && token.length() > 1)
            {
                httpget.addHeader("Authorization", "Bearer " + token);
            }

            httpget.addHeader("Content-Type", "application/json");

            //Execute and get the response.
            HttpResponse response = httpclient.execute(httpget);
            String json =  EntityUtils.toString(response.getEntity());

            return new JSONObject(json);

        } catch (Exception ex){
            return null;
        }
    }

    /**
     * Post request to endpoint
     * @param endpoint
     * @param params
     * @param token
     * @return
     */
    private JSONObject sendRequestPost(String endpoint,ArrayList<BasicNameValuePair> params, String token)
    {
        try{
            CloseableHttpClient httpclient = HttpClients.createDefault();

            HttpPost httppost = new HttpPost(getEndpointWithQuery(endpoint, params));

            if(params != null && params.size() > 0)
            {
                httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            }

            if(token != null && token.length() > 1)
            {
                httppost.addHeader("Authorization", "Bearer " + token);
            }

            httppost.addHeader("Content-Type", "application/json");

            //Execute and get the response.
            HttpResponse response = httpclient.execute(httppost);
            String json =  EntityUtils.toString(response.getEntity());

            return new JSONObject(json);

        } catch (Exception ex){
            return null;
        }
    }

    private String getEndpointWithQuery(String endpoint, ArrayList<BasicNameValuePair> params){
        String url = host + endpoint;
        if (params != null && params.size() > 0)
        {
            url += "?";
            for(int i = 0; i < params.size(); i++)
            {
                url += params.get(i).getName() + "=" + params.get(i).getValue();

                if(params.size() > i+1)
                {
                    url += "&";
                }
            }
        }
        return url;
    }
}
