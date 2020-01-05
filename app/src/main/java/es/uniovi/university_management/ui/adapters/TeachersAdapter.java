package es.uniovi.university_management.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.uniovi.university_management.R;
import es.uniovi.university_management.classes.Teacher;

public class TeachersAdapter extends RecyclerView.Adapter<TeachersAdapter.MyViewHolder> {

    private List<Teacher> listaProfesores;
    private Context context;

    public TeachersAdapter(List<Teacher> listaProfesores, Context context) {
        this.listaProfesores = listaProfesores;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profesor, parent, false);
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Teacher profesor = listaProfesores.get(position);
        holder.nombre.setText(profesor.getName());
        holder.email.setText(profesor.getEmail());
        holder.eliminar.setOnClickListener(view -> {

            listaProfesores.remove(position);
            TeachersAdapter.this.notifyItemRemoved(position);

        });


    }

    @Override
    public int getItemCount() {
        return listaProfesores.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nombre;
        public TextView email;
        public ImageButton eliminar;

        public MyViewHolder(View view) {
            super(view);
            nombre = view.findViewById(R.id.nombre_profesor);
            email = view.findViewById(R.id.email_profesor);
            eliminar = view.findViewById(R.id.botonEliminarProfesor);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("message/rfc822");
                    i.putExtra(Intent.EXTRA_EMAIL, new String[]{email.getText().toString()});
                    i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
                    i.putExtra(Intent.EXTRA_TEXT, "body of email");
                    try {
                        context.startActivity(Intent.createChooser(i, "Enviar Email..."));
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(context, "No tiene clientes de correo instalados.", Toast.LENGTH_SHORT).show();

                    }
                }

            });


        }


    }

}



