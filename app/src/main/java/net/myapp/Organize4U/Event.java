package net.myapp.Organize4U;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;





public class Event extends Fragment implements View.OnClickListener {
    EditText title;
    EditText Location;
    EditText Description;
    Button addEvent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_events, container, false);
        title = view.findViewById(R.id.edTitle);
        Location = view.findViewById(R.id.edLocation);
        Description = view.findViewById(R.id.edDescription);
        addEvent = view.findViewById(R.id.btnAdd);
addEvent.setOnClickListener(this);

        return view;
    }

    @Override
        public void onClick(View v) {
            if (!title.getText().toString().isEmpty() && !Location.getText().toString().isEmpty() && !Description
                    .getText().toString().isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setData(CalendarContract.Events.CONTENT_URI);
                intent.putExtra(CalendarContract.Events.TITLE, title.getText().toString());
                intent.putExtra(CalendarContract.Events.EVENT_LOCATION, Location.getText().toString());
                intent.putExtra(CalendarContract.Events.DESCRIPTION, Description.getText().toString());
                intent.putExtra(CalendarContract.Events.ALL_DAY, true);

                if(intent.resolveActivity(getActivity().getPackageManager()) != null){
                    startActivity(intent);
                }else {
                    Toast.makeText(getActivity(), "There is no app that support this action", Toast.LENGTH_SHORT).show();

                }
            }else{
                Toast.makeText(getActivity(), "Please fill all the fields",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }


