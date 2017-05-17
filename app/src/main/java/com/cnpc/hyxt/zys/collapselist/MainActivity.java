package com.cnpc.hyxt.zys.collapselist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //保存重点关注组的数据
    private List<FocusGroupBean> focusList;
    private PinnedHeaderExpandableListView explistview;
    private PinnedHeaderExpandableAdapter adapter;
    private int expandFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化的方法
        init();
    }

    /*
     * 定义方法 实现初始化
     */
    private void init() {
        //初始化数据
        initData();
        //初始化布局
        initView();
    }

    /*
     * 定义方法 初始化数据
     */
    private void initData() {
        focusList = new ArrayList<>();
        //创建河北组的数据
        List<VeDetailBean> hbList = new ArrayList<>();
        hbList.add(new VeDetailBean("冀A N7832"));
        hbList.add(new VeDetailBean("冀A N7834"));
        hbList.add(new VeDetailBean("冀A N7856"));
        hbList.add(new VeDetailBean("冀A N7812"));
        hbList.add(new VeDetailBean("冀A N7898"));
        hbList.add(new VeDetailBean("冀A N7804"));
        focusList.add(new FocusGroupBean("河北组", hbList));

        //创建北京组的数据
        List<VeDetailBean> bjList = new ArrayList<>();
        bjList.add(new VeDetailBean("京A C9527"));
        bjList.add(new VeDetailBean("京A C3432"));
        bjList.add(new VeDetailBean("京A C9343"));
        bjList.add(new VeDetailBean("京A C9512"));
        bjList.add(new VeDetailBean("京A C9509"));
        bjList.add(new VeDetailBean("京A C9587"));
        bjList.add(new VeDetailBean("京A C0932"));
        bjList.add(new VeDetailBean("京A C8762"));
        bjList.add(new VeDetailBean("京A C9123"));
        focusList.add(new FocusGroupBean("北京组", bjList));

        //创建天津组的数据
        List<VeDetailBean> tjList = new ArrayList<>();
        tjList.add(new VeDetailBean("津N A5621"));
        tjList.add(new VeDetailBean("津N B5372"));
        tjList.add(new VeDetailBean("津N B2312"));
        focusList.add(new FocusGroupBean("天津组", tjList));

        //创建上海组的数据
        List<VeDetailBean> shList = new ArrayList<>();
        shList.add(new VeDetailBean("沪B A5001"));
        shList.add(new VeDetailBean("沪B A8888"));
        focusList.add(new FocusGroupBean("上海组", shList));
    }

    /*
     * 定义方法 初始化布局
     */
    private void initView() {
        explistview = (PinnedHeaderExpandableListView) findViewById(R.id.exp_list_view);
        //展示数据
        showData(focusList);
    }


    /**
     * 定义方法展示数据
     *
     * @param response
     */
    protected void showData(List<FocusGroupBean> response) {
        // 设置头布局
        explistview.setHeaderView(getLayoutInflater().inflate(
                R.layout.group_head, explistview, false));
        // 对适配器进行非空判断
        if (adapter == null) {
            adapter = new PinnedHeaderExpandableAdapter(response,
                    MainActivity.this, explistview);
            explistview.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }

        // 设置滑动的监听
        explistview.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 在滑动状态改变的时候 查看是否有保存的已打开的子条目 如果有则关闭
                if (adapter.openLayout != null) {
                    adapter.openLayout.closeLayout();
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                // 当列表滑动的时候 头布局进行相关变化
                final long flatPos = explistview
                        .getExpandableListPosition(firstVisibleItem);
                int groupPosition = ExpandableListView
                        .getPackedPositionGroup(flatPos);
                int childPosition = ExpandableListView
                        .getPackedPositionChild(flatPos);
                explistview.configureHeaderView(groupPosition, childPosition);

            }

        });
    }

    /**
     * 创建组数据被点击的监听
     *
     * @author song
     */
    class GroupClickListener implements ExpandableListView.OnGroupClickListener {
        @Override
        public boolean onGroupClick(ExpandableListView parent, View v,
                                    int groupPosition, long id) {
            if (expandFlag == -1) {
                // 展开被点击的group
                explistview.expandGroup(groupPosition);
                // 设置被选中的group置于顶端
                explistview.setSelectedGroup(groupPosition);
                expandFlag = groupPosition;
            } else if (expandFlag == groupPosition) {
                explistview.collapseGroup(expandFlag);
                expandFlag = -1;
            } else {
                explistview.collapseGroup(expandFlag);
                // 展开被选中的group
                explistview.expandGroup(groupPosition);
                // 设置被选中的group置于顶端
                explistview.setSelectedGroup(groupPosition);
                expandFlag = groupPosition;
            }
            return true;
        }
    }
}
