package history_tab_classes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.dmytro.myapplication.ApiModule;
import com.example.dmytro.myapplication.Controller;
import com.example.dmytro.myapplication.R;
import com.example.dmytro.myapplication.User;
import com.example.dmytro.myapplication.UserSetting;

import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dmytro on 05.01.2017.
 */

public class HistoryTab1 extends Fragment {



    ArrayList<HistoryObject> list=new ArrayList<HistoryObject>();
    ListView lvDetails;
    DetailsListAdapter adapter;
Controller controller=new Controller();
    User user=new User();
    UserSetting settings=new UserSetting();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.history_tab1, container, false);
        lvDetails =(ListView)rootView.findViewById(R.id.listview_details);
        user=controller.loadUserCredentials(getContext());
        settings=controller.loadSettings(getContext());
        if(controller.isInternetAvailable(getContext())) {
            if (controller.checkWifiConnection(getContext())) {
                controller.showToastMessage(getContext(), getContext().getString(R.string.HistTab1WifiMsg));
                getArchive();
            }
            else{
                if(settings.getDataType()){
                    controller.showToastMessage(getContext(), getContext().getString(R.string.HistTab1SimMsg));
                    getArchive();
                }
                else
                {
                    controller.showToastMessage(getContext(), getContext().getString(R.string.HistTab1WarningMsg));
                }
            }
        } else
        {
            controller.showToastMessage(getContext(),getContext().getString(R.string.HistTab1LimConnMsg));
            list=controller.loadHistory(getContext());
            adapter = new DetailsListAdapter(getContext(), list);
            lvDetails.setAdapter(adapter);
          }

        return rootView;
    }
private void getArchive()
{
    Call<ArrayList<HistoryObject>> jsonCall = ApiModule.getClient().getHistory(user.getTokenType()+" "+user.getAccessToken());
    jsonCall.enqueue(new Callback<ArrayList<HistoryObject>>() {
        @Override
        public void onResponse(Call<ArrayList<HistoryObject>> call, Response<ArrayList<HistoryObject>> response) {
            if (response.code() == 200) {
                list = response.body();

                if (list != null) {
                    controller.saveHistory(getContext(), list);
                    adapter = new DetailsListAdapter(getContext(), list);
                    lvDetails.setAdapter(adapter);
                } else {
                    controller.showToastMessage(getContext(), "Список порожній!");
                }
            } else {
                controller.showToastMessage(getContext(), "Мережева помилка! Перевірте з'єднання з інтернетом");
            }
        }
        @Override
        public void onFailure(Call<ArrayList<HistoryObject>> call, Throwable t) {

        }
    });
}

}

