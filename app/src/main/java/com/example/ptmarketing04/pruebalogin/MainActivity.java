package com.example.ptmarketing04.pruebalogin;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ImageView imv1;
    EditText etName, etPass;
    TextView tvMess;
    NotificationManager notifyMgr;


    ArrayList<String> usuarios;
    private String url_consulta = "http://iesayala.ddns.net/natalia/php.php";
    private JSONArray jSONArray;
    private DevuelveJSON devuelveJSON;
    private User user;
    private ArrayList<User> arrayUsuarios;
    ArrayList<HashMap<String, String>> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imv1 = (ImageView)findViewById(R.id.imv1);
        etName = (EditText)findViewById(R.id.etName);
        etPass = (EditText)findViewById(R.id.etPass);
        tvMess = (TextView)findViewById(R.id.tvMess);

        url_consulta = "http://iesayala.ddns.net/natalia/php.php";

        devuelveJSON = new DevuelveJSON();
        new ComprobarLoguin().execute();

        notifyMgr = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

    }


    //Para obtener el ultimo registro e una tabla
    //SELECT * FROM `usuario` ORDER BY `etiqueta` DESC LIMIT 1




    public void crearNot(View view){
        switch(view.getId())
        {
            case R.id.bt1:
                notification1(
                        1,
                        R.drawable.ic_notification,
                        etName.getText().toString(),
                        etPass.getText().toString()
                );
                break;
            case R.id.bt2:
                notification2(
                        2,
                        R.drawable.ic_notification,
                        etName.getText().toString(),
                        etPass.getText().toString()
                );

                break;
            case R.id.bt3:
                comprobarUser();
                break;
            case R.id.bt4:
                Intent intent = new Intent(this, Eslabon.class);
                startActivity(intent);
                break;
            case R.id.bt5:
                break;
            case R.id.bt6:
                break;
        }

    }

    public void notification1(int id, int iconId, String titulo, String contenido) {


        NotificationCompat.Builder builder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(iconId)
                        .setLargeIcon(BitmapFactory.decodeResource(
                                getResources(),
                                R.drawable.ic_notification
                                )
                        )
                        .setContentTitle(titulo)
                        .setContentText(contenido)
                        .setColor(getResources().getColor(R.color.colorAccent));


        // Construir la notificación y emitirla

        notifyMgr.notify(id, builder.build());
    }

    public void notification2(int id, int iconId, String titulo, String contenido ) {

        // Creación del builder
        NotificationCompat.Builder builder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(iconId)
                        .setLargeIcon(BitmapFactory.decodeResource(
                                getResources(),
                                R.drawable.ic_notification
                                )
                        )
                        .setContentTitle(titulo)
                        .setContentText(contenido)
                        .setColor(getResources().getColor(R.color.colorAccent));


        // Nueva instancia del intent apuntado hacia Eslabon
        Intent intent = new Intent(this, Eslabon.class);

        // Crear pila
        TaskStackBuilder stackBuilder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            stackBuilder = TaskStackBuilder.create(this);
            // Añadir actividad padre
            stackBuilder.addParentStack(Eslabon.class);

            // Referenciar Intent para la notificación
            stackBuilder.addNextIntent(intent);

            // Obtener PendingIntent resultante de la pila
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            // Asignación del pending intent
            builder.setContentIntent(resultPendingIntent);

            // Remover notificacion al interactuar con ella
            builder.setAutoCancel(true);

        }


        // Construir la notificación y emitirla
        notifyMgr.notify(id, builder.build());
    }

    public void comprobarUser(){
        String name = etName.getText().toString();
        String pass = etPass.getText().toString();
        for(int i=0;i<arrayUsuarios.size();i++){
            if(name.equals(arrayUsuarios.get(i).getName()) && pass.equals(arrayUsuarios.get(i).getPass())){
                tvMess.setText("Usuario: "+ name + " Email: "+arrayUsuarios.get(i).getEmail());
                break;
            }else{
                tvMess.setText("No es correcto");
            }
        }

    }


    ///////Task para comprobar conexcion de usuario
    class ComprobarLoguin extends AsyncTask<String, String, JSONArray> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(MainActivity.this);
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
                Toast.makeText(MainActivity.this, "JSON Array nulo",
                        Toast.LENGTH_LONG).show();
            }

        }


    }




}
