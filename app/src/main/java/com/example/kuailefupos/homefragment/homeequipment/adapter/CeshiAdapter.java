package com.example.kuailefupos.homefragment.homeequipment.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.kuailefupos.R;
import com.example.kuailefupos.homefragment.homeequipment.bean.TerminalBean;
import com.example.kuailefupos.homefragment.homeequipment.hodler.ChooserRecyclerViewHodler;
import com.example.kuailefupos.homefragment.homemessage.bean.BusinessMessageBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2021/2/24
 * 描述:
 */
public class CeshiAdapter extends RecyclerView.Adapter <ChooserRecyclerViewHodler>{
    private List<TerminalBean> list = new ArrayList<>();
    public SparseArray<Boolean> ischeck;
    public SparseArray<CheckBox> listbox;
    private boolean selectedState = false;

    /*******创建接口************/
    // add click callback
    OnAddClickListener onItemAddClick;
    public static interface OnAddClickListener {
        public void onItemClick(); //传递boolean类型数据给Fragment
    }
    public void setOnAddClickListener(OnAddClickListener onItemAddClick) {
        this.onItemAddClick = onItemAddClick;
    }
    public void addAllData(List<TerminalBean> dataList) {
        this.list.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clearData() {
        this.list.clear();
    }


    public CeshiAdapter(Context context){
        ischeck = new SparseArray<Boolean>();
        listbox = new SparseArray<CheckBox>();
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public ChooserRecyclerViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_fragment_list_item,parent,false);
        return new ChooserRecyclerViewHodler(view);
    }

    @Override
    public void onBindViewHolder(ChooserRecyclerViewHodler holder, int position) {
        holder.textView_item_number.setText(list.get(position).getPosCode());
        holder.postype.setText(list.get(position).getPosTypeName());
        holder.constraintLayout.setId(position);
        SetSelectedState(holder.checkBox_item, ischeck.get(position, false));
        listbox.put(position, holder.checkBox_item);
        holder.constraintLayout.setOnClickListener(onclick);
    }

    public View.OnClickListener onclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = v.getId();
            setIsSelectedState(true);
            if (ischeck == null) {
                ischeck.put(position, true);
            } else {
                if (!ischeck.get(position, false)) {
                    ischeck.put(position, true);
                } else {
                    ischeck.put(position, false);
                }
            }
            SetSelectedState(listbox.get(position), ischeck.get(position));
            onItemAddClick.onItemClick();

        }
    };

    public void setIsSelectedState(boolean state) {
        selectedState = state;
    }

    public boolean getIsSelectedState() {
        return selectedState;
    }

    public void SetSelectedState(CheckBox checkBox, boolean type) {
        if (getIsSelectedState()) {
            checkBox.setChecked(type);
            if (type) {
                checkBox.setBackgroundResource(R.mipmap.feedback_true);
            } else {
                checkBox.setBackgroundResource(R.mipmap.feedback_false);
            }
        }
    }

    public void getAllSelect() {
        try {
            setIsSelectedState(true);
            if (getIsSelectedState()) {
                int length = list.size();
                ischeck.clear();
                for (int i = 0; i < length; i++) {
                    ischeck.put(i, true);
                    onItemAddClick.onItemClick();
                    if (i < listbox.size()) {
                        SetSelectedState(listbox.get(i), ischeck.get(i));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setAllSelect() {
        try {
            setIsSelectedState(true);
            if (getIsSelectedState()) {
                ischeck.clear();
                int length = list.size();
                for (int i = 0; i < length; i++) {
                    ischeck.put(i, false);
                    if (i < listbox.size()) {
                        SetSelectedState(listbox.get(i), ischeck.get(i));
                    }
                }
                onItemAddClick.onItemClick();
            }
        } catch (Exception e) {
            e.printStackTrace();
            /** 为避免index下标越界 */
        }
    }


}
