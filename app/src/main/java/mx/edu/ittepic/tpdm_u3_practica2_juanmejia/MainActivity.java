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

public class MainActivity extends AppCompatActivity {
    Button cerrar;
    TextView bienvenido;
    FirebaseAuth fba;
    FirebaseAuth.AuthStateListener asl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cerrar=findViewById(R.id.cerrar);
        bienvenido=findViewById(R.id.bienvenido);
        fba=FirebaseAuth.getInstance();

        asl=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser usuario=firebaseAuth.getCurrentUser();
                if(usuario == null||!usuario.isEmailVerified()){
                    //no está autenticado
                    startActivity(new Intent(MainActivity.this,Main2Activity.class));
                    finish();
                }

            }
        };

        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fba.signOut();

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        fba.addAuthStateListener(asl);
    }

    @Override
    protected void onStop() {
        super.onStop();
        fba.removeAuthStateListener(asl);
    }
}

