package org.fitzeng.zzchat.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.fitzeng.zzchat.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BlankFragment_find#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragment_find extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SHOW_TITLE = "Title";
    private static final String ARG_SHOW_CONTENT = "Content";
    //private static final String ARG_SHOW_NAME = "Name";

    // TODO: Rename and change types of parameters
    private String mTitle;
    private String mContent;
    private Button bt_favorate;
    private TextView tv_title;
    private TextView tv_content;

    public BlankFragment_find() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param title Parameter 1.
     * @param content Parameter 2.
     * @return A new instance of fragment BlankFragment_find.
     */
    // TODO: Rename and change types and number of parameters
    public static BlankFragment_find newInstance(String title, String content) {
        BlankFragment_find fragment = new BlankFragment_find();
        Bundle args = new Bundle();
        args.putString(ARG_SHOW_TITLE, title);
        args.putString(ARG_SHOW_CONTENT, content);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(ARG_SHOW_TITLE);
            mContent = getArguments().getString(ARG_SHOW_CONTENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_blank_fragment_find, container, false);
        tv_title = (TextView)rootview.findViewById(R.id.tv_title);
        tv_content = (TextView)rootview.findViewById(R.id.tv_content);
        bt_favorate = (Button)rootview.findViewById(R.id.favorate);
        bt_favorate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Toast.makeText(getActivity(), "收藏成功", Toast.LENGTH_LONG).show();
            }
        });
        return rootview;
    }

}