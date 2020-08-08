package km.gxy.com.funtest.weather;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import km.gxy.com.funtest.BaseFragment;
import km.gxy.com.funtest.R;
import km.gxy.com.funtest.common.CommonUrls;
import km.gxy.com.funtest.db.City;
import km.gxy.com.funtest.db.County;
import km.gxy.com.funtest.db.Province;
import km.gxy.com.funtest.util.HttpUtil;
import km.gxy.com.funtest.util.Utility;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author xiayi.gu@2020/8/8
 * @Project FunWeather
 */
public class ChooseAreaFragment extends BaseFragment {
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTRY = 2;
    private ProgressDialog progressDialog;
    private TextView tvTitle;
    private Button btnBack;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<>();

    private List<Province> provinces;
    private List<City> cities;
    private List<County> countries;

    private Province selectedProvince;
    private City selectCity;
    private County selectedCountry;

    private int curLevel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fw_choose_area, container, false);
        tvTitle = view.findViewById(R.id.title_text);
        btnBack = view.findViewById(R.id.btn_back);
        listView = view.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (curLevel == LEVEL_PROVINCE) {
                    selectedProvince = provinces.get(position);
                    queryCities();
                } else if (curLevel == LEVEL_CITY) {
                    selectCity = cities.get(position);
                    queryCounties();
                }else{
                    selectedCountry = countries.get(position);
                    showToast(selectedCountry.getCountyName());
                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (curLevel == LEVEL_COUNTRY) {
                    queryCities();
                } else if (curLevel == LEVEL_CITY) {
                    queryProvinces();
                }

                // TODO 键盘的返回键也需要监控@Activity
            }
        });
        queryProvinces();
    }

    private void queryProvinces() {
        tvTitle.setText("中国");
        switchBackBtn(false);
        provinces = DataSupport.findAll(Province.class);
        if (provinces.size() > 0) {
            dataList.clear();
            for (Province p : provinces) {
                dataList.add(p.getProvinceName());
            }
            notifyAdapterChanged();
            curLevel = LEVEL_PROVINCE;
        } else {
            curLevel = LEVEL_PROVINCE;
            queryFromServer(CommonUrls.URL_LOCATION);
        }
    }

    private void queryCities() {
        tvTitle.setText(selectedProvince.getProvinceName());
        switchBackBtn(true);
        cities = DataSupport.where("provinceId= ?",
                String.valueOf(selectedProvince.getId())).find(City.class);
        if (cities.size() > 0) {
            dataList.clear();
            for (City c : cities) {
                dataList.add(c.getCityName());
            }
            notifyAdapterChanged();
            curLevel = LEVEL_CITY;
        } else {
            curLevel = LEVEL_CITY;
            int provinceCode = selectedProvince.getProvinceCode();
            queryFromServer(CommonUrls.URL_LOCATION + provinceCode);
        }
    }

    private void queryCounties() {
        tvTitle.setText(selectCity.getCityName());
        switchBackBtn(true);
        countries = DataSupport.where("cityId=?",
                String.valueOf(selectCity.getId())).find(County.class);
        if (countries.size() > 0) {
            dataList.clear();
            for (County c : countries) {
                dataList.add(c.getCountyName());
            }
            notifyAdapterChanged();
            curLevel = LEVEL_COUNTRY;
        } else {
            curLevel = LEVEL_COUNTRY;
            int provinceCode = selectedProvince.getProvinceCode();
            int cityCode = selectCity.getCityCode();
            queryFromServer(CommonUrls.URL_LOCATION + provinceCode + "/" + cityCode);
        }
    }

    private void queryFromServer(String url) {
        showProgressDialog();
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean isSuccess = false;
                switch (curLevel) {
                    case LEVEL_PROVINCE:
                        isSuccess = Utility.handleProvinceResponse(responseText);
                        break;
                    case LEVEL_CITY:
                        isSuccess = Utility.handleCityResponse(responseText, selectedProvince.getId());
                        break;
                    case LEVEL_COUNTRY:
                        isSuccess = Utility.handleCountryResponse(responseText, selectCity.getId());
                        break;
                }
                if (isSuccess) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            switch (curLevel) {
                                case LEVEL_PROVINCE:
                                    queryProvinces();
                                    break;
                                case LEVEL_CITY:
                                    queryCities();
                                    break;
                                case LEVEL_COUNTRY:
                                    queryCounties();
                                    break;
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        showToast("加载失败！");
                    }
                });
            }
        });

    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载。。。");
            progressDialog.setCancelable(false);
        }
    }

    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    private void switchBackBtn(boolean on) {
        btnBack.setVisibility(on ? View.VISIBLE : View.GONE);
    }

    private void notifyAdapterChanged() {
        adapter.notifyDataSetChanged();
        listView.setSelection(0);
    }
}
