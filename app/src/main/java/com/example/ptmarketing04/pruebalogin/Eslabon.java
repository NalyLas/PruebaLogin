package com.example.ptmarketing04.pruebalogin;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Eslabon extends AppCompatActivity {

    EditText etname, etemail, etpass;
    ArrayList<String> usuarios;
    private String url_consulta = "http://iesayala.ddns.net/natalia/php.php";
    private String url_subida = "http://iesayala.ddns.net/natalia/prueba.php";
    private JSONArray jSONArray;
    protected JSONObject jsonObject;
    private DevuelveJSON devuelveJSON;
    private User user;
    private ArrayList<User> arrayUsuarios;
    ArrayList<HashMap<String, String>> userList;

    protected String uname,uemail,upass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eslabon);

        etname = (EditText)findViewById(R.id.etLogName);
        etpass = (EditText)findViewById(R.id.etLogPass);
        etemail = (EditText)findViewById(R.id.etLogEmail);

        url_consulta = "http://iesayala.ddns.net/natalia/php.php";
        url_subida = "http://iesayala.ddns.net/natalia/prueba.php";

        devuelveJSON = new DevuelveJSON();
        new ComprobarLoguin().execute();

    }

    public void  insertarRegistro(View view){
        boolean exist = false;
        if(!etname.getText().equals("") && !etemail.getText().equals("") && !etpass.getText().equals("")){
            uname = etname.getText().toString();
            uemail = etemail.getText().toString();
            upass = etpass.getText().toString();

            for(int i=0;i<arrayUsuarios.size();i++){
                if(uemail.equals(arrayUsuarios.get(i).getEmail())){
                    Toast.makeText(Eslabon.this, "Existe un usuario con ese email",
                            Toast.LENGTH_LONG).show();
                    exist = true;
                    break;
                }
            }

            if(!exist){
                new RegistroTask().execute();
            }
        }else{
            Toast.makeText(Eslabon.this, "Rellena todos los campos",
                    Toast.LENGTH_LONG).show();
        }

    }



    ///////Task para comprobar conexcion de usuario
    class ComprobarLoguin extends AsyncTask<String, String, JSONArray> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(Eslabon.this);
            pDialog.setMessage("Cargando...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            Log.e("pre carga: ", url_consulta);
            pDialog.show();
        }

        @Override
        protected JSONArray doInBackground(String... args) {

            try {
                HashMap<String, String> parametrosPost = new HashMap<>();
                parametrosPost.put("ins_sql", "Select * from Usuarios");

                Log.e("carga: ", parametrosPost.get("ins_sql"));
                jSONArray = devuelveJSON.sendRequest(url_consulta, parametrosPost);

                if (jSONArray != null) {
                    return jSONArray;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(JSONArray json) {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
            if (json != null) {
                arrayUsuarios =new ArrayList<User>();
                for (int i = 0; i < json.length(); i++) {
                    try {
                        JSONObject jsonObject = json.getJSONObject(i);
                        user = new User();
                        user.setId(jsonObject.getInt("ID_user"));
                        user.setName(jsonObject.getString("Name"));
                        user.setEmail(jsonObject.getString("Email"));
                        user.setPass(jsonObject.getString("Password"));
                        arrayUsuarios.add(user);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            } else {
                Toast.makeText(Eslabon.this, "JSON Array nulo",
                        Toast.LENGTH_LONG).show();
            }

        }


    }


    ///////Task para registrar un nuevo usuario
    class RegistroTask extends AsyncTask<String, String, JSONArray> {
        private ProgressDialog pDialog;
        int add;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(Eslabon.this);
            pDialog.setMessage("Cargando...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONArray doInBackground(String... args) {
            try {
                HashMap<String, String> parametrosPost = new HashMap<>();
                parametrosPost.put("ins_sql",  "INSERT INTO `Usuarios`(`ID_user`, `Name`, `Email`, `Password`) VALUES (3,'" + uname + "','" + uemail + "','" + upass + "')");

                jSONArray = devuelveJSON.sendRequest(url_subida, parametrosPost);

                Log.e("array",jSONArray.toString());
                if (jSONArray != null) {
                    return jSONArray;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(JSONArray json) {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
            if (json != null) {
                    try {
                        JSONObject jsonObject = json.getJSONObject(0);
                        add = jsonObject.getInt("added");
                        Log.e("add:----->",add+"");


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                   /*********************************
                estas añadiendo pero no consigues controlar lo que devuelve
                    prueba a hacer un sendrequest como el de la app de megasur
                    que recoja el objeto json en lugar de un array
                    porque lo que devuelves no se puede convertir a array
                    y cambia el task para trabajar segun eso

                ********************************/

              /*  if(add!=0){
                    Toast.makeText(Eslabon.this, "Registro guardado",
                            Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(Eslabon.this, "ha ocurrido un error",
                            Toast.LENGTH_LONG).show();
                }*/

            } else {
                Toast.makeText(Eslabon.this, "JSON Array nulo",
                        Toast.LENGTH_LONG).show();
            }

        }


    }


}
