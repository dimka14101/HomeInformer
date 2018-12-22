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

public class ArchiveListAdapter extends BaseAdapter {


    private Context mContext;
    private ArrayList<ArchiveObject> archiveList;

    //Constructor

    public ArchiveListAdapter(Context mContext, ArrayList<ArchiveObject> mArchiveList) {
        this.mContext = mContext;
        this.archiveList = mArchiveList;
    }

    @Override
    public int getCount() {
        return archiveList.size();
    }

    @Override
    public Object getItem(int position) {
        return archiveList.get(position);
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

        tvName.setText(String.format("%.1f", archiveList.get(position).getTemp1A())+" | "+
                String.format("%.1f", archiveList.get(position).getTemp2A())+" | "+
                String.format("%.1f",  archiveList.get(position).getHumiditiA())+" | "+
                String.format("%.1f",  archiveList.get(position).getLuminosityA())+" | "+
                String.format("%.1f",  archiveList.get(position).getPressureA())+" | "+
                String.format("%.1f", archiveList.get(position).getAltitudeA()));
        tvDescription.setText(archiveList.get(position).getTimeperiodA());

        //Save product id to tag
        v.setTag(archiveList.get(position).getDetailsIdA());

        return v;
    }
}

