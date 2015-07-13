package com.jaguarlabs.xlr8;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class SetReminderActivity extends Activity {
	private int startYear, startMonth, startDay, hour, minut, horario;
	private TextView dateText, timeText;
	private CheckBox repeat;
	private RadioGroup gruporadio;
	private RadioButton daily,weekly,monthly;
	String message;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_reminder);
		dateText = (TextView) findViewById(R.id.textDate);
		timeText = (TextView) findViewById(R.id.textTime);
		repeat=(CheckBox)findViewById(R.id.checkBoxRepeat);
		gruporadio=(RadioGroup)findViewById(R.id.radioGroup1);
		daily=(RadioButton)findViewById(R.id.daily);
		weekly=(RadioButton)findViewById(R.id.weekly);
		monthly=(RadioButton)findViewById(R.id.monthly);
		Intent intent = getIntent();
		message = intent.getStringExtra(ShowMissionActivity.MISSION);
		
		if(repeat.isSelected()){
	    	gruporadio.setEnabled(true);
	    	daily.setEnabled(true);
	    	weekly.setEnabled(true);
	    	monthly.setEnabled(true);
		}
		else
		{
	    	gruporadio.setEnabled(false);
	    	daily.setEnabled(false);
	    	weekly.setEnabled(false);
	    	monthly.setEnabled(false);
		}

		repeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

		    @Override
		    public void onCheckedChanged(CompoundButton checkBox, boolean checked) {
		        //basically, since we will set enabled state to whatever state the checkbox is
		        //therefore, we will only have to setEnabled(checked)
		    	gruporadio.setEnabled(checked);
		    	daily.setEnabled(checked);
		    	weekly.setEnabled(checked);
		    	monthly.setEnabled(checked);
		    }
		});



	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.set_reminder, menu);
		return true;
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.Date:
			showDatePicker(true);
			break;
		case R.id.Time:
			showDatePicker(false);
			break;
		case R.id.setReminderButton:
			if (dateText.getText().toString().length() > 0) {
				if (timeText.getText().toString().length() > 0) {
					addEventToCalendar();
				} else {
					Context context = getApplicationContext();
					String texto = "Please select a time";
					int duracion = Toast.LENGTH_LONG;
					Toast toast = Toast.makeText(context, texto, duracion);
					toast.show();
				}
			} else {
				Context context = getApplicationContext();
				String texto = "Please select a date";
				int duracion = Toast.LENGTH_LONG;
				Toast toast = Toast.makeText(context, texto, duracion);
				toast.show();
			}

			break;
		}
	}

	public void showDatePicker(boolean validacion) {
		// Inflate your custom layout containing 2 DatePickers
		LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
		if (validacion) {
			View customView = inflater.inflate(R.layout.data_picker_layout,
					null);

			// Define your date pickers
			final DatePicker dpStartDate = (DatePicker) customView
					.findViewById(R.id.DatePicker);

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setView(customView); // Set the view of the dialog to your
											// custom layout
			builder.setTitle("Select date");
			builder.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							startYear = dpStartDate.getYear();
							startMonth = dpStartDate.getMonth()+1;
							startDay = dpStartDate.getDayOfMonth();
							Calendar calendario = Calendar.getInstance();
							SimpleDateFormat formato1 = new SimpleDateFormat("dd-MM-yyyy");
							String fecha = formato1.format(calendario.getTime());
							String []date=fecha.split("-");
							if( startMonth>=Integer.valueOf(date[1]) && startYear>=Integer.valueOf(date[2]))
							{
								if (startDay >= Integer.valueOf(date[0])
										&& startMonth > Integer
												.valueOf(date[1])) {
									
									dateText.setText(String.valueOf(startYear)
											+ "/" + String.valueOf(startMonth)
											+ "/" + String.valueOf(startDay));
								}else if(startDay > Integer.valueOf(date[0])
										&& startMonth >= Integer
												.valueOf(date[1])){
									dateText.setText(String.valueOf(startYear)
											+ "/" + String.valueOf(startMonth)
											+ "/" + String.valueOf(startDay));
								}
								else
								{
									Context context = getApplicationContext();
									String texto = "Please select a future date";
									int duracion = Toast.LENGTH_LONG;
									Toast toast = Toast.makeText(context, texto, duracion);
									toast.show();
								}
							}
							else
							{
								Context context = getApplicationContext();
								String texto = "Please select a future date";
								int duracion = Toast.LENGTH_LONG;
								Toast toast = Toast.makeText(context, texto, duracion);
								toast.show();
							}

						}
					});
			builder.create().show();
		} else {
			View customView = inflater.inflate(R.layout.time_picker_layout,
					null);
			// final DatePicker dpEndDate = (DatePicker)
			// customView.findViewById(R.id.dpEndDate);
			final TimePicker timer = (TimePicker) customView
					.findViewById(R.id.timePicker1);
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setView(customView); // Set the view of the dialog to your
											// custom layout
			builder.setTitle("Select time");
			builder.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							hour = timer.getCurrentHour();
							minut = timer.getCurrentMinute();
							timeText.setText(String.valueOf(hour) + ":"
									+ String.valueOf(minut));
						}
					});
			builder.create().show();
		}

	}
	
	private void addEventToCalendar(){
		  Calendar cal = Calendar.getInstance();
		  
		  cal.set(Calendar.DAY_OF_MONTH, startDay);
		  cal.set(Calendar.MONTH, startMonth);
		  cal.set(Calendar.YEAR, startYear);
		  
		  cal.set(Calendar.HOUR_OF_DAY, hour);
		  cal.set(Calendar.MINUTE, minut);
		  
		  Intent intent = new Intent(Intent.ACTION_EDIT);
		  intent.setType("vnd.android.cursor.item/event");

		  intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, cal.getTimeInMillis());
		  intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, cal.getTimeInMillis()+60*60*1000);

		  intent.putExtra(Events.ALL_DAY, false);
		if (repeat.isSelected()) {
			if(daily.isSelected()){
			intent.putExtra(Events.RRULE, "FREQ=DAILY");
			}
			else if(weekly.isSelected()){
				intent.putExtra(Events.RRULE, "FREQ=WEEKLY");	
			}
			else if(monthly.isSelected()){
				intent.putExtra(Events.RRULE, "FREQ=MONTHLY");
			}
		}
		  intent.putExtra(Events.TITLE, "Mission");
		  intent.putExtra(Events.DESCRIPTION, message);
		  //intent.putExtra(Events.EVENT_LOCATION,"Calle ....");

		  startActivity(intent);
		 }

}
