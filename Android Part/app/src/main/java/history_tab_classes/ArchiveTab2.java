package history_tab_classes;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.dmytro.myapplication.ApiModule;
import com.example.dmytro.myapplication.Controller;
import com.example.dmytro.myapplication.R;
import com.example.dmytro.myapplication.User;
import com.example.dmytro.myapplication.UserSetting;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import history_tab_classes.ArchiveListAdapter;
import history_tab_classes.ArchiveObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dmytro on 05.01.2017.
 */

public class ArchiveTab2 extends Fragment {



    ArrayList<ArchiveObject> list=new ArrayList<ArchiveObject>();
    ListView lvArchive;
    ArchiveListAdapter adapter;
    Controller controller=new Controller();
    User user=new User();
    UserSetting settings=new UserSetting();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.history_tab2, container, false);
        lvArchive =(ListView)rootView.findViewById(R.id.listview_archive);
        user=controller.loadUserCredentials(getContext());
        settings=controller.loadSettings(getContext());
        if(controller.isInternetAvailable(getContext())) {
            if (controller.checkWifiConnection(getContext())) {
                controller.showToastMessage(getContext(), getContext().getString(R.string.HistTab2WifiMsg));
                getHistory();
            }else{
                if(settings.getDataType()){
                    controller.showToastMessage(getContext(), getContext().getString(R.string.HistTab2SimMsg));
                    getHistory();
                }
                else
                {
                    controller.showToastMessage(getContext(), getContext().getString(R.string.HistTab2WarningMsg));
                }
            }
        }else {
            controller.showToastMessage(getContext(), getContext().getString(R.string.HistTab2LimConnMsg));
            list = controller.loadArchive(getContext());
            adapter = new ArchiveListAdapter(getContext(), list);
            lvArchive.setAdapter(adapter);
        }


        return rootView;
    }


    private void getHistory()
    {

        Call<ArrayList<ArchiveObject>> jsonCall = ApiModule.getClient().getArchive(user.getTokenType()+" "+user.getAccessToken());
        jsonCall.enqueue(new Callback<ArrayList<ArchiveObject>>() {

            @Override
            public void onResponse(Call<ArrayList<ArchiveObject>> call, Response<ArrayList<ArchiveObject>> response) {
                if (response.code() == 200) {
                    list = response.body();
                    if (list != null) {
                        controller.saveArchive(getContext(), list);
                        adapter = new ArchiveListAdapter(getContext(), list);
                        lvArchive.setAdapter(adapter);
                    } else {
                        controller.showToastMessage(getContext(), "Список порожній!");
                    }
                } else {
                    controller.showToastMessage(getContext(), "Мережева помилка! Перевірте з'єднання з інтернетом");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ArchiveObject>> call, Throwable t) {

            }
        });
    }
}
