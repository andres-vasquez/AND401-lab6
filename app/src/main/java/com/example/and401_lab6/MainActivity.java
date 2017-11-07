package com.example.and401_lab6;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    private static final String LOG = MainActivity.class.getSimpleName();

    //View variables
    private EditText mTextEditText;
    private TextView mResultsEditText;
    private Button mInternaButton;
    private Button mExternaButton;
    private Button mPrefsButton;

    //Menu variables
    private static final int ID_MENU_1 = 1;
    private static final int ID_MENU_2 = 2;
    private static final int ID_MENU_3 = 3;
    private static final int ID_MENU_4 = 4;

    //Code for get the Callback in Runtime permisson check
    private static final int WRITE_PERMISSIOMN_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTextEditText = (EditText) findViewById(R.id.texto_edit_text);
        mResultsEditText = (TextView) findViewById(R.id.resultados_text_view);
        mInternaButton = (Button) findViewById(R.id.interno_button);
        mExternaButton = (Button) findViewById(R.id.externo_button);
        mPrefsButton = (Button)findViewById(R.id.prefs_button);

        mInternaButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String textValue = mTextEditText.getText().toString();
                try {
                    OutputStreamWriter out = new OutputStreamWriter(openFileOutput("tekhne.txt",Context.MODE_PRIVATE));
                    out.write(textValue);
                    out.close();

                    Toast.makeText(getApplicationContext(),"Guardado",Toast.LENGTH_SHORT).show();
                } catch (Exception ex) {
                    Log.e(LOG, "Error saving the text into Internal Storage");
                }
            }
        });


        mExternaButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String externalMemoryStatus = Environment.getExternalStorageState();
                boolean isMemoryReady=false;

                if (externalMemoryStatus.equals(Environment.MEDIA_MOUNTED))
                {
                    isMemoryReady=true;
                    Toast.makeText(getApplicationContext(), "External Storage ready!", Toast.LENGTH_SHORT).show();
                }
                else if (externalMemoryStatus.equals(Environment.MEDIA_MOUNTED_READ_ONLY))
                {
                    Toast.makeText(getApplicationContext(), "External Storage in Read Only Mode", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "External Storage unavailable", Toast.LENGTH_SHORT).show();
                }

                if(isMemoryReady)
                {
                    //Revision de permisos
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {

                        //Solicitar el permiso de escritura
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                WRITE_PERMISSIOMN_CODE);
                    } else {
                        //Continuar guardando el resultado
                        saveInExternalStorage();
                    }
                }
            }
        });

        mPrefsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textValue = mTextEditText.getText().toString();
                SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("Brayan",textValue);
                editor.apply();
            }
        });
    }

    /**
     * Save the text in External Storage
     */
    private void saveInExternalStorage(){
        String textValue= mTextEditText.getText().toString();

        try
        {
            File path = new File(Environment.getExternalStorageDirectory(),"Nilton.txt");
            OutputStreamWriter out = new OutputStreamWriter(
                    new FileOutputStream(path)
            );
            out.write(textValue);
            out.close();
        }
        catch (Exception ex)
        {
            Log.e(LOG, "Error saving the text into External Storage");
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, ID_MENU_1, Menu.NONE, "Interna")
                .setIcon(android.R.drawable.ic_menu_add);
        menu.add(Menu.NONE, ID_MENU_2, Menu.NONE, "Externa")
                .setIcon(android.R.drawable.stat_notify_sdcard_prepare);
        menu.add(Menu.NONE, ID_MENU_3, Menu.NONE, "Recursos")
                .setIcon(android.R.drawable.ic_dialog_info);
        menu.add(Menu.NONE, ID_MENU_4, Menu.NONE, "Preferences")
                .setIcon(android.R.drawable.ic_dialog_info);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case ID_MENU_1:
                try
                {
                    BufferedReader arhivo = new BufferedReader(
                            new InputStreamReader(openFileInput("tekhne.txt")));
                    String texto = arhivo.readLine();
                    arhivo.close();

                    mResultsEditText.setText(texto);
                }
                catch (Exception ex)
                {
                    Log.e(LOG, "Error reading the text from Internal Storage");
                }
                return true;
            case ID_MENU_2:
                try
                {
                    File path = new File(Environment.getExternalStorageDirectory(),"Nilton.txt");

                    BufferedReader arhivo = new BufferedReader(
                            new InputStreamReader(new FileInputStream(path)));
                    String texto = arhivo.readLine();
                    arhivo.close();

                    mResultsEditText.setText(texto);
                }
                catch (Exception ex)
                {
                    Log.e(LOG, "Error reading the text from External Storage");
                }
                return true;

            case ID_MENU_3:
                try
                {
                    InputStream ruta = getResources().openRawResource(R.raw.prueba_csv);
                    BufferedReader arhivo = new BufferedReader(
                            new InputStreamReader(ruta));

                    mResultsEditText.setText("");
                    String fila ="";
                    while ((fila=arhivo.readLine())!=null){
                        mResultsEditText.append(fila+"\n");
                    }
                    arhivo.close();
                }
                catch (Exception ex)
                {
                    Log.e(LOG, "Error reading the text from RAW resource");
                }
                return true;
            case ID_MENU_4:
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    String valor = prefs.getString("Brayan","");
                mResultsEditText.setText(valor);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Get the RUN TIME permission check result
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            //If the requestCode is the same of the request action
            case WRITE_PERMISSIOMN_CODE: {
                //If the permission is granted the grantResults array is not empty
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //Once permission granted continue saving the data
                    saveInExternalStorage();
                } else {
                    Toast.makeText(getApplicationContext(),"Permission denied",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
