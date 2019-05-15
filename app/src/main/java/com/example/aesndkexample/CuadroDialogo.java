package com.example.aesndkexample;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class CuadroDialogo {

    public interface FinalizoCuadroDialogo{
        void ResultadoCuadroDialogo(String Ffin);
        void ResultadoCuadroDialogo2(String Ffin);
    }

    private FinalizoCuadroDialogo interfaz;
    public CuadroDialogo(Context context, FinalizoCuadroDialogo finalizoCuadroDialogo){

        interfaz = finalizoCuadroDialogo;
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.cuadro_dialogo);

        final EditText setFin = (EditText) dialog.findViewById(R.id.set_Fin);
        Button button = (Button) dialog.findViewById(R.id.button_Set);
        Button button2 = (Button) dialog.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interfaz.ResultadoCuadroDialogo(setFin.getText().toString());
                dialog.dismiss();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interfaz.ResultadoCuadroDialogo2(setFin.getText().toString());
                dialog.dismiss();
            }
        });

        dialog.show();

    }
}
