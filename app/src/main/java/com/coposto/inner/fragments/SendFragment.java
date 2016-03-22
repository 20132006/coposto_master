package com.coposto.inner.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.coposto.Coposto.MainActivity;
import com.coposto.R;
import com.coposto.models.NavItem;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class SendFragment extends Fragment {

	static String destination_from,destination_to;
	static String period_from,period_to;
	static String parcel_name,parcel_weight,parcel_price;
	public static final int RESULT_LOAD_IMAGE=1;
	static Boolean from=false,to=false;
	static View view_real;
	DrawerLayout drawerLayout;
	RelativeLayout drawerPane;
	ListView lvNav;

	List<NavItem> listNavItems;
	List<Fragment> listFragments;

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

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		final int position = 4;
		view_real = inflater.inflate(R.layout.send_fragment, container, false);


		final TextView Period_From = (TextView) view_real.findViewById(R.id.period_from);
		Period_From.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (Period_From.isFocused() && Period_From.getText().toString().equals("")) {
					from = true;
					DatePickerFragment dialog = DatePickerFragment.newInstance();
					dialog.show(getActivity().getFragmentManager(), "DatePickerFragment");
				}

			}
		});


		final TextView Period_To = (TextView) view_real.findViewById(R.id.period_to);
		Period_To.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (Period_To.isFocused() && Period_To.getText().toString().equals("")) {
					to = true;
					DatePickerFragment dialog = DatePickerFragment.newInstance();
					dialog.show(getActivity().getFragmentManager(), "DatePickerFragment");
				}

			}
		});


		final Button submit = (Button) view_real.findViewById(R.id.submit_button);
		submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				view_real.setBackgroundResource(R.drawable.background_send_smooth);
				new AlertDialog.Builder(myContext)
						.setTitle("Do you want to save?")
						.setPositiveButton("YES", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {

								destination_from = ((EditText)view_real.findViewById(R.id.destination_from)).getText().toString();
								destination_to = ((EditText)view_real.findViewById(R.id.destination_to)).getText().toString();
								period_from = ((EditText)view_real.findViewById(R.id.period_from)).getText().toString();
								period_to = ((EditText)view_real.findViewById(R.id.period_to)).getText().toString();
								parcel_name = ((EditText)view_real.findViewById(R.id.parcel_name)).getText().toString();
								parcel_weight = ((EditText)view_real.findViewById(R.id.parcel_weight)).getText().toString();
								parcel_price = ((EditText)view_real.findViewById(R.id.parcel_price)).getText().toString();

								String method = "store_parcel_information";
								BackgroundTask backgroundTask = new BackgroundTask(myContext);
								backgroundTask.execute( method ,destination_from ,destination_to ,period_from ,period_to ,parcel_name ,parcel_weight ,parcel_price );

								view_real.setBackgroundResource(R.drawable.background_send);
								Toast.makeText(getContext(), "Your parcel saved", Toast.LENGTH_LONG).show();

							}
						})
						.setNegativeButton("NO", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								view_real.setBackgroundResource(R.drawable.background_send);
								// do nothing
							}
						})
						.setIcon(android.R.drawable.ic_dialog_alert)
						.show();

			}
		});

		TextView parcel_image = (TextView) view_real.findViewById(R.id.parcel_image);
		parcel_image.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, RESULT_LOAD_IMAGE);
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
			EditText PERIOD_from = (EditText) view_real.findViewById(R.id.period_from);
			EditText PERIOD_to = (EditText) view_real.findViewById(R.id.period_to);
			if (from) {
				from = false;
				PERIOD_from.setText(formattedDate);
			} else if (to) {
				to=false;
				PERIOD_to.setText(formattedDate);
			}
		}

		public static DatePickerFragment newInstance() {
			DatePickerFragment f = new DatePickerFragment();
			return f;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);


		if (data != null) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = myContext.getContentResolver().query(selectedImage, filePathColumn, null, null, null);

			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			TextView IMAGE_PATH = (TextView) view_real.findViewById(R.id.parcel_image);
			IMAGE_PATH.setText(picturePath);
			cursor.close();
		} else {
			Toast.makeText(getActivity(), "Try Again!!", Toast.LENGTH_SHORT)
					.show();
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
			alertDialog.setTitle("Saving Information....");
		}

		@Override
		protected String doInBackground(String... params) {
			String reg_url = "http://10.20.17.247/coposto/store_parcel.php";
			String method = params[0];
			if (method == "store_parcel_information") {
				String name = params[1];
				String user_name = params[2];
				String user_pass = params[3];
				try {
					URL url = new URL(reg_url);
					HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
					httpURLConnection.setRequestMethod("POST");
					httpURLConnection.setDoOutput(true);

					//httpURLConnection.setDoInput(true);
					OutputStream OS = httpURLConnection.getOutputStream();
					BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
					String data = 	URLEncoder.encode("destination_from", "UTF-8") + "=" + URLEncoder.encode(destination_from, "UTF-8") + "&" +
									URLEncoder.encode("destination_to", "UTF-8") + "=" + URLEncoder.encode(destination_to, "UTF-8") + "&" +
									URLEncoder.encode("period_from", "UTF-8") + "=" + URLEncoder.encode(period_from, "UTF-8") + "&" +
									URLEncoder.encode("period_to", "UTF-8") + "=" + URLEncoder.encode(period_to, "UTF-8") + "&" +
									URLEncoder.encode("parcel_name", "UTF-8") + "=" + URLEncoder.encode(parcel_name, "UTF-8") + "&" +
									URLEncoder.encode("parcel_weight", "UTF-8") + "=" + URLEncoder.encode(parcel_weight, "UTF-8") + "&" +
									URLEncoder.encode("parcel_price", "UTF-8") + "=" + URLEncoder.encode(parcel_price, "UTF-8");
					bufferedWriter.write(data);
					bufferedWriter.flush();
					bufferedWriter.close();
					OS.close();
					InputStream IS = httpURLConnection.getInputStream();
					IS.close();
					//httpURLConnection.connect();
					httpURLConnection.disconnect();
					return "Storing Success...";
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
			if (result == "Storing Success...") {
				((EditText)view_real.findViewById(R.id.destination_from)).setText("");
				((EditText)view_real.findViewById(R.id.destination_to)).setText("");
				((EditText)view_real.findViewById(R.id.period_from)).setText("");
				((EditText)view_real.findViewById(R.id.period_to)).setText("");
				((EditText)view_real.findViewById(R.id.parcel_name)).setText("");
				((EditText)view_real.findViewById(R.id.parcel_weight)).setText("");
				((EditText)view_real.findViewById(R.id.parcel_price)).setText("");
			} else {
				alertDialog.setMessage(result);
				alertDialog.show();
			}

		}
	}
}
