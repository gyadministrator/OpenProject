package com.gy.android.openproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gy.android.openproject.R;
import com.gy.android.openproject.bean.Case;
import com.gy.android.openproject.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ListAdapter extends BaseAdapter {
    private Context context;
    private List<Case.DataBeanX.DataBean> allValues;

    public ListAdapter(Context context, List<Case.DataBeanX.DataBean> allValues) {
        this.context = context;
        this.allValues = allValues;
    }

    @Override
    public int getCount() {
        return allValues.size();
    }

    @Override
    public Object getItem(int i) {
        return allValues.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_adapter, null);
        }
        Case.DataBeanX.DataBean dataBean = allValues.get(i);
        ImageView project_image = view.findViewById(R.id.project_image);
        TextView project_name = view.findViewById(R.id.project_name);
        TextView type = view.findViewById(R.id.type);
        TextView type_name = view.findViewById(R.id.type_name);
        TextView day_num = view.findViewById(R.id.day_num);
        ImageView user_image = view.findViewById(R.id.user_image);
        TextView user_name = view.findViewById(R.id.user_name);
        TextView pub_time = view.findViewById(R.id.pub_time);
        TextView require = view.findViewById(R.id.require);
        TextView money = view.findViewById(R.id.money);
        TextView apply = view.findViewById(R.id.apply);
        Picasso.with(context).load(Constants.IMAGE_URL + dataBean.getImagePath()).placeholder(R.mipmap.project).error(R.mipmap.project).resize(100, 150).into(project_image);
        project_name.setText(dataBean.getName());
        if (dataBean.getType() == 2) {
            type.setText("悬赏");
        } else if (dataBean.getType() == 1) {
            type.setText("项目");
        }
        type_name.setText(dataBean.getApplication());
        day_num.setText(dataBean.getCycle() + "天");
        Picasso.with(context).load(Constants.IMAGE_URL + dataBean.getUserAccountIconPath()).placeholder(R.mipmap.project).error(R.mipmap.project).into(user_image);
        user_name.setText(dataBean.getUserAccountNickname());
        pub_time.setText("发布于 " + dataBean.getPublishTime());
        StringBuffer stringBuffer = new StringBuffer();
        for (int j = 0; j < dataBean.getSkillList().size(); j++) {
            Case.DataBeanX.DataBean.SkillListBean skillListBean = dataBean.getSkillList().get(j);
            stringBuffer.append(skillListBean.getValue());
            stringBuffer.append(" ");
        }
        require.setText("技能要求:" + stringBuffer.toString());
        money.setText("￥" + dataBean.getBudgetMaxByYuan());
        apply.setText("申请" + dataBean.getApplyCount() + "人");
        return view;
    }
}
