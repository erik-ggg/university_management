package es.uniovi.university_management.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.uniovi.university_management.R;
import es.uniovi.university_management.classes.Subject;

public class SubjectsAdapter extends RecyclerView.Adapter<SubjectsAdapter.MyViewHolder> {

    private List<Subject> listaAsignaturas;

    public SubjectsAdapter(List<Subject> listaAsignaturas) {
        this.listaAsignaturas = listaAsignaturas;
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
    }

    @Override
    public int getItemCount() {
        return listaAsignaturas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView titulo;

        public MyViewHolder(View view) {
            super(view);
            titulo = view.findViewById(R.id.nombre_asignatura);


        }

    }


}
