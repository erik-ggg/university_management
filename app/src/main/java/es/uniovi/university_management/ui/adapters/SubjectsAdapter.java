package es.uniovi.university_management.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.uniovi.university_management.R;
import es.uniovi.university_management.classes.Subject;

public class SubjectsAdapter extends RecyclerView.Adapter<SubjectsAdapter.MyViewHolder> {

    private List<Subject> listaAsignaturas;
    private Context context;

    public SubjectsAdapter(List<Subject> listaAsignaturas, Context context) {
        this.listaAsignaturas = listaAsignaturas;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_asignatura, parent, false);
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Subject asignatura = listaAsignaturas.get(position);
        holder.titulo.setText(asignatura.getName());
        holder.eliminar.setOnClickListener(view -> {

            listaAsignaturas.remove(position);
            SubjectsAdapter.this.notifyItemRemoved(position);

        });


    }

    @Override
    public int getItemCount() {
        return listaAsignaturas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView titulo;
        public ImageButton eliminar;

        public MyViewHolder(View view) {
            super(view);
            titulo = view.findViewById(R.id.nombre_asignatura);
            eliminar = view.findViewById(R.id.botonEliminarAsignatura);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, Subject.class);
                    i.putExtra("nombreAsignatura", titulo.getText().toString());
                    context.startActivity(i);
                }

            });


        }



        }

    }



