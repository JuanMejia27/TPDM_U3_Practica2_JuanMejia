package mx.edu.ittepic.tpdm_u3_practica2_juanmejia;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Main2Activity extends AppCompatActivity {

    Button entrar, registrar;
    EditText correo,contra;
    FirebaseAuth fba;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        entrar=findViewById(R.id.entrar);
        registrar=findViewById(R.id.registrar);
        correo=findViewById(R.id.correo);
        contra=findViewById(R.id.contra);

        fba=FirebaseAuth.getInstance();

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fba.createUserWithEmailAndPassword(correo.getText().toString(),
                        contra.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(Main2Activity.this,"Usuario creado",
                                            Toast.LENGTH_LONG).show();
                                    fba.getCurrentUser().sendEmailVerification();
                                }else{
                                    Toast.makeText(Main2Activity.this,"Error no se pudo crear usuario",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fba.signInWithEmailAndPassword(correo.getText().toString(),correo.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    //Si se autentico
                                    if(fba.getCurrentUser().isEmailVerified()){
                                        startActivity(new Intent(Main2Activity.this,MainActivity.class));
                                    }else{
                                        mensajeVerificar(fba.getCurrentUser());
                                    }
                                }else{
                                    Toast.makeText(Main2Activity.this,
                                            "Error verifique el usuario y/o contraseña",
                                            Toast.LENGTH_LONG).show();
                                }

                            }
                        });
            }
        });
    }
    private void mensajeVerificar(final FirebaseUser usuarioLogueado){
        AlertDialog.Builder alerta=new AlertDialog.Builder(this);
        alerta.setTitle("ERROR!")
                .setMessage("Al parecer aun no has verificado tu usuario\n ¿Deseas que el correo de verificación se envie nuevamente")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        usuarioLogueado.sendEmailVerification();
                        Toast.makeText(Main2Activity.this,"Correo de verificación reenviado ", Toast.LENGTH_LONG)
                                .show();
                        fba.signOut();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fba.signOut();
            }
        }).show();
    }
}