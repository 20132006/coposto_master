package com.coposto.inner.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.coposto.Coposto.MainActivity;
import com.coposto.R;
import com.coposto.models.NavItem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BringFragment extends Fragment {

	static View view_real;
	EditText Going_From ;
	EditText Going_To ;
	EditText Date_of_Flight;

	DrawerLayout drawerLayout;
	RelativeLayout drawerPane;
	ListView lvNav;

	List<NavItem> listNavItems;
	List<Fragment> listFragments;

	List <String> Parcel_less_information;
	List <String> Parcel_more_information;
	ArrayAdapter<String> arrayAdapter;
	private FragmentActivity myContext;

	View v;

	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		drawerLayout = ((MainActivity)this.getActivity()).getDrawerLayout();
		drawerPane = ((MainActivity)this.getActivity()).getRelativeLayout();
		lvNav = ((MainActivity)this.getActivity()).getListView();

		listNavItems = ((MainActivity)this.getActivity()).getListNavItem();
		listFragments = ((MainActivity)this.getActivity()).getListFragment();

	}

	@Override
	public void onAttach(Activity activity) {
		myContext=(FragmentActivity) activity;
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		view_real = inflater.inflate(R.layout.bring_fragment, container, false);

		final int position = 4;

		Going_From = (EditText) view_real.findViewById(R.id.going_from);
		Going_To = (EditText) view_real.findViewById(R.id.going_to);

		Date_of_Flight = (EditText) view_real.findViewById(R.id.data_of_flight);
		//final EditText editText = (EditText) view_real.findViewById(R.id.data_of_flight);
		Date_of_Flight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (Date_of_Flight.isFocused() && Date_of_Flight.getText().toString().equals(""))
				{
					DatePickerFragment dialog = DatePickerFragment.newInstance();
					dialog.show(getActivity().getFragmentManager(), "DatePickerFragment");
				}

			}
		});

		Button find_button = (Button) view_real.findViewById(R.id.find_button);
		find_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String method = "GET List of Parcels";
				BackgroundTask backgroundTask = new BackgroundTask(myContext);
				backgroundTask.execute(method, Date_of_Flight.getText().toString(), Going_From.getText().toString(), Going_To.getText().toString());

			}
		});
		return view_real;
	}

	public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
		Context mContext;

		//@Override
		//public void onActivityCreated(Bundle savedInstanceState){
		//    super.onActivityCreated(savedInstanceState);
		//}

		public DatePickerFragment() {
			mContext = getActivity();
		}
		Calendar c;
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);

			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);

		}
		public void onDateSet(DatePicker view, int year, int month, int day) {
			c = Calendar.getInstance();
			c.set(year, month, day);

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String formattedDate = sdf.format(c.getTime());
			EditText editText = (EditText) view_real.findViewById(R.id.data_of_flight);
			editText.setText(formattedDate);
		}

		public static DatePickerFragment newInstance() {
			DatePickerFragment f = new DatePickerFragment();
			return f;
		}
	}
	public class BackgroundTask extends AsyncTask<String,Void,String> {
		AlertDialog alertDialog;
		Context ctx;

		BackgroundTask(Context ctx) {
			this.ctx = ctx;
		}

		@Override
		protected void onPreExecute() {
			alertDialog = new AlertDialog.Builder(ctx).create();
			alertDialog.setTitle("Login Information....");
		}

		@Override
		protected String doInBackground(String... params) {
			String login_url = "http://10.20.17.247/coposto/get_parcels.php";
			String method = params[0];
			if (method == "GET List of Parcels") {
				String login_phone = params[1];
				String login_pass = params[2];
				try {
					URL url = new URL(login_url);
					HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
					httpURLConnection.setRequestMethod("POST");
					httpURLConnection.setDoOutput(true);
					httpURLConnection.setDoInput(true);
					OutputStream outputStream = httpURLConnection.getOutputStream();
					BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
					String data = URLEncoder.encode("Going_From", "UTF-8") + "=" + URLEncoder.encode(Going_From.getText().toString(), "UTF-8") + "&" +
							URLEncoder.encode("Going_To", "UTF-8") + "=" + URLEncoder.encode(Going_To.getText().toString(), "UTF-8");
					bufferedWriter.write(data);
					bufferedWriter.flush();
					bufferedWriter.close();
					outputStream.close();

					InputStream inputStream = httpURLConnection.getInputStream();
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
					String response = "";
					String line = "";
					while ((line = bufferedReader.readLine()) != null) {
						response += line;
					}
					bufferedReader.close();
					inputStream.close();
					httpURLConnection.disconnect();
					return response;


				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}


			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(String result) {
			int login_success = result.indexOf("Success");

			Going_From.setText("");
			Going_To.setText("");
			Date_of_Flight.setText("");

			Parcel_less_information = new ArrayList<String>();
			Parcel_more_information = new ArrayList<String>();

			if (login_success >= 0) {
				String parcel_info;
				while (true)
				{
					int l,r;
					if (result.length()<=15)
						break;
					l = result.indexOf("<")+1;
					r = result.indexOf(">");
					parcel_info = result.substring(l,r);

					Parcel_more_information.add(parcel_info);
					int starting , ending;
					starting = result.indexOf("Parcel Name")-1;
					ending = result.indexOf("Parcel Weight")-3;
					Parcel_less_information.add(parcel_info.substring(starting,ending));
					result = result.substring(r+1);
				}

				view_real.setBackgroundResource(R.drawable.background_bring_smooth);
				AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
				//builderSingle.setIcon(R.drawable.ic_launcher);


				builderSingle.setTitle("Select Parcel To See Details");

				//arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, your_array_list );
				arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_view, Parcel_less_information);


				builderSingle.setNegativeButton(
						"Cancel",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								view_real.setBackgroundResource(R.drawable.background_bring);
							}
						});

				builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								String strName = Parcel_more_information.get(which);
								AlertDialog.Builder builderInner = new AlertDialog.Builder(getActivity());
								builderInner.setMessage(strName);
								builderInner.setTitle("More Information About Parcel");
								builderInner.setPositiveButton("Offer", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										//do something
										Toast.makeText(myContext,"Your Offer Successfully Submitted",Toast.LENGTH_SHORT).show();
										view_real.setBackgroundResource(R.drawable.background_bring);
									}
								}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {
										dialog.dismiss();
										view_real.setBackgroundResource(R.drawable.background_bring);
									}
								}).show();
							}
						});
				builderSingle.show();

			} else {
				new AlertDialog.Builder(myContext)
						.setTitle("Fail To Download")
						.setMessage("Pleas try again.")
						.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						})
						.setIcon(android.R.drawable.ic_dialog_alert)
						.show();
			}

		}
	}
}
