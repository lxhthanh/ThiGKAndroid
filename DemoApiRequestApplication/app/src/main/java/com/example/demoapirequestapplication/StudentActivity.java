package com.example.demoapirequestapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.demoapirequestapplication.IHolder.ILoginView;
import com.example.demoapirequestapplication.IHolder.IView;
import com.example.demoapirequestapplication.model.StudentModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class StudentActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Button btnInsert;
    List<StudentModel> models;
    String mID = "";
    static Boolean CheckLoadData = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        onIntent();
        models = new ArrayList<>();
        btnInsert = findViewById(R.id.btn_insert);
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewAddress();
            }
        });
        recyclerView = findViewById(R.id.student_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        CheckLoadData = true;
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if(CheckLoadData == true) {
                    Map<String, String> mMap = new HashMap<>();
                    mMap.put("id", mID);
                    new StudentAsyncTask(new IView() {
                        @Override
                        public void onRequestSuccess(Bitmap bitmap) {

                        }

                        @Override
                        public void onGetDataSuccess(JSONArray jsonArray) {
                            models.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    StudentModel model = new StudentModel();
                                    model.setProduct_name(jsonObject.getString("product_name"));
                                    model.setDescription(jsonObject.getString("description"));
                                    model.setPrice(Integer.valueOf(jsonObject.getString("price")));
                                    model.setProducer(jsonObject.getString("producer"));
                                    model.setId(Integer.valueOf(jsonObject.getString("id")));
                                    models.add(model);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            MyViewAdapter adapter = new MyViewAdapter(StudentActivity.this, R.layout.student_item, models, mID);
                            recyclerView.setAdapter(adapter);
                        }
                    }, mMap).execute("http://www.vidophp.tk/api/account/getdata");
                    CheckLoadData = false;
                }
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask, 0, 2000);
    }

    private  void onIntent(){
        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        if(b!=null)
        {
            mID =(String) b.get("iID");

        }
    }
    public void AddNewAddress() {
        AlertDialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder( StudentActivity.this );
        View view = getLayoutInflater().inflate( R.layout.dialog_insert, null );

        final EditText edtUserID = view.findViewById( R.id.edtUserID );
        final EditText edtName = view.findViewById( R.id.edtName );
        final EditText edtNumber = view.findViewById( R.id.edtNumber );
        final EditText edtCode = view.findViewById( R.id.edtCode );
        final EditText edtDescription = view.findViewById( R.id.edtDescription );


        edtUserID.setText( mID);

        builder.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final String mName = edtName.getText().toString();
                final String mNumber = edtNumber.getText().toString();
                final String mCode = edtCode.getText().toString();
                String mDescription = edtDescription.getText().toString();
                if(mName.equals("") && mNumber.equals("") && mCode.equals("") && mDescription.equals("")){
                    Toast.makeText(StudentActivity.this,"Please Enter Information Missing",Toast.LENGTH_SHORT).show();
                }
                else{
                    Map<String, String> mMap = new HashMap<>();
                    mMap.put( "user_id", mID );
                    mMap.put( "name", mName );
                    mMap.put( "number", mNumber );
                    mMap.put( "code", mCode );
                    mMap.put( "description", mDescription );
                    new ChangeInfoAsyncTask( new ILoginView() {
                        @Override
                        public void onLoginSuccess(String m, int iID) {

                        }

                        @Override
                        public void onLoginFail(String m) {
                            if(m.equals("OK")){
                                CheckLoadData = true;
                                Toast.makeText(StudentActivity.this,"Insert Successfully",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, mMap).execute("http://www.vidophp.tk/api/account/dataaction?context=insert");

                    dialogInterface.dismiss();

                }

            }
        } );

        builder.setNeutralButton( "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        } );

        builder.setView( view );
        dialog = builder.create();
        dialog.show();
    }
}
