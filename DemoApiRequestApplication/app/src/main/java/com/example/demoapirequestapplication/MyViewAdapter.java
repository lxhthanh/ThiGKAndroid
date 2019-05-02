package com.example.demoapirequestapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.demoapirequestapplication.IHolder.ILoginView;
import com.example.demoapirequestapplication.model.StudentModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyViewAdapter extends RecyclerView.Adapter<MyViewAdapter.ViewHolder> {
    List<StudentModel> models;
    int mResource;
    Context mContext;
    String iID;
    public MyViewAdapter(Context context,int resource, List<StudentModel> objects,String ID){
        this.mContext = context;
        this.mResource = resource;
        this.models = objects;
        this.iID = ID;
    }
    @NonNull
    @Override
    public MyViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(mResource,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewAdapter.ViewHolder viewHolder, int i) {
        final StudentModel model = models.get(i);
        viewHolder.edtProductName.setText(model.getProduct_name());
        viewHolder.edtPrice.setText(String.valueOf(model.getPrice()));
        viewHolder.edtProducer.setText(model.getProducer());
        viewHolder.edtDescription.setText(model.getDescription());
        viewHolder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,String> mMap = new HashMap<>();
                mMap.put("user_id",iID);
                mMap.put("name",viewHolder.edtProductName.getText().toString());
                mMap.put("number",viewHolder.edtPrice.getText().toString());
                mMap.put("description",viewHolder.edtDescription.getText().toString());
                mMap.put("code",viewHolder.edtProducer.getText().toString());
                mMap.put("id", String.valueOf(model.getId()));
                new ChangeInfoAsyncTask( new ILoginView() {
                    @Override
                    public void onLoginSuccess(String m, int iID) {

                    }

                    @Override
                    public void onLoginFail(String m) {
                        if(m.equals("OK")){
                            StudentActivity.CheckLoadData = true;
                            Toast.makeText(mContext,"Update Successfully",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, mMap).execute("http://www.vidophp.tk/api/account/dataaction?context=update");
            }
        });
        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,String> mMap = new HashMap<>();
                mMap.put("user_id",iID);
                mMap.put("id", String.valueOf(model.getId()));
                new ChangeInfoAsyncTask( new ILoginView() {
                    @Override
                    public void onLoginSuccess(String m, int iID) {

                    }

                    @Override
                    public void onLoginFail(String m) {
                        if(m.equals("OK")){
                            StudentActivity.CheckLoadData = true;
                            Toast.makeText(mContext,"Delete Successfully",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, mMap).execute("http://www.vidophp.tk/api/account/dataaction?context=delete");
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private EditText edtProductName;
        private EditText edtPrice;
        private  EditText edtProducer;
        private EditText edtDescription;
        private Button btnUpdate;
        private Button btnDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.edtProductName = itemView.findViewById(R.id.edt_product_name);
            this.edtPrice = itemView.findViewById(R.id.edt_price);
            this.edtProducer = itemView.findViewById(R.id.edt_producer);
            this.edtDescription = itemView.findViewById(R.id.edt_description);
            this.btnUpdate = itemView.findViewById(R.id.btn_update);
            this.btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
