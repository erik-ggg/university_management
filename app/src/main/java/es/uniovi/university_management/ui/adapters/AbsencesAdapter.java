package es.uniovi.university_management.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;

import es.uniovi.university_management.R;
import es.uniovi.university_management.classes.Absence;
import es.uniovi.university_management.util.DateParser;

public class AbsencesAdapter extends RecyclerView.Adapter<AbsencesAdapter.MyViewHolder> {

    private List<Absence> listaAusencias;
    private Context context;
    private Activity absencesActivity;


    public AbsencesAdapter(List<Absence> listaAusencias, Context context, Activity timeTableActivity) {
        this.listaAusencias = listaAusencias;
        this.context = context;
        this.absencesActivity = timeTableActivity;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_horario, parent, false);
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull AbsencesAdapter.MyViewHolder holder, int position) {
        Absence absence = listaAusencias.get(position);
        Date date = absence.getDate().getTime();
        DateParser parser = new DateParser();
        holder.fecha.setText(parser.dateToStringWithoutHour(date));
        if (absence.isAutomatic())
            holder.automatica.setText("Automática");
        holder.eliminar.setOnClickListener(view -> {

            confirmaBorrado(position);
        });


    }


    private void confirmaBorrado(int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(absencesActivity);

        builder.setTitle("Eliminar fecha")
                .setMessage("La fecha se eliminará definitivamente del horario, ¿está seguro?")
                .setPositiveButton("OK",
                        (dialog, which) -> {

                            listaAusencias.remove(pos);
                            AbsencesAdapter.this.notifyDataSetChanged();
                            //TODO notificar cambios a la base de datos
                        })
                .setNegativeButton("CANCELAR",
                        (dialog, which) -> dialog.cancel())
                .show();

    }


    @Override
    public int getItemCount() {
        return listaAusencias.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView fecha;
        public TextView automatica;
        public ImageButton eliminar;

        public MyViewHolder(View view) {
            super(view);
            fecha = view.findViewById(R.id.dayOfTheLesson);
            automatica = view.findViewById(R.id.hours);
            eliminar = view.findViewById(R.id.botonEliminarFecha);


        }


    }

}

