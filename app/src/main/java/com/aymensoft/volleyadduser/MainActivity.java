package com.aymensoft.volleyadduser;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView tvText;
    EditText etphone, etpass, ettocken;
    Button btnadd, btnGet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvText=(TextView)findViewById(R.id.tv_text);
        etphone=(EditText)findViewById(R.id.et_phone);
        etpass=(EditText)findViewById(R.id.et_pass);
        ettocken=(EditText)findViewById(R.id.et_tocken);
        btnadd=(Button)findViewById(R.id.btn_add);
        btnGet=(Button)findViewById(R.id.btn_get);
        AppController.getInstance().getRequestQueue().getCache().clear();
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUser();
            }
        });
        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetEvaluation();
            }
        });

    }

    public void SignUser(){
        String  tag_string_req = "signup";
        String url = "http://perfect-city.net/user_api_add";
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.app_name));
        pDialog.show();

        JSONObject params = new JSONObject();
        try {
            params.put("phoneNumber", "23245674");
            params.put("password", etpass.getText().toString());
            params.put("token", ettocken.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response", response.toString());
                         pDialog.hide();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                VolleyLog.d("error", "Error: " + error.getMessage());
                pDialog.hide();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("key", "123456");
                return headers;
            }
        };
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);
    }

    public void GetEvaluation(){
        final String tag_json_obj = "json_obj_req";

        String url = "http://perfect-city.net/get_evaluation_doctor/11";

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response", response.toString());
                        try {
                            tvText.setText(response.getString("data"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pDialog.hide();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                tvText.setText(error.getMessage());
                VolleyLog.d("error", "Error: " + error.getMessage());
                // hide the progress dialog
                pDialog.hide();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");
                headers.put("key", "123456");
                return headers;
            }
        };
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

}
