package es.uniovi.university_management.ui.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.uniovi.university_management.R;
import es.uniovi.university_management.classes.Teacher;
import es.uniovi.university_management.repositories.TeachersRepository;
import es.uniovi.university_management.ui.MapsActivity;

public class TeachersAdapter extends RecyclerView.Adapter<TeachersAdapter.MyViewHolder> {

    private List<Teacher> listaProfesores;
    private Context context;
    private Activity teachersActivity;

    public TeachersAdapter(List<Teacher> listaProfesores, Context context, Activity teachersActivity) {
        this.listaProfesores = listaProfesores;
        this.context = context;
        this.teachersActivity = teachersActivity;
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

            showPopup(view, position, holder);

        });


    }

    private void showPopup(View view, final int position, MyViewHolder holder) {
        View menuItemView = view.findViewById(R.id.botonEliminarProfesor);
        Context wrapper = new ContextThemeWrapper(context, R.style.MyPopupMenu);
        PopupMenu popup = new PopupMenu(wrapper, menuItemView);
        MenuInflater inflate = popup.getMenuInflater();
        inflate.inflate(R.menu.menu_card, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {


            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.send_mail:
                        sendMail(holder.email.getText().toString());
                        break;
                    case R.id.get_ubication:
                        getLocation(listaProfesores.get(position).getOffice().getCoordinates());
                        break;
                    case R.id.delete_mail:
                        confirmaBorrado(position);
                        break;
                    default:
                        return false;
                }
                return false;
            }
        });
        popup.show();
    }


    private void confirmaBorrado(int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(teachersActivity);

        builder.setTitle("Eliminar profesor")
                .setMessage("El profesor se eliminará definitivamente, ¿está seguro?")
                .setPositiveButton("OK",
                        (dialog, which) -> {
                            TeachersRepository repository = new TeachersRepository();
                            repository.deleteTeacher(listaProfesores.get(pos).getName(), context);

                            listaProfesores.remove(pos);
                            TeachersAdapter.this.notifyDataSetChanged();

                        })
                .setNegativeButton("CANCELAR",
                        (dialog, which) -> dialog.cancel())
                .show();
    }

    private void sendMail(String mail) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{mail});
        i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
        i.putExtra(Intent.EXTRA_TEXT, "body of email");
        try {
            context.startActivity(Intent.createChooser(i, "Enviar Email..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "No tiene clientes de correo instalados.", Toast.LENGTH_SHORT).show();

        }
    }

    private void getLocation(String coordinates) {
        Intent i = new Intent(context, MapsActivity.class);
        i.putExtra("coordenadas", coordinates);
        context.startActivity(i);
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
            /*view.setOnClickListener(new View.OnClickListener() {
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

            });*/


        }


    }

}



