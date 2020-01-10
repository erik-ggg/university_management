package es.uniovi.university_management.ui.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import es.uniovi.university_management.R;
import es.uniovi.university_management.classes.TimeSubject;

public class DatesAdapter extends RecyclerView.Adapter<DatesAdapter.MyViewHolder> {

    private TimeSubject horario;
    private Context context;


    public DatesAdapter(TimeSubject horario, Context context) {
        this.horario = horario;
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
        String dateString = horario.getStartDate().get(position);
        String hoursString = horario.getStartTime().get(position);
        Date date = parsearFecha(dateString);
        holder.day.setText(dateString);
        holder.hours.setText(hoursString);
        holder.eliminar.setOnClickListener(view -> {

            confirmaBorrado(position);
        });


    }

    private Date parsearFecha(String dateString) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH.mm");
        Date fechaHorario = null;
        try {
            fechaHorario = df.parse(dateString);
        } catch (ParseException e) {
            Log.i("Fechas", "No se ha podido parsear la fecha.");
            e.printStackTrace();
        }
        return fechaHorario;
    }


    private void confirmaBorrado(int pos) {
        /*AlertDialog.Builder builder = new AlertDialog.Builder(teachersActivity);

        builder.setTitle("Eliminar profesor")
                .setMessage("El profesor se eliminará definitivamente, ¿está seguro?")
                .setPositiveButton("OK",
                        (dialog, which) -> {

                            horario.remove(pos);
                            DatesAdapter.this.notifyItemRemoved(pos);
                        })
                .setNegativeButton("CANCELAR",
                        (dialog, which) -> dialog.cancel())
                .show();
    */
    }


    @Override
    public int getItemCount() {
        return horario.getStartDate().size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView day;
        public TextView hours;
        public ImageButton eliminar;

        public MyViewHolder(View view) {
            super(view);
            day = view.findViewById(R.id.dayOfTheLesson);
            hours = view.findViewById(R.id.hours);
            eliminar = view.findViewById(R.id.botonEliminarFecha);


        }


    }

}



