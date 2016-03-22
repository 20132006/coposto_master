package com.coposto.inner.fragments;

/**
 * Created by netlab on 1/11/16.
 */
/*
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    Context mContext;

    View view_;
    //@Override
    //public void onActivityCreated(Bundle savedInstanceState){
    //    super.onActivityCreated(savedInstanceState);
    //}
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view_ = inflater.inflate(R.layout.bring_fragment, container, false);
        return null;
    }

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

    }

    public static DatePickerFragment newInstance() {
        DatePickerFragment f = new DatePickerFragment();
        return f;
    }
}
*/