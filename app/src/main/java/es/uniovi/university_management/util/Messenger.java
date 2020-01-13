package es.uniovi.university_management.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class Messenger {

    public void MostrarMensajeShort(Context context, String Message) {
        Toast toast1 = Toast.makeText(context, Message, Toast.LENGTH_SHORT);
        toast1.setGravity(Gravity.CENTER, 0, 0);

        toast1.show();
    }

    public void MostrarMensajeLong(Context context, String Message) {
        Toast toast1 = Toast.makeText(context, Message, Toast.LENGTH_LONG);
        toast1.setGravity(Gravity.CENTER, 0, 0);

        toast1.show();
    }
}
