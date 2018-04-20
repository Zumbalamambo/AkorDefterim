package com.cnbcyln.app.akordefterim.DragNDropExpandableListView;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnbcyln.app.akordefterim.R;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;

public final class DragNDropAdapter extends BaseExpandableListAdapter{
    private int selectedGroup;
    private int selectedChild;

    private AkorDefterimSys AkorDefterimSys;
    private Typeface YaziFontu;
    private ArrayList<String> ListGroups;
    private Map<String, ArrayList<String>> ListChilds;
    private Activity activity;
    private LayoutInflater inflater;

    //private List<SnfListeler> snfListeler;
    //private List<SnfSarkilar> snfSarkilar;

    public DragNDropAdapter(Activity activity, Map<String, ArrayList<String>> ListChilds) {
        this.activity = activity;
        //this.snfListeler = snfListeler;
        //this.snfSarkilar = snfSarkilar;
        this.ListChilds = ListChilds;

        AkorDefterimSys = AkorDefterimSys.getInstance();
        AkorDefterimSys.activity = activity;
        YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular"); // Genel yazı fontunu belirttik
        inflater = LayoutInflater.from(activity);

        ListGroups = new ArrayList<String>();
        ListGroups.addAll(ListChilds.keySet());
    }

    public void onPick(int[] position) {
        selectedGroup = position[0];
        selectedChild = position[1];
    }

    public void onDrop(int[] from, int[] to) {
        if (to[0] > ListChilds.size() || to[0] < 0 || to[1] < 0) return;

        String tValue = getValue(from);
        ListChilds.get(ListChilds.keySet().toArray()[from[0]]).remove(tValue);
        ListChilds.get(ListChilds.keySet().toArray()[to[0]]).add(to[1], tValue);
        selectedGroup = -1;
        selectedChild = -1;
        notifyDataSetChanged();
    }

    private String getValue(int[] id) {
        return (String) ListChilds.get(ListChilds.keySet().toArray()[id[0]]).get(id[1]);
    }

    private class ViewHolderGroup {
        TextView lblListeAdi;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return ListGroups.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public int getGroupCount() {
        return ListGroups.size();
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup parent) {
        /*View satirView = view;
        ViewHolderGroup holder;

        if (satirView == null) {
            satirView = inflater.inflate(R.layout.explstsarkilistesi_header, null);

            holder = new ViewHolderGroup();
            holder.lblListeAdi = satirView.findViewById(R.id.lblListeAdi);
        } else
            holder = (ViewHolderGroup) satirView.getTag();

        // Layout parameters for the ExpandableListView
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 64);

        holder.lblListeAdi.setLayoutParams(lp);
        // Center the text vertically
        holder.lblListeAdi.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        // Set the text starting position
        holder.lblListeAdi.setPadding(36, 0, 0, 0);

        holder.lblListeAdi.setText(ListGroups.get(groupPosition));*/

        // Layout parameters for the ExpandableListView
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 64);

        TextView textView = new TextView(activity);
        textView.setLayoutParams(lp);
        // Center the text vertically
        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        // Set the text starting position
        textView.setPadding(36, 0, 0, 0);

        textView.setText(ListGroups.get(groupPosition));

        return textView;
    }

    private class ViewHolderChild {
        TextView lblSanatciSarkiadi;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // return ListChilds.get(ListGroups.get(groupPosition)).get(childPosition);
        return ListChilds.get(ListChilds.keySet().toArray()[groupPosition]).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // return ListChilds.get(ListGroups.get(groupPosition)).size();
        return ListChilds.get(ListChilds.keySet().toArray()[groupPosition]).size();

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {
        ViewHolderChild holder;

        if (view == null) {
            view = inflater.inflate(R.layout.explstsarkilistesi_item, null);

            holder = new ViewHolderChild();
            holder.lblSanatciSarkiadi = view.findViewById(R.id.lblSanatciSarkiadi);
            view.setTag(holder);
        } else
            holder = (ViewHolderChild) view.getTag();

        holder.lblSanatciSarkiadi.setText((String)getChild(groupPosition, childPosition));

        if (groupPosition != selectedGroup && childPosition != selectedChild) {
            view.setVisibility(View.VISIBLE);
            //ImageView iv = view.findViewById(R.id.ImgDragger);
            //iv.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}