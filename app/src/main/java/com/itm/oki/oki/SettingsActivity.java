package com.itm.oki.oki;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {
    private static final int TONE_PICKER = 999;
    private static final int WRITE_PERMISSION = 1;
    LinearLayout tono_layout, alerta_layout, calibrar_layout, camara_layout;
    TextView alerta_text, calibrar_text, camara_text, tono_text;
    TextInputEditText phone_text;
    ScrollView scroll_view;
    Ringtone ringtone;
    String ringTonePath;
    Uri ringUri;

//    Alert Types
    ArrayList<Integer> alertTypes;
    boolean[] checkedTypes;

//    Camara Types
    int selectedCamera = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Agregamos el boton de regresar al TitleBar
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        tono_layout = (LinearLayout) findViewById(R.id.tono_layout);
        ringtone = RingtoneManager.getRingtone(this, Settings.System.DEFAULT_ALARM_ALERT_URI);
        tono_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tonePicker();
            }
        });
        scroll_view = (ScrollView) findViewById(R.id.scroll_view);


        alerta_layout = (LinearLayout) findViewById(R.id.alert_layout);
        alertTypes = new ArrayList<>();
        alertTypes.add(0);
        checkedTypes = new boolean[getResources().getStringArray(R.array.tipos_alertas).length];
        checkedTypes[0] = true;
        alerta_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogChooser();
            }
        });
        alerta_text = (TextView) findViewById(R.id.alerta_text);


        calibrar_layout = (LinearLayout) findViewById(R.id.calibrar_layout);
        calibrar_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPreview();
            }
        });
        camara_layout = (LinearLayout) findViewById(R.id.camara_layout);
        camara_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraDialogChooser();
            }
        });

        phone_text = (TextInputEditText) findViewById(R.id.phone_text);
//        calibrar_text = (TextView) findViewById(R.id.calibrar_text);
        camara_text = (TextView) findViewById(R.id.camara_text);
        tono_text = (TextView) findViewById(R.id.tono_text);
        tono_text.setText(ringtone.getTitle(this));
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);

    }

    private boolean checkWriteSettings(){
        PackageManager pm = this.getPackageManager();
        int hasPerm = pm.checkPermission(
                Manifest.permission.WRITE_SETTINGS,
                getPackageName()
        );
        return (hasPerm == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_SETTINGS) != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_SETTINGS)){
                showSnackbar("Necesitamos permissos de escritura");

            } else {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.WRITE_SETTINGS},
                        WRITE_PERMISSION
                );
            }
            return;
        }
    }

    public void tonePicker(){
        boolean canWrite = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            canWrite = Settings.System.canWrite(this);
        }else {
            canWrite = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED;
        }
        if(canWrite){
            Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Seleccione el Tono:");
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,RingtoneManager.TYPE_ALARM);
            ringUri = RingtoneManager.getActualDefaultRingtoneUri(SettingsActivity.this, RingtoneManager.TYPE_ALARM);
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, ringUri);
            startActivityForResult(intent, TONE_PICKER);
        } else {
//            requestPermission();
          if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, WRITE_PERMISSION);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_SETTINGS}, WRITE_PERMISSION);
            }
        }
    }

    public void showSnackbar(String texto){
        Snackbar snackbar = Snackbar
                .make(scroll_view, texto, Snackbar.LENGTH_INDEFINITE)
                .setAction("OPCIONES", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                });
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            snackbar.setActionTextColor(getResources().getColor(R.color.colorSecondaryAccent,null));
        }
        snackbar.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            ringUri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            if(ringUri != null){
                ringTonePath = ringUri.toString();
                Log.d("RINGTAG", "onActivityResult: "+ringUri);
                RingtoneManager.setActualDefaultRingtoneUri(
                        SettingsActivity.this,
                        RingtoneManager.TYPE_ALARM,
                        ringUri
                );

                ringtone = RingtoneManager.getRingtone(this, ringUri);

                tono_text.setText(ringtone.getTitle(this));
            }
        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void alertDialogChooser(){
        final boolean[] tempCheckTypes = checkedTypes;
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle(R.string.alerta_dialog_title)
                .setMultiChoiceItems(R.array.tipos_alertas, checkedTypes, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if(isChecked){
                            tempCheckTypes[which] = true;
                            alertTypes.add(which);
                        } else if(alertTypes.contains(which)){
                            if(tempCheckTypes[which] == true)
                                tempCheckTypes[which] = false;
                            alertTypes.remove(Integer.valueOf(which));
                        }
                    }
                })
                .setPositiveButton(R.string.alerta_dialog_ok_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selectedIndex = "";
                        String[] alerts = getResources().getStringArray(R.array.tipos_alertas);
                        checkedTypes = tempCheckTypes;
                        Log.d("BUILDER", "onClick: "+checkedTypes.toString());
                        for(Integer i: alertTypes){
                            if(alertTypes.size() > 1 && alertTypes.indexOf(i) == alertTypes.size()-2){
                                selectedIndex += alerts[i] + " y ";
                            }else{
                                selectedIndex += alerts[i] + ", ";
                            }

                        }

                        selectedIndex = selectedIndex.substring(0,selectedIndex.length()-2);

                        alerta_text.setText(selectedIndex);
                    }
                })
                .setNegativeButton(R.string.alerta_dialog_cancel_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    private void openPreview(){
        Intent previewActivity = new Intent(this, GooglyEyesActivity.class);
        startActivity(previewActivity);
    }

    private void cameraDialogChooser(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle(R.string.camara_dialog_title)
                .setSingleChoiceItems(R.array.tipos_camaras, selectedCamera, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }

                })

                // Set the action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // user clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog

                        selectedCamera = ((AlertDialog)dialog).getListView().getCheckedItemPosition();

                        String[] camaras = getResources().getStringArray(R.array.tipos_camaras);
                        camara_text.setText(camaras[selectedCamera]);

                    }
                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // removes the dialog from the screen

                    }
                })
                .show();


    }
}
