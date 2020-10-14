package com.deyi.hfybase.baseModule.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deyi.hfybase.baseModule.event.NullBean;
import com.deyi.hfybase.baseModule.inter.IBaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author hfy
 * @description
 * @time 2020/10/12
 */
public abstract class MBaseFragment extends Fragment implements IBaseFragment {
    //不建议使用getActivity方法
    protected Context mContext;
    //判断UI是否已经加载完成。因为setUserVisibleHint()会在onCreateView()之前调用
    protected boolean isPrepared = false;
    //缓存FragmentView
    protected View rootView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        isPrepared = true;
        return inflater.inflate(bindLayout(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
        bindButterKnife(view);
        init();
        initView(view);
        lazyLoad();
    }

    /**
     * 懒加载数据
     */
    private void lazyLoad() {
        boolean visable = getUserVisibleHint();
        if (visable && isPrepared) {
            loadData();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            lazyLoad();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unBindButterKnife();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void NullEvent(NullBean bean) {

    }

}
