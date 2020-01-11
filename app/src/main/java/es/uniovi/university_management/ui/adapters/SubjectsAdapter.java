package es.uniovi.university_management.ui.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import es.uniovi.university_management.R;
import es.uniovi.university_management.classes.Subject;
import es.uniovi.university_management.database.AppDatabase;
import es.uniovi.university_management.model.SubjectEntity;
import es.uniovi.university_management.ui.SubjectActivity;
import es.uniovi.university_management.ui.Subjects;

public class SubjectsAdapter extends RecyclerView.Adapter<SubjectsAdapter.MyViewHolder> implements Filterable {

    private List<Subject> listaAsignaturas;
    private List<Subject> listaAsignaturasFiltered;
    private Context context;
    private Subjects subjects;

    public SubjectsAdapter(List<Subject> listaAsignaturas, Context context, Subjects subjects) {
        this.listaAsignaturas = listaAsignaturas;
        listaAsignaturasFiltered = listaAsignaturas;
        this.context = context;
        this.subjects = subjects;
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

            confirmaBorrado(position);

        });


    }

    @Override
    public int getItemCount() {
        return listaAsignaturas.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    listaAsignaturasFiltered = listaAsignaturas;
                } else {
                    List<Subject> filteredList = new ArrayList<>();
                    for (Subject row : listaAsignaturas) {


                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    listaAsignaturasFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listaAsignaturasFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listaAsignaturasFiltered = (ArrayList<Subject>) filterResults.values;
                notifyDataSetChanged();
            }
        };
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
                    Intent i = new Intent(context, SubjectActivity.class);
                    i.putExtra("nombreAsignatura", titulo.getText().toString());
                    context.startActivity(i);
                }

            });

        }


        }


    private void confirmaBorrado(int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(subjects);

        builder.setTitle("Eliminar Asignatura")
                .setMessage("La asignatura se eliminará definitivamente, ¿está seguro?")
                .setPositiveButton("OK",
                        (dialog, which) -> {
                            //String asignaturaBorra = listaAsignaturasFiltered.get(pos).getName();


                            Thread t = new Thread() {
                                public void run() {
                                    AppDatabase db = AppDatabase.Companion.getAppDatabase(context);
                                    SubjectEntity subjectEntity = db.subjectDao().getByName(listaAsignaturas.get(pos).getName());
                                    db.subjectDao().delete(subjectEntity);
                                    listaAsignaturas.remove(pos);

                                }
                            };
                            t.start();
                            SubjectsAdapter.this.notifyDataSetChanged();


                        })
                .setNegativeButton("CANCELAR",
                        (dialog, which) -> dialog.cancel())
                .show();
    }


}



