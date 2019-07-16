package de.htw.saar.frontend.service;

import com.google.gson.JsonArray;
import de.htw.saar.frontend.model.Categorie;
import de.htw.saar.frontend.model.User;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.primefaces.json.JSONArray;
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
     * Gibt die Kategorien des Benutzers zurück
     * @param token
     * @return
     * @throws Exception
     */
    public ArrayList<Categorie> getUserCategories(String token) throws Exception
    {
        JSONArray result = sendRequestGetArray("categories",null,"eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjhkYWM0NDlhYzUxNzkzNWExMTYwZTQ2NzQ1MGM5NGQzNGQ4NzJkZjE4MzgwN2U0ZTQzNDEzZjY0MzkzNzU2ZDg1MGY2Yjk5ZTZlZTlhNzZjIn0.eyJhdWQiOiIxIiwianRpIjoiOGRhYzQ0OWFjNTE3OTM1YTExNjBlNDY3NDUwYzk0ZDM0ZDg3MmRmMTgzODA3ZTRlNDM0MTNmNjQzOTM3NTZkODUwZjZiOTllNmVlOWE3NmMiLCJpYXQiOjE1NjI3NDg5NDIsIm5iZiI6MTU2Mjc0ODk0MiwiZXhwIjoxNTk0MzcxMzQyLCJzdWIiOiIyIiwic2NvcGVzIjpbXX0.l_pk05dfr8WP1vCClTIKXVHfK9xZ-l1Gd8zGijAeC4mfjTLdyoek_nLetjyBOsxC8bxxKUwGksu3x6q-FLI3pOGDvFvhqiaru451uvHL90wk_gpg5Lef_8bq4XTQgAuaLj3RUIkTQxTnl7aHPLltK98IYFqK7lhoKioie9VVruQL0hO_jBvO1Tjq7Q6kwvo6RYEZOhduVo-XTY1_MnCcpnlgKo9T1IMoxLJ5Hgf4GxUMN8IyB8v3wxCCD4vWt_KLxkEFdad51-TYnAAQJgGl61hquDgikiZch3R-jvRv9fG05OX0ngjoINtJqgyEP5dYmHJ8_7Fmy8jj3GxTfv2S8R2-3UI5n8OK_2Z8n1y1qehgmzp_UN8Ru1bRBc3EZ1X-Od29LEwNiOGPJgILe3hi8M08eS16xYlQfSywko_mvno4WxJm9lv2Bb5Xv2IuNDz66sViZSkh4CaHVQsh_JK2bvwiTujWI7dXivgsD-cfD2qsikDr4xx-h-mPHfyeNU6Y6dUeUEpF1JQr2Y9yrMPPsANYKGUCG8mMX18zk5XpMK-wBwlwUtFx94Yz-8AF9nJVcPdHUGScjlSWBuaUfyQ9wtBqXvAWdOi--fbn0Es3pkBdO1IM5CwW9GwmvWESQYOLC8ENpBg34Ta52zvC3J3CBoJ0OO0MA934_0jrrIqxcog");

        ArrayList<Categorie> resultList = new ArrayList<>();

        for (int i = 0; i < result.length(); i++)
        {
            JSONObject jsonObj = result.getJSONObject(i);

            resultList.add(new Categorie(jsonObj.getInt("id"),jsonObj.getString("name")));
        }

        return resultList;
    }



    /**
     * get request to endpoint
     * @param endpoint
     * @param params
     * @param token
     * @return
     */
    private JSONArray sendRequestGetArray(String endpoint, ArrayList<BasicNameValuePair> params, String token)
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

            httpclient.close();
            return new JSONArray(json);
        } catch (Exception ex){
            return null;
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

            httpclient.close();
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
            httpclient.close();
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
