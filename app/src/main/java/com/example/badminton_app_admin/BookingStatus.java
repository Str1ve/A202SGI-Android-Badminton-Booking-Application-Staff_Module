package com.example.badminton_app_admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.badminton_app_admin.Model.Booking;
import com.example.badminton_app_admin.Model.Court;
import com.example.badminton_app_admin.Model.Request;
import com.example.badminton_app_admin.ViewHolder.BookingViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class BookingStatus extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;



    FirebaseRecyclerAdapter<Request, BookingViewHolder> adapter;

    FirebaseDatabase db;
    DatabaseReference requests;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_status);
        getSupportActionBar().hide();

        //Firebase
        db = FirebaseDatabase.getInstance();
        requests = db.getReference("Requests");


        //Init
        recyclerView = (RecyclerView)findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOrders();


    }

    private void loadOrders() {
        adapter = new FirebaseRecyclerAdapter<Request, BookingViewHolder>(
                Request.class,
                R.layout.booking_layout,
                BookingViewHolder.class,
                requests
        ) {
            @Override
            protected void populateViewHolder(BookingViewHolder bookingViewHolder, Request request, int i) {
                bookingViewHolder.txt_orderId.setText(adapter.getRef(i).getKey());

                bookingViewHolder.txt_fullName.setText(request.getName());

                List<Booking> courts = request.getCourts();
                String courtString = "";
                for (int j=0; j<courts.size(); j++){
                    if(j!=courts.size()-1)
                        courtString += courts.get(j).getTimeslot() + ", ";
                    else
                        courtString += courts.get(j).getTimeslot();

                }
                bookingViewHolder.txt_orderTime.setText(courtString);

                bookingViewHolder.txt_orderPrice.setText(request.getTotal());

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference categoryTable = database.getReference("Category");

                String categoryName = courts.get(0).getMenuId();

                categoryTable.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child(categoryName).exists()){
                            Court court = snapshot.child(categoryName).getValue(Court.class);
                            bookingViewHolder.txt_orderCategory.setText(court.getName());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }
}