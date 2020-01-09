package es.uniovi.university_management.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.uniovi.university_management.R;
import es.uniovi.university_management.classes.Teacher;

public class TimetableAdapter extends RecyclerView.Adapter<TimetableAdapter.MyViewHolder> {

    private List<Teacher> listaHorarios;
    private Context context;


    public TimetableAdapter(List<Teacher> listaHorarios, Context context) {
        this.listaHorarios = listaHorarios;
        this.context = context;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_horario, parent, false);
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Teacher profesor = listaHorarios.get(position);
        holder.nombre.setText(profesor.getName());
        holder.email.setText(profesor.getEmail());


    }


    @Override
    public int getItemCount() {
        return listaHorarios.size();
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


        }


    }

}



