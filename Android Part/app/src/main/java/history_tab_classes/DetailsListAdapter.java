package history_tab_classes;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dmytro.myapplication.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Dmytro on 06.01.2017.
 */

public class DetailsListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<HistoryObject> detailsList;

    //Constructor

    public DetailsListAdapter(Context mContext, ArrayList<HistoryObject> mProductList) {
        this.mContext = mContext;
        this.detailsList = mProductList;
    }

    @Override
    public int getCount() {
        return detailsList.size();
    }

    @Override
    public Object getItem(int position) {
        return detailsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(mContext, R.layout.item_archive_and_details_list, null);
        TextView tvName = (TextView)v.findViewById(R.id.tv_name);
        TextView tvDescription = (TextView)v.findViewById(R.id.tv_description);
        //Set text for TextView

            tvName.setText(String.format("%.1f", detailsList.get(position).getTemperature1())+" | "+
                    String.format("%.1f", detailsList.get(position).getTemperature2())+" | "+
                    String.format("%.1f",detailsList.get(position).getHumidity())+" | "+
                    String.format("%.1f",detailsList.get(position).getLuminosity())+" | "+
                    String.format("%.1f",detailsList.get(position).getPressure())+" | "+
                    String.format("%.1f",detailsList.get(position).getAltitude()));
            tvDescription.setText(detailsList.get(position).getDatetime());

            //Save product id to tag
            v.setTag(detailsList.get(position).getDetailsId());

        return v;
    }
}
