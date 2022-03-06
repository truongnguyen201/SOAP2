package topica.edu.vn.soap2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import topica.edu.vn.config.Configuration;
import topica.edu.vn.model.Contact;

public class MainActivity extends AppCompatActivity {
    EditText txtLayMa;
    TextView txtMa,txtPhone,txtTen;

    Button btnLayThongTin;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
        addEvents();
    }

    private void addEvents() {
        btnLayThongTin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLilayThongTin();
            }
        });

    }

    private void xuLilayThongTin() {
        int ma=Integer.parseInt(txtMa.getText().toString());
    }

    private void addControls() {
        txtLayMa=findViewById(R.id.txtLayMa);
        txtMa=findViewById(R.id.txtMa);
        txtTen=findViewById(R.id.txtTen);
        txtPhone=findViewById(R.id.txtPhone);
        btnLayThongTin=findViewById(R.id.btnlayThongTin);
        progressDialog.setTitle("Thông Báo");
        progressDialog.setMessage("Vui lòng Chờ");
        progressDialog.setCanceledOnTouchOutside(false);
    }

    class  getDetailTask extends AsyncTask<Integer,Void, Contact>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            txtMa.setText("");
            txtPhone.setText("");
            txtTen.setText("");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Contact contact) {
            super.onPostExecute(contact);
            progressDialog.dismiss();
            txtMa.setText(contact.getTen()+"");
            txtPhone.setText(contact.getPhone());
            txtTen.setText(contact.getTen());
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Contact doInBackground(Integer... integers) {
            try {
                int ma=integers[0];
                SoapObject reqquet=new SoapObject(Configuration.NAME_SPACE,Configuration.METHOD_GET_DETAIL);
                reqquet.addProperty(Configuration.PARAM_GET_DETAIL,ma);

                SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet=true;

                envelope.setOutputSoapObject(reqquet);

                HttpTransportSE httpTransportSE=new HttpTransportSE(Configuration.SERVER_URL);
                httpTransportSE.call(Configuration.SOAP_ACTION_GETDETAIL,envelope);

                SoapObject data= (SoapObject) envelope.getResponse();
                Contact contact=new Contact();
                //lay tung propaty binh thuong chi co 1 propaty
                if(data.hasProperty("Ma"))
                {
                   contact.setMa(Integer.parseInt(data.getPropertyAsString("Ma")));
                }
                if(data.hasProperty("Ten"))
                {
                    contact.setTen(data.getPropertyAsString("Ten"));

                }
                if(data.hasProperty("Phone"))
                {
                    contact.setTen(data.getPropertyAsString("Phone"));
                }
                return contact;

            }
          catch (Exception exception)
          {
              Log.e("LOI", exception.toString());
          }

            return null;
        }
    }
}