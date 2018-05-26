package co.dtechsystem.carefer.UI.Activities;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import co.dtechsystem.carefer.Adapters.MyOrdersRecycleViewAdapter;
import co.dtechsystem.carefer.Models.MyOrdersModel;
import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.AppConfig;
import co.dtechsystem.carefer.Utils.Validations;

import static com.nexmo.sdk.verify.core.service.BaseService.gson;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyOrdersFragment extends Fragment  {
    private MyOrdersRecycleViewAdapter mMyOrdersRecycleViewAdapter;
    private RecyclerView myOrdersRecylerView;
    MainActivity activity;
    View view;

    public MyOrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_my_orders, container, false);
        myOrdersRecylerView = (RecyclerView) view.findViewById(R.id.rv_my_orders);
        setUpToolbar();

        return view;
    }
    @Override
     public void onResume() {
        if (Validations.isInternetAvailable(activity, true)) {
            activity.loading.show();
            APiGetMyOrdersListData();
        }
        super.onResume();
    }

    private void APiGetMyOrdersListData() {
        // prepare the Request
        RequestQueue queue = Volley.newRequestQueue(activity);
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, AppConfig.APiMyOrdersList +  activity.sUser_ID, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response

                        MyOrdersModel mMyOrdersModel = gson.fromJson(response.toString(), MyOrdersModel.class);
                        if (mMyOrdersModel.getOrdersData() != null && mMyOrdersModel.getOrdersData().size() > 0) {
                            mMyOrdersRecycleViewAdapter = new MyOrdersRecycleViewAdapter(activity, mMyOrdersModel.getOrdersData());
                            myOrdersRecylerView.setVisibility(View.VISIBLE);
                            view.findViewById(R.id.tv_no_record_found).setVisibility(View.GONE);
                            SetListData();
                            activity.loading.close();

                        } else {
                            activity.loading.close();
                            myOrdersRecylerView.setVisibility(View.GONE);
                            activity.aQuery.find(R.id.tv_no_record_found).getTextView().setVisibility(View.VISIBLE);
                            activity.showToast(getResources().getString(R.string.no_record_found));

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        activity.loading.close();
                        activity.SendFireBaseError(String.valueOf(error));
                        activity.showToast(getResources().getString(R.string.some_went_wrong));
                        Log.d("Error.Response", String.valueOf(error));
                    }
                });

        // add it to the RequestQueue
        RetryPolicy policy = new DefaultRetryPolicy(AppConfig.socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        getRequest.setRetryPolicy(policy);
        queue.add(getRequest);
    }





    private void SetListData() {
        myOrdersRecylerView.getItemAnimator().setChangeDuration(700);
        myOrdersRecylerView.setAdapter(mMyOrdersRecycleViewAdapter);
        LinearLayoutManager mgridLayoutManager = new LinearLayoutManager(activity);
        myOrdersRecylerView.setLayoutManager(mgridLayoutManager);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity=(MainActivity)context;
    }

    private void setUpToolbar(){
        TextView title= (TextView) activity.findViewById(R.id.toolbar_title);
        title.setText(R.string.title_order_history);

        final ImageButton search=(ImageButton) activity.findViewById(R.id.toolbar_search);
        final EditText searchfiled=(EditText) activity.findViewById(R.id.searchfiled);
        search.setVisibility(View.VISIBLE);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                activity.findViewById(R.id.searchfiled).setVisibility(View.VISIBLE);
//                activity.findViewById(R.id.toolbar_back).setVisibility(View.GONE);
//                activity.findViewById(R.id.toolbar_title).setVisibility(View.GONE);

            }
        });



        final ImageButton back=(ImageButton) activity.findViewById(R.id.toolbar_back);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();

            }
        });

        activity.findViewById(R.id.toolbar_fav).setVisibility(View.GONE);
        activity.findViewById(R.id.toolbar_refresh).setVisibility(View.GONE);
        activity.findViewById(R.id.toolbar_edit).setVisibility(View.GONE);
        activity.findViewById(R.id.toolbar_filter).setVisibility(View.GONE);
        activity.findViewById(R.id.toolbar_sort).setVisibility(View.GONE);
        activity.findViewById(R.id.searchfiled).setVisibility(View.GONE);

    }

}