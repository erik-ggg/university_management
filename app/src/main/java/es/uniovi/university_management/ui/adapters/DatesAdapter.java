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

import es.uniovi.university_management.R;
import es.uniovi.university_management.classes.TimeSubject;
import es.uniovi.university_management.repositories.SectionTimeRepository;
import es.uniovi.university_management.util.DateParser;

public class DatesAdapter extends RecyclerView.Adapter<DatesAdapter.MyViewHolder> {

    private TimeSubject horario;
    private Context context;
    private Activity timeTableActivity;
    private String subjectName;
    private int type;


    public DatesAdapter(String subjectName, int type, TimeSubject horario, Context context, Activity timeTableActivity) {
        this.horario = horario;
        this.context = context;
        this.timeTableActivity = timeTableActivity;
        this.subjectName = subjectName;
        this.type = type;
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
        holder.day.setText(dateString);
        holder.hours.setText(hoursString);
        holder.eliminar.setOnClickListener(view -> confirmaBorrado(position));
    }



    private void confirmaBorrado(int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(timeTableActivity);
        builder.setTitle("Eliminar fecha")
                .setMessage("La fecha se eliminará definitivamente del horario, ¿está seguro?")
                .setPositiveButton("OK",
                        (dialog, which) -> {
                            // si se elimina en bd entonces se elimina aqui. Deberia ser asi
                            Date date = DateParser.stringToDate(horario.getStartDate().get(pos), horario.getStartTime().get(pos));
                            SectionTimeRepository repository = new SectionTimeRepository();
                            repository.delete(subjectName, type, date.getTime(), context);

                            horario.getStartDate().remove(pos);
                            horario.getStartTime().remove(pos);
                            DatesAdapter.this.notifyDataSetChanged();
                        })
                .setNegativeButton("CANCELAR",
                        (dialog, which) -> dialog.cancel())
                .show();
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



