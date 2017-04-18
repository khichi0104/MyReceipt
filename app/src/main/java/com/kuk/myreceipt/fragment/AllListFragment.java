package com.kuk.myreceipt.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kuk.myreceipt.R;
import com.kuk.myreceipt.adapter.ReceiptRecycleviewAdapter;
import com.kuk.myreceipt.model.ReceiptModel;
import com.kuk.myreceipt.model.ReceiptSQLite;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AllListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AllListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ReceiptRecycleviewAdapter adapter;
    RecyclerView receiptListView;
    ReceiptSQLite db;
    static AllListFragment instance;

    Context context = this.getContext();

    public static AllListFragment getInsance() {
    return instance;
    }

    private OnFragmentInteractionListener mListener;

    public AllListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllListFragment newInstance(String param1, String param2) {
        AllListFragment fragment = new AllListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_all_list, container, false);

        db = new ReceiptSQLite(v.getContext());
        receiptListView = (RecyclerView)v.findViewById(R.id.receiptList);
        adapter = new ReceiptRecycleviewAdapter(this);
        receiptListView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(v.getContext());
        receiptListView.setLayoutManager(linearLayoutManager);
        updateData();

        // Inflate the layout for this fragment
        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void updateData(){
        List<ReceiptModel> m = db.getReceiptList();

        adapter.updateData( m );
        Log.d("GetList","GET!!!!");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateData();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
