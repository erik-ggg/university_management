package es.uniovi.university_management.ui.adapters;

import android.app.Activity;
import android.app.AlertDialog;
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
import es.uniovi.university_management.classes.Test;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.MyViewHolder> {

    private List<Test> listaNotas;
    private Context context;
    private Activity subjectActivity;

    public SubjectAdapter(List<Test> listaNotas, Context context) {
        this.listaNotas = listaNotas;
        this.context = context;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nota, parent, false);
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Test nota = listaNotas.get(position);
        holder.nombre.setText(nota.getName());
        holder.mark.setText(String.valueOf(nota.getMark()));
        holder.eliminar.setOnClickListener(view -> {

            //confirmaBorrado(position);

        });


    }


    private void confirmaBorrado(int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(subjectActivity);

        builder.setTitle("Eliminar nota")
                .setMessage("La nota se eliminará definitivamente, ¿está seguro?")
                .setPositiveButton("OK",
                        (dialog, which) -> {

                            listaNotas.remove(pos);
                            SubjectAdapter.this.notifyDataSetChanged();
                        })
                .setNegativeButton("CANCELAR",
                        (dialog, which) -> dialog.cancel())
                .show();
    }


    @Override
    public int getItemCount() {

        return listaNotas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nombre;
        public TextView mark;
        public ImageButton eliminar;

        public MyViewHolder(View view) {
            super(view);
            nombre = view.findViewById(R.id.tittle_nota);
            mark = view.findViewById(R.id.value_nota);
            eliminar = view.findViewById(R.id.botonEliminarNota);

        }


    }

}



